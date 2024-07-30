package org.example.numeric;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Int32Test {

    @Test
    void testToBinary() {
        Int32 a = new Int32(1);
        System.out.println("1: " + a.toBinary(' '));
        Assertions.assertEquals(a.toBinary(' '),
                "00000000 00000000 00000000 00000001");

        Int32 b = new Int32(-1);
        System.out.println("-1: " + b.toBinary(' '));
        Assertions.assertEquals(b.toBinary(' '),
                "11111111 11111111 11111111 11111111");

        Int32 c = new Int32(2147483647);
        System.out.println("2147483647(2^31 - 1): " + c.toBinary(' '));
        Assertions.assertEquals(c.toBinary(' '),
                "01111111 11111111 11111111 11111111");

        Int32 d = new Int32(-2147483648);
        System.out.println("-2147483648(-2^31): " + d.toBinary(' '));
        Assertions.assertEquals(d.toBinary(' '),
                "10000000 00000000 00000000 00000000");
    }

    @Test
    void testSign() {
        int v = 1;
        Int32 a = new Int32(v);
        System.out.println(v + ":" + (a.sign() ? "-" : "+"));
        Assertions.assertEquals(a.sign(), v < 0);

        v = 0;
        a = new Int32(v);
        System.out.println(v + ":" + (a.sign() ? "-" : "+"));
        Assertions.assertEquals(a.sign(), v < 0);

        v = -1;
        a = new Int32(-1);
        System.out.println(v + ":" + (a.sign() ? "-" : "+"));
        Assertions.assertEquals(a.sign(), v < 0);
    }

    @Test
    void testBit() {
        int v = 2147483645;
        Int32 a = new Int32(v);
        System.out.println(a.toBinary(' '));
        Assertions.assertEquals(a.bit(31), (v & (1 << 31)) != 0);
        Assertions.assertEquals(a.bit(30), (v & (1 << 30)) != 0);
        Assertions.assertEquals(a.bit(2), (v & (1 << 2)) != 0);
        Assertions.assertEquals(a.bit(1), (v & (1 << 1)) != 0);
        Assertions.assertEquals(a.bit(0), (v & (1 << 0)) != 0);
    }

    @Test
    void testAdd() {
        int v1 = 0, v2 = 1;
        Int32 a = new Int32(v1);
        Int32 b = new Int32(v2);
        System.out.println(v1 + " + " + v2 + ": " + a.add(b).toDecimal());
        Assertions.assertEquals(a.add(b).toInt(), v1 + v2);

        v1 = 2;
        v2 = 2147483645;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + " + " + v2 + ": " + a.add(b).toDecimal());
        Assertions.assertEquals(a.add(b).toInt(), v1 + v2);

        v1 = 3;
        v2 = 2147483645;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + " + " + v2 + ": " + a.add(b).toDecimal());
        Assertions.assertEquals(a.add(b).toInt(), v1 + v2);

        v1 = -2;
        v2 = -2147483647;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + " + " + v2 + ": " + a.add(b).toDecimal());
        Assertions.assertEquals(a.add(b).toInt(), v1 + v2);
    }

    @Test
    void testOverflowCheck() {
        Int32 a = new Int32(1);
        Int32 b = new Int32(-2147483648);
        System.out.println("1 + (-2147483648) overflow:" + a.addWithOverflowCheck(b).getRight());
        Assertions.assertFalse(a.addWithOverflowCheck(b).getRight());

        Int32 c = new Int32(1);
        Int32 d = new Int32(2147483647);
        System.out.println("1 + 2147483647 overflow:" + c.addWithOverflowCheck(d).getRight());
        Assertions.assertTrue(c.addWithOverflowCheck(d).getRight());

        Int32 e = new Int32(-3);
        Int32 f = new Int32(-2147483646);
        System.out.println("-3 + (-2147483646) overflow:" + e.addWithOverflowCheck(f).getRight());
        Assertions.assertTrue(e.addWithOverflowCheck(f).getRight());
    }

    @Test
    void testSubtract() {
        int v1 = 20;
        int v2 = 3;
        Int32 a = new Int32(v1);
        Int32 b = new Int32(v2);
        System.out.println(v1 + " - " + v2 + " = " + a.subtract(b).toInt());
        Assertions.assertEquals(a.subtract(b).toInt(), v1 - v2);

        v1 = 100;
        v2 = -30;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + " - " + v2 + " = " + a.subtract(b).toInt());
        Assertions.assertEquals(a.subtract(b).toInt(), v1 - v2);

        v1 = 1;
        v2 = -2147483648;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + " - " + v2 + " = " + a.subtract(b).toInt());
        Assertions.assertEquals(a.subtract(b).toInt(), v1 - v2);

        v1 = -2;
        v2 = 2147483647;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + " - " + v2 + " = " + a.subtract(b).toInt());
        Assertions.assertEquals(a.subtract(b).toInt(), v1 - v2);
    }

    @Test
    void testCompare() {
        int v1 = 0;
        int v2 = 5;
        Int32 a = new Int32(v1);
        Int32 b = new Int32(v2);
        System.out.println(v1 + (Integer.compare(v1, v2) == -1 ? "<" : v1 == v2 ? "==" : ">") + v2);
        Assertions.assertEquals(a.compareTo(b), Integer.compare(v1, v2));

        v1 = -10;
        v2 = -5;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + (Integer.compare(v1, v2) == -1 ? "<" : v1 == v2 ? "==" : ">") + v2);
        Assertions.assertEquals(a.compareTo(b), Integer.compare(v1, v2));

        v1 = -5;
        v2 = 2147483647;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + (Integer.compare(v1, v2) == -1 ? "<" : v1 == v2 ? "==" : ">") + v2);
        Assertions.assertEquals(a.compareTo(b), Integer.compare(v1, v2));

        v1 = -2147483648;
        v2 = 2147483647;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + (Integer.compare(v1, v2) == -1 ? "<" : v1 == v2 ? "==" : ">") + v2);
        Assertions.assertEquals(a.compareTo(b), Integer.compare(v1, v2));

        v1 = -2147483648;
        v2 = -2147483648;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + (Integer.compare(v1, v2) == -1 ? "<" : v1 == v2 ? "==" : ">") + v2);
        Assertions.assertEquals(a.compareTo(b), Integer.compare(v1, v2));
    }

    @Test
    void testMultiply() {
        int v1 = 20, v2 = 32930;
        Int32 a = new Int32(v1);
        Int32 b = new Int32(v2);
        System.out.println(v1 + " * " + v2  + " = " + a.multiply(b).toInt());
        Assertions.assertEquals(a.multiply(b).toInt(), v1 * v2);

        v1 = 303040234;
        v2 = 9382833;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + " * " + v2  + " = " + a.multiply(b).toInt());
        Assertions.assertEquals(a.multiply(b).toInt(), v1 * v2);

        v1 = -23040234;
        v2 = -82833;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + " * " + v2  + " = " + a.multiply(b).toInt());
        Assertions.assertEquals(a.multiply(b).toInt(), v1 * v2);

        v1 = 0;
        v2 = 231;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + " * " + v2  + " = " + a.multiply(b).toInt());
        Assertions.assertEquals(a.multiply(b).toInt(), v1 * v2);

        v1 = -303040234;
        v2 = 2382833;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + " * " + v2  + " = " + a.multiply(b).toInt());
        Assertions.assertEquals(a.multiply(b).toInt(), v1 * v2);
    }

    @Test
    void testMultiplyOverflow(){
        int v1 = 20, v2 = 32930;
        Int32 a = new Int32(v1);
        Int32 b = new Int32(v2);
        System.out.println(((long) v1 * v2) + " == " +  a.multiply(b).toInt());
        Assertions.assertEquals(a.multiplyWithOverflowCheck(b).getRight(),
                (long)v1 * v2 != (long)a.multiply(b).toInt());

        v1 = 303040234;
        v2 = 9382833;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(((long) v1 * v2) + " == " +  a.multiply(b).toInt());
        Assertions.assertEquals(a.multiplyWithOverflowCheck(b).getRight(),
                (long)v1 * v2 != a.multiply(b).toInt());

        v1 = -23040234;
        v2 = -82833;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(((long) v1 * v2) + " == " +  a.multiply(b).toInt());
        Assertions.assertEquals(a.multiplyWithOverflowCheck(b).getRight(),
                (long)v1 * v2 != a.multiply(b).toInt());

        v1 = 0;
        v2 = 231;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(((long) v1 * v2) + " == " +  a.multiply(b).toInt());
        Assertions.assertEquals(a.multiplyWithOverflowCheck(b).getRight(),
                (long)v1 * v2 != a.multiply(b).toInt());

        v1 = -324;
        v2 = 231;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(((long) v1 * v2) + " == " +  a.multiply(b).toInt());
        Assertions.assertEquals(a.multiplyWithOverflowCheck(b).getRight(),
                (long)v1 * v2 != a.multiply(b).toInt());

        v1 = -303040234;
        v2 = 2382833;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(((long) v1 * v2) + " == " +  a.multiply(b).toInt());
        Assertions.assertEquals(a.multiplyWithOverflowCheck(b).getRight(),
                (long)v1 * v2 != a.multiply(b).toInt());

    }

    @Test
    void testDivide() {
        int v1 = -2147483648;
        int v2 = -2147483648;
        Int32 a = new Int32(v1);
        Int32 b = new Int32(v2);
        System.out.println(v1 + " / " + v2 + " = " + a.divide(b).toInt());
        Assertions.assertEquals(v1 / v2, a.divide(b).toInt());

        v1 = 2147483647;
        v2 = -2147483648;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + " / " + v2 + " = " + a.divide(b).toInt());
        Assertions.assertEquals(v1 / v2, a.divide(b).toInt());

        v1 = 2147483647;
        v2 = -2147483647;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + " / " + v2 + " = " + a.divide(b).toInt());
        Assertions.assertEquals(v1 / v2, a.divide(b).toInt());

        v1 = 0;
        v2 = -2147483648;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + " / " + v2 + " = " + a.divide(b).toInt());
        Assertions.assertEquals(v1 / v2, a.divide(b).toInt());

        v1 = 2147483647;
        v2 = 1;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + " / " + v2 + " = " + a.divide(b).toInt());
        Assertions.assertEquals(v1 / v2, a.divide(b).toInt());

        v1 = -2147483648;
        v2 = -1;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + " / " + v2 + " = " + a.divide(b).toInt());
        Assertions.assertEquals(v1 / v2, a.divide(b).toInt());

        v1 = -2147483648;
        v2 = 1;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + " / " + v2 + " = " + a.divide(b).toInt());
        Assertions.assertEquals(v1 / v2, a.divide(b).toInt());

        v1 = -214748;
        v2 = 323;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + " / " + v2 + " = " + a.divide(b).toInt());
        Assertions.assertEquals(v1 / v2, a.divide(b).toInt());

        v1 = 234214748;
        v2 = -48329;
        a = new Int32(v1);
        b = new Int32(v2);
        System.out.println(v1 + " / " + v2 + " = " + a.divide(b).toInt());
        Assertions.assertEquals(v1 / v2, a.divide(b).toInt());
    }

}