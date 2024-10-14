package com.cbctr.sss;

import java.math.BigInteger;
import java.util.Random;

import static com.cbctr.sss.CryptoContext.getContext;

public class Node {
    private final BigInteger privateKey;
    private final BigInteger globalPrivateKeyShare;
    private final BigInteger publicShare;

    public Node() {
        this.privateKey = new BigInteger(getContext().getP().bitLength(), new Random()).mod(getContext().getP());
        this.globalPrivateKeyShare = BigInteger.ONE;
        this.publicShare = getContext().getG().modPow(privateKey, getContext().getP());
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public BigInteger getPublicShare() {
        return publicShare;
    }

    public BigInteger getGlobalPrivateKeyShare() {
        return globalPrivateKeyShare;
    }

    public BigInteger computePartialDecryption(BigInteger c1) {
        return c1.modPow(privateKey, getContext().getP());
    }

    public void addToGlobalShare(BigInteger partOfKey) {
        globalPrivateKeyShare.add(partOfKey).mod(getContext().getQ());
    }

    public void computeShareAndBroadCast(Group group) {
        var f = ShamirScheme.getPolynomial(new Secret(getPrivateKey()), getContext().getK(), getContext().getP());
        // var thisPosition = group.getNodePosition(this);
        group.broadcastShare(ShamirScheme.getShares(group.getNodeSize(), f));
    }

    public EncryptedText encrypt(BigInteger plainText, BigInteger publicKey) {
        BigInteger ephemeralKey = ShamirScheme.nextRandomBigInteger(getContext().getP());
        return new EncryptedText(getContext().getG().modPow(ephemeralKey, getContext().getP()),
            plainText.multiply(publicKey.modPow(ephemeralKey, getContext().getP())).mod(getContext().getP()));
    }
}
