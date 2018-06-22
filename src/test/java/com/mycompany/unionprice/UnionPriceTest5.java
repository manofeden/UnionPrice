package com.mycompany.unionprice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UnionPriceTest5 {
	private static UnionPrice up;

	static Date dt(String strDate) {
		return TestUtils.makeDate(strDate);
	}

	@BeforeClass
	public static void setUp() throws Exception {
		ProdPrice pp0 = new ProdPrice(1L, "100", 1, 1, dt("01.01.2001 00:00:00"), dt("20.01.2001 23:59:59"), 100L);
		ProdPrice pp1 = new ProdPrice(1L, "100", 1, 1, dt("21.01.2001 00:00:00"), dt("31.01.2001 23:59:59"), 120L);

		ProdPrice ppn0 = new ProdPrice(1L, "100", 1, 1, dt("12.01.2001 00:00:00"), dt("20.01.2001 23:59:59"), 110L);

		List<ProdPrice> listold = new ArrayList<>();
		listold.add(pp0);
		listold.add(pp1);

		List<ProdPrice> listnew = new ArrayList<>();
		listnew.add(ppn0);

		up = new UnionPrice(listold, listnew);
	}

	@Test
	public void testLenghtListunion() {
		Assert.assertTrue("Error: size of Listunion is not correct", 3 == up.getListunion().size());
	}

	@Test
	public void testGetListunionSortByBegin() {
		List<ProdPrice> list = new ArrayList<>();
		list.add(new ProdPrice(1L, "100", 1, 1, dt("01.01.2001 00:00:00"), dt("12.01.2001 00:00:00"), 100L));
		list.add(new ProdPrice(1L, "100", 1, 1, dt("12.01.2001 00:00:00"), dt("20.01.2001 23:59:59"), 110L));
		list.add(new ProdPrice(1L, "100", 1, 1, dt("21.01.2001 00:00:00"), dt("31.01.2001 23:59:59"), 120L));

		Assert.assertTrue(list.equals(up.getListunionSortByBegin()));
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
