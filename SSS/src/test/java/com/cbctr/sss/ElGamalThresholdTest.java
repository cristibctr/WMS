package com.cbctr.sss;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ElGamalThresholdTest {

    @Test
    public void testNodeKeyGeneration() {
        BigInteger p = new BigInteger("104729");
        BigInteger g = new BigInteger("2");
        Node node = new Node(p, g);

        assertNotNull(node.getPrivateKey());
        assertNotNull(node.getPublicShare());
        assertTrue(node.getPublicShare().compareTo(BigInteger.ZERO) > 0);
        assertTrue(node.getPublicShare().compareTo(p) < 0);
    }

    @Test
    public void testElGamalEncryptionDecryption() {
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

        List<BigInteger> partialDecryptions = new ArrayList<>();
        for (int i = 0; i < t; i++) {
            partialDecryptions.add(nodes.get(i).computePartialDecryption(ciphertext[0]));
        }

        BigInteger decryptedMessage = elGamal.decrypt(partialDecryptions, ciphertext[0], ciphertext[1]);
        assertEquals(message, decryptedMessage);
    }
}
