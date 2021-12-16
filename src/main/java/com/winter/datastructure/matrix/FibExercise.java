package com.winter.datastructure.matrix;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * 斐波那契练习
 * f(n)=f(n-1)+f(n-3)
 *
 * @author zhangdongdong
 * @date 2021-12-15
 */
public class FibExercise {
    /**
     * 计算n年后奶牛数量 f(n)=f(n-1)+f(n-3)
     *
     * @param n
     * @return
     */
    public static long calFibCow(long n) {
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        if (n == 3) {
            return 3;
        }
        long[] ary = new long[]{1, 2, 3};
        for (long i = 4; i <= n; i++) {
            long an = ary[2] + ary[0];
            ary[0] = ary[1];
            ary[1] = ary[2];
            ary[2] = an;
        }
        return ary[2];
    }

    /**
     * 根据矩阵快速幂计算斐波那契数列
     *
     * @param n   第n项
     * @param mod 取模
     * @return
     */
    public static long calFibByMatrixPow(int n, int mod) {
        if (n == 1) {
            return mod > 0 ? 1 % mod : 1;
        }
        if (n == 2) {
            return mod > 0 ? 2 % mod : 2;
        }
        if (n == 3) {
            return mod > 0 ? 3 % mod : 3;
        }
        //最开始3年
        long[][] baseCow = new long[][]{{1}, {2}, {3}};
        //用于计算的原始矩阵
        long[][] matrixA = new long[][]{{0, 1, 0}, {0, 0, 1}, {1, 0, 1}};
        //计算矩阵的n-1次方
        matrixA = calMatrixPow(matrixA, n - 1, mod);
        //与基础矩阵计算
        matrixA = matrixMultiply(matrixA, baseCow, mod);
        return mod > 0 ? matrixA[0][0] % mod : matrixA[0][0];
    }

    /**
     * 计算一个数的n次方
     *
     * @param a 数字
     * @param n 次方
     * @return
     */
    public static long calPow(long a, int n) {
        long ax = 1;
        while (n > 0) {
            if ((n & 1) == 1) {
                ax = ax * a;
            }
            n = n >> 1;
            if (n > 0) {
                a = a * a;
            }
        }
        return ax;
    }

    /**
     * 计算矩阵的n次方
     *
     * @param a
     * @param n
     * @return
     */
    public static long[][] calMatrixPow(long[][] a, int n, int mod) {
        long[][] ax = null;
        while (n > 0) {
            if ((n & 1) == 1) {
                ax = ax == null ? a : matrixMultiply(ax, a, mod);
            }
            n = n >> 1;
            if (n > 0) {
                a = matrixMultiply(a, a, mod);
            }
        }
        return ax;
    }

    /**
     * 矩阵乘法计算
     *
     * @param a
     * @param b
     * @return
     */
    public static long[][] matrixMultiply(long[][] a, long[][] b, int mod) {
        if (a[0].length != b.length) {
            throw new IllegalArgumentException("矩阵a的列数与矩阵b的行数不等");
        }
        long[][] result = new long[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                for (int k = 0; k < b.length; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                    if (mod > 0) {
                        result[i][j] = result[i][j] % mod;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 打印矩阵的方法
     *
     * @param a
     */
    public static void printMatrix(long[][] a) {
        for (long[] bb : a) {
            System.out.println(Arrays.toString(bb));
        }
    }

    public static void main(String[] args) {
        int year = 100;
        int mod = 1000000007;
        long sourceFib = calFibCow(year);
        System.out.println("线性算出" + year + "年奶牛数量:" + sourceFib + ",取模：" + mod + " 后 :" + sourceFib % mod);
        System.out.println("快速幂算出2的11次方");
        System.out.println(calPow(2, 11));
        System.out.println("测试矩阵计算");
        printMatrix(matrixMultiply(new long[][]{{1, 2}, {1, 3}}, new long[][]{{3}, {4}}, 1));
        long[][] aa = new long[][]{{1, 2}, {1, 3}};
        long[][] bb = new long[][]{{1, 1}, {1, 1}};
        for (int i = 0; i < 5; i++) {
            bb = matrixMultiply(aa, bb, mod);
        }
        System.out.println("----------------------");
        printMatrix(bb);
        bb = calMatrixPow(aa, 5, mod);
        System.out.println("-------------");
        printMatrix(bb);
        System.out.println("------------------------");
        //最开始3年
        long[][] baseCow = new long[][]{{1}, {2}, {3}};
        //用于计算的原始矩阵
        long[][] matrixA = new long[][]{{0, 1, 0}, {0, 0, 1}, {1, 0, 1}};
        matrixA = calMatrixPow(matrixA, year - 1, mod);
        matrixA = matrixMultiply(matrixA, baseCow, mod);
        System.out.println("矩阵快速幂算出" + year + "年奶牛数量:" + matrixA[0][0] + ",取模:" + mod + " 后的值:" + matrixA[0][0] % mod);
        printMatrix(matrixA);
        System.out.println("------------------直接矩阵幂-----");
        System.out.println(calFibByMatrixPow(year, mod));


    }
}
