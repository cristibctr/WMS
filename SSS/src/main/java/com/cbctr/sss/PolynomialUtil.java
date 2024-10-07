package com.cbctr.sss;

import java.math.BigInteger;

public class PolynomialUtil {
    public static BigInteger evaluate(Polynomial f, BigInteger x) {
        BigInteger result = BigInteger.ZERO;
        for (int i = f.coefficients().length - 1; i >= 0; i--) {
            result = result.add(f.coefficients()[i].multiply(x.modPow(BigInteger.valueOf(f.coefficients().length - 1 - i), f.q()))).mod(f.q());
        }
        return result;
    }
}
