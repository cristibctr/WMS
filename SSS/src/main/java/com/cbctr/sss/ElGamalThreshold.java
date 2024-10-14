package com.cbctr.sss;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

public class ElGamalThreshold {
    private final BigInteger p;
    private final BigInteger g;
    private final List<Node> nodes;
    private final BigInteger publicKey;

    public ElGamalThreshold(BigInteger p, BigInteger g, List<Node> nodes) {
        this.p = p;
        this.g = g;
        this.nodes = nodes;
        this.publicKey = computeJointPublicKey();
    }

    private BigInteger computeJointPublicKey() {
        BigInteger product = BigInteger.ONE;
        for (Node node : nodes) {
            product = product.multiply(node.getPublicShare()).mod(p);
        }
        return product;
    }

    public BigInteger[] encrypt(BigInteger message) {
        BigInteger k = new BigInteger(p.bitLength(), new Random()).mod(p);
        BigInteger c1 = g.modPow(k, p);
        BigInteger c2 = message.multiply(publicKey.modPow(k, p)).mod(p);
        return new BigInteger[]{c1, c2};
    }

    public BigInteger decrypt(List<BigInteger> partialDecryptions, BigInteger c1, BigInteger c2) {
        BigInteger combinedDecryption = BigInteger.ONE;
        for (BigInteger partialDecryption : partialDecryptions) {
            combinedDecryption = combinedDecryption.multiply(partialDecryption).mod(p);
        }
        return c2.multiply(combinedDecryption.modInverse(p)).mod(p);
    }
}
