package com.cbctr.sss;

import java.math.BigInteger;
import java.util.Random;

import static com.cbctr.sss.CryptoContext.getContext;

public class Node {
    private final BigInteger privateKey;
    private BigInteger globalPrivateKeyShare;
    private final BigInteger publicShare;

    public Node() {
        this.privateKey = new BigInteger(getContext().getQ().bitLength(), new Random()).mod(getContext().getQ());
        this.globalPrivateKeyShare = BigInteger.ZERO;
        this.publicShare = getContext().getAlpha().modPow(privateKey, getContext().getP());
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

    public void addToGlobalShare(BigInteger partOfKey) {
        globalPrivateKeyShare = globalPrivateKeyShare.add(partOfKey).mod(getContext().getQ());
    }

    public void computeShareAndBroadcast(Group group) {
        var f = ShamirScheme.getPolynomial(new Secret(getPrivateKey()), getContext().getK(), getContext().getQ());
        group.broadcastShare(ShamirScheme.getShares(getContext().getN(), f));
    }

    public EncryptedText encrypt(BigInteger plainText, BigInteger publicKey) {
        BigInteger ephemeralKey = ShamirScheme.nextRandomBigInteger(getContext().getP());
        return new EncryptedText(getContext().getAlpha().modPow(ephemeralKey, getContext().getP()),
            plainText.multiply(publicKey.modPow(ephemeralKey, getContext().getP())).mod(getContext().getP()));
    }
}
