package fr.veridiangames.core.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Time
{
	public static String getTime()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");//dd/MM/yyyy
		Calendar calendar = Calendar.getInstance();
		String time = dateFormat.format(calendar.getTime());
		return time;
	}
}
