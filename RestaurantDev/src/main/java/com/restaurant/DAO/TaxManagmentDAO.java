package com.restaurant.DAO;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.Setting;
import com.restaurant.Entity.Tax;
import AllNotivications.AllNotifications;
import SettingsManagment.SettingName;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaxManagmentDAO {

	

	// Add new Tax
	public static boolean addTax(String taxName, double percent, boolean active) {
		boolean isSave = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();

			Tax t = new Tax();
			t.setTaxName(taxName);
			t.setTaxPercent(percent);
			t.setStatus(active);

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

	// Get Tax List

	public static ObservableList<Tax> getAllTax() {
		Session session = null;
		try {

			ObservableList<Tax> t = FXCollections.observableArrayList();
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Tax");
			List<Tax> list = nq.list();

			for (Tax tax : list) {

				t.add(tax);

			}

			return t;

		} catch (Exception e) {
			e.printStackTrace();
			return FXCollections.observableArrayList();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	// Get One Tax
	public static Tax getTax() {
		Session session = null;
		Tax d = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Tax");
			List<Tax> list = nq.list();

			for (Tax li : list) {

				if (li.isStatus() == true) {

					d = new Tax();
					d.setTaxId(li.getTaxId());
					d.setTaxName(li.getTaxName());
					d.setTaxPercent(li.getTaxPercent());
					d.setStatus(li.isStatus());

				}
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

	// Update Tax
	public boolean updateTax(int id, String taxname, double percent, boolean active) {
		boolean isUpdate = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Tax tax = session.get(Tax.class, id);

			if (tax != null) {
				tax.setTaxName(taxname);
				tax.setTaxPercent(percent);
				tax.setStatus(active);
				Transaction tx = session.beginTransaction();
				session.update(tax);
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

	// Delete Tax
	public static boolean deleteTax(int id) {

		Session session = null;
		Transaction transaction = null;
		boolean isDeleted = false;
		try {

			session = FactoryProvider.getFactory().openSession();
			transaction = session.beginTransaction();
			Tax tax = session.get(Tax.class, id);

			if (tax != null) {
				// If the entity exists, delete it
				session.delete(tax);
				transaction.commit();
				isDeleted = true;
			} else {
				AllNotifications.error("Tax", "Record Not Found", "");
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

	public static double applyTaxOnTotal(double totalAmt) {
		// Get and Apply tax
		double getTax = 0;
		double amt=0;
		String taxs = SettingName.applyTaxSetting;
		Setting txsetting = SettingDAO.getSetting(taxs);
		if (txsetting != null) {
			if (txsetting.isStatus() == true) {
				Tax tax = TaxManagmentDAO.getTax();
				if (tax != null) {

					getTax = tax.getTaxPercent();

				}
				totalAmt = getTax / 100 * totalAmt + totalAmt;

				 amt = (int) Math.round(totalAmt);
			}
			}
		
		return amt;

	}

	public static double getTaxTotal(double totalAmt) {
		double getTax = 0;
		double amt=0;
		String taxs = SettingName.applyTaxSetting;
		Setting txsetting = SettingDAO.getSetting(taxs);
		if (txsetting != null) {
			if (txsetting.isStatus() == true) {
		Tax tax = TaxManagmentDAO.getTax();
		if (tax != null) {

			getTax = tax.getTaxPercent();

		}
		totalAmt = getTax / 100 * totalAmt;

		 amt = (int) Math.round(totalAmt);
			}
			}
		return amt;

	}

	public static double getTaxTotal(double totalAmt, double txt) {

		totalAmt = txt / 100 * totalAmt;

		return totalAmt;

	}

	public static double taxGet() {
		double getTax = 0;
		
		String taxs = SettingName.applyTaxSetting;
		Setting txsetting = SettingDAO.getSetting(taxs);
		if (txsetting != null) {
			if (txsetting.isStatus() == true) {
		
				Tax tax = TaxManagmentDAO.getTax();
				if (tax != null) {

					getTax = tax.getTaxPercent();

				}
				
			}
			}
		
		return getTax;

	}
}
