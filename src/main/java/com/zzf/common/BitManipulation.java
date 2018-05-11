package com.zzf.common;

import java.util.Random;

/**
 * 移位运算符
 * 左移位运算符（<<）能将运算符左边的运算对象向左移动运算符右侧指定的位数（在低位补0）。
 * “有符号”右移位运算符（>>）则将运算符左边的运算对象向右移动运算符右侧指定的位数。
 * “有符号”右移位运算符使用了“符号扩展”：若值为正，则在高位插入0；若值为负，则在高位插入1。
 * Java也添加了一种“无符号”右移位运算符（>>>），它使用了“零扩展”：无论正负，都在高位插入0。
 * 原码：一个整数，按照绝对值大小转换成的二进制数，称为原码
 * 反码：将二进制数按位取反，所得的新二进制数称为原二进制数的反码。
 * 补码：反码加1称为补码。
 * 在计算机中，负数以其正值的补码形式表达
 * @author Administrator
 *
 */
public class BitManipulation {
	public static void main(String[] args) {
		Random rand = new Random();
		int i = rand.nextInt();//-1954712664
		int j = rand.nextInt();//255930769
		pBinInt("-1", -1);//11111111111111111111111111111111
		pBinInt("+1", +1);//00000000000000000000000000000001
		int maxpos = 2147483647;
		pBinInt("maxpos", maxpos);//01111111111111111111111111111111
		int maxneg = -2147483648;
		pBinInt("maxneg", maxneg);//10000000000000000000000000000000
		pBinInt("i", i);	//10001011011111010111001110101000(-1954712664)
		pBinInt("~i", ~i);	//01110100100000101000110001010111(1954712663)
		pBinInt("-i", -i);	//01110100100000101000110001011000(1954712664)
		pBinInt("j", j);	//00001111010000010011000110010001(255930769)
		pBinInt("i & j", i & j);//00001011010000010011000110000000(188821888)
		pBinInt("i | j", i | j);//10001111011111010111001110111001(-1887603783)
		pBinInt("i ^ j", i ^ j);//10000100001111000100001000111001(-2076425671)
		pBinInt("i << 5", i << 5);//01101111101011100111010100000000(1873704192)
		pBinInt("i >> 5", i >> 5);//11111100010110111110101110011101(-61084771)
		pBinInt("(~i) >> 5", (~i) >> 5);//00000011101001000001010001100010(61084770)
		pBinInt("i >>> 5", i >>> 5);//00000100010110111110101110011101(73132957)
		pBinInt("(~i) >>> 5", (~i) >>> 5);//00000011101001000001010001100010(61084770)

		long l = rand.nextLong();//2484355767208597707
		long m = rand.nextLong();//49970207502029558
		pBinLong("-1L", -1L);//1111111111111111111111111111111111111111111111111111111111111111
		pBinLong("+1L", +1L);//0000000000000000000000000000000000000000000000000000000000000001
		long ll = 9223372036854775807L;
		pBinLong("maxpos", ll);//0111111111111111111111111111111111111111111111111111111111111111
		long lln = -9223372036854775808L;
		pBinLong("maxneg", lln);//1000000000000000000000000000000000000000000000000000000000000000
		pBinLong("l", l);//0010001001111010001101000110100001011000001010010101100011001011(2484355767208597707)
		pBinLong("~l", ~l);//1101110110000101110010111001011110100111110101101010011100110100(-2484355767208597708)
		pBinLong("-l", -l);//1101110110000101110010111001011110100111110101101010011100110101(-2484355767208597707)
		pBinLong("m", m);//0000000010110001100001111010001110010011100000101101001011110110(49970207502029558)
		pBinLong("l & m", l & m);//0000000000110000000001000010000000010000000000000101000011000010(13515334636032194)
		pBinLong("l | m", l | m);//0010001011111011101101111110101111011011101010111101101011111111(2520810640074595071)
		pBinLong("l ^ m", l ^ m);//0010001011001011101100111100101111001011101010111000101000111101(2507295305438562877)
		pBinLong("l << 5", l << 5);//0100111101000110100011010000101100000101001010110001100101100000(5712408255836920160)
		pBinLong("l >> 5", l >> 5);//0000000100010011110100011010001101000010110000010100101011000110(77636117725268678)
		pBinLong("(~l) >> 5", (~l) >> 5);//1111111011101100001011100101110010111101001111101011010100111001(-77636117725268679)
		pBinLong("l >>> 5", l >>> 5);//0000000100010011110100011010001101000010110000010100101011000110(77636117725268678)
		pBinLong("(~l) >>> 5", (~l) >>> 5);//0000011011101100001011100101110010111101001111101011010100111001(498824634578154809)
	}

	static void pBinInt(String s, int i) {
		System.out.println(s + ", int: " + i + ", binary: ");
		System.out.print("   ");
		for (int j = 31; j >= 0; j--)
			if (((1 << j) & i) != 0)
				System.out.print("1");
			else
				System.out.print("0");
		System.out.println();
	}

	static void pBinLong(String s, long l) {
		System.out.println(s + ", long: " + l + ", binary: ");
		System.out.print("   ");
		for (int i = 63; i >= 0; i--)
			if (((1L << i) & l) != 0)
				System.out.print("1");
			else
				System.out.print("0");
		System.out.println();
	}

}