package com.cbctr.sss;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BigInteger p = new BigInteger("104729"); 
        BigInteger g = new BigInteger("2"); 
        int n = 5; 
        int t = 3; 

        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nodes.add(new Node(p, g));
        }

        ElGamalThreshold elGamal = new ElGamalThreshold(p, g, nodes);

        BigInteger message = new BigInteger("42");
        BigInteger[] ciphertext = elGamal.encrypt(message);
        System.out.println("Encrypted: " + ciphertext[0] + ", " + ciphertext[1]);

        List<BigInteger> partialDecryptions = new ArrayList<>();
        for (int i = 0; i < t; i++) {
            partialDecryptions.add(nodes.get(i).computePartialDecryption(ciphertext[0]));
        }
        BigInteger decryptedMessage = elGamal.decrypt(partialDecryptions, ciphertext[0], ciphertext[1]);
        System.out.println("Decrypted message: " + decryptedMessage);
    }
}
