package example.core.multithreading;

import java.math.BigInteger;

public class PowerCalculator {

    public static BigInteger calculate(BigInteger base, BigInteger power ){

        BigInteger result = BigInteger.ONE;

        for( BigInteger i = BigInteger.ONE; i.compareTo(power) <= 0; i = i.add(BigInteger.ONE) ) {
            result = result.multiply(base);
        }

        return result;
    }

}
