package com.example.coinProject.common;

import java.util.LinkedList;

public class LimitedQueue<E> extends LinkedList<E> {

    private int limit;

    public LimitedQueue(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(E e) {
        super.add(e);
        while (size() > limit) {super.remove();}
        return true;
    }
}
