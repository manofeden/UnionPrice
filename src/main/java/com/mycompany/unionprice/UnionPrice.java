package com.mycompany.unionprice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UnionPrice {
	private List<ProdPrice> listnew;
	private List<ProdPrice> listunion;
	private Map<String, List<ProdPrice>> mapP;
	private boolean isNewPriceInserted;

	public UnionPrice(List<ProdPrice> listold, List<ProdPrice> listnew) {
		this.listnew = listnew;

		// копируем все старые цены
		listunion = new ArrayList<>(listold);

		mapP = listold.stream().collect(Collectors.groupingBy(ProdPrice::getGroupId));

		addNewPrice();
	}

	private void addNewPrice() {
		for (ProdPrice newPrice : listnew) {

			// если товар не имеет цен
			List<ProdPrice> listgroup = mapP.get(newPrice.getGroupId());
			if (listgroup == null) {
				listunion.add(newPrice);
				continue;
			}

			// получаем список цен, где есть пересечение по периоду действия
			List<ProdPrice> listfilter = listFilter(listgroup, newPrice);

			// если старые цены не пересекаются в периодах действия с новыми ценами
			if (listfilter.isEmpty()) {
				listunion.add(newPrice);
				continue;
			}

			checkFilerList(listfilter, newPrice);
		}
	}

	private List<ProdPrice> listFilter(List<ProdPrice> listgroup, ProdPrice newPrice) {
		List<ProdPrice> filterlist = new ArrayList<>();
		for (ProdPrice pp : listgroup) {
			if (pp.getBegin().after(newPrice.getEnd()) || pp.getEnd().before(newPrice.getBegin())) {
				continue;
			}

			filterlist.add(pp);
		}

		return filterlist;
	}

	//@formatter:off
	private void checkFilerList(List<ProdPrice> listfilter, ProdPrice newPrice) {
		isNewPriceInserted=false;
		for (ProdPrice oldPrice : listfilter) {
			// могут быть разные id для одинаковых цен в старом и новом массиве цен
			if (oldPrice.getProduct_code().equals(newPrice.getProduct_code())
					&& oldPrice.getNumber().equals(newPrice.getNumber())
					&& oldPrice.getDepart().equals(newPrice.getDepart())
					&& oldPrice.getBegin().equals(newPrice.getBegin())
					&& oldPrice.getEnd().equals(newPrice.getEnd())
					&& oldPrice.getValue().equals(newPrice.getValue())) {
				continue;
			}
			
			if (checkPrice(oldPrice, newPrice)) {
				isNewPriceInserted=true;
			}
		}
	}
	//@formatter:on

	private boolean checkPrice(ProdPrice oldPrice, ProdPrice newPrice) {
		long b_new = newPrice.getBegin().getTime();
		long e_new = newPrice.getEnd().getTime();

		long b_old = oldPrice.getBegin().getTime();
		long e_old = oldPrice.getEnd().getTime();

		// если значения цен одинаковы, то период действия цены увеличивается согласно
		// периоду новой цены
		if (oldPrice.getValue().equals(newPrice.getValue())) {
			oldPrice.setEnd(newPrice.getEnd());
			return false;
		}

		// если период старой цены полностью перекрывается периодом новой цены
		if ((b_old >= b_new & e_old <= e_new)) {
			oldPrice.setValue(newPrice.getValue());
			oldPrice.setBegin(newPrice.getBegin());
			oldPrice.setEnd(newPrice.getEnd());
			return false;
		}

		// если период новой цены начинается позже и заканчивается позже
		if (b_old < b_new & e_old <= e_new) {
			insertNewPrice(newPrice);
			oldPrice.setEnd(newPrice.getBegin());
			return true;
		}

		// если период новой цены начинается раньше и заканчивается раньше
		if (b_old > b_new & e_old > e_new) {
			insertNewPrice(newPrice);
			oldPrice.setBegin(newPrice.getEnd());
			return true;
		}

		// если период новой цены попадает внутрь периода старой цены, то
		// добавляем новую цены, у старой уменьшаем конец действия и добавляем ещё одну
		// цену со значение старой цены и периодом действия
		// от конца новой цены до конца старой цены
		if (b_old < b_new & e_old > e_new) {
			insertNewPrice(newPrice);
			ProdPrice pp1 = new ProdPrice(null, oldPrice.getProduct_code(), oldPrice.getNumber(), oldPrice.getDepart(), newPrice.getEnd(), oldPrice.getEnd(), oldPrice.getValue());
			listunion.add(pp1);
			oldPrice.setEnd(newPrice.getBegin());
			return true;
		}

		return false;
	}

	private void insertNewPrice(ProdPrice newPrice) {
		if (!isNewPriceInserted) {
			listunion.add(newPrice);
		}
	}

	private Comparator<ProdPrice> BeginComparator = new Comparator<ProdPrice>() {
		public int compare(ProdPrice pp1, ProdPrice pp2) {
			Long t1 = pp1.getBegin().getTime();
			Long t2 = pp2.getBegin().getTime();

			// ascending order
			return t1.compareTo(t2);
		}
	};

	private Comparator<ProdPrice> CodeComparator = new Comparator<ProdPrice>() {
		public int compare(ProdPrice pp1, ProdPrice pp2) {
			String code1 = pp1.getProduct_code();
			String code2 = pp2.getProduct_code();

			// ascending order
			return code1.compareTo(code2);
		}
	};

	public List<ProdPrice> getListunion() {
		return listunion;
	}

	public List<ProdPrice> getListunionSortByBegin() {
		Collections.sort(listunion, BeginComparator);
		return listunion;
	}

	public List<ProdPrice> getListunionSortByCode() {
		Collections.sort(listunion, CodeComparator);
		return listunion;
	}

	public void printUnionList() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		for (ProdPrice pp : listunion) {
			StringBuilder bf = new StringBuilder();
			bf.append(pp.getId());
			bf.append(" ");
			bf.append(pp.getProduct_code());
			bf.append(" ");
			bf.append(pp.getNumber());
			bf.append(" ");
			bf.append(pp.getDepart());
			bf.append(" ");
			bf.append(sdf.format(pp.getBegin()));
			bf.append(" ");
			bf.append(sdf.format(pp.getEnd()));
			bf.append(" ");
			bf.append(pp.getValue());
			bf.append(" ");
			System.out.println(bf);
		}
	}
}
