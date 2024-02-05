package com.restaurant.DAO;


import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.Customer;
import AllNotivications.AllNotifications;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerDAO {

	
	// Add new Customer
	public static boolean addCustomer(int saleId, String name, Long contact, String email, String address,
			double totalAmt, double pendingAmt, boolean active) {
		boolean isSave = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();

			Customer c = new Customer();
			c.setSaleId(saleId);
			c.setCustomerName(name);
			c.setContact(contact);
			c.setEmail(email);
			c.setAddress(address);
			c.setTotalAmount(totalAmt);
			c.setPendingAmount(pendingAmt);
			c.setStatus(active);

			Transaction tx = session.beginTransaction();

			Integer us = (Integer) session.save(c);
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

	public static ObservableList<Customer> getAllCustomer() {
		try {

			ObservableList<Customer> depList = FXCollections.observableArrayList();
			Session s = FactoryProvider.getFactory().openSession();
			Query nq = s.createQuery("from Customer");
			List<Customer> list = nq.list();

			for (Customer customer : list) {

				depList.add(customer);

			}

			return depList;

		} catch (Exception e) {
			e.printStackTrace();
			return FXCollections.observableArrayList();
		}

	}

	// Update Customer
	public boolean updateCustomer(int id, int saleId, String name, Long contact, String email, String address,
			double totalAmt, double pendingAmt, boolean active) {
		boolean isUpdate = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Customer c = session.get(Customer.class, id);

			if (c != null) {
				c.setCustomerId(id);
				c.setSaleId(saleId);
				c.setCustomerName(name);
				c.setContact(contact);
				c.setEmail(email);
				c.setAddress(address);
				c.setTotalAmount(totalAmt);
				c.setPendingAmount(pendingAmt);
				c.setStatus(active);

				Transaction tx = session.beginTransaction();
				session.update(c);
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

	// Delete Category
	public static boolean deleteCustomer(int id) {

		Session session = null;
		Transaction transaction = null;
		boolean isDeleted = false;
		try {

			session = FactoryProvider.getFactory().openSession();
			transaction = session.beginTransaction();
			Customer c = session.get(Customer.class, id);
			if (c != null) {
				// If the entity exists, delete it
				session.delete(c);
				transaction.commit();
				isDeleted = true;
			} else {
				AllNotifications.error("Customer", "Record Not Found", "");
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

	public static Customer getCustomer(int id) {
		Customer customer = null;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Customer where customerId=:id");
			nq.setParameter("id", id);
			customer = (Customer) nq.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return customer;
	}

}
