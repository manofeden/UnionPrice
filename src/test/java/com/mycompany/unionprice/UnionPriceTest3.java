package com.mycompany.unionprice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UnionPriceTest3 {
	private static UnionPrice up;

	static Date dt(String strDate) {
		return TestUtils.makeDate(strDate);
	}

	@BeforeClass
	public static void setUp() throws Exception {
		ProdPrice pp0 = new ProdPrice(1L, "100", 1, 1, dt("01.01.2001 00:00:00"), dt("31.01.2001 23:59:59"), 7000L);
		ProdPrice pp1 = new ProdPrice(2L, "100", 1, 1, dt("01.02.2001 00:00:00"), dt("20.02.2001 23:59:59"), 6500L);

		ProdPrice ppn0 = new ProdPrice(1L, "100", 1, 1, dt("25.01.2001 00:00:00"), dt("05.02.2001 23:59:59"), 5000L);
		ProdPrice ppn2 = new ProdPrice(3L, "100", 1, 1, dt("01.03.2001 00:00:00"), dt("31.03.2001 23:59:59"), 8000L);

		List<ProdPrice> listold = new ArrayList<>();
		listold.add(pp0);
		listold.add(pp1);

		List<ProdPrice> listnew = new ArrayList<>();
		listnew.add(ppn0);
		listnew.add(ppn2);

		up = new UnionPrice(listold, listnew);
	}

	@Test
	public void testLenghtListunion() {
		Assert.assertTrue("Error: size of Listunion is not correct", 4 == up.getListunion().size());
	}

	@Test
	public void testGetListunionSortByBegin() {
		List<ProdPrice> list = new ArrayList<>();
		list.add(new ProdPrice(1L, "100", 1, 1, dt("01.01.2001 00:00:00"), dt("25.01.2001 00:00:00"), 7000L));
		list.add(new ProdPrice(2L, "100", 1, 1, dt("05.02.2001 23:59:59"), dt("20.02.2001 23:59:59"), 6500L));
		list.add(new ProdPrice(1L, "100", 1, 1, dt("25.01.2001 00:00:00"), dt("05.02.2001 23:59:59"), 5000L));
		list.add(new ProdPrice(3L, "100", 1, 1, dt("01.03.2001 00:00:00"), dt("31.03.2001 23:59:59"), 8000L));

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
