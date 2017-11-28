package com.two.emergencylending.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	private final static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd");
	public static int BUFFER_SIZE = 512;

	/**
	 * 是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNotEmpty(String s) {
		return s != null && !"".equals(s.trim());
	}


	public static String format(String src, Object... objects) {
		int k = 0;
		for (Object obj : objects) {
			src = src.replace("{" + k + "}", obj.toString());
			k++;
		}
		return src;
	}

	public static String null2Blank(String str) {
		return (str == null ? "" : str);
	}

	public static boolean isNullOrEmpty(String str) {
		if ((str == null) || str.equals("") || str.equals("null") || str.equals("NULL")) {
			return true;
		} else {
			return false;
		}
	}

	public static String InputStreamToString(InputStream in) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
			outStream.write(data, 0, count);
		}
		data = null;
		return new String(outStream.toByteArray(), "ISO-8859-1");

	}

	/**
	 * 将字符串转位日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 以友好的方式显示时间
	 * 
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.format(cal.getTime());
		String paramDate = dateFormater2.format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = dateFormater2.format(time);
		}
		return ftime;
	}

	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.format(today);
			String timeDate = dateFormater2.format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}

	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 字符串转布尔值
	 * 
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 小数点后两位小数
	 */
	public static void formatDot(Editable edt) {
		String temp = edt.toString();
		int posDot = temp.indexOf(".");
		if (posDot <= 0)
			return;
		if (temp.length() - posDot - 1 > 2) {
			edt.delete(posDot + 3, posDot + 4);
		}
	}

	/**
	 * 将输入流转化成字符串
	 * 
	 * @param inputStream输入流
	 * @param encoding
	 *            字符编码类型,如果encoding传入null，则默认使用utf-8编码。
	 * @return 字符串
	 * @throws IOException
	 * @author lvmeng
	 */
	public static String inputToString(InputStream inputStream, String encoding) throws IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		inputStream.close();
		bos.close();
		if (TextUtils.isEmpty(encoding)) {
			encoding = "UTF-8";
		}
		return new String(bos.toByteArray(), encoding);
	}

	/**
	 * 设置需要高亮的字
	 * 
	 * @param wholeText
	 *            原始字符串
	 * @param spanableText
	 *            需要高亮的字符串
	 * @return 高亮后的字符串
	 */
	public static SpannableString getSpanableText(String wholeText, String spanableText) {
		if (TextUtils.isEmpty(wholeText))
			wholeText = "";
		SpannableString spannableString = new SpannableString(wholeText);
		if (spanableText.equals(""))
			return spannableString;
		wholeText = wholeText.toLowerCase();
		spanableText = spanableText.toLowerCase();
		int startPos = wholeText.indexOf(spanableText);
		if (startPos == -1) {
			int tmpLength = spanableText.length();
			String tmpResult = "";
			for (int i = 1; i <= tmpLength; i++) {
				tmpResult = spanableText.substring(0, tmpLength - i);
				int tmpPos = wholeText.indexOf(tmpResult);
				if (tmpPos == -1) {
					tmpResult = spanableText.substring(i, tmpLength);
					tmpPos = wholeText.indexOf(tmpResult);
				}
				if (tmpPos != -1)
					break;
				tmpResult = "";
			}
			if (tmpResult.length() != 0) {
				return getSpanableText(wholeText, tmpResult);
			} else {
				return spannableString;
			}
		}
		int endPos = startPos + spanableText.length();
		do {
			endPos = startPos + spanableText.length();
			spannableString.setSpan(new BackgroundColorSpan(Color.YELLOW), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			startPos = wholeText.indexOf(spanableText, endPos);
		} while (startPos != -1);
		return spannableString;
	}

	/**
	 * 去掉字符串首尾，中间的空格，trim()，不仅仅是去掉空格，此处主要是增加去掉中间的空格
	 * 
	 * @param str
	 * @return
	 */
	public static String removeSpace(String str) {

		if (!TextUtils.isEmpty(str)) {
			return str.trim().replaceAll(" ", "");
		} else {
			return str;
		}
	}

	/**
	 * 去掉负号，截取负号后面的String
	 * 
	 * @param str
	 * @return
	 */
	public static String removeSign(String str) {
		if (str.contains("-")) {
			return str.substring(str.indexOf("-"), str.length());
		} else {
			return str;
		}
	}

	/**
	 * 根据key、value读取country.text文件中国家信息到
	 * 
	 * @param key
	 *            读取国家信息的key值
	 * @return 当前key对应的国家信息
	 */

	public static String readString(Context context, final String filename) {
		try {
			InputStream is = context.getAssets().open(filename + ".txt");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			return new String(buffer, "UTF-8");

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 截取字符串中的验证码
	 * 
	 * @param content
	 * @return
	 */
	public static String getNumbers(String content) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		String number = "";
		while (matcher.find()) {
			number = matcher.group(0);
			if (number.length() == 6) {
				return number;
			}
        }
        return "";
    }
	

    /**
     * 一行一行的读取asset目录下的txt文本，得到一个数组
     * @param context
     * @param filename
     * @return
     */
    public static List<String> readStringByLine(Context context, final String filename) {
        List<String> strLines = new ArrayList<String>();
        InputStream is;
        try
        {
            is = context.getAssets().open(filename + ".txt");
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(is));
            String str = null;
            while ((str = bfReader.readLine()) != null)
            {
                strLines.add(str);
            }
            bfReader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return strLines;
    }
}
