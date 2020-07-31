package com.summer.gateway.until;

public class Pair<A, B> {
    private final A one;
    private final B two;

    public Pair(A one, B two) {
        this.one = one;
        this.two = two;
    }

    public A getOne() {
        return one;
    }

    public B getTwo() {
        return two;
    }
}
