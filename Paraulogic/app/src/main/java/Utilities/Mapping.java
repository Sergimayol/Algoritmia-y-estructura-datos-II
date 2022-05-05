package Utilities;


import java.util.Iterator;

/**
 * @param <K>
 * @param <V>
 * @author Sergi
 */
public interface Mapping<K, V> {

    /**
     * @param key
     * @return V
     */
    public V get(K key);

    /**
     * @param key
     * @param value
     * @return V
     */
    public V put(K key, V value);

    /**
     * @param key
     * @return V
     */
    public V remove(K key);

    /**
     * @return boolean
     */
    public boolean isEmpty();

    /**
     * @return Iterator
     */
    public Iterator iterator();

}
