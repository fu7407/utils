package com.zzf.common.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具
 * @author 张增福
 */
public class StringUtil {

	/**
	 * 字符串替换函数,String的replace函数不能处理'|'符号
	 * @param strSource 被替换的源字符串
	 * @param strFrom   要查找并替换的子字符串
	 * @param strTo     要替换为的子字符串
	 * @return 替换完成的字符串
	 */
	public static String replace(String strSource, String strFrom, String strTo) {
		if ("".equals(strFrom))
			return strSource;
		StringBuffer sb = new StringBuffer();
		int intPos;
		while ((intPos = strSource.indexOf(strFrom)) != -1) {
			sb.append(strSource.substring(0, intPos)).append(strTo);
			strSource = strSource.substring(intPos + strFrom.length());
		}
		return sb.append(strSource).toString();
	}

	/**
	 * 将 Long/Double 数据 整数位前面补 位 例如: 31 补位后变成 0031 数据.
	 * @param num Long 被格式化前的数据
	 * @param formatStr 需要 格式化的数据样式
	 * @return
	 */
	public static String numberFillDigit(Object num, String formatStr) {
		NumberFormat formatter = NumberFormat.getNumberInstance();
		int bit = formatStr.length();
		formatter.setMinimumIntegerDigits(bit);
		formatter.setGroupingUsed(false);
		return formatter.format(num);
	}

	/**
	 * 清trim
	 * @param strSource 被替换的源字符串
	 * @return 替换完成的字符串
	 */
	public static String trim(Object strSource) {
		if (strSource == null)
			return null;
		return String.valueOf(strSource).trim();
	}

	/**
	 * 截取字符串左侧指定长度的字符串
	 * @param input 输入字符串
	 * @param count 截取长度
	 * @return 截取字符串
	 */
	public static String left(String input, int count) {
		if (isEmpty(input))
			return "";
		count = (count > input.length()) ? input.length() : count;
		return input.substring(0, count);
	}

	/**
	 * 截取字符串右侧指定长度的字符串
	 * @param input 输入字符串
	 * @param count 截取长度
	 * @return 截取字符串
	 */
	public static String right(String input, int count) {
		if (isEmpty(input))
			return "";
		count = (count > input.length()) ? input.length() : count;
		return input.substring(input.length() - count, input.length());
	}

	/**
	 * 从指定位置开始截取指定长度的字符串
	 * @param input 输入字符串
	 * @param index 截取位置，左侧第一个字符索引值是1
	 * @param count 截取长度
	 * @return 截取字符串
	 */
	public static String middle(String input, int index, int count) {
		if (isEmpty(input))
			return "";
		count = (count > input.length() - index + 1) ? input.length() - index + 1 : count;
		return input.substring(index - 1, index + count - 1);
	}

	/**
	 * 将一种字符编码（charSet1）转换为另一种字符编码（charSet2）
	 * @param input
	 * @param charSet1 原字符编码
	 * @param charSet2 目标字符编码
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String CharSetToCharSet(String input, String charSet1, String charSet2) throws UnsupportedEncodingException {
		if (isEmpty(input))
			return "";
		return new String(input.getBytes(charSet1), charSet2);
	}

	/**
	 * Unicode转换成GBK字符集
	 * @param input 待转换字符串
	 * @return 转换完成字符串
	 */
	public static String UnicodeToGB(String input) throws UnsupportedEncodingException {
		return CharSetToCharSet(input, "ISO8859_1", "GBK");
	}

	/**
	 * Unicode转换成UTF-8字符集
	 * @param input 待转换字符串
	 * @return 转换完成字符串
	 */
	public static String UnicodeToUTF8(String input) throws UnsupportedEncodingException {
		return CharSetToCharSet(input, "ISO8859_1", "UTF-8");
	}

	/**
	 * 获得字符串编码
	 * @param input
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getCharsetName(String input) throws UnsupportedEncodingException {
		Map<String, Charset> charsets = Charset.availableCharsets();
		for (Map.Entry<String, Charset> entry : charsets.entrySet()) {
			if (input.equals(new String(input.getBytes(entry.getKey()), entry.getKey()))) {//判断当前字符串的编码格式
				return entry.getKey();
			}
		}
		return "";
	}

	/**
	 * 分隔字符串成数组.(使用StringTokenizer，String的split函数不能处理'|'符号)
	 * @param input 输入字符串
	 * @param delim 分隔符
	 * @return 分隔后数组
	 */
	public static String[] splitString(String input, String delim) {
		if (isEmpty(input))
			return null;
		ArrayList<String> al = new ArrayList<String>();
		for (StringTokenizer stringtokenizer = new StringTokenizer(input, delim); stringtokenizer.hasMoreTokens(); al.add(stringtokenizer.nextToken())) {
		}
		String result[] = new String[al.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = (String) al.get(i);
		}
		return result;
	}

	/**
	 * 判断字符是否为空
	 * @param input 某字符串
	 * @return 包含则返回true，否则返回false
	 */
	public static boolean isEmpty(String value) {
		return value == null || "".equals(value.trim());
	}

	/**
	 * 判断某字符串是否由数字组成
	 * @param value
	 * @return
	 */
	public static boolean isNumber(String value) {
		String pat = "[0-9]+";
		if (isFitRegex(value, pat)) {
			return true;
		}
		return false;
	}

	/**
	 * 是否符合日期规则（2011-05-11）
	 * 也可以用String类的方法:value.matches("\\d{4}-\\d{2}-\\d{2}")
	 * @param value
	 * @return
	 */
	public static boolean isDate(String value) {
		String pat = "\\d{4}-\\d{2}-\\d{2}";
		if (isFitRegex(value, pat)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断value值是否符合某个正则表达式的规则
	 * @param value
	 * @param regex
	 * @return
	 */
	public static boolean isFitRegex(String value, String regex) {
		if (isEmpty(value))
			return false;
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(value);
		if (m.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 字符串content中是否包含有正则表达式regex的子字符串
	 * @param content
	 * @param regex
	 * @return
	 */
	public static boolean getIsContent(String content, String regex) {
		if (isEmpty(content))
			return false;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		return matcher.find();
	}

	/**
	 * 按照字符串的数字将字符串拆分（输入：A1B22C333D4444E55555F,返回数组：{"A","B","C","D","E","F"}）
	 * 也可以用String类的方法:value.split("\\d+")
	 * 根据"|"来切割时，因为"|"在正则表达式中表示或的概念，所以需要对其进行转义，应该这样调用value.split("\\|")；
	 * @param value
	 * @return
	 */
	public static String[] split(String value) {
		if (isEmpty(value))
			return null;
		String regex = "\\d+";
		Pattern p = Pattern.compile(regex);
		return p.split(value);
	}

	/**
	 * 将全部的数字替换为"_"（输入：A1B22C333D4444E55555F,返回：A_B_C_D_E_F）
	 * 也可以用String类的方法:value.replaceAll("\\d+", "_");
	 * @param value
	 * @return
	 */
	public static String replaceAll(String value) {
		if (isEmpty(value))
			return null;
		String regex = "\\d+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(value);
		return m.replaceAll("_");
	}

	/**
	 * 字符串反转（如输入“abcdef” 则输出“fedcba”）
	 * @param str
	 * @return
	 */
	public static String reverse(String str) {
		if (str == null)
			return null;
		return new StringBuffer(str).reverse().toString();
	}

	/**
	 * 获得0-9,a-z,A-Z范围的随机数
	 * @param length 随机数长度
	 * @return String
	 */
	public static String getRandomChar(int length) {
		char[] chr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
				'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
				'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		Random random = new Random();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buffer.append(chr[random.nextInt(62)]);
		}
		return buffer.toString();
	}

	/**
	 * 
	 * @param _double
	 * @param dfpatten "#,##0.00"
	 * @return
	 */
	public static String formatDoubleToString(double _double, String dfpatten) {
		java.text.DecimalFormat df = new java.text.DecimalFormat(dfpatten);
		return df.format(_double);
	}

	public static String filterHTML(String input) {
		if (isEmpty(input))
			return "";
		StringBuffer filtered = new StringBuffer();
		for (int i = 0; i <= input.length() - 1; i++) {
			switch (input.charAt(i)) {
				case '&':
					filtered.append("&amp;");
					break;
				case '<':
					filtered.append("&lt;");
					break;
				case '>':
					filtered.append("&gt;");
					break;
				case '"':
					filtered.append("&#034;");
					break;
				case '\'':
					filtered.append("&#039;");
					break;
				default:
					filtered.append(input.charAt(i));
			}
		}
		return (filtered.toString());
	}

	/**
	 * 全角 转换成 -> 半角
	 * @param QJstr
	 * @return String
	 */
	public static final String QBchange(String QJstr) {
		if (isEmpty(QJstr))
			return "";
		StringBuffer outStr = new StringBuffer();
		String Tstr = "";
		byte[] b = null;
		try {
			for (int i = 0; i < QJstr.length(); i++) {
				Tstr = QJstr.substring(i, i + 1);
				b = Tstr.getBytes("unicode");
				if (b[3] == -1) {
					b[2] = (byte) (b[2] + 32);
					b[3] = 0;
					outStr.append(new String(b, "unicode"));
				} else {
					outStr.append(Tstr);
				}
			}
		} catch (java.io.UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return outStr.toString();
	}

	public static void fun1() {// ASCII转换为字符串
		String s = "22307 35806 24555 20048";// ASCII码
		String[] chars = s.split(" ");
		System.out.println("ASCII 汉字 \n----------------------");
		for (int i = 0; i < chars.length; i++) {
			System.out.println(chars[i] + " " + (char) Integer.parseInt(chars[i]));
		}
	}

	/**
	 * 字符串转换为ASCII码
	 * 
	 */
	public static void fun2() {
		String s = "新年快乐！";// 字符串
		char[] chars = s.toCharArray(); // 把字符中转换为字符数组    
		for (int i = 0; i < chars.length; i++) {// 输出结果    
			System.out.println(" " + chars[i] + " " + (int) chars[i]);
		}
	}

	/**
	 * 编写一个截取字符串的函数，输入为一个字符串和字节数，输出为按字节截取的字符串。但是要保证汉字不被截半个，
	 * 如“我ABC”4，应该截为“我AB”，输入“我ABC汉DEF”，6，应该输出为“我ABC”而不是“我ABC+汉的半个”
	 * @param str
	 * @param bytes
	 * @return
	 */
	public static String SplitIt(String str, int bytes) {
		int loopCount = (str.length() % bytes == 0) ? (str.length() / bytes) : (str.length() / bytes + 1);
		for (int i = 1; i <= loopCount; i++) {
			if (i == loopCount) {
				return str.substring((i - 1) * bytes, str.length());
			} else {
				return str.substring((i - 1) * bytes, (i * bytes));
			}
		}
		return "";
	}

	/**
	 * 编写一个截取字符串的函数，输入为一个字符串和字节数，输出为按字节截取的字符串。但是要保证汉字不被截半个，
	 * 如“我ABC”4，应该截为“我AB”，输入“我ABC汉DEF”，6，应该输出为“我ABC”而不是“我ABC+汉的半个”
	 * @param str
	 * @param n
	 * @return
	 * @throws Exception
	 */
	public static String trimGBK(String str, int n) throws Exception {
		byte[] buf = str.getBytes("GBK");
		int num = 0;
		boolean bChineseFirstHalf = false;
		for (int i = 0; i < n; i++) {
			if (buf[i] < 0 && !bChineseFirstHalf) {
				bChineseFirstHalf = true;
			} else {
				num++;
				bChineseFirstHalf = false;
			}
		}
		return str.substring(0, num);
	}

	public static void regxChNNum() {
		String source = "[你好][123][很好][11][测试一下][86]";
		String reg_charset = "([\u4E00-\u9FA5]{1,10}\\]\\[[0-9]{1,10})";
		Pattern p = Pattern.compile(reg_charset);
		Matcher m = p.matcher(source);
		String temp = "";
		while (m.find()) {
			temp = m.group(1);
			System.out.println(temp.replace("][", "(") + ")");
		}
	}

	public static boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!isChinese(c)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	/**
	 * 根据阿拉伯数字得到中文数字（如：输入12300.3，则输出：壹万贰仟叁佰.叁）
	 * @param moneyStr
	 * @return
	 */
	public static String convertToChineseNumber(String moneyStr) {
		HashMap<String, String> chineseNumberMap = new HashMap<String, String>();
		chineseNumberMap.put("0", "零");
		chineseNumberMap.put("1", "壹");
		chineseNumberMap.put("2", "贰");
		chineseNumberMap.put("3", "叁");
		chineseNumberMap.put("4", "肆");
		chineseNumberMap.put("5", "伍");
		chineseNumberMap.put("6", "陆");
		chineseNumberMap.put("7", "柒");
		chineseNumberMap.put("8", "捌");
		chineseNumberMap.put("9", "玖");
		chineseNumberMap.put(".", ".");
		HashMap<String, String> chineseMoneyPattern = new HashMap<String, String>();
		chineseMoneyPattern.put("1", "拾");
		chineseMoneyPattern.put("2", "佰");
		chineseMoneyPattern.put("3", "仟");
		chineseMoneyPattern.put("4", "万");
		chineseMoneyPattern.put("5", "拾");
		chineseMoneyPattern.put("6", "佰");
		chineseMoneyPattern.put("7", "仟");
		chineseMoneyPattern.put("8", "亿");

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < moneyStr.length(); i++) {
			sb.append(chineseNumberMap.get(moneyStr.substring(i, i + 1)));
		}
		String fractionPart = "";
		int moneyPatternCursor = 1;
		int indexOfDot = sb.indexOf(".");
		if (indexOfDot != -1) {//有小数点
			for (int i = indexOfDot - 1; i > 0; i--) {
				sb.insert(i, chineseMoneyPattern.get("" + moneyPatternCursor));
				moneyPatternCursor = moneyPatternCursor == 8 ? 1 : moneyPatternCursor + 1;
			}
			fractionPart = sb.substring(sb.indexOf("."));
			sb.delete(sb.indexOf("."), sb.length());
		} else {//没有小数点
			for (int i = sb.length() - 1; i > 0; i--) {
				sb.insert(i, chineseMoneyPattern.get("" + moneyPatternCursor));
				moneyPatternCursor = moneyPatternCursor == 8 ? 1 : moneyPatternCursor + 1;
			}
		}
		while (sb.indexOf("零拾") != -1) {
			sb.replace(sb.indexOf("零拾"), sb.indexOf("零拾") + 2, "零");
		}
		while (sb.indexOf("零佰") != -1) {
			sb.replace(sb.indexOf("零佰"), sb.indexOf("零佰") + 2, "零");
		}
		while (sb.indexOf("零仟") != -1) {
			sb.replace(sb.indexOf("零仟"), sb.indexOf("零仟") + 2, "零");
		}
		while (sb.indexOf("零万") != -1) {
			sb.replace(sb.indexOf("零万"), sb.indexOf("零万") + 2, "万");
		}
		while (sb.indexOf("零亿") != -1) {
			sb.replace(sb.indexOf("零亿"), sb.indexOf("零亿") + 2, "亿");
		}
		while (sb.indexOf("零零") != -1) {
			sb.replace(sb.indexOf("零零"), sb.indexOf("零零") + 2, "零");
		}
		if (sb.lastIndexOf("零") == sb.length() - 1)
			sb.delete(sb.length() - 1, sb.length());
		return sb.append(fractionPart).toString();
	}

	/**
	 * 金额转换（如：输入1011，则输出：壹仟零壹拾壹元）
	 * @param moneyStr
	 * @return
	 */
	public static String convertToChineseNumber(int money) {
		char[] data = new char[] { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' };
		char[] units = new char[] { '元', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿' };
		StringBuffer sb = new StringBuffer();
		int unit = 0;
		while (money != 0) {
			sb.insert(0, units[unit++]);
			int number = money % 10;
			sb.insert(0, data[number]);
			money /= 10;
		}
		return sb.reverse().toString().replaceAll("零[拾佰仟]", "零").replaceAll("零+万", "万").replaceAll("零+元", "元").replaceAll("零+", "零");
	}

	/**
	 * 求List中的各字符串的最大公子串
	 * @param strings
	 * @return
	 */
	public static String findMaxSub(List strings) {
		String s = (String) strings.get(0);
		int len = s.length();
		int i = 0;
		String maxSub = "";
		while (i < len - maxSub.length()) {
			for (int j = len; j >= i + maxSub.length(); j--) {
				String sub = s.substring(i, j);
				int p = 1;
				while (p < strings.size() && contains((String) strings.get(p), sub))
					p++;
				if (p == strings.size()) {
					maxSub = sub;
					break;
				}
			}
			i++;
		}
		return maxSub;
	}

	/**
	 * main字符串中是否包含sub子字符串
	 * @param main
	 * @param sub
	 * @return
	 */
	public static boolean contains(String main, String sub) {
		if (main.indexOf(sub) != -1) {
			return true;
		}
		return false;
	}

	/**
	 * 加密算法
	 */
	public static String MD5(String str) {
		return encodeBase64(str.getBytes());
	}

	public static String encodeBase64(byte[] data) {
		String cvt = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789#@$";
		int fillchar = '*';
		int c;
		int len = data.length;
		StringBuffer ret = new StringBuffer(((len / 3) + 1) * 4);
		for (int i = 0; i < len; ++i) {
			c = (data[i] >> 2) & 0x3f;
			ret.append(cvt.charAt(c));
			c = (data[i] << 4) & 0x3f;
			if (++i < len) {
				c |= (data[i] >> 4) & 0x0f;
			}
			ret.append(cvt.charAt(c));
			if (i < len) {
				c = (data[i] << 2) & 0x3f;
				if (++i < len) {
					c |= (data[i] >> 6) & 0x03;
				}
				ret.append(cvt.charAt(c));
			} else {
				++i;
				ret.append((char) fillchar);
			}
			if (i < len) {
				c = data[i] & 0x3f;
				ret.append(cvt.charAt(c));
			} else {
				ret.append((char) fillchar);
			}
		}
		return ret.toString();
	}

	public static void main(String[] args) throws Exception {
		//System.out.println(StringUtil.trimGBK("我ABC汉DEF",7));
	}

}