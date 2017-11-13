package org.ngsandbox.zookeeper;

public interface Listener<T> {
    default void removed(String s){}

    default void added(String s, T model){}

    default void updated(String s, T model){}
}
