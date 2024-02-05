package com.restaurant.DAO;


import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.PaymentMethod;
import AllNotivications.AllNotifications;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PaymentMethodDAO {

	

	// Add new Category
	public static boolean addPaymentMethod(String name,String upiId, boolean upiIdStatus, boolean active) {
		boolean isSave = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();

			PaymentMethod pm = new PaymentMethod();
			pm.setPaymentMethod(name);
			pm.setUpiId(upiId);
			pm.setUpiIdStatus(upiIdStatus);
			pm.setStatus(active);
			Transaction tx = session.beginTransaction();

			Integer us = (Integer) session.save(pm);
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

	// Get Category List

	public static ObservableList<PaymentMethod> getAllPaymentMethod() {
		Session session = null;
		try {

			ObservableList<PaymentMethod> depList = FXCollections.observableArrayList();
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from PaymentMethod");
			List<PaymentMethod> list = nq.list();

			for (PaymentMethod paymentMethod : list) {

				depList.add(paymentMethod);

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

	// Update PaymentMethod
	public boolean updatePaymentMethod(int id, String name,String upiId, boolean upiIdStatus, boolean active) {
		boolean isUpdate = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			PaymentMethod pm = session.get(PaymentMethod.class, id);

			if (pm != null) {
				pm.setPaymentMethod(name);
				pm.setUpiId(upiId);
				pm.setUpiIdStatus(upiIdStatus);
				pm.setStatus(active);

				Transaction tx = session.beginTransaction();
				session.update(pm);
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

	// Delete Category
	public static boolean deletePaymentMethod(int id) {

		Session session = null;
		Transaction transaction = null;
		boolean isDeleted = false;
		try {

			session = FactoryProvider.getFactory().openSession();
			transaction = session.beginTransaction();
			PaymentMethod pm = session.get(PaymentMethod.class, id);

			if (pm != null) {
				// If the entity exists, delete it
				session.delete(pm);
				transaction.commit();
				isDeleted = true;
			} else {
				AllNotifications.error("PaymentMethod", "Record Not Found", "");
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

	// Load PaymentMethod in Choicebox
	public static List<PaymentMethod> getTrueAll() {
		Session session = null;
		List<PaymentMethod> list = new ArrayList<PaymentMethod>();
		try {

			session = FactoryProvider.getFactory().openSession();
			Query q = session.createQuery("from PaymentMethod where status=:act");
			q.setParameter("act", true);
			list = q.list();

		} catch (Exception e) {
			
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return list;

	}
	
	
	public static String getUPIId() {
		Session session = null;
		String upiId = null;

		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from PaymentMethod where status=:act AND upiIdStatus=:upiStatu");
			nq.setParameter("act", true);
			nq.setParameter("upiStatu", true);
			List<PaymentMethod> list = nq.list();

			for (PaymentMethod li : list) {

				if(li.isStatus()==true && li.isUpiIdStatus()==true)
				{
					upiId = li.getUpiId();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return upiId;

	}

}
