package com.summer.gateway.until;

public class Triple<A, B, C> extends Pair<A, B> {
    private final C three;

    public Triple(A one, B two, C three) {
        super(one, two);
        this.three = three;
    }

    public C getThree() {
        return three;
    }
}
