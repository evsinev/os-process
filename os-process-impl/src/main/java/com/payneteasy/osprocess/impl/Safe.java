package com.payneteasy.osprocess.impl;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

public final class Safe {

    private Safe() {
    }

    public static <T> List<T> safeList(List<T> aList) {
        return aList != null ? aList : emptyList();
    }

    public static <T> T[] safeArray(T[] aArray) {
        //noinspection unchecked
        return aArray != null ? aArray : (T[]) Collections.EMPTY_LIST.toArray();
    }
}
