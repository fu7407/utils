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
 * �ַ���������
 * @author ������
 */
public class StringUtil {

	/**
	 * �ַ����滻����,String��replace�������ܴ���'|'����
	 * @param strSource ���滻��Դ�ַ���
	 * @param strFrom   Ҫ���Ҳ��滻�����ַ���
	 * @param strTo     Ҫ�滻Ϊ�����ַ���
	 * @return �滻��ɵ��ַ���
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
	 * �� Long/Double ���� ����λǰ�油 λ ����: 31 ��λ���� 0031 ����.
	 * @param num Long ����ʽ��ǰ������
	 * @param formatStr ��Ҫ ��ʽ����������ʽ
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
	 * ��trim
	 * @param strSource ���滻��Դ�ַ���
	 * @return �滻��ɵ��ַ���
	 */
	public static String trim(Object strSource) {
		if (strSource == null)
			return null;
		return String.valueOf(strSource).trim();
	}

	/**
	 * ��ȡ�ַ������ָ�����ȵ��ַ���
	 * @param input �����ַ���
	 * @param count ��ȡ����
	 * @return ��ȡ�ַ���
	 */
	public static String left(String input, int count) {
		if (isEmpty(input))
			return "";
		count = (count > input.length()) ? input.length() : count;
		return input.substring(0, count);
	}

	/**
	 * ��ȡ�ַ����Ҳ�ָ�����ȵ��ַ���
	 * @param input �����ַ���
	 * @param count ��ȡ����
	 * @return ��ȡ�ַ���
	 */
	public static String right(String input, int count) {
		if (isEmpty(input))
			return "";
		count = (count > input.length()) ? input.length() : count;
		return input.substring(input.length() - count, input.length());
	}

	/**
	 * ��ָ��λ�ÿ�ʼ��ȡָ�����ȵ��ַ���
	 * @param input �����ַ���
	 * @param index ��ȡλ�ã�����һ���ַ�����ֵ��1
	 * @param count ��ȡ����
	 * @return ��ȡ�ַ���
	 */
	public static String middle(String input, int index, int count) {
		if (isEmpty(input))
			return "";
		count = (count > input.length() - index + 1) ? input.length() - index + 1 : count;
		return input.substring(index - 1, index + count - 1);
	}

	/**
	 * ��һ���ַ����루charSet1��ת��Ϊ��һ���ַ����루charSet2��
	 * @param input
	 * @param charSet1 ԭ�ַ�����
	 * @param charSet2 Ŀ���ַ�����
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String CharSetToCharSet(String input, String charSet1, String charSet2) throws UnsupportedEncodingException {
		if (isEmpty(input))
			return "";
		return new String(input.getBytes(charSet1), charSet2);
	}

	/**
	 * Unicodeת����GBK�ַ���
	 * @param input ��ת���ַ���
	 * @return ת������ַ���
	 */
	public static String UnicodeToGB(String input) throws UnsupportedEncodingException {
		return CharSetToCharSet(input, "ISO8859_1", "GBK");
	}

	/**
	 * Unicodeת����UTF-8�ַ���
	 * @param input ��ת���ַ���
	 * @return ת������ַ���
	 */
	public static String UnicodeToUTF8(String input) throws UnsupportedEncodingException {
		return CharSetToCharSet(input, "ISO8859_1", "UTF-8");
	}

	/**
	 * ����ַ�������
	 * @param input
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getCharsetName(String input) throws UnsupportedEncodingException {
		Map<String, Charset> charsets = Charset.availableCharsets();
		for (Map.Entry<String, Charset> entry : charsets.entrySet()) {
			if (input.equals(new String(input.getBytes(entry.getKey()), entry.getKey()))) {//�жϵ�ǰ�ַ����ı����ʽ
				return entry.getKey();
			}
		}
		return "";
	}

	/**
	 * �ָ��ַ���������.(ʹ��StringTokenizer��String��split�������ܴ���'|'����)
	 * @param input �����ַ���
	 * @param delim �ָ���
	 * @return �ָ�������
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
	 * �ж��ַ��Ƿ�Ϊ��
	 * @param input ĳ�ַ���
	 * @return �����򷵻�true�����򷵻�false
	 */
	public static boolean isEmpty(String value) {
		return value == null || "".equals(value.trim());
	}

	/**
	 * �ж�ĳ�ַ����Ƿ����������
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
	 * �Ƿ�������ڹ���2011-05-11��
	 * Ҳ������String��ķ���:value.matches("\\d{4}-\\d{2}-\\d{2}")
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
	 * �ж�valueֵ�Ƿ����ĳ��������ʽ�Ĺ���
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
	 * �ַ���content���Ƿ������������ʽregex�����ַ���
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
	 * �����ַ��������ֽ��ַ�����֣����룺A1B22C333D4444E55555F,�������飺{"A","B","C","D","E","F"}��
	 * Ҳ������String��ķ���:value.split("\\d+")
	 * ����"|"���и�ʱ����Ϊ"|"��������ʽ�б�ʾ��ĸ��������Ҫ�������ת�壬Ӧ����������value.split("\\|")��
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
	 * ��ȫ���������滻Ϊ"_"�����룺A1B22C333D4444E55555F,���أ�A_B_C_D_E_F��
	 * Ҳ������String��ķ���:value.replaceAll("\\d+", "_");
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
	 * �ַ�����ת�������롰abcdef�� �������fedcba����
	 * @param str
	 * @return
	 */
	public static String reverse(String str) {
		if (str == null)
			return null;
		return new StringBuffer(str).reverse().toString();
	}

	/**
	 * ���0-9,a-z,A-Z��Χ�������
	 * @param length ���������
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
	 * ȫ�� ת���� -> ���
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

	public static void fun1() {// ASCIIת��Ϊ�ַ���
		String s = "22307 35806 24555 20048";// ASCII��
		String[] chars = s.split(" ");
		System.out.println("ASCII ���� \n----------------------");
		for (int i = 0; i < chars.length; i++) {
			System.out.println(chars[i] + " " + (char) Integer.parseInt(chars[i]));
		}
	}

	/**
	 * �ַ���ת��ΪASCII��
	 * 
	 */
	public static void fun2() {
		String s = "������֣�";// �ַ���
		char[] chars = s.toCharArray(); // ���ַ���ת��Ϊ�ַ�����    
		for (int i = 0; i < chars.length; i++) {// ������    
			System.out.println(" " + chars[i] + " " + (int) chars[i]);
		}
	}

	/**
	 * ��дһ����ȡ�ַ����ĺ���������Ϊһ���ַ������ֽ��������Ϊ���ֽڽ�ȡ���ַ���������Ҫ��֤���ֲ����ذ����
	 * �硰��ABC��4��Ӧ�ý�Ϊ����AB�������롰��ABC��DEF����6��Ӧ�����Ϊ����ABC�������ǡ���ABC+���İ����
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
	 * ��дһ����ȡ�ַ����ĺ���������Ϊһ���ַ������ֽ��������Ϊ���ֽڽ�ȡ���ַ���������Ҫ��֤���ֲ����ذ����
	 * �硰��ABC��4��Ӧ�ý�Ϊ����AB�������롰��ABC��DEF����6��Ӧ�����Ϊ����ABC�������ǡ���ABC+���İ����
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
		String source = "[���][123][�ܺ�][11][����һ��][86]";
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
	 * ���ݰ��������ֵõ��������֣��磺����12300.3���������Ҽ��Ǫ����.����
	 * @param moneyStr
	 * @return
	 */
	public static String convertToChineseNumber(String moneyStr) {
		HashMap<String, String> chineseNumberMap = new HashMap<String, String>();
		chineseNumberMap.put("0", "��");
		chineseNumberMap.put("1", "Ҽ");
		chineseNumberMap.put("2", "��");
		chineseNumberMap.put("3", "��");
		chineseNumberMap.put("4", "��");
		chineseNumberMap.put("5", "��");
		chineseNumberMap.put("6", "½");
		chineseNumberMap.put("7", "��");
		chineseNumberMap.put("8", "��");
		chineseNumberMap.put("9", "��");
		chineseNumberMap.put(".", ".");
		HashMap<String, String> chineseMoneyPattern = new HashMap<String, String>();
		chineseMoneyPattern.put("1", "ʰ");
		chineseMoneyPattern.put("2", "��");
		chineseMoneyPattern.put("3", "Ǫ");
		chineseMoneyPattern.put("4", "��");
		chineseMoneyPattern.put("5", "ʰ");
		chineseMoneyPattern.put("6", "��");
		chineseMoneyPattern.put("7", "Ǫ");
		chineseMoneyPattern.put("8", "��");

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < moneyStr.length(); i++) {
			sb.append(chineseNumberMap.get(moneyStr.substring(i, i + 1)));
		}
		String fractionPart = "";
		int moneyPatternCursor = 1;
		int indexOfDot = sb.indexOf(".");
		if (indexOfDot != -1) {//��С����
			for (int i = indexOfDot - 1; i > 0; i--) {
				sb.insert(i, chineseMoneyPattern.get("" + moneyPatternCursor));
				moneyPatternCursor = moneyPatternCursor == 8 ? 1 : moneyPatternCursor + 1;
			}
			fractionPart = sb.substring(sb.indexOf("."));
			sb.delete(sb.indexOf("."), sb.length());
		} else {//û��С����
			for (int i = sb.length() - 1; i > 0; i--) {
				sb.insert(i, chineseMoneyPattern.get("" + moneyPatternCursor));
				moneyPatternCursor = moneyPatternCursor == 8 ? 1 : moneyPatternCursor + 1;
			}
		}
		while (sb.indexOf("��ʰ") != -1) {
			sb.replace(sb.indexOf("��ʰ"), sb.indexOf("��ʰ") + 2, "��");
		}
		while (sb.indexOf("���") != -1) {
			sb.replace(sb.indexOf("���"), sb.indexOf("���") + 2, "��");
		}
		while (sb.indexOf("��Ǫ") != -1) {
			sb.replace(sb.indexOf("��Ǫ"), sb.indexOf("��Ǫ") + 2, "��");
		}
		while (sb.indexOf("����") != -1) {
			sb.replace(sb.indexOf("����"), sb.indexOf("����") + 2, "��");
		}
		while (sb.indexOf("����") != -1) {
			sb.replace(sb.indexOf("����"), sb.indexOf("����") + 2, "��");
		}
		while (sb.indexOf("����") != -1) {
			sb.replace(sb.indexOf("����"), sb.indexOf("����") + 2, "��");
		}
		if (sb.lastIndexOf("��") == sb.length() - 1)
			sb.delete(sb.length() - 1, sb.length());
		return sb.append(fractionPart).toString();
	}

	/**
	 * ���ת�����磺����1011���������ҼǪ��ҼʰҼԪ��
	 * @param moneyStr
	 * @return
	 */
	public static String convertToChineseNumber(int money) {
		char[] data = new char[] { '��', 'Ҽ', '��', '��', '��', '��', '½', '��', '��', '��' };
		char[] units = new char[] { 'Ԫ', 'ʰ', '��', 'Ǫ', '��', 'ʰ', '��', 'Ǫ', '��' };
		StringBuffer sb = new StringBuffer();
		int unit = 0;
		while (money != 0) {
			sb.insert(0, units[unit++]);
			int number = money % 10;
			sb.insert(0, data[number]);
			money /= 10;
		}
		return sb.reverse().toString().replaceAll("��[ʰ��Ǫ]", "��").replaceAll("��+��", "��").replaceAll("��+Ԫ", "Ԫ").replaceAll("��+", "��");
	}

	/**
	 * ��List�еĸ��ַ���������Ӵ�
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
	 * main�ַ������Ƿ����sub���ַ���
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
	 * �����㷨
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
		//System.out.println(StringUtil.trimGBK("��ABC��DEF",7));
	}

}