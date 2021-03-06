package com.mycompany.unionprice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UnionPriceTest4 {
	private static UnionPrice up;

	static Date dt(String strDate) {
		return TestUtils.makeDate(strDate);
	}

	@BeforeClass
	public static void setUp() throws Exception {
		ProdPrice pp0 = new ProdPrice(1L, "100", 1, 1, dt("01.01.2001 00:00:00"), dt("31.01.2001 23:59:59"), 7000L);

		ProdPrice ppn0 = new ProdPrice(1L, "100", 1, 1, dt("12.01.2001 00:00:00"), dt("15.01.2001 23:59:59"), 5000L);

		List<ProdPrice> listold = new ArrayList<>();
		listold.add(pp0);

		List<ProdPrice> listnew = new ArrayList<>();
		listnew.add(ppn0);

		up = new UnionPrice(listold, listnew);
	}

	@Test
	public void testLenghtListunion() {
		Assert.assertTrue("Error: size of Listunion is not correct", 3 == up.getListunion().size());
	}

	@Test
	public void testGetListunion() {
		List<ProdPrice> list = new ArrayList<>();
		list.add(new ProdPrice(1L, "100", 1, 1, dt("01.01.2001 00:00:00"), dt("12.01.2001 00:00:00"), 7000L));
		list.add(new ProdPrice(1L, "100", 1, 1, dt("12.01.2001 00:00:00"), dt("15.01.2001 23:59:59"), 5000L));
		list.add(new ProdPrice(null, "100", 1, 1, dt("15.01.2001 23:59:59"), dt("31.01.2001 23:59:59"), 7000L));

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
