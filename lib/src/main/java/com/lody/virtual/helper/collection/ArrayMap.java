package com.lody.virtual.helper.collection;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
public class ArrayMap<K, V> extends SimpleArrayMap<K, V> implements Map<K, V> {
    MapCollections<K, V> mCollections;
    public ArrayMap() {
        super();
    }
    public ArrayMap(int capacity) {
        super(capacity);
    }
    public ArrayMap(SimpleArrayMap map) {
        super(map);
    }
    private MapCollections<K, V> getCollection() {
        if (mCollections == null) {
            mCollections = new MapCollections<K, V>() {
                @Override
                protected int colGetSize() {
                    return mSize;
                }
                @Override
                protected Object colGetEntry(int index, int offset) {
                    return mArray[(index<<1) + offset];
                }
                @Override
                protected int colIndexOfKey(Object key) {
                    return indexOfKey(key);
                }
                @Override
                protected int colIndexOfValue(Object value) {
                    return indexOfValue(value);
                }
                @Override
                protected Map<K, V> colGetMap() {
                    return ArrayMap.this;
                }
                @Override
                protected void colPut(K key, V value) {
                    put(key, value);
                }
                @Override
                protected V colSetValue(int index, V value) {
                    return setValueAt(index, value);
                }
                @Override
                protected void colRemoveAt(int index) {
                    removeAt(index);
                }
                @Override
                protected void colClear() {
                    clear();
                }
            };
        }
        return mCollections;
    }
    public boolean containsAll(Collection<?> collection) {
        return MapCollections.containsAllHelper(this, collection);
    }
    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        ensureCapacity(mSize + map.size());
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
    public boolean removeAll(Collection<?> collection) {
        return MapCollections.removeAllHelper(this, collection);
    }
    public boolean retainAll(Collection<?> collection) {
        return MapCollections.retainAllHelper(this, collection);
    }
    @Override
    public Set<Entry<K, V>> entrySet() {
        return getCollection().getEntrySet();
    }
    @Override
    public Set<K> keySet() {
        return getCollection().getKeySet();
    }
    @Override
    public Collection<V> values() {
        return getCollection().getValues();
    }
}
