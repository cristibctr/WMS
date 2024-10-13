package com.cbctr.sss;

import org.apache.commons.lang3.Validate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class ShamirScheme {
    private final Random random;

    public ShamirScheme(Random random) {
        this.random = random;
    }

    public static void main(String[] args) {
        int n = 5, k = 3;
        BigInteger Q = BigInteger.valueOf(65521);
        Secret secret = new Secret(new BigInteger(args[0]));
        ShamirScheme shamirScheme = new ShamirScheme(new Random());
        Polynomial f = shamirScheme.getPolynomial(secret, k, Q);
        List<Share> shares = shamirScheme.getShares(n, f);
        System.out.println("The shares: " + shares);
        Secret reconstructedSecret = shamirScheme.getSecret(shamirScheme.getKRandomShares(shares, k), Q);
        System.out.println("The reconstructed secret: " + reconstructedSecret);
    }

    public Polynomial getPolynomial(Secret s, int k, BigInteger q) {
        Validate.isTrue(s.secret().compareTo(q) < 0, "the secret must be smaller than the prime");
        BigInteger[] coefficients = Stream.concat(
                Stream.iterate(0, i -> i+1).limit(k-1).map(i -> nextRandomBigInteger(q)),
                Stream.of(s.secret())
        ).toArray(BigInteger[]::new);
        return new Polynomial(coefficients, q);
    }

    public List<Share> getShares(int n, Polynomial f) {
        Validate.isTrue(n > 0, "n must be positive");
        Validate.isTrue(BigInteger.valueOf(n).compareTo(f.q()) < 0, "n must be smaller than the prime");
        // random x values don't make it more secure
        // https://crypto.stackexchange.com/questions/63488/shamir-scheme-whats-the-problem-of-not-using-random-x-coordinates
        return Stream.iterate(BigInteger.ONE, i -> i.add(BigInteger.ONE)).limit(n)
                .map(x -> new Share(x, PolynomialUtil.evaluate(f, x)))
                .toList();
    }

    public Secret getSecret(List<Share> shares, BigInteger Q) {
        BigInteger finalSecret = BigInteger.ZERO;
        for (var share : shares) {
            BigInteger multiplications = BigInteger.ONE;
            List<Share> allButCurrent = new ArrayList<>(List.copyOf(shares));
            allButCurrent.remove(share);
            for (var anotherShare : allButCurrent) {
                BigInteger denominator = anotherShare.x().subtract(share.x()).modInverse(Q);
                multiplications = multiplications.multiply(
                        anotherShare.x().multiply(denominator).mod(Q)
                ).mod(Q);
            }
            finalSecret = finalSecret.add(share.y().multiply(multiplications).mod(Q)).mod(Q);
        }
        return new Secret(finalSecret);
    }

    private List<Share> getKRandomShares(List<Share> shares, int k) {
        Collections.shuffle(new ArrayList<>(shares), random);
        return shares.subList(0, k);
    }

    private BigInteger nextRandomBigInteger(BigInteger n) {
        BigInteger result = new BigInteger(n.bitLength(), random);
        while( result.compareTo(n) >= 0 ) {
            result = new BigInteger(n.bitLength(), random);
        }
        return result;
    }

}
