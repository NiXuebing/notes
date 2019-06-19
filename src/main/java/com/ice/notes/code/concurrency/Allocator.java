package com.ice.notes.code.concurrency;

import java.util.ArrayList;
import java.util.List;

public class Allocator {

    private List<Object> als = new ArrayList<>();

    synchronized boolean apply(Object from, Object to) {
        if (als.contains(from) || als.contains(to)) {
            return false;
        } else {
            als.add(from);
            als.add(to);
        }
        return true;
    }

    synchronized void free(Object from, Object to) {
        als.remove(from);
        als.remove(to);
    }



}

class Account {
    private Allocator actr;
    private int balance;

    void transfer(Account target, int amt) {
        while (!actr.apply(this, target));
    }


}