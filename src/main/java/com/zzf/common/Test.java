package com.zzf.common;

import java.util.Arrays;

public class Test {

	/**
	 * 根据行数、列数得到螺旋整列(根据‘回’字（即多个口字）来考虑) 1 2 3 4 5 16 17 18 19 6 15 24 25 20 7 14
	 * 23 22 21 8 13 12 11 10 9
	 * 
	 * @param rows
	 * @param cols
	 */
	public void gethelix(int rows, int cols) {
		int sum = 1;
		int array[][] = new int[rows][cols];
		int index = 0;
		while (rows * cols >= sum) {
			for (int i = index; i < rows - index; i++) {// 一个口字一个口字的写
				if (i == index) {
					for (int j = index; j < cols - index; j++) {// ‘口’字上面一横
						array[i][j] = sum;
						sum++;
					}
				} else if (i == rows - index - 1) {
					for (int j = index; j < cols - index; j++) {// ‘口’字下面一横
						array[i][cols - j - 1] = sum;
						sum++;
					}
					for (int k = i - 1; k >= index + 1; k--) {// ‘口’字左面一竖
						array[k][index] = sum;
						sum++;
					}
				} else {
					for (int j = index; j < cols - index; j++) {// ‘口’字右面一竖
						array[i][cols - index - 1] = sum;
						sum++;
						break;
					}
				}
			}
			index++;
		}

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				System.out.print(array[i][j] + "\t");
			}
			System.out.println("");
		}
	}

	/** 
	 * 有N个人围成一圈，第一个人从1开始报数，报到M的人出列，求最后一个出列的人。 这是一个约瑟夫环问题
	 * @param total  总的人数 
	 * @param num    第几号出队 
	 */
	public void queue(int total, int num) {
		boolean[] arr = new boolean[total]; // 定义一个数组，true表示没有出队列的，false表示已经出队列的
		Arrays.fill(arr, true);
		int next = 1;// 移动变量，如：1 2 3 1 2 3 1 2
		int index = 0; // 数组下标
		int count = total; // 剩下的人数

		// 如果剩下的人数为1人时，停止报数
		while (count > 1) {
			if (arr[index] == true) {
				if (next == 3) {
					arr[index] = false;
					--count; // 剩下的人数减1
					next = 1; // 移动变量复位，从1开始报数
					System.out.println("依次出列的人为：" + (index + 1));
				} else {
					++next;
				}
			}
			++index;
			if (index == total) {
				index = 0; // 数组下标复位，从0开始新重遍历
			}
		}
		for (int i = 0; i < total; i++) {
			if (arr[i] == true) {
				System.out.println("最后出列的人为：" + (i + 1));
			}
		}
	}

	/**
	 * 递归问题，后一个数字等于前两个数字之和
	 * 一列数的规则如下：1、1、2、3、5、8、13、21、34……求第30位数是多少
	 * @param value
	 */
	public int recursion(int value) {
		if (value == 1 || value == 2)
			return 1;
		else
			return recursion(value - 1) + recursion(value - 2);
	}

	/**
	 * 有一个集合a，里面有n个正整数，乱序排列。给定一个正整数N，求a中任意两个数相加等于N，共有哪些种组合情况。
	 * 如，集合为{1,3,44,2,4,5,54,222,368}  N=6,则结果集为{1,5}，{2,4}
	 */
	public String getSum(int[] array, int n) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			if (array[i] >= n)
				continue;
			for (int j = i + 1; j < array.length; j++) {
				if (array[i] + array[j] == n)
					sb.append("{").append(array[i]).append(",").append(array[j]).append("}");
			}
		}
		return sb.toString();
	}

	/**
	 * 实现一个函数，对一个正整数n，算得到1需要的最少操作次数。
	 * 操作规则为：如果n为偶数，将其除以2；如果n为奇数，可以加1或减1；一直处理下去。
	 * @param n
	 * @return
	 */
	public int func(int n) {
		if (n == 1)
			return 0;
		if (n % 2 == 0)
			return 1 + func(n / 2);
		int x = func(n + 1);
		int y = func(n - 1);
		if (x > y)
			return y + 1;
		else
			return x + 1;
	}

	/**
	 * 将一个十六进制数的字符串参数转换成整数
	 * @param str "13abf"
	 * @return
	 */
	public int getSum(String str) {
		int len = str.length();
		int sum = 0;
		for (int i = 0; i < len; i++) {
			char c = str.charAt(len - 1 - i);
			int n = Character.digit(c, 16);// 返回使用指定基数的字符 ch 的数值(如c=f，返回15)
			sum += n * (1 << (4 * i)); // n乘16*i
		}
		return sum;
	}

	public void get() {
		for (int a = 1; a < 10; a++) {
			for (int b = 1; b < 10; b++) {
				for (int c = 1; c < 10; c++) {
					if ((a * 100 + b * 10 + c) < (a * 100 + c * 10 + b)
							&& (a * 100 + b * 10 + c) < (b * 100 + a * 10 + c)
							&& (a * 100 + b * 10 + c) < (b * 100 + c * 10 + a)
							&& (a * 100 + b * 10 + c) < (c * 100 + a * 10 + b)
							&& (a * 100 + b * 10 + c) < (c * 100 + b * 10 + a)) {
						if (a * 100 + c * 10 + b + b * 100 + a * 10 + c + b * 100 + c * 10 + a + c * 100 + a * 10 + b
								+ c * 100 + b * 10 + a == 3194) {
							System.out.println("a=" + a + ",b=" + b + ",c=" + c);
						}
					}
				}
			}
		}
		System.out.println("over");
	}

	public static void func() {
		int[] arr = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		Arrays.stream(arr).map((x) -> x = x + 1).forEach(System.out::println);
		Arrays.stream(arr).forEach(System.out::println);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Test test = new Test();
		// test.gethelix(5, 6);
		// test.queue(10, 6);
		// char ss='a';
		// char kk='A';
		// System.out.println(ss>kk);
		// System.out.println( test.getSum(new
		// int[]{1,3,44,2,4,5,54,222,368},8));
		// System.out.println("j="+test.getSum("13abf"));
		// test.get();//testdev
		// String tt = "10.000";
		// int index = tt.indexOf(".");
		// System.out.println(tt.substring(index + 1, tt.length()));
		// System.out.println(tt.substring(0, index));
		test.func();
	}

}
