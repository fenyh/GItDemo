package com.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Demo2 {

	public static void main(String[] args) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse("2014-5-28");
			Long l = date.getTime();
			System.out.println(l);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
