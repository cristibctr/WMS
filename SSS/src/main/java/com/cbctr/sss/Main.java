package com.cbctr.sss;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // N = 10, K = 6
        // q = prim
        // q - 160 bits
        //TODO: change p and q
        BigInteger p = new BigInteger("1907");
        BigInteger q = new BigInteger("953");
        BigInteger g = new BigInteger("2");
        int n = 10;
        int k = 6;
        CryptoContext.setContext(p, q, n, k, g);

        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nodes.add(new Node());
        }

        Group nodesGroup = new Group(nodes);
        var encrypted = nodes.get(2).encrypt(new BigInteger("8"), nodesGroup.getPublicKey());
        System.out.println(nodesGroup.decrypt(encrypted));
    }
}
