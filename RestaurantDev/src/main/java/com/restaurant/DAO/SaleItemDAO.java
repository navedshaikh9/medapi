package com.restaurant.DAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.SaleItem;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SaleItemDAO {

	

	public boolean save(int sale_id, String item, int qty, double price, double total_price) {
		boolean isSave = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();

			SaleItem saleItem = new SaleItem();
			saleItem.setSaleId(sale_id);
			saleItem.setItemName(item);
			saleItem.setQty(qty);
			saleItem.setPrice(price);
			saleItem.setItemTotal(total_price);
			saleItem.setCreated(LocalDate.now());
			Transaction tx = session.beginTransaction();

			Integer us = (Integer) session.save(saleItem);
			if (us > 0) {
				isSave = true;
			}

			tx.commit();

		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return isSave;

	}

	public static ObservableList<SaleItem> getSaleItem(int sale_id) {
		Session session = null;

		try {
			ObservableList<SaleItem> depList = FXCollections.observableArrayList();
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from SaleItem where saleId=:id");
			nq.setParameter("id", sale_id);
			List<SaleItem> list = nq.list();

			for (SaleItem saleItem : list) {

				if (sale_id == saleItem.getSaleId()) {

					depList.add(saleItem);
				}

			}

			return depList;

		} catch (Exception e) {
			e.printStackTrace();
			return FXCollections.observableArrayList();
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	public static double getTotal(int sale_id) {
		Session session = null;
		double total = 0;
		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from SaleItem where saleId=:id");
			nq.setParameter("id", sale_id);
			List<SaleItem> list = nq.list();

			for (SaleItem li : list) {

				if (sale_id == li.getSaleId()) {
					total = total + li.getItemTotal();
				}

			}

			return total;

		} catch (Exception e) {
			// TODO: handle exception
			return total;
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}
	
	
	public static List<SaleItem> getAllSaleItem() {

		List<SaleItem> list = new ArrayList();

		try {

			Session s = FactoryProvider.getFactory().openSession();
			Query nq = s.createQuery("from SaleItem");
			 list = nq.list();

			return list;

		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}

	}
	
	public static List<SaleItem> getSaleItemDESC() {

		List<SaleItem> list = new ArrayList();

		try {

			Session s = FactoryProvider.getFactory().openSession();
			Query nq = s.createQuery("FROM SaleItem ORDER BY qty DESC");
			 list = nq.list();

			return list;

		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}

	}
	
	
	
	public static Map<String, Integer> getItemQty() {
	    Session session = null;
	    Map<String, Integer> dataMap = new HashMap<>();

	    try {
	        session = FactoryProvider.getFactory().openSession();
	        Query<Object[]> query = session.createQuery("SELECT itemName, SUM(qty) FROM SaleItem GROUP BY itemName", Object[].class);
	        List<Object[]> resultList = query.getResultList();

	        for (Object[] result : resultList) {
	            String itemName = (String) result[0];
	            Long sumQty = (Long) result[1];
	            int qty = sumQty != null ? sumQty.intValue() : 0;
	            dataMap.put(itemName, qty);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }
	    return dataMap;
	}

	
}
