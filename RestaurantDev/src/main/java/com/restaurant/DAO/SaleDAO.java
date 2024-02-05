package com.restaurant.DAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.Sale;
import AllNotivications.CommenFunction;
import database.FactoryProvider;

public class SaleDAO {

	// Genrate Id
	public static int saleId() {
		int us = 0;
		Session session = null;

		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("select MAX(saleId) from Sale");
			Integer maxId = (Integer) nq.uniqueResult();

			if (maxId != null) {
				us = maxId + 1;
			} else {
				us = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return us;
	}

	public boolean add(int sale_id, String order, String order_type, String payMethod, double totalamt, double discount,
			double tax, double grandTotal) {
		boolean isSave = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();

			Sale sale = new Sale();
			sale.setSaleId(sale_id);
			sale.setSaleOrder(order);
			sale.setOrderType(order_type);
			sale.setPayMethod(payMethod);
			sale.setTotalAmt(totalamt);
			sale.setDiscountAmt(discount);
			sale.setTaxPercent(tax);
			sale.setGrandTotal(grandTotal);
			sale.setCreated(LocalDate.now());
			sale.setTime(CommenFunction.getTime());
			sale.setStatus(true);

			Transaction tx = session.beginTransaction();

			Integer us = (Integer) session.save(sale);
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

	public static Sale openBill(int id) {
		Session session = null;
		Sale d = null;
		try {
			List<Sale> list = new ArrayList<Sale>();
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Sale where saleId=:id");
			nq.setParameter("id", id);
			d = (Sale) nq.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return d;

	}

	public static Sale getSale() {
		Session session = null;
		Sale d = null;
		try {
			List<Sale> list = new ArrayList<Sale>();
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Sale where created=:date");
			nq.setParameter("date", LocalDate.now());
			d = (Sale) nq.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return d;
	}

	public static int getOrderTotal(String order) {
		Session session = null;
		int total = 0;

		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Sale where saleOrder=:order and status=:st");
			nq.setParameter("order", order);
			nq.setParameter("st", true);
			List<Sale> list = nq.list();

			for (Sale li : list) {

				if (li.getCreated().equals(LocalDate.now())) {
					total++;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return total;

	}

	public static double getOrderSale(String order) {
		Session session = null;
		double total = 0;

		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Sale where saleOrder=:order and status=:st");
			nq.setParameter("order", order);
			nq.setParameter("st", true);
			List<Sale> list = nq.list();

			for (Sale li : list) {

				if (li.getCreated().equals(LocalDate.now())) {
					total = total + li.getGrandTotal();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return total;

	}

	public static double getTax() {
		Session session = null;
		double total = 0;

		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Sale where status=:st");
			nq.setParameter("st", true);
			List<Sale> list = nq.list();

			for (Sale li : list) {
				if (li.getCreated().equals(LocalDate.now())) {
					total = total + li.getTaxPercent();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return total;

	}

	public static double getDiscount() {
		Session session = null;
		double total = 0;

		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Sale where status=:st");
			nq.setParameter("st", true);
			List<Sale> list = nq.list();

			for (Sale li : list) {
				if (li.getCreated().equals(LocalDate.now())) {
					total = total + li.getDiscountAmt();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return total;

	}

	public static double getTotal() {
		Session session = null;
		double total = 0;

		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Sale where status=:st");
			nq.setParameter("st", true);
			List<Sale> list = nq.list();

			for (Sale li : list) {
				if (li.getCreated().equals(LocalDate.now())) {

					total = total + li.getGrandTotal();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return total;

	}

	public static double getTotalWithTax() {
		Session session = null;
		double total = 0;

		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Sale where status=:st");
			nq.setParameter("st", true);
			List<Sale> list = nq.list();

			for (Sale li : list) {
				if (li.getCreated().equals(LocalDate.now())) {
					double taxAmtCount = TaxManagmentDAO.getTaxTotal(li.getTotalAmt(), li.getTaxPercent());
					total += taxAmtCount;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return total;

	}

	public static double getPayMethodTotal(String payMethod) {
		Session session = null;
		double total = 0;

		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("FROM Sale where status=:st and payMethod=:pay and created=:date");
			nq.setParameter("st", true);
			nq.setParameter("pay", payMethod);
			nq.setParameter("date", LocalDate.now());
			List<Sale> list = nq.list();

			for (Sale li : list) {
				total = total + li.getGrandTotal();

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return total;

	}

	public static Map<String, Double> fetchPieChartData() {
		Session session = null;
		Map<String, Double> dataMap = new HashMap<>();
		double amt = 0;
		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("FROM Sale where created=:date");
			nq.setParameter("date", LocalDate.now());
			List<Sale> list = nq.list();

			for (Sale li : list) {
				dataMap.put(li.getSaleOrder(), li.getGrandTotal());
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

	public static Map<String, Double> fetchPieChartData(LocalDate from_date, LocalDate to_date) {
		Session session = null;
		Map<String, Double> dataMap = new HashMap<>();
		double amt = 0;
		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("FROM Sale WHERE created BETWEEN :startDate AND :endDate");
			nq.setParameter("startDate", from_date);
			nq.setParameter("endDate", to_date);
			List<Sale> list = nq.list();

			for (Sale li : list) {
				dataMap.put(li.getSaleOrder(), li.getGrandTotal());
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

	public static List<Sale> getAllSale() {
		
		List<Sale> list = null;
	
		Session session = null;
		
		try {

			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Sale");
			list = nq.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		return list;
		
	}

	public static List<Sale> getAllSale(LocalDate from_date, LocalDate to_date) {

		Session session = null;
		List<Sale> list = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("FROM Sale WHERE created BETWEEN :startDate AND :endDate");
			nq.setParameter("startDate", from_date);
			nq.setParameter("endDate", to_date);
			list = nq.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;
	}
	
	

}
