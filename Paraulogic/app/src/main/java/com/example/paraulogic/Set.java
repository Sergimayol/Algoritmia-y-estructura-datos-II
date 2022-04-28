package com.example.paraulogic;

import java.util.Iterator;

/**
 * @param <E>
 * @author Sergi
 */
public interface Set<E> {

    /**
     * @param elem
     * @return boolean
     */
    public boolean contains(E elem);

    /**
     * @param elem
     * @return boolean
     */
    public boolean add(E elem);

    /**
     * @param elem
     * @return boolean
     */
    public boolean remove(E elem);

    /**
     * @return boolean
     */
    public boolean isEmpty();

    /**
     * @return Iterator
     */
    public Iterator iterator();
}

