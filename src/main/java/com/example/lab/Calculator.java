package com.example.lab;

/**
 * Lab 2 + Lab 3: Calculator.
 * Quyet dinh thiet ke: chia cho 0 nem ArithmeticException (ro rang hon tra ve Infinity/NaN).
 * Cong/tru/nhan dung Math.*Exact de bao tran so thay vi am tham tran.
 */
public class Calculator {

    public int add(int a, int b) {
        return Math.addExact(a, b);
    }

    public int subtract(int a, int b) {
        return Math.subtractExact(a, b);
    }

    public int multiply(int a, int b) {
        return Math.multiplyExact(a, b);
    }

    public double divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Khong the chia cho 0");
        }
        return (double) a / b;
    }

    public boolean isEven(int input) {
        return input % 2 == 0;
    }

    public int sqrt(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Khong the lay can bac hai cua so am: " + n);
        }
        return (int) Math.sqrt(n);
    }
}
