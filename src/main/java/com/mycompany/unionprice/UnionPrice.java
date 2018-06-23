package com.mycompany.unionprice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UnionPrice {
	private List<ProdPrice> listOld;
	private List<ProdPrice> listNew;
	private List<ProdPrice> listUnion;
	private List<ProdPrice> listFilter;
	private List<ProdPrice> listToDelete;
	private Map<String, List<ProdPrice>> mapPrice;
	private boolean isNewPriceInserted;

	long oldBegin, oldEnd, newBegin, newEnd;

	public UnionPrice(List<ProdPrice> listOld, List<ProdPrice> listNew) {

		if (listOld == null || listNew == null) {
			return;
		}

		if (listOld.isEmpty()) {
			listUnion = listNew;
			return;
		}

		if (listNew.isEmpty()) {
			listUnion = listOld;
			return;
		}

		this.listOld = listOld;
		this.listNew = listNew;

		// копируем все старые цены
		listUnion = new LinkedList<>(listOld);
		listToDelete = new LinkedList<>();
		mapPrice = new HashMap<>();

		doUnionPrice();
	}

	private void doUnionPrice() {
		mapPrice = listOld.stream().collect(Collectors.groupingBy(pp -> {
			return makeGroupId(pp);
		}));

		for (ProdPrice newPrice : listNew) {

			// если товар не имеет цен
			List<ProdPrice> listGroup = mapPrice.get(makeGroupId(newPrice));
			if (listGroup == null) {
				listUnion.add(newPrice);
				continue;
			}

			// получаем список цен, где есть пересечение по периоду действия
			listFilter = getListFilter(listGroup, newPrice);

			// если старые цены не пересекаются в периодах действия с новыми ценами
			if (listFilter.isEmpty()) {
				listUnion.add(newPrice);
				continue;
			}

			checkListFilter(newPrice);
		}

		// удаляем цены помеченные к удалению
		if (!listToDelete.isEmpty()) {
			listUnion.removeAll(listToDelete);
		}
	}

	// формируем идентификатор группы
	private String makeGroupId(ProdPrice prodPrice) {
		StringBuilder sb = new StringBuilder();
		sb.append(prodPrice.getProduct_code());
		sb.append(".");
		sb.append(prodPrice.getNumber());
		sb.append(".");
		sb.append(prodPrice.getDepart());

		return sb.toString();
	}

	private List<ProdPrice> getListFilter(List<ProdPrice> listgroup, ProdPrice newPrice) {
		List<ProdPrice> filterlist = new ArrayList<>();
		for (ProdPrice prodPrice : listgroup) {
			if (prodPrice.getBegin().after(newPrice.getEnd()) || prodPrice.getEnd().before(newPrice.getBegin())) {
				continue;
			}

			filterlist.add(prodPrice);
		}

		return filterlist;
	}

	//@formatter:off
	private void checkListFilter(ProdPrice newPrice) {
		isNewPriceInserted=false;		
		
		for (ProdPrice oldPrice : listFilter) {
			// могут быть разные id для одинаковых цен в старом и новом массиве цен
			if (oldPrice.getProduct_code().equals(newPrice.getProduct_code())
					&& oldPrice.getNumber().equals(newPrice.getNumber())
					&& oldPrice.getDepart().equals(newPrice.getDepart())
					&& oldPrice.getBegin().equals(newPrice.getBegin())
					&& oldPrice.getEnd().equals(newPrice.getEnd())
					&& oldPrice.getValue().equals(newPrice.getValue())) {
				continue;
			}			
			
			if (oldPrice.getValue().equals(newPrice.getValue())) {				
				unionPeriod(oldPrice, newPrice);
				continue;				
			}
			
			checkPeriod(oldPrice, newPrice);
		}
	}
	//@formatter:on

	// если значения цен одинаковы, то период действия цены увеличивается согласно
	// периоду новой цены
	private void unionPeriod(ProdPrice oldPrice, ProdPrice newPrice) {
		if (newPrice.getBegin().before(oldPrice.getBegin())) {
			oldPrice.setBegin(newPrice.getBegin());
		}

		if (newPrice.getEnd().after(oldPrice.getEnd())) {
			oldPrice.setEnd(newPrice.getEnd());
		}

		isNewPriceInserted = true;
	}

	private void checkPeriod(ProdPrice oldPrice, ProdPrice newPrice) {
		oldBegin = oldPrice.getBegin().getTime();
		oldEnd = oldPrice.getEnd().getTime();

		newBegin = newPrice.getBegin().getTime();
		newEnd = newPrice.getEnd().getTime();

		// если период старой цены полностью перекрывается периодом новой цены
		if ((oldBegin >= newBegin & oldEnd <= newEnd)) {
			insertNewPrice(newPrice);

			// добавляем старую цену в список удаляемых цен
			listToDelete.add(oldPrice);
			return;
		}

		// если период новой цены начинается позже и заканчивается позже
		if (oldBegin < newBegin & oldEnd <= newEnd) {
			insertNewPrice(newPrice);
			oldPrice.setEnd(newPrice.getBegin());
			return;
		}

		// если период новой цены начинается раньше и заканчивается раньше
		if (oldBegin > newBegin & oldEnd > newEnd) {
			insertNewPrice(newPrice);
			oldPrice.setBegin(newPrice.getEnd());
			return;
		}

		// если период новой цены попадает внутрь периода старой цены, то
		// добавляем новую цену, у старой уменьшаем конец действия и добавляем ещё одну
		// цену со значение старой цены и периодом действия
		// от конца новой цены до конца старой цены
		if (oldBegin < newBegin & oldEnd > newEnd) {
			insertNewPrice(newPrice);
			ProdPrice pp1 = new ProdPrice(null, oldPrice.getProduct_code(), oldPrice.getNumber(), oldPrice.getDepart(), newPrice.getEnd(), oldPrice.getEnd(), oldPrice.getValue());
			listUnion.add(pp1);
			oldPrice.setEnd(newPrice.getBegin());
		}
	}

	private void insertNewPrice(ProdPrice newPrice) {
		if (!isNewPriceInserted) {
			listUnion.add(newPrice);
			isNewPriceInserted = true;
		}
	}

	private Comparator<ProdPrice> BeginComparator = new Comparator<ProdPrice>() {
		public int compare(ProdPrice prodPrice1, ProdPrice prodPrice2) {
			Long t1 = prodPrice1.getBegin().getTime();
			Long t2 = prodPrice2.getBegin().getTime();

			// ascending order
			return t1.compareTo(t2);
		}
	};

	private Comparator<ProdPrice> CodeComparator = new Comparator<ProdPrice>() {
		public int compare(ProdPrice prodPrice1, ProdPrice prodPrice2) {
			String code1 = prodPrice1.getProduct_code();
			String code2 = prodPrice2.getProduct_code();

			// ascending order
			return code1.compareTo(code2);
		}
	};

	public List<ProdPrice> getListunion() {
		return listUnion;
	}

	public List<ProdPrice> getListunionSortByBegin() {
		Collections.sort(listUnion, BeginComparator);
		return listUnion;
	}

	public List<ProdPrice> getListunionSortByCode() {
		Collections.sort(listUnion, CodeComparator);
		return listUnion;
	}

	public void printUnionList() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		for (ProdPrice prodPrice : listUnion) {
			StringBuilder sb = new StringBuilder();
			sb.append(prodPrice.getId());
			sb.append(" ");
			sb.append(prodPrice.getProduct_code());
			sb.append(" ");
			sb.append(prodPrice.getNumber());
			sb.append(" ");
			sb.append(prodPrice.getDepart());
			sb.append(" ");
			sb.append(sdf.format(prodPrice.getBegin()));
			sb.append(" ");
			sb.append(sdf.format(prodPrice.getEnd()));
			sb.append(" ");
			sb.append(prodPrice.getValue());
			sb.append(" ");
			System.out.println(sb);
		}
	}
}
