package com.mycompany.unionprice;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Пример использования класса UnionPrice
public class App {
	static Date dt(String strDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		LocalDateTime localDateTime = LocalDateTime.parse(strDate, formatter);
		ZoneId defaultZoneId = ZoneId.systemDefault();
		return Date.from(localDateTime.atZone(defaultZoneId).toInstant());
	}

	public static void main(String[] args) {
		ProdPrice pp0 = new ProdPrice(1L, "122856", 1, 1, dt("01.01.2013 00:00:00"), dt("31.01.2013 23:59:59"), 11000L);
		ProdPrice pp1 = new ProdPrice(1L, "122856", 2, 1, dt("10.01.2013 00:00:00"), dt("20.01.2013 23:59:59"), 99000L);
		ProdPrice pp2 = new ProdPrice(1L, "6654", 1, 2, dt("01.01.2013 00:00:00"), dt("31.01.2013 00:00:00"), 5000L);

		ProdPrice ppn0 = new ProdPrice(1L, "122856", 1, 1, dt("20.01.2013 00:00:00"), dt("20.02.2013 23:59:59"), 11000L);
		ProdPrice ppn1 = new ProdPrice(1L, "122856", 2, 1, dt("15.01.2013 00:00:00"), dt("25.01.2013 23:59:59"), 92000L);
		ProdPrice ppn2 = new ProdPrice(1L, "6654", 1, 2, dt("12.01.2013 00:00:00"), dt("13.01.2013 00:00:00"), 4000L);

		List<ProdPrice> listold = new ArrayList<>();
		listold.add(pp0);
		listold.add(pp1);
		listold.add(pp2);

		List<ProdPrice> listnew = new ArrayList<>();
		listnew.add(ppn0);
		listnew.add(ppn1);
		listnew.add(ppn2);

		UnionPrice up = new UnionPrice(listold, listnew);
		up.getListunionSortByCode();
		up.printUnionList();
	}

}
