package com.mycompany.unionprice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UnionPriceTest6 {
	private static UnionPrice up;

	static Date dt(String strDate) {
		return TestUtils.makeDate(strDate);
	}

	@BeforeClass
	public static void setUp() throws Exception {
		ProdPrice pp0 = new ProdPrice(1L, "100", 1, 1, dt("01.01.2001 00:00:00"), dt("10.01.2001 23:59:59"), 80L);
		ProdPrice pp1 = new ProdPrice(1L, "100", 1, 1, dt("11.01.2001 00:00:00"), dt("20.01.2001 23:59:59"), 87L);
		ProdPrice pp2 = new ProdPrice(1L, "100", 1, 1, dt("21.01.2001 00:00:00"), dt("31.01.2001 23:59:59"), 90L);

		ProdPrice ppn0 = new ProdPrice(1L, "100", 1, 1, dt("08.01.2001 00:00:00"), dt("15.01.2001 23:59:59"), 80L);
		ProdPrice ppn1 = new ProdPrice(1L, "100", 1, 1, dt("15.01.2001 00:00:00"), dt("25.01.2001 23:59:59"), 85L);

		List<ProdPrice> listold = new ArrayList<>();
		listold.add(pp0);
		listold.add(pp1);
		listold.add(pp2);

		List<ProdPrice> listnew = new ArrayList<>();
		listnew.add(ppn0);
		listnew.add(ppn1);

		up = new UnionPrice(listold, listnew);
	}

	@Test
	public void testLenghtListunion() {
		Assert.assertTrue("Error: size of Listunion is not correct", 3 == up.getListunion().size());
	}

	@Test
	public void testGetListunion() {
		List<ProdPrice> list = new ArrayList<>();
		list.add(new ProdPrice(1L, "100", 1, 1, dt("01.01.2001 00:00:00"), dt("15.01.2001 00:00:00"), 80L));
		list.add(new ProdPrice(1L, "100", 1, 1, dt("25.01.2001 23:59:59"), dt("31.01.2001 23:59:59"), 90L));
		list.add(new ProdPrice(1L, "100", 1, 1, dt("15.01.2001 00:00:00"), dt("25.01.2001 23:59:59"), 85L));

		Assert.assertTrue(list.equals(up.getListunion()));
	}

	@AfterClass
	public static void print() {
		System.out.println("******* befor Sort *******");
		up.printUnionList();

		up.getListunionSortByBegin();
		System.out.println("\n******* after Sort *******");
		up.printUnionList();
	}

}
