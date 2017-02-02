import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;



/**
     * Create a hash map with no entries. The backing array has an initial
     * capacity of {@code INITIAL_CAPACITY}.
     *
     * Do not use magic numbers!
     *
     * Use constructor chaining.
     */

/**
     * Create a hash map with no entries. The backing array has an initial
     * capacity of {@code initialCapacity}.
     *
     * You may assume {@code initialCapacity} will always be positive.
     *
     * @param initialCapacity initial capacity of the backing array
     */
public class a<K, V> implements HashMapInterface<K, V>, Gradable<K, V> {

    private MapEntry<K, V>[] table;
    private int size;

    public HashMap() {
        clear();
    }

    @Override
    public V add(K key, V value) {
        boostSize();

        if (key == null || value == null) {
            throw new IllegalArgumentException("null argument");
        }

        MapEntry<K, V> item = new MapEntry<K, V>(key, value);
        V oldValue = null;

        int hash = addHelper(generateKey(item), key);
        if (hash > 0) {
            table[--hash] = item;
            size++;
        } else {
            hash = Math.abs(hash) - 1;
            oldValue = table[hash].getValue();
            table[hash] = item;
        }

        return oldValue;
    }

    /**
     * helper for add, contains three sets of return values:
     * 1 - negative non-zero integer: object exists in the hashtable
     * 2 - positive non-zero integer: object does not exist in the hash
     *  table
     * 3 - zero, not used
     *
     * to get the hash, take Math.abs(returnValue)-1
     *
     * @param hash value to search for
     * @param key to look for
     * @return integer representing state (see above)
     */
    private int addHelper(int hash, K key) {
        boolean endLoop = false;

        do {
            // check if key is equal, else increment hash (calculating the remainder)
            if (!isSlotEmpty(hash)
                    && table[hash].getKey() != null
                    && table[hash].getKey().equals(key)) {
                hash = -1 * (hash + 1);
                endLoop = true;
            } else if (!isSlotEmpty(hash)) {
                hash++;
                hash %= table.length;
            } else  {
                endLoop = true;
            }
        } while (!endLoop);
        return (hash >= 0) ? hash + 1 : hash;
    }

    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("null argument");
        }
        V removedValue = null;
        int hash = getHelper(generateKey(key), key);
        MapEntry<K, V> item = null;
        if (hash != 0) {
            item = table[(Math.abs(hash) - 1)];
        }

        if (hash >= 0 || item == null || item.isRemoved()) {
            removedValue = null; // no element exists
        } else if (item != null) {
            item.setRemoved(true);
            removedValue = item.getValue();
            size--;
        }
        return removedValue;
    }

    @Override
    public V get(K key) {
        if (size == 0 || key == null) {
            return null;
        }
        MapEntry<K, V> item = null;
        int hash = getHelper(generateKey(key), key);
        if (hash != 0) {
            item = table[(Math.abs(hash) - 1)];
        }
        return (hash >= 0 || item == null || item.isRemoved()) ? null : item.getValue();
    }

    /**
     * helper for get, contains three sets of return values:
     * 1 - negative non-zero integer: object exists in the hashtable
     * 2 - positive non-zero integer: object does not exist in the hash
     *  table
     * 3 - zero, value not found
     *
     * to get the hash, take Math.abs(returnValue)-1
     *
     * @param hash value to search for
     * @param key to look for
     * @return integer representing state (see above)
     */
    private int getHelper(int hash, K key) {
        boolean endLoop = false;
        int hashOrig = hash;
        do {
            // check if key is equal, else increment hash (calculating the remainder)
            if (table[hash] != null
              && table[hash].getKey() != null
              && table[hash].getKey().equals(key)) {
                hash = -1 * (hash + 1);
                endLoop = true;
            } else if (table[hash] != null) {
                hash++;
                hash %= table.length;
                if (hash == hashOrig) {
                    // item not in table
                    return 0;
                }
            } else  {
                endLoop = true;
            }
        } while (!endLoop);
        return (hash >= 0) ? hash + 1 : hash;
    }

    @Override
    public boolean contains(K key) {
        return get(key) != null;
    }

    @Override
    public void clear() {
        table = (MapEntry<K, V>[]) new MapEntry[STARTING_SIZE];
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public MapEntry<K, V>[] toArray() {
        return table;
    }

    @Override
    public Set<K> keySet() {
        HashSet<K> keys = new HashSet<K>(size);
        for (MapEntry<K, V> item : table) {
            if (!(item == null || item.isRemoved())) {
                keys.add(item.getKey());
            }
        }
        return keys;
    }

    @Override
    public List<V> values() {
        List<V> keys = new ArrayList<V>(size);
        for (MapEntry<K, V> item : table) {
            if (!(item == null || item.isRemoved())) {
                keys.add(item.getValue());
            }
        }
        return keys;
    }

    /**
     * Generates a key (overloaded method)
     *
     * @param item is a MapEntry to generate a key
     * @return the hash
     */
    private int generateKey(MapEntry<K, V> item) {
        if (item == null || item.getKey() == null) {
            throw new IllegalArgumentException();
        }
        return generateKey(item.getKey());
    }

    /**
     * Generates a key
     *
     * @param key is a key to generate a key
     * @return the hash
     */
    private int generateKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        return Math.abs(key.hashCode() % table.length);
    }

    /**
     * Boosts the size as necessary of the backing array
     *
     * @return the new size of the array
     */
    private int boostSize() {
        if (loadFactorNext() > MAX_LOAD_FACTOR) {
            MapEntry<K, V>[] tablePrime = table;
            clear();
            table = (MapEntry<K, V>[]) new MapEntry[tablePrime.length * 2];

            for (MapEntry<K, V> item : tablePrime) {
                if (item != null && !item.isRemoved()) {
                    add(item.getKey(), item.getValue());
                }
            }
        }
        return table.length;
    }

    /**
     * Returns whether a slot is empty
     * @param hash is the hash key
     * @return whether slot is empty
     */
    private boolean isSlotEmpty(int hash) {
        return table[hash] == null || (table[hash].isRemoved());
    }

    /**
     * Returns the load factor for the hash table,
     * on the next element
     * @return the load factor as a double
     */
    private double loadFactorNext() {
        return ((double) (size + 1)) / ((double) table.length);
    }
}