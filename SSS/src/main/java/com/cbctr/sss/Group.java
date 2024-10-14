package com.cbctr.sss;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.cbctr.sss.CryptoContext.getContext;

public class Group {
    private final List<Node> nodes;
    private final BigInteger publicKey;

    public Group(List<Node> nodes) {
        this.nodes = nodes;
        this.publicKey = computeJointPublicKey();
        computeShares();
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    private BigInteger computeJointPublicKey() {
        BigInteger product = BigInteger.ONE;
        for (Node node : nodes) {
            product = product.multiply(node.getPublicShare()).mod(getContext().getP());
        }
        return product;
    }

    public BigInteger decrypt(EncryptedText encrypted) {
        return encrypted.c2().multiply(encrypted.c1().modPow(computeSecretKey(), getContext().getP())
                .modInverse(getContext().getP())).mod(getContext().getP());
    }

    private void computeShares() {
        nodes.forEach(node -> node.computeShareAndBroadCast(this));
    }

    public void broadcastShare(List<Share> share) {
        for (int i = 0; i < share.size(); i++) {
            nodes.get(i).addToGlobalShare(share.get(i).y());
        }
    }

    public int getNodePosition(Node node) {
        return nodes.indexOf(node);
    }

    private BigInteger computeSecretKey() {
        List<Share> allShares = new ArrayList();
        for (int i = 0; i < nodes.size(); i++) {
            allShares.add(new Share(BigInteger.valueOf(i), nodes.get(i).getGlobalPrivateKeyShare()));
        }
        return ShamirScheme.getSecret(allShares, getContext().getP()).secret();
    }

    public int getNodeSize() {
        return nodes.size();
    }

    // public void computeShareAndBroadCast() {
    //     nodes.stream().forEach(node -> {
    //         ShamirScheme.getPolynomial(new Secret(node.getPrivateKey()), kThreshold, p);
    //     });
    // }

    // public BigInteger[] encrypt(BigInteger message) {
    //     BigInteger k = new BigInteger(p.bitLength(), new Random()).mod(p);
    //     BigInteger c1 = g.modPow(k, p);
    //     BigInteger c2 = message.multiply(publicKey.modPow(k, p)).mod(p);
    //     return new BigInteger[]{c1, c2};
    // }

    // public BigInteger decrypt(List<BigInteger> partialDecryptions, BigInteger c1, BigInteger c2) {
    //     BigInteger combinedDecryption = BigInteger.ONE;
    //     for (BigInteger partialDecryption : partialDecryptions) {
    //         combinedDecryption = combinedDecryption.multiply(partialDecryption).mod(p);
    //     }
    //     return c2.multiply(combinedDecryption.modInverse(p)).mod(p);
    // }
}
