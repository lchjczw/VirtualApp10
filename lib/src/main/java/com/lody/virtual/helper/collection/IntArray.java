package com.lody.virtual.helper.collection;
import java.util.Arrays;
public class IntArray {
    private static final int[] EMPTY_ARRAY = new int[0];
    private int[] mData;
    private int mSize;
    private IntArray() {
    }
    public IntArray(int capacity) {
        this.mData = new int[capacity];
    }
    public static IntArray of(int... values) {
        IntArray array = new IntArray();
        array.mData = Arrays.copyOf(values, values.length);
        array.mSize = values.length;
        return array;
    }
    public void clear() {
        mSize = 0;
    }
    public void optimize() {
        if (mSize > mData.length) {
            mData = Arrays.copyOf(mData, mSize);
        }
    }
    public int[] getAll() {
        return mSize > 0 ? Arrays.copyOf(mData, mSize) : EMPTY_ARRAY;
    }
    public int get(int index) {
        return mData[index];
    }
    public int[] getRange(int start, int end) {
        return Arrays.copyOfRange(mData, start, end);
    }
    public void set(int index, int value) {
        if (index >= mSize) {
            throw new IndexOutOfBoundsException("Index " + index + " is greater than the list size " + mSize);
        }
        mData[index] = value;
    }
    private void ensureCapacity() {
        if (mSize <= mData.length) {
            return;
        }
        int newCap = mData.length;
        while (mSize > newCap) {
            newCap = newCap * 3 / 2 + 1;
        }
        mData = Arrays.copyOf(mData, newCap);
    }
    public int size() {
        return mSize;
    }
    public void addAll(int[] items) {
        int target = mSize;
        mSize += items.length;
        ensureCapacity();
        System.arraycopy(items, 0, mData, target, items.length);
    }
    public void add(int item) {
        ++mSize;
        ensureCapacity();
        mData[mSize - 1] = item;
    }
    public void remove(int index) {
        remove(index, 1);
    }
    public void remove(int index, int count) {
        System.arraycopy(mData, index + count, mData, index, mSize - index - count);
        mSize -= count;
    }
    public boolean contains(int item) {
        for (int i = 0; i < mSize; ++i) {
            if (mData[i] == item) {
                return true;
            }
        }
        return false;
    }
}
