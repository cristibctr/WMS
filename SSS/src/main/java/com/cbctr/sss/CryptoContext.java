package com.cbctr.sss;

import java.math.BigInteger;

public class CryptoContext {
    private static CryptoContext cryptoContext;

    private final BigInteger p;
    private final BigInteger q;
    private final int n;
    private final int k;
    private final BigInteger g;

    private CryptoContext(BigInteger p, BigInteger q, int n, int k, BigInteger g){
        this.p = p;
        this.q = q;
        this.n = n;
        this.k = k;
        this.g = g;
    }

    public static CryptoContext getContext() {
        if(cryptoContext == null)
            throw new ContextUnavailableException();
        return cryptoContext;
    }

    public static void setContext(BigInteger p, BigInteger q, int n, int k, BigInteger g) {
        cryptoContext = new CryptoContext(p, q, n, k, g);
    }

    public BigInteger getP() {
        return this.p;
    }

    public BigInteger getQ() {
        return this.q;
    }

    public BigInteger getG() {
        return this.g;
    }

    public int getN() {
        return this.n;
    }

    public int getK() {
        return this.k;
    }

    private static class ContextUnavailableException extends RuntimeException {}
    
}