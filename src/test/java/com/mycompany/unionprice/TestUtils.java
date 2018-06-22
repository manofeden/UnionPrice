package com.mycompany.unionprice;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TestUtils {

	public static Date makeDate(String strDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		LocalDateTime localDateTime = LocalDateTime.parse(strDate, formatter);
		ZoneId defaultZoneId = ZoneId.systemDefault();
		return Date.from(localDateTime.atZone(defaultZoneId).toInstant());
	}
}
