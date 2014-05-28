package com.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Demo1 {

	public static void main(String[] args) {
		long l = 1400487114;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(l);
		String str = format.format(date);
		try {
			Date date1 = format.parse(str);
			System.out.println(date1.getYear());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(str);
	}
}
