package com.mycompany.unionprice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

public class UnionPriceTest7 {
	private static UnionPrice up;

	static Date dt(String strDate) {
		return TestUtils.makeDate(strDate);
	}

	@Test
	public void testGetListunion1() {
		up = new UnionPrice(null, null);

		Assert.assertTrue(up.getListunion() == null);
	}

	@Test
	public void testGetListunion2() {
		List<ProdPrice> list = new ArrayList<>();
		ProdPrice pp0 = new ProdPrice(1L, "100", 1, 1, dt("01.01.2013 00:00:00"), dt("31.01.2013 23:59:59"), 7000L);
		list.add(pp0);

		up = new UnionPrice(list, new ArrayList<>());
		Assert.assertTrue(up.getListunion().equals(list));
	}

	@Test
	public void testGetListunion3() {
		List<ProdPrice> list = new ArrayList<>();
		up = new UnionPrice(list, list);

		Assert.assertTrue(up.getListunion().equals(new ArrayList<>()));
	}

	@AfterClass
	public static void print() {
		up.printUnionList();
	}

}
