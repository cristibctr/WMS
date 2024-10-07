package com.cbctr.sss;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class ShamirScheme {
    private final Random random;

    public ShamirScheme(Random random) {
        this.random = random;
    }

    public static void main(String[] args) {
        int n = 5, k = 3, q = 11;
        Secret secret = new Secret(BigInteger.valueOf(Integer.parseInt(args[0])));
        ShamirScheme shamirScheme = new ShamirScheme(new Random());
        Polynomial f = shamirScheme.getPolynomial(secret, k, BigInteger.valueOf(q));
        List<Share> shares = shamirScheme.getShares(n, f);
    }

    public Polynomial getPolynomial(Secret s, int k, BigInteger q) {
        BigInteger[] coefficients = Stream.concat(
                random.ints().limit(k).mapToObj(BigInteger::valueOf),
                Stream.of(s.secret())
        ).toArray(BigInteger[]::new);
        return new Polynomial(coefficients, q);
    }

    public List<Share> getShares(int n, Polynomial f) {
        return random.ints().limit(n).mapToObj(BigInteger::valueOf)
                .map(x -> new Share(x, PolynomialUtil.evaluate(f, x)))
                .toList();
    }

    public Secret getSecret(List<Share> shares, int q) {
        BigInteger finalSecret = BigInteger.ZERO;
        BigInteger Q = BigInteger.valueOf(q);
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
            finalSecret = finalSecret.add(share.y().multiply(multiplications).mod(Q));
        }
        return new Secret(finalSecret);
    }


}
