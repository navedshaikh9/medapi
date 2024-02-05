package com.restaurant.DAO;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.restaurant.Entity.EmpAccess;

import AllNotivications.AllNotifications;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EmpAccessDAO {

	

	// Add new Employee
	public static boolean save(String empName, String username, String password, boolean active) {
		boolean isSave = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();
			EmpAccess user = new EmpAccess();
			user.setUserName(empName);
			user.setUserId(username);
			user.setPassword(password);
			user.setStatus(active);

			Transaction tx = session.beginTransaction();

			Integer us = (Integer) session.save(user);
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

	// Get all Employee List
	public static ObservableList<EmpAccess> getAllSubUsers() {
		try {

			ObservableList<EmpAccess> empList = FXCollections.observableArrayList();
			Session s = FactoryProvider.getFactory().openSession();
			Query nq = s.createQuery("from EmpAccess");
			List<EmpAccess> list = nq.list();

			for (EmpAccess employee : list) {

				empList.add(employee);

			}

			return empList;

		} catch (Exception e) {
			e.printStackTrace();
			return FXCollections.observableArrayList();
		}

	}

	// Update Employee
	public static boolean update(int subuserId, String empName, String username, String password, boolean active) {
		boolean isUpdate = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			EmpAccess user = session.get(EmpAccess.class, subuserId);

			if (user != null) {
				user.setUserName(empName);
				user.setUserId(username);
				user.setPassword(password);
				user.setStatus(active);

				Transaction tx = session.beginTransaction();
				session.update(user);
				tx.commit();
				isUpdate = true;
			}

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
		return isUpdate;
	}

	// Delete Employee
	public static boolean delete(int subuserId) {
		Session session = null;
		Transaction transaction = null;
		boolean isDeleted = false;
		try {

			session = FactoryProvider.getFactory().openSession();
			transaction = session.beginTransaction();
			EmpAccess user = session.get(EmpAccess.class, subuserId);

			if (user != null) {
				// If the entity exists, delete it
				session.delete(user);
				transaction.commit();
				isDeleted = true;
			} else {
				AllNotifications.error("Sub User", "Record Not Found", "");
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

	public static boolean getUserLoginStatus(String userId, String password) {
		boolean status = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from EmpAccess WHERE userId=:userid AND password=:pass");
			nq.setParameter("userid", userId);
			nq.setParameter("pass", password);
			List<EmpAccess> list = nq.list();
			for (EmpAccess b : list) {
				if (userId.equals(b.getUserId()) && password.equals(b.getPassword()) && b.isStatus() == true) {
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

	public static boolean getDuplicatUser(String userId) {
		boolean status = true;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();

			Query nq = session.createQuery("from EmpAccess WHERE userId=:userid");
			nq.setParameter("userid", userId);
			List<EmpAccess> list = nq.list();
			for (EmpAccess b : list) {
				if (userId.equals(b.getUserId())) {
					status = false;
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

	public static boolean checkUserAccess() {
		List<EmpAccess> list = null;
		boolean status = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
	Query nq = session.createQuery("FROM EmpAccess");
		list = nq.list();
		if (list == null || list.isEmpty()) {
			status = true;
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
	
	
	public static String getUserName(String userId) {
		String userName=null;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();

			Query nq = session.createQuery("from EmpAccess WHERE userId=:userid");
			nq.setParameter("userid", userId);
			List<EmpAccess> list = nq.list();
			for (EmpAccess b : list) {
				if (userId.equals(b.getUserId())) {
					userName=b.getUserName();
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return userName;

	}
}
