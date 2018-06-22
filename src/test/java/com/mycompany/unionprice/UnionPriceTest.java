package com.mycompany.unionprice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UnionPriceTest {
	private static UnionPrice up;

	// название не соответствует конвенции java code style conventions, и можно было
	// бы без этого метода обходиться, но так нагляднее в тестах
	static Date dt(String strDate) {
		return TestUtils.makeDate(strDate);
	}

	@BeforeClass
	public static void setUp() throws Exception {
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

		up = new UnionPrice(listold, listnew);
	}

	@Test
	public void testLenghtListunion() {
		Assert.assertTrue("Error: size of Listunion is not correct", 6 == up.getListunion().size());
	}

	@Test
	public void testGetListunionSort() {
		List<ProdPrice> list = new ArrayList<>();
		list.add(new ProdPrice(1L, "122856", 1, 1, dt("01.01.2013 00:00:00"), dt("20.02.2013 23:59:59"), 11000L));
		list.add(new ProdPrice(1L, "122856", 2, 1, dt("10.01.2013 00:00:00"), dt("15.01.2013 00:00:00"), 99000L));
		list.add(new ProdPrice(1L, "122856", 2, 1, dt("15.01.2013 00:00:00"), dt("25.01.2013 23:59:59"), 92000L));
		list.add(new ProdPrice(1L, "6654", 1, 2, dt("01.01.2013 00:00:00"), dt("12.01.2013 00:00:00"), 5000L));
		list.add(new ProdPrice(1L, "6654", 1, 2, dt("12.01.2013 00:00:00"), dt("13.01.2013 00:00:00"), 4000L));
		list.add(new ProdPrice(null, "6654", 1, 2, dt("13.01.2013 00:00:00"), dt("31.01.2013 00:00:00"), 5000L));

		System.out.println("******* befor Sort *******");
		up.printUnionList();

		Assert.assertTrue(list.equals(up.getListunionSortByCode()));
	}

	@AfterClass
	public static void print() {
		System.out.println("\n******* after Sort *******");
		up.printUnionList();
	}

}
