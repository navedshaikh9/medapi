package com.restaurant.DAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.Trial;
import AllNotivications.CommenFunction;
import database.FactoryProvider;

public class TrialDAO {

	

	// Add new Charges
	public static boolean startPlan(String userId,String outletId, String ipAddress, String licenceKey, LocalDate start,
			LocalDate end) {
		boolean isSave = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();

			CommenFunction cf = new CommenFunction();

			Trial t = new Trial();
			t.setAccountId(userId);
			t.setOutletId(outletId);
			t.setStart(start);
			t.setEnd(end);
			t.setIpAddress(ipAddress);
			t.setUserLicenceKey(licenceKey);
			t.setStatus(true);
			Transaction tx = session.beginTransaction();

			Integer us = (Integer) session.save(t);
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

	public static Trial checkTrayel() {
		Session session = null;
		Trial d = null;
		try {

			List<Trial> list = new ArrayList<Trial>();
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Trial WHERE status=:st");
			nq.setParameter("st", true);
			list = nq.list();

			for (Trial li : list) {

				d = new Trial();
				d.setTrailId(li.getTrailId());
				d.setStart(li.getStart());
				d.setEnd(li.getEnd());
				d.setStatus(li.isStatus());

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (session != null) {
				session.close();
			}
		}
		return d;
	}

	// checkTryel
	public static boolean checkTrayelStatus() {
		Session session = null;
		boolean status = false;
		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Trial where status=:st");
			nq.setParameter("st", true);
			List<Trial> list = nq.list();
			for (Trial b : list) {
				if (b.isStatus() == true
						&& (LocalDate.now().equals(b.getStart()) || LocalDate.now().isAfter(b.getStart()))
						&& (LocalDate.now().equals(b.getEnd()) || LocalDate.now().isBefore(b.getEnd()))) {
					status = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return status;

	}

	// Update Category
	public static boolean update(int id) {
		boolean isUpdate = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Trial trail = session.get(Trial.class, id);

			if (trail != null) {
				trail.setStatus(false);
				Transaction tx = session.beginTransaction();
				session.update(trail);
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

	public static boolean checkDuplicatLicenceKeyStatus(String key) {
		boolean status = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Trial where userLicenceKey=:st");
			nq.setParameter("st", key);
			List<Trial> list = nq.list();
			for (Trial b : list) {
				if (key.equals(b.getUserLicenceKey())) {
					status = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return status;

	}

	public static String getUserId() {

		String userId = null;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Trial where status=:st");
			nq.setParameter("st", true);
			List<Trial> list = nq.list();
			for (Trial li : list) {
				if (li.isStatus() == true) {
					userId = li.getAccountId();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return userId;

	}
	
	
	
	public static String getAccountId() {

		String accountId = null;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Trial where status=:st");
			nq.setParameter("st", true);
			List<Trial> list = nq.list();
			for (Trial li : list) {
				if (li.isStatus() == true) {
					accountId = li.getAccountId();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return accountId;

	}
	
	
	public static String getOutLetId() {

		String outLetId = null;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Trial where status=:st");
			nq.setParameter("st", true);
			List<Trial> list = nq.list();
			for (Trial li : list) {
				if (li.isStatus() == true) {
					outLetId = li.getOutletId();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return outLetId;

	}

}
