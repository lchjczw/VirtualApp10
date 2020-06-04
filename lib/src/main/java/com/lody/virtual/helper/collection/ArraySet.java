package com.lody.virtual.helper.collection;
import android.util.Log;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
public final class ArraySet<E> implements Collection<E>, Set<E> {
    private static final boolean DEBUG = false;
    private static final String TAG = "ArraySet";
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    static Object[] mBaseCache;
    static int mBaseCacheSize;
    static Object[] mTwiceBaseCache;
    static int mTwiceBaseCacheSize;
    int[] mHashes;
    Object[] mArray;
    int mSize;
    MapCollections<E, E> mCollections;
    public ArraySet() {
        mHashes = ContainerHelpers.EMPTY_INTS;
        mArray = ContainerHelpers.EMPTY_OBJECTS;
        mSize = 0;
    }
    public ArraySet(int capacity) {
        if (capacity == 0) {
            mHashes = ContainerHelpers.EMPTY_INTS;
            mArray = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            allocArrays(capacity);
        }
        mSize = 0;
    }
    public ArraySet(ArraySet<E> set) {
        this();
        if (set != null) {
            addAll(set);
        }
    }
    public ArraySet(Collection<E> set) {
        this();
        if (set != null) {
            addAll(set);
        }
    }
    private static void freeArrays(final int[] hashes, final Object[] array, final int size) {
        if (hashes.length == (BASE_SIZE*2)) {
            synchronized (ArraySet.class) {
                if (mTwiceBaseCacheSize < CACHE_SIZE) {
                    array[0] = mTwiceBaseCache;
                    array[1] = hashes;
                    for (int i=size-1; i>=2; i--) {
                        array[i] = null;
                    }
                    mTwiceBaseCache = array;
                    mTwiceBaseCacheSize++;
                    if (DEBUG) Log.d(TAG, "Storing 2x cache " + array
                            + " now have " + mTwiceBaseCacheSize + " entries");
                }
            }
        } else if (hashes.length == BASE_SIZE) {
            synchronized (ArraySet.class) {
                if (mBaseCacheSize < CACHE_SIZE) {
                    array[0] = mBaseCache;
                    array[1] = hashes;
                    for (int i=size-1; i>=2; i--) {
                        array[i] = null;
                    }
                    mBaseCache = array;
                    mBaseCacheSize++;
                    if (DEBUG) Log.d(TAG, "Storing 1x cache " + array
                            + " now have " + mBaseCacheSize + " entries");
                }
            }
        }
    }
    private int indexOf(Object key, int hash) {
        final int N = mSize;
        if (N == 0) {
            return ~0;
        }
        int index = ContainerHelpers.binarySearch(mHashes, N, hash);
        if (index < 0) {
            return index;
        }
        if (key.equals(mArray[index])) {
            return index;
        }
        int end;
        for (end = index + 1; end < N && mHashes[end] == hash; end++) {
            if (key.equals(mArray[end])) return end;
        }
        for (int i = index - 1; i >= 0 && mHashes[i] == hash; i--) {
            if (key.equals(mArray[i])) return i;
        }
        return ~end;
    }
    private int indexOfNull() {
        final int N = mSize;
        if (N == 0) {
            return ~0;
        }
        int index = ContainerHelpers.binarySearch(mHashes, N, 0);
        if (index < 0) {
            return index;
        }
        if (null == mArray[index]) {
            return index;
        }
        int end;
        for (end = index + 1; end < N && mHashes[end] == 0; end++) {
            if (null == mArray[end]) return end;
        }
        for (int i = index - 1; i >= 0 && mHashes[i] == 0; i--) {
            if (null == mArray[i]) return i;
        }
        return ~end;
    }
    private void allocArrays(final int size) {
        if (size == (BASE_SIZE*2)) {
            synchronized (ArraySet.class) {
                if (mTwiceBaseCache != null) {
                    final Object[] array = mTwiceBaseCache;
                    mArray = array;
                    mTwiceBaseCache = (Object[])array[0];
                    mHashes = (int[])array[1];
                    array[0] = array[1] = null;
                    mTwiceBaseCacheSize--;
                    if (DEBUG) Log.d(TAG, "Retrieving 2x cache " + mHashes
                            + " now have " + mTwiceBaseCacheSize + " entries");
                    return;
                }
            }
        } else if (size == BASE_SIZE) {
            synchronized (ArraySet.class) {
                if (mBaseCache != null) {
                    final Object[] array = mBaseCache;
                    mArray = array;
                    mBaseCache = (Object[])array[0];
                    mHashes = (int[])array[1];
                    array[0] = array[1] = null;
                    mBaseCacheSize--;
                    if (DEBUG) Log.d(TAG, "Retrieving 1x cache " + mHashes
                            + " now have " + mBaseCacheSize + " entries");
                    return;
                }
            }
        }
        mHashes = new int[size];
        mArray = new Object[size];
    }
    @Override
    public void clear() {
        if (mSize != 0) {
            freeArrays(mHashes, mArray, mSize);
            mHashes = ContainerHelpers.EMPTY_INTS;
            mArray = ContainerHelpers.EMPTY_OBJECTS;
            mSize = 0;
        }
    }
    public void ensureCapacity(int minimumCapacity) {
        if (mHashes.length < minimumCapacity) {
            final int[] ohashes = mHashes;
            final Object[] oarray = mArray;
            allocArrays(minimumCapacity);
            if (mSize > 0) {
                System.arraycopy(ohashes, 0, mHashes, 0, mSize);
                System.arraycopy(oarray, 0, mArray, 0, mSize);
            }
            freeArrays(ohashes, oarray, mSize);
        }
    }
    @Override
    public boolean contains(Object key) {
        return indexOf(key) >= 0;
    }
    public int indexOf(Object key) {
        return key == null ? indexOfNull() : indexOf(key, key.hashCode());
    }
    public E valueAt(int index) {
        return (E)mArray[index];
    }
    @Override
    public boolean isEmpty() {
        return mSize <= 0;
    }
    @Override
    public boolean add(E value) {
        final int hash;
        int index;
        if (value == null) {
            hash = 0;
            index = indexOfNull();
        } else {
            hash = value.hashCode();
            index = indexOf(value, hash);
        }
        if (index >= 0) {
            return false;
        }
        index = ~index;
        if (mSize >= mHashes.length) {
            final int n = mSize >= (BASE_SIZE*2) ? (mSize+(mSize>>1))
                    : (mSize >= BASE_SIZE ? (BASE_SIZE*2) : BASE_SIZE);
            if (DEBUG) Log.d(TAG, "add: grow from " + mHashes.length + " to " + n);
            final int[] ohashes = mHashes;
            final Object[] oarray = mArray;
            allocArrays(n);
            if (mHashes.length > 0) {
                if (DEBUG) Log.d(TAG, "add: copy 0-" + mSize + " to 0");
                System.arraycopy(ohashes, 0, mHashes, 0, ohashes.length);
                System.arraycopy(oarray, 0, mArray, 0, oarray.length);
            }
            freeArrays(ohashes, oarray, mSize);
        }
        if (index < mSize) {
            if (DEBUG) Log.d(TAG, "add: move " + index + "-" + (mSize-index)
                    + " to " + (index+1));
            System.arraycopy(mHashes, index, mHashes, index + 1, mSize - index);
            System.arraycopy(mArray, index, mArray, index + 1, mSize - index);
        }
        mHashes[index] = hash;
        mArray[index] = value;
        mSize++;
        return true;
    }
    public void addAll(ArraySet<? extends E> array) {
        final int N = array.mSize;
        ensureCapacity(mSize + N);
        if (mSize == 0) {
            if (N > 0) {
                System.arraycopy(array.mHashes, 0, mHashes, 0, N);
                System.arraycopy(array.mArray, 0, mArray, 0, N);
                mSize = N;
            }
        } else {
            for (int i=0; i<N; i++) {
                add(array.valueAt(i));
            }
        }
    }
    @Override
    public boolean remove(Object object) {
        final int index = indexOf(object);
        if (index >= 0) {
            removeAt(index);
            return true;
        }
        return false;
    }
    public E removeAt(int index) {
        final Object old = mArray[index];
        if (mSize <= 1) {
            if (DEBUG) Log.d(TAG, "remove: shrink from " + mHashes.length + " to 0");
            freeArrays(mHashes, mArray, mSize);
            mHashes = ContainerHelpers.EMPTY_INTS;
            mArray = ContainerHelpers.EMPTY_OBJECTS;
            mSize = 0;
        } else {
            if (mHashes.length > (BASE_SIZE*2) && mSize < mHashes.length/3) {
                final int n = mSize > (BASE_SIZE*2) ? (mSize + (mSize>>1)) : (BASE_SIZE*2);
                if (DEBUG) Log.d(TAG, "remove: shrink from " + mHashes.length + " to " + n);
                final int[] ohashes = mHashes;
                final Object[] oarray = mArray;
                allocArrays(n);
                mSize--;
                if (index > 0) {
                    if (DEBUG) Log.d(TAG, "remove: copy from 0-" + index + " to 0");
                    System.arraycopy(ohashes, 0, mHashes, 0, index);
                    System.arraycopy(oarray, 0, mArray, 0, index);
                }
                if (index < mSize) {
                    if (DEBUG) Log.d(TAG, "remove: copy from " + (index+1) + "-" + mSize
                            + " to " + index);
                    System.arraycopy(ohashes, index + 1, mHashes, index, mSize - index);
                    System.arraycopy(oarray, index + 1, mArray, index, mSize - index);
                }
            } else {
                mSize--;
                if (index < mSize) {
                    if (DEBUG) Log.d(TAG, "remove: move " + (index+1) + "-" + mSize
                            + " to " + index);
                    System.arraycopy(mHashes, index + 1, mHashes, index, mSize - index);
                    System.arraycopy(mArray, index + 1, mArray, index, mSize - index);
                }
                mArray[mSize] = null;
            }
        }
        return (E)old;
    }
    public boolean removeAll(ArraySet<? extends E> array) {
        final int N = array.mSize;
        final int originalSize = mSize;
        for (int i = 0; i < N; i++) {
            remove(array.valueAt(i));
        }
        return originalSize != mSize;
    }
    @Override
    public int size() {
        return mSize;
    }
    @Override
    public Object[] toArray() {
        Object[] result = new Object[mSize];
        System.arraycopy(mArray, 0, result, 0, mSize);
        return result;
    }
    @Override
    public <T> T[] toArray(T[] array) {
        if (array.length < mSize) {
            @SuppressWarnings("unchecked") T[] newArray
                = (T[]) Array.newInstance(array.getClass().getComponentType(), mSize);
            array = newArray;
        }
        System.arraycopy(mArray, 0, array, 0, mSize);
        if (array.length > mSize) {
            array[mSize] = null;
        }
        return array;
    }
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof Set) {
            Set<?> set = (Set<?>) object;
            if (size() != set.size()) {
                return false;
            }
            try {
                for (int i=0; i<mSize; i++) {
                    E mine = valueAt(i);
                    if (!set.contains(mine)) {
                        return false;
                    }
                }
            } catch (NullPointerException ignored) {
                return false;
            } catch (ClassCastException ignored) {
                return false;
            }
            return true;
        }
        return false;
    }
    @Override
    public int hashCode() {
        final int[] hashes = mHashes;
        int result = 0;
        for (int i = 0, s = mSize; i < s; i++) {
            result += hashes[i];
        }
        return result;
    }
    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(mSize * 14);
        buffer.append('{');
        for (int i=0; i<mSize; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            Object value = valueAt(i);
            if (value != this) {
                buffer.append(value);
            } else {
                buffer.append("(this Set)");
            }
        }
        buffer.append('}');
        return buffer.toString();
    }
    private MapCollections<E, E> getCollection() {
        if (mCollections == null) {
            mCollections = new MapCollections<E, E>() {
                @Override
                protected int colGetSize() {
                    return mSize;
                }
                @Override
                protected Object colGetEntry(int index, int offset) {
                    return mArray[index];
                }
                @Override
                protected int colIndexOfKey(Object key) {
                    return indexOf(key);
                }
                @Override
                protected int colIndexOfValue(Object value) {
                    return indexOf(value);
                }
                @Override
                protected Map<E, E> colGetMap() {
                    throw new UnsupportedOperationException("not a map");
                }
                @Override
                protected void colPut(E key, E value) {
                    add(key);
                }
                @Override
                protected E colSetValue(int index, E value) {
                    throw new UnsupportedOperationException("not a map");
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
    @Override
    public Iterator<E> iterator() {
        return getCollection().getKeySet().iterator();
    }
    @Override
    public boolean containsAll(Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean addAll(Collection<? extends E> collection) {
        ensureCapacity(mSize + collection.size());
        boolean added = false;
        for (E value : collection) {
            added |= add(value);
        }
        return added;
    }
    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean removed = false;
        for (Object value : collection) {
            removed |= remove(value);
        }
        return removed;
    }
    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean removed = false;
        for (int i=mSize-1; i>=0; i--) {
            if (!collection.contains(mArray[i])) {
                removeAt(i);
                removed = true;
            }
        }
        return removed;
    }
}
