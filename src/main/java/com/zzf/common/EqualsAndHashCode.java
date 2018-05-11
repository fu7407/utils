package com.zzf.common;

import java.util.Arrays;
/**
 * 当改写equals()的时候，总是要改写hashCode()
 * 根据一个类的equals方法（改写后），两个截然不同的实例有可能在逻辑上是相等的，
 * 但是，根据Object.hashCode方法，它们仅仅是两个对象。因此，违反了“相等的对象必须具有相等的散列码”。
 * @author zhangzf
 */
public class EqualsAndHashCode {
	private short ashort;
	private char achar;
	private byte abyte;
	private boolean abool;
	private long along;
	private float afloat;
	private double adouble;
	private EqualsAndHashCode aObject;
	private int[] ints;
	private EqualsAndHashCode[] units;

	/**
	 * [1]使用instanceof操作符检查“实参是否为正确的类型”。
　　	 * [2]对于类中的每一个“关键域”，检查实参中的域与当前对象中对应的域值。
　　  * [2.1]对于非float和double类型的原语类型域，使用==比较；
　　  * [2.2]对于对象引用域，递归调用equals方法；
　　  * [2.3]对于float域，使用Float.floatToIntBits(afloat)转换为int，再使用==比较；
　　  * [2.4]对于double域，使用Double.doubleToLongBits(adouble) 转换为int，再使用==比较；
　　  * [2.5]对于数组域，调用Arrays.equals方法。
	 */
	public boolean equals(Object o) {
		if (!(o instanceof EqualsAndHashCode))
			return false;
		EqualsAndHashCode unit = (EqualsAndHashCode) o;
		return unit.ashort == ashort && unit.achar == achar && unit.abyte == abyte && unit.abool == abool
				&& unit.along == along && Float.floatToIntBits(unit.afloat) == Float.floatToIntBits(afloat)
				&& Double.doubleToLongBits(unit.adouble) == Double.doubleToLongBits(adouble)
				&& unit.aObject.equals(aObject) && Arrays.equals(ints,unit.ints) && Arrays.equals(units,unit.units);
	}

	/**
	 * [1]把某个非零常数值，例如17，保存在int变量result中；
　　  * [2]对于对象中每一个关键域f（指equals方法中考虑的每一个域）：
　　  * [2.1]boolean型，计算(f ? 0 : 1);
　　  * [2.2]byte,char,short型，计算(int);
　　  * [2.3]long型，计算(int) (f ^ (f>>>32));
　　  * [2.4]float型，计算Float.floatToIntBits(afloat);
　　  * [2.5]double型，计算Double.doubleToLongBits(adouble)得到一个long，再执行[2.3];
　　  * [2.6]对象引用，递归调用它的hashCode方法;
　　  * [2.7]数组域，对其中每个元素调用它的hashCode方法。
　　  * [3]将上面计算得到的散列码保存到int变量c，然后执行 result=37*result+c;
　　  * [4]返回result。
	 */
	public int hashCode() {
		int result = 17;
		result = 37 * result + (int) ashort;
		result = 37 * result + (int) achar;
		result = 37 * result + (int) abyte;
		result = 37 * result + (abool ? 0 : 1);
		result = 37 * result + (int) (along ^ (along >>> 32));
		result = 37 * result + Float.floatToIntBits(afloat);
		long tolong = Double.doubleToLongBits(adouble);
		result = 37 * result + (int) (tolong ^ (tolong >>> 32));
		result = 37 * result + aObject.hashCode();
		result = 37 * result + intsHashCode(ints);
		result = 37 * result + unitsHashCode(units);
		return result;
	}

	private int intsHashCode(int[] aints) {
		int result = 17;
		for (int i = 0; i < aints.length; i++)
			result = 37 * result + aints[i];
		return result;
	}

	private int unitsHashCode(EqualsAndHashCode[] aUnits) {
		int result = 17;
		for (int i = 0; i < aUnits.length; i++)
			result = 37 * result + aUnits[i].hashCode();
		return result;
	}
}
