package org.example.numeric;

import org.apache.commons.lang3.tuple.Pair;

public final class Int32 implements Comparable<Int32> {
    private static final Int32 INT_32_MIN = new Int32(-2147483648);
    private static final Int32 INT_32_MAX = new Int32(2147483647);
    private static final Int32 ZERO = new Int32(0);
    private static final Int32 ONE = new Int32(1);

    private final byte[] arr = new byte[4];

    private static byte parseBinary(String s) {
        byte b = 0;
        for (int i = 7 ; i >= 0; i--) {
            b = (byte)(b | (s.charAt(7 - i) == '1' ? (1 << i) : 0));
        }
        return b;
    }

    public Int32(int value) {
        arr[3] =  (byte)(511 & (value >> 24));
        arr[2] = (byte)(511 & (value >> 16));
        arr[1] = (byte)(511 & (value >> 8));
        arr[0] = (byte)(511 & value);
    }

    public Int32(String binary) {
        arr[3] = parseBinary(binary.substring(0, 8));
        arr[2] = parseBinary(binary.substring(8, 16));
        arr[1] = parseBinary(binary.substring(16, 24));
        arr[0] = parseBinary(binary.substring(24, 32));
    }

    public String toBinary() {
        return toBinary('\0');
    }

    public String toBinary(char separator) {
        StringBuilder builder = new StringBuilder();
        for (int i = 3; i >= 0; i--) {
            for (int j = 7; j >= 0; j--) {
                builder.append((arr[i] & (1 << j)) != 0 ? "1" : "0");
            }
            if (separator != '\0') {
                builder.append(separator);
            }
        }
        if (separator != '\0') {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public Int32 negate() {
        StringBuilder builder = new StringBuilder();
        for (int i = 31; i >= 0; i--) {
            builder.append(bit(i) ? "0" : "1");
        }
        return new Int32(builder.toString()).add(ONE);
    }

    public Int32 add(Int32 b) {
        boolean carry = false;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            int count = 0;
            count += bit(i) ? 1 : 0;
            count += b.bit(i) ? 1 : 0;
            count += carry ? 1 : 0;
            builder.append((count & 1) != 0 ? "1" : "0");
            carry = count >= 2;
        }
        return new Int32(builder.reverse().toString());
    }

    public Pair<Int32, Boolean> addWithOverflowCheck(Int32 b) {
        Int32 result = add(b);
        boolean signA = sign();
        boolean signB = b.sign();
        return Pair.of(result, (signA == signB) & (result.sign() ^ signA));
    }

    public Int32 subtract(Int32 b) {
        return add(b.negate());
    }

    public boolean bit(int i) {
        return (arr[i >> 3] & (1 << (i & 7))) != 0;
    }

    public boolean sign() {
        return (arr[3] >> 7) == -1;
    }

    public String toDecimal() {
        return String.valueOf(toInt());
    }

    public int toInt() {
        int result = 0;
        result = result | (arr[3] << 24);
        result = result | ((arr[2] << 16) & 0X00FFFFFF);
        result = result | ((arr[1] << 8) & 0X0000FFFF);
        result = result | (arr[0] & 0X000000FF);
        return result;
    }

    @Override
    public int compareTo(Int32 b) {
        if (sign() ^ b.sign()) {
            return sign() ? -1 : 1;
        }
        Int32 sub = subtract(b);
        return sub.sign() ? -1 : (sub.toInt() == 0 ? 0 : 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (!(obj instanceof Int32)) {
            return false;
        }
        return toInt() == ((Int32) obj).toInt();
    }

    public Int32 multiply(Int32 b) {
        Int32 sum = ZERO;
        Int32 adder = this;

        for (int i = 0; i < 32; i++) {
            if (b.bit(i)) {
                sum = sum.add(adder);
            }
            adder = adder.add(adder);
        }
        return sum;
    }

    public Pair<Int32, Boolean> multiplyWithOverflowCheck(Int32 b) {
        Int32 result = multiply(b);
        if ((sign() ^ b.sign()) ^ result.sign()) {
            return Pair.of(result, true);
        }

        long longResult = (long) toInt() * b.toInt();
        int upper32 = (int)(longResult >> 32);
        return Pair.of(result, result.sign() ? upper32 != -1 : upper32 != 0);
    }

    public Int32 divide(Int32 b) {
        if (toInt() == 0) {
            return this;
        }

        if (b.toInt() == 0) {
            throw new ArithmeticException("divisor is zero");
        }

        boolean signA = sign();
        boolean signB = b.sign();

        if (signA ^ signB) {
            if (signA) {
                if (INT_32_MIN.equals(this)) {
                    return add(b).negate().divide(b).add(ONE).negate();
                } else {
                    return negate().divide(b).negate();
                }
            } else {
                if (INT_32_MIN.equals(b)) {
                    return ZERO;
                } else {
                    return divide(b.negate()).negate();
                }
            }
        }

        if (signA) {
            if (INT_32_MIN.equals(b)) {
                return INT_32_MIN.equals(this) ? ONE : ZERO;
            } else {
                if (INT_32_MIN.equals(this)) {
                    return subtract(b).negate().divide(b.negate()).add(ONE);
                } else {
                    return negate().divide(b.negate());
                }
            }
        }

        // this > 0, b > 0
        return doDivide(this, b).getLeft();
    }

    private static Pair<Int32, Int32> doDivide(Int32 a, Int32 b) {
        if (a.compareTo(b) < 0) {
            return Pair.of(ZERO, ZERO);
        }
        Int32 doubleDivisor = b.add(b);
        if (doubleDivisor.sign()) {
            // overflow
            return Pair.of(ONE, ZERO);
        }

        Pair<Int32, Int32> pair = doDivide(a, doubleDivisor);
        Int32 quotient = pair.getLeft();
        Int32 sum = pair.getRight();
        sum = sum.add(doubleDivisor).compareTo(a) <= 0 ? sum.add(doubleDivisor) : sum;

        if (a.subtract(sum).compareTo(b) < 0) {
            return Pair.of(quotient.add(quotient), sum);
        } else {
            return Pair.of(quotient.add(quotient).add(ONE), sum);
        }
    }
}
