package com.zzf.common.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理工具
 * @author 张增福
 */

public class DateUtil {

	private static DateUtil dateUtil = null;

	/**一秒钟的毫秒数*/
	public static final long MILLIS_PER_SECOND = 1000;

	/**一分钟的毫秒数*/
	public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;

	/**一小时的毫秒数*/
	public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;

	/**一天的毫秒数*/
	public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

	/**默认日期模式*/
	public static final String FORMAT_DAY = "yyyy-MM-dd";

	/**
	 * 获得DateUtil实例
	 * @return
	 */
	public static synchronized DateUtil getInstance() {
		if (dateUtil == null)
			dateUtil = new DateUtil();
		return dateUtil;
	}

	/**
	 * 将日期类型数据按照指定格式转化为字符串类型
	 * @param date 日期类型数据
	 * @param pattern 指定输出格式
	 * @return 
	 */
	public static String format(Date date, String pattern) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			return formatter.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 将字符串日期从格式"yyyy-MM-dd"转换为另外一种指定格式
	 * @param date
	 * @param pattern 转换后格式
	 * @return
	 * @throws ParseException
	 */
	public static String format(String date, String pattern) throws ParseException {
		return format(date, FORMAT_DAY, pattern);
	}

	/**
	 * 将字符串日期从一种格式转换为另外一种指定格式
	 * @param date
	 * @param pattern1 转换前格式
	 * @param pattern2 转换后格式
	 * @return
	 * @throws ParseException
	 */
	public static String format(String date, String pattern1, String pattern2) throws ParseException {
		return format(strTodate(date, pattern1), pattern2);
	}

	/**
	 * 给日期date中的某一个域((YEAR,MONTH,DAY_OF_MONTH,HOUR_OF_DAY,MINUTE,SECOND,MILLISECOND))的值添加一个数值
	 * @param date 2010-10-20
	 * @param calendarField Calendar.DAY_OF_MONTH
	 * @param amount 10
	 * @return 2010-10-30
	 */
	public static Date add(Date date, int calendarField, int amount) {
		if (date == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}

	/**
	 * 重新设置日期date中的某一个域(YEAR,MONTH,DAY_OF_MONTH,HOUR_OF_DAY,MINUTE,SECOND,MILLISECOND)的值为amount
	 * @param date 2010-10-20
	 * @param calendarField Calendar.DAY_OF_MONTH
	 * @param amount 10
	 * @return 2010-10-10
	 */
	public static Date set(Date date, int calendarField, int amount) {
		if (date == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setLenient(false);
		c.setTime(date);
		c.set(calendarField, amount);
		return c.getTime();
	}

	/**
	 * 转换日期为字符串，格式"yyyy-MM-dd"
	 * @param date 日期
	 * @return 返回格式化的日期字符串
	 */
	public static String dateToStr(Date date) {
		return dateToStr(date, FORMAT_DAY);
	}

	/**
	 * 转换日期为字符串
	 * @param date 日期
	 * @param pattern 日期模式
	 * @return 返回格式化的日期字符串
	 */
	public static String dateToStr(Date date, String pattern) {
		if (date == null)
			return "";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	/**
	 * 转换格式"yyyy-MM-dd"字符串为日期，
	 * @param date 日期
	 * @return 返回格式化的日期字符串
	 * @throws ParseException 
	 */
	public static Date strTodate(String date) throws ParseException {
		return strTodate(date, FORMAT_DAY);
	}

	/**
	 * 转换字符串为日期
	 * @param date 日期
	 * @param pattern 日期模式
	 * @return 返回格式化的日期字符串
	 * @throws ParseException
	 */
	public static Date strTodate(String date, String pattern) throws ParseException {
		if (date == null || "".equals(date.trim()))
			return null;
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 将String类型的日期转换为Calendar类型
	 * @param date
	 * @param pattern 日期模式
	 * @return
	 * @throws ParseException
	 */
	public static Calendar strToCalendar(String date, String pattern) throws ParseException {
		if (date == null || "".equals(date.trim()))
			return null;
		Date temp = strTodate(date, pattern);
		Calendar c = Calendar.getInstance();
		c.setTime(temp);
		return c;
	}

	/**
	 * 转换Calendar类型为字符串
	 * @param date 日期
	 * @param pattern 日期模式
	 * @return 返回格式化的日期字符串
	 */
	public static String calendarToStr(Calendar calendar, String pattern) {
		if (calendar == null)
			return "";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(calendar.getTime());
	}

	/**
	 * 将Date类型的日期转换为Calendar类型
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Calendar dateToCalendar(Date date) throws ParseException {
		if (date == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}

	/**
	 * 取得两个日期之间的天数
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public static int getDayDiff(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return 0;
		if (date1.before(date2)) {
			return (int) ((date2.getTime() - date1.getTime()) / MILLIS_PER_DAY);
		}
		return (int) ((date1.getTime() - date2.getTime()) / MILLIS_PER_DAY);
	}

	/**
	 * 取得两个日期之间的天数
	 * @param date1 开始日期 格式为"yyyy-MM-dd" 
	 * @param date2 结束日期 格式为"yyyy-MM-dd"
	 * @throws ParseException 
	 */
	public static int getDayDiff(String date1, String date2) throws ParseException {
		return getDayDiff(date1, date2, FORMAT_DAY);
	}

	/**
	 * 取得两个日期之间的天数
	 * @param date1 开始日期  
	 * @param date2 结束日期 
	 * @param pattern 日期模式
	 * @throws ParseException 
	 */
	public static int getDayDiff(String date1, String date2, String pattern) throws ParseException {
		return getDayDiff(strTodate(date1, pattern), strTodate(date2, pattern));
	}

	/**
	 * 计算两个日期之间的月差(如一个日期为8月10，一个日期为10月8号，当flag为true时，返回2，否则返回1)
	 * @param date1
	 * @param date2
	 * @param flag
	 * @return
	 */
	public static int getMonthDiff(Date date1, Date date2, boolean flag) throws ParseException {
		return getMonthDiff(dateToCalendar(date1), dateToCalendar(date2), flag);
	}

	/**
	 * 计算两个日期之间的月差(如一个日期为8月10，一个日期为10月8号，当flag为true时，返回2，否则返回1)
	 * @param date1 格式：yyyy-MM-dd
	 * @param date2 格式：yyyy-MM-dd
	 * @param flag
	 * @return
	 */
	public static int getMonthDiff(String date1, String date2, boolean flag) throws ParseException {
		return getMonthDiff(date1, date2, flag, FORMAT_DAY);
	}

	/**
	 * 计算两个日期之间的月差(如一个日期为8月10，一个日期为10月8号，当flag为true时，返回2，否则返回1)
	 * @param date1 格式：yyyy-MM-dd
	 * @param date2 格式：yyyy-MM-dd
	 * @param flag
	 * @param pattern 日期模式
	 * @return
	 */
	public static int getMonthDiff(String date1, String date2, boolean flag, String pattern) throws ParseException {
		return getMonthDiff(strToCalendar(date1, pattern), strToCalendar(date2, pattern), flag);
	}

	/**
	 * 计算两个日期之间的月差(如一个日期为8月10，一个日期为10月8号，当flag为true时，返回2，否则返回1)
	 * @param calendar1
	 * @param calendar2
	 * @param flag
	 * @return
	 */
	public static int getMonthDiff(Calendar calendar1, Calendar calendar2, boolean flag) throws ParseException {
		if (calendar2 == null || calendar1 == null)
			return 0;
		int iMonth = 0;
		if (calendar2.equals(calendar1))
			return 0;
		if (calendar1.after(calendar2)) {
			Calendar temp = calendar1;
			calendar1 = calendar2;
			calendar2 = temp;
		}
		iMonth = (calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR)) * 12 + calendar2.get(Calendar.MONTH) - calendar1.get(Calendar.MONTH);
		if (!flag && (calendar2.get(Calendar.DAY_OF_MONTH) < calendar1.get(Calendar.DAY_OF_MONTH))) {
			iMonth -= 1;
		}
		return iMonth;
	}

	/**
	 * 取得两个日期之间的年数
	 * @param calendar1  
	 * @param calendar2 
	 * @return
	 */
	public static int getYearDiff(Calendar calendar1, Calendar calendar2) throws ParseException {
		if (calendar2 == null || calendar1 == null)
			return 0;
		if (calendar2.equals(calendar1))
			return 0;
		if (calendar1.after(calendar2)) {
			Calendar temp = calendar1;
			calendar1 = calendar2;
			calendar2 = temp;
		}
		return calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR);
	}

	/**
	 * 取得两个日期之间的年数
	 * @param date1  
	 * @param date2 
	 * @return
	 */
	public static int getYearDiff(Date date1, Date date2) throws ParseException {
		return getYearDiff(dateToCalendar(date1), dateToCalendar(date2));
	}

	/**
	 * 取得两个日期之间的年数
	 * @param date1 开始日期 格式为"yyyy-MM-dd" 
	 * @param date2 结束日期 格式为"yyyy-MM-dd"
	 * @return
	 */
	public static int getYearDiff(String date1, String date2) throws ParseException {
		return getYearDiff(date1, date2, FORMAT_DAY);
	}

	/**
	 * Long类型转换为日期类型
	 * @param millis
	 * @return
	 */
	public static Date getDate(long millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		return cal.getTime();
	}

	/**
	 * 取得两个日期之间的年数
	 * @param date1 开始日期 
	 * @param date2 结束日期 
	 * @param pattern 日期模式
	 * @return
	 */
	public static int getYearDiff(String date1, String date2, String pattern) throws ParseException {
		return getYearDiff(strTodate(date1, pattern), strTodate(date2, pattern));
	}

	/**
	 * 将int类型转换为Date类型
	 * @param date
	 * @return
	 */
	public static Date intToDate(int date) {
		if (String.valueOf(date).length() != 8)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, date / 10000);
		cal.set(Calendar.MONTH, ((date % 10000) / 100) - 1);
		cal.set(Calendar.DAY_OF_MONTH, date % 100);
		return cal.getTime();
	}

	/**
	 * 返回当前时间戳
	 * @return
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 取的指定时间的星期值
	 * @param date
	 * @return
	 */
	public static String getWeekday(Date date) {
		String day = "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		switch (cal.get(Calendar.DAY_OF_WEEK)) {
			case 1:
				day = "星期日";
				break;
			case 2:
				day = "星期一";
				break;
			case 3:
				day = "星期二";
				break;
			case 4:
				day = "星期三";
				break;
			case 5:
				day = "星期四";
				break;
			case 6:
				day = "星期五";
				break;
			case 7:
				day = "星期六";
				break;
		}
		return day;
	}

	/**
	 * 获得指定日期的年份
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static int getYear(Date date) throws ParseException {
		return dateToCalendar(date).get(Calendar.YEAR);
	}

	/**
	 * 获得指定日期的月份
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static int getMonth(Date date) throws ParseException {
		return dateToCalendar(date).get(Calendar.MONTH) + 1;
	}

	/**
	 * 获得指定日期的日
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static int getDay(Date date) throws ParseException {
		return dateToCalendar(date).get(Calendar.DATE);
	}

	/**
	 * 获得指定日期的月份中的最后一天
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static int getLastDayOfMonth(Date date) throws ParseException {
		return dateToCalendar(date).getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获得指定年月的一共有多少天，亦即该月最后一天的天数
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, 01);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 检查字符串是否为合理的日期
	 * @param str
	 * @param level 日期的层级（如只包含年月日，则为3,包含年月日时分秒，则为6）
	 * @return 是合理的日期则返回true，否则返回false;
	 */
	public static boolean isDate(String str, int level) {
		if (str == null || "".equals(str.trim()))
			return false;
		int[] date = toArray(str, level);
		if (date[0] < 1000 || date[0] > 9999)
			return false;
		if (date[1] < 1 || date[1] > 12)
			return false;
		if (date[2] < 1 || date[2] > 31)
			return false;
		if (date[3] < 0 || date[3] > 23)
			return false;
		if (date[4] < 0 || date[4] > 59)
			return false;
		if (date[5] < 0 || date[5] > 59)
			return false;
		if (date[2] > getLastDayOfMonth(date[0], date[1]))
			return false;
		return true;
	}

	/**
	 * 将日期字符串转为整型数组：年，月，日，小时，分钟，秒
	 * @param str 格式为"yyyy-MM-dd" 或 "yyyy-MM-dd HH:MM:SS"
	 * @param level 需要精确取值的层数，必须不大于7，如为1，则返回年，3返回年月日
	 */
	public static int[] toArray(String str, int level) {
		int[] date = { 0, 0, 0, 0, 0, 0 }; // year,month,day,hour,minute,second
		if (str == null || "".equals(str.trim()))
			return date;
		String s = str;
		if (level > 6)
			level = 6;
		for (int i = 0; i < level; i++) {
			int len = s.length();
			if (len == 0)
				break;
			int start = 0, end = 0;
			for (int j = 0; j < len; j++) {
				char c = s.charAt(j);
				if (c >= '0' || c <= '9') {
					start = j; // 找到数字起始位置
					for (int k = j + 1; k < len; k++) {
						c = s.charAt(k);
						if (c < '0' || c > '9') {
							end = k; // 找到数字截止位置
							break;
						}
					}
					break;
				}
			}
			if (end == 0) {
				date[i] = new Integer(s.substring(start)).intValue();
				break; // 已经到了最后位置，退出循环
			} else {
				date[i] = new Integer(s.substring(start, end)).intValue();
				s = s.substring(end + 1);
			}
		}
		return date;
	}

	public static void main(String[] args) throws ParseException {
		//System.out.println(DateUtil.getWeekday(new Date()));
	}

}
