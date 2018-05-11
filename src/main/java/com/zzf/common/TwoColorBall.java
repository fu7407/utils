package com.zzf.common;

import java.util.Arrays;
import java.util.Random;

public class TwoColorBall {
	
	/**
	 * 从sum个数字中选出n个数字，flag为true表示允许重复，为false表示不允许重复
	 * @param sum
	 * @param n
	 * @param flag
	 * @return
	 */
	public int [] getBall(int sum,int n,boolean flag){
		if(sum<n)return null;
		Random rd = new Random();
		int [] result = new int[n];
		int temp = -1;
		for(int i=0;i<result.length;i++){
			temp = rd.nextInt(sum-1)+1;//产生的随机数会包括0，所以需要这么处理
			if(!flag && isContain(result,temp)){//不允许重复，并已产生该值
				i--;
			}else{
				result[i] = temp;
			}
		}
		return result;
	}
	
	/**
	 * 数组中是否含有某值ֵ
	 * @param array
	 * @param value
	 * @return
	 */
	public boolean isContain(int [] array,int value){
		for(int i=0;i<array.length;i++){
			if(array[i]==value){
				return true;
			}
		}
		return false;
	}
	
	public String getTwoColorBall(){
		StringBuffer sb = new StringBuffer();
		int [] redBall = getBall(33,6,false);
		Arrays.sort(redBall);
		for(int i=0;i<redBall.length;i++){
			sb.append(redBall[i]+"  ");
		}
		sb.append("+ "+getBall(16,1,false)[0]);
		return sb.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TwoColorBall a = new TwoColorBall();
		System.out.println(a.getTwoColorBall());
	}

}
