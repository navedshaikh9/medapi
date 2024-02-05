package com.restaurant.DAO;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.Unit;
import AllNotivications.AllNotifications;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UnitDAO {

	
	// Add new Unit
	public static boolean addUnit(String name, boolean st) {
		boolean isSave = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();

			Unit unit = new Unit();
			unit.setAccountId(TrialDAO.getAccountId());
			unit.setOutletId(TrialDAO.getOutLetId());
			unit.setUnitName(name);
			unit.setStatus(st);

			Transaction tx = session.beginTransaction();

			Integer us = (Integer) session.save(unit);
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

	// Get Unit List

	public static ObservableList<Unit> getAllUnit() {
		Session session = null;
		try {

			ObservableList<Unit> depList = FXCollections.observableArrayList();
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Unit");
			List<Unit> list = nq.list();

			for (Unit unit : list) {

				depList.add(unit);

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

	// Update Unit
	public boolean updateUnit(int id, String name, boolean sta) {
		boolean isUpdate = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Unit unit = session.get(Unit.class, id);

			if (unit != null) {
				unit.setUnitName(name);
				unit.setStatus(sta);
				Transaction tx = session.beginTransaction();
				session.update(unit);
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

	// Delete Unit
	public static boolean deleteUnit(int id) {

		Session session = null;
		Transaction transaction = null;
		boolean isDeleted = false;
		try {

			session = FactoryProvider.getFactory().openSession();
			transaction = session.beginTransaction();
			Unit unit = session.get(Unit.class, id);

			if (unit != null) {
				// If the entity exists, delete it
				session.delete(unit);
				transaction.commit();
				isDeleted = true;
			} else {
				AllNotifications.error("Unit", "Record Not Found", "");
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

	// Load Unit in Choicebox
	public static List<Unit> getactiveUnit() {
		Session session = null;
		List<Unit> list = new ArrayList<Unit>();
		try {

			session = FactoryProvider.getFactory().openSession();
			Unit cat = new Unit();
			Query q = session.createQuery("from Unit where status=:st");
			q.setParameter("st", true);
			list = q.list();

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
