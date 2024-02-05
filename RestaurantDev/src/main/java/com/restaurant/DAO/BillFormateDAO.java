package com.restaurant.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.BillFormate;

import database.FactoryProvider;

public class BillFormateDAO {

	

	public static boolean insertHotelDetails(String name, String type, String address, String contact, String message) {

		boolean status = false;
		Session session = null;

		try {
			session = FactoryProvider.getFactory().openSession();

			BillFormate bf = new BillFormate();
			bf.setHotelName(name);
			bf.setHotelType(type);
			bf.setAddress(address);
			bf.setContact(contact);
			bf.setMessage(message);
			bf.setStatus(true);
			Transaction tx = session.beginTransaction();

			Integer us = (Integer) session.save(bf);
			if (us > 0) {
				status = true;
			}
			tx.commit();
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
		return status;

	}

	// Update Hotel Detiles
	public static boolean update(String name, String type, String address, String contact, String message) {
		boolean status = false;
		boolean active = true;
		Session localSession = null;

		try {

			localSession = FactoryProvider.getFactory().openSession();

			// Load the BillFormate entity by its ID
			BillFormate billFormate = localSession.get(BillFormate.class, 1);

			if (billFormate != null) {
				// Data exists, update it
				billFormate.setHotelName(name);
				billFormate.setHotelType(type);
				billFormate.setAddress(address);
				billFormate.setContact(contact);
				billFormate.setMessage(message);
				billFormate.setStatus(true);

				Transaction tx = localSession.beginTransaction();
				localSession.update(billFormate);
				tx.commit();
				status = true;
			}
		} catch (Exception e) {
			if (localSession != null) {
				localSession.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			if (localSession != null) {
				localSession.close();
			}
		}

		return status;
	}

	public static BillFormate getHotelDetails() {
		BillFormate hd = null;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();
			Query query = session.createQuery("FROM BillFormate WHERE status = :status");
			query.setParameter("status", true);
			hd = (BillFormate) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return hd;
	}

}
