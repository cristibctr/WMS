package com.cbctr.sss;

import java.math.BigInteger;
import java.util.Random;

public class Node {
    private final BigInteger privateKey;
    private final BigInteger publicShare;
    private final BigInteger p;
    private final BigInteger g;

    public Node(BigInteger p, BigInteger g) {
        this.p = p;
        this.g = g;
        this.privateKey = new BigInteger(p.bitLength(), new Random()).mod(p);
        this.publicShare = g.modPow(privateKey, p);
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public BigInteger getPublicShare() {
        return publicShare;
    }

    public BigInteger computePartialDecryption(BigInteger c1) {
        return c1.modPow(privateKey, p);
    }
}
