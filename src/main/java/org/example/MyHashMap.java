package org.example;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    int capacity = 16;
    final double LOAD_FACTOR = 0.75;
    int size = 0;
    int threshold = (int) (capacity * LOAD_FACTOR);
    Entry<K,V>[] table = new Entry[capacity];

    private static class Entry<K, V>{
        K key;
        V value;
        int hash;
        Entry<K, V> next;

        public Entry (K key, V value, int hash, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }

    }

    private int hash(K key) {
        return key == null ? 0 : (key.hashCode() ^ (key.hashCode() >>> 16));
    }

    private int indexFor(int h, int length) {
        return h & (length - 1);
    }

    private void addEntry(K key, V value, int hash, int index) {
        Entry<K, V> e = table[index];
        table[index] = new Entry<>(key, value, hash, e);
        size++;
    }

    private void transfer( Entry<K,V>[] newTable) {
        int newCapacity = newTable.length;

        for (int i = 0; i < table.length; i++) {
            Entry<K,V> e = table[i];

            while (e != null) {
                Entry<K,V> next = e.next;
                int index = indexFor(e.hash, newCapacity);
                e.next = newTable[index];
                newTable[index] = e;
                e = next;
            }

        }
    }

    private void resize( int newCapacity) {
        Entry<K,V>[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        capacity = newCapacity;
    }

    private boolean mapIsFilled() {
        return (size >= threshold);
    }

    @Override
    public void put(K key, V value) {

        if (mapIsFilled()) {
            resize(capacity * 2);
        }



        int hash = hash(key);
        int index = indexFor(hash, capacity);
        Entry<K, V> e = table[index];

        while (e != null) {

            if (e.hash == hash &&
                    (key == null ? e.key == null : key.equals(e.key))) {
                e.value = value;
                return;
            }

            e = e.next;
        }

        addEntry(key, value, hash, index);
    }

    @Override
    public V getValue(K key) {

        int hash = hash(key);
        int index = indexFor(hash, capacity);
        Entry<K, V> e = table[index];

        while (e != null) {

            if (e.hash == hash && (key == null ? e.key == null : key.equals(e.key)))  {
                return e.value;
            }

            e = e.next;
        }
        return null;

    }

    @Override
    public int getSize() {
        return size;
    }
}