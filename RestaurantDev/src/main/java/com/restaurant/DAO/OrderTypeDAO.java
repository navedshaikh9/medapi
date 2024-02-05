package com.restaurant.DAO;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.OrderType;
import AllNotivications.AllNotifications;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class OrderTypeDAO {

	

	// Add new OrderType
	public static boolean addOrderType(String orderType, boolean active) {
		boolean isSave = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();

			OrderType o = new OrderType();
			o.setOrderType(orderType);
			o.setStatus(active);
			Transaction tx = session.beginTransaction();

			Integer us = (Integer) session.save(o);
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

	// Get OrderType List

	public static ObservableList<OrderType> getAllOrderType() {
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();
			ObservableList<OrderType> depList = FXCollections.observableArrayList();
			Query nq = session.createQuery("from OrderType");
			List<OrderType> list = nq.list();

			for (OrderType orderType : list) {

				depList.add(orderType);

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

	// Update OrderType
	public boolean updateOrderType(int id, String type, boolean active) {
		boolean isUpdate = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			OrderType ot = session.get(OrderType.class, id);

			if (ot != null) {
				ot.setOrderType(type);
				ot.setStatus(active);

				Transaction tx = session.beginTransaction();
				session.update(ot);
				tx.commit();
				isUpdate = true;
			}

		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return isUpdate;
	}

	// Delete OrderType
	public static boolean deleteOrderType(int id) {
		Session session = null;
		Transaction transaction = null;
		boolean isDeleted = false;
		try {

			session = FactoryProvider.getFactory().openSession();
			transaction = session.beginTransaction();
			OrderType ot = session.get(OrderType.class, id);

			if (ot != null) {
				// If the entity exists, delete it
				session.delete(ot);
				transaction.commit();
				isDeleted = true;
			} else {
				AllNotifications.error("OrderType", "Record Not Found", "");
			}

		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return isDeleted;
	}

	// Load OrderType in Choicebox
	public static List<OrderType> getTrueOrderType() {

		Session session = null;
		List<OrderType> list = new ArrayList<OrderType>();
		try {
			session = FactoryProvider.getFactory().openSession();
			OrderType cat = new OrderType();
			Query q = session.createQuery("from OrderType where status=:ac");
			q.setParameter("ac", true);
			list = q.list();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return list;

	}

	public static ObservableList<OrderType> getOrderType() {
		Session session = null;
		ObservableList<OrderType> depList = FXCollections.observableArrayList();
		try {

			session = FactoryProvider.getFactory().openSession();
			Query q = session.createQuery("from OrderType where status=:ac");
			q.setParameter("ac", true);
			List<OrderType> list = q.list();

			for (OrderType orderType : list) {

				depList.add(orderType);

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
}
