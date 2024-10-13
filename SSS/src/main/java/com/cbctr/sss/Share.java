package com.cbctr.sss;

import java.math.BigInteger;

public record Share(BigInteger x, BigInteger y) {
    @Override
    public String toString() {
        return "Share{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
