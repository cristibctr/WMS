package com.cbctr.sss;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class ShamirSchemeTest {
    ShamirScheme shamirScheme;

    @Before
    public void setup() {
        shamirScheme = new ShamirScheme();
    }

    @Test
    public void shamirScheme_correct_number_of_shares() {
        int n = 5, q = 11;
        Polynomial f = new Polynomial(
                new BigInteger[]{BigInteger.valueOf(4), BigInteger.valueOf(6), BigInteger.valueOf(2)},
                BigInteger.valueOf(q));
        List<Share> shares = shamirScheme.getShares(n, f);
        assertThat(shares.size(), is(n));
    }

    @Test
    public void shamirScheme_correct_share_values() {
        int n = 3, q = 11;
        Polynomial f = new Polynomial(
                new BigInteger[]{BigInteger.valueOf(-4), BigInteger.valueOf(6), BigInteger.valueOf(2), BigInteger.valueOf(0)},
                BigInteger.valueOf(q));
        List<Share> shares = shamirScheme.getShares(n, f);
        assertThat(shares, hasItems(
                new Share(BigInteger.valueOf(1), BigInteger.valueOf(4)),
                new Share(BigInteger.valueOf(2), BigInteger.valueOf(7)),
                new Share(BigInteger.valueOf(3), BigInteger.valueOf(7))));
    }

    @Test
    public void getSecret_reconstructs_original_secret_with_known_shares() {
        int q = 11;
        Secret secret = new Secret(BigInteger.valueOf(10));
        BigInteger[] coefficients = new BigInteger[]{
                BigInteger.valueOf(2),
                BigInteger.valueOf(7),
                BigInteger.valueOf(10)
        };
        Polynomial f = new Polynomial(coefficients, BigInteger.valueOf(q));

        List<Share> shares = Arrays.asList(
                new Share(BigInteger.valueOf(1), PolynomialUtil.evaluate(f, BigInteger.valueOf(1))),
                new Share(BigInteger.valueOf(2), PolynomialUtil.evaluate(f, BigInteger.valueOf(2))),
                new Share(BigInteger.valueOf(3), PolynomialUtil.evaluate(f, BigInteger.valueOf(3))),
                new Share(BigInteger.valueOf(4), PolynomialUtil.evaluate(f, BigInteger.valueOf(4))),
                new Share(BigInteger.valueOf(5), PolynomialUtil.evaluate(f, BigInteger.valueOf(5)))
        );

        Secret reconstructedSecret = shamirScheme.getSecret(List.of(new Share[]{shares.get(0), shares.get(1), shares.get(3)}), BigInteger.valueOf(q));

        assertThat(reconstructedSecret, is(secret));
    }
}
