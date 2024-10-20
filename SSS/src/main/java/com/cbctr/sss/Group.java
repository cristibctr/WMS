package com.cbctr.sss;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
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
        nodes.forEach(node -> node.computeShareAndBroadcast(this));
    }

    public void broadcastShare(List<Share> share) {
        for (int i = 0; i < share.size(); i++) {
            nodes.get(i).addToGlobalShare(share.get(i).y());
        }
    }

    private BigInteger computeSecretKey() {
        List<Share> allShares = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            allShares.add(new Share(BigInteger.valueOf(i + 1), nodes.get(i).getGlobalPrivateKeyShare()));
        }
        // basically get some random node's share to simulate being able to decrypt using any k nodes
        Collections.shuffle(allShares);
        List<Share> kShares = allShares.subList(0, getContext().getK());
        return ShamirScheme.getSecret(kShares, getContext().getQ()).secret();
    }

}
