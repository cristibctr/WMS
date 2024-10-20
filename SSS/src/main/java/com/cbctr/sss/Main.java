package com.cbctr.sss;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.cbctr.sss.CryptoContext.getContext;

public class Main {
    public static void main(String[] args) {
        setContext();

        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < getContext().getN(); i++) {
            nodes.add(new Node());
        }

        Group nodesGroup = new Group(nodes);
        String message = "8";
        System.out.println("The message is : " + message);
        // Simulate the second node in the MANET sending an encrypted message
        var encrypted = nodes.get(2).encrypt(new BigInteger(message), nodesGroup.getPublicKey());
        // Simulate the group decrypting the message
        System.out.println("After decryption the message is: " + nodesGroup.decrypt(encrypted));
    }

    private static void setContext() {
        // N = 10, K = 6
        // q = prim
        // q - 160 bits
        //TODO: change p and q
        BigInteger p = new BigInteger("983");
        BigInteger q = new BigInteger("491");
        BigInteger alpha = new BigInteger("6");
        int n = 10;
        int k = 6;
        CryptoContext.setContext(p, q, n, k, alpha);
    }
}