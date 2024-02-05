package com.restaurant.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.Discount;
import com.restaurant.Entity.Setting;
import AllNotivications.AllNotifications;
import SettingsManagment.SettingName;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DiscountDAO {

	

	// Add new Discount
	public static boolean addDiscount(String disName, double disPrice, double applyPrice, boolean active) {
		boolean isSave = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();

			Discount d = new Discount();
			d.setDiscountName(disName);
			d.setDiscountPrice(disPrice);
			d.setApplyDiscount(applyPrice);
			d.setStatus(active);

			Transaction tx = session.beginTransaction();

			Integer us = (Integer) session.save(d);
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

	// Get all Acctive Discount List

	public static ObservableList<Discount> getAllDiscount() {
		try {

			ObservableList<Discount> depList = FXCollections.observableArrayList();
			Session s = FactoryProvider.getFactory().openSession();
			Query nq = s.createQuery("from Discount");
			List<Discount> list = nq.list();

			for (Discount discount : list) {

				if (discount.isStatus() == true) {

					depList.add(discount);

				}
			}

			return depList;

		} catch (Exception e) {
			e.printStackTrace();
			return FXCollections.observableArrayList();
		}

	}

	public static ObservableList<Discount> loadAllDescount() {
		try {

			ObservableList<Discount> depList = FXCollections.observableArrayList();
			Session s = FactoryProvider.getFactory().openSession();
			Query nq = s.createQuery("from Discount");
			List<Discount> list = nq.list();

			for (Discount discount : list) {

				depList.add(discount);

			}

			return depList;

		} catch (Exception e) {
			e.printStackTrace();
			return FXCollections.observableArrayList();
		}

	}

	// Update Discount
	public boolean updateDiscount(int id, String disName, double disPrice, double applyPrice, boolean active) {
		boolean isUpdate = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Discount c = session.get(Discount.class, id);

			if (c != null) {
				c.setDiscountName(disName);
				c.setDiscountPrice(disPrice);
				c.setApplyDiscount(applyPrice);
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

	// Delete Discount
	public static boolean deleteDiscount(int id) {
		Session session = null;
		Transaction transaction = null;
		boolean isDeleted = false;
		try {

			session = FactoryProvider.getFactory().openSession();
			transaction = session.beginTransaction();
			Discount c = session.get(Discount.class, id);

			if (c != null) {
				// If the entity exists, delete it
				session.delete(c);
				transaction.commit();
				isDeleted = true;
			} else {
				AllNotifications.error("Discount", "Record Not Found", "");
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

	// Offer Apply

	public static Discount applyOffer(double total) {
		Discount des = null;
		Session session = null;

		try {

			double ap = 0;
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Discount");
			List<Discount> list = nq.list();

			for (Discount li : list) {

				if (li.isStatus() == true) {

					if (total > li.getApplyDiscount()) {
						des = new Discount();
						des.setDiscountName(li.getDiscountName());
						des.setDiscountPrice(li.getDiscountPrice());
						break;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return des;
	}

	public static double applyDiscount(double totalAmt) {

		// Get and Apply Offer
		double getDiscPrice = 0;

		Discount disc = DiscountDAO.applyOffer(totalAmt);
		if (disc != null) {
			getDiscPrice = disc.getDiscountPrice();
		}
		totalAmt = totalAmt - getDiscPrice;

		return totalAmt;

	}

	public static double getDiscountAmount(double totalAmt) {
		double getDiscPrice = 0;

		String offer = SettingName.showOffer;
		Setting ofsetting = SettingDAO.getSetting(offer);

		if (ofsetting != null) {

			if (ofsetting.isStatus() == true) {
				// Add offer

				Discount disc = DiscountDAO.applyOffer(totalAmt);
				if (disc != null) {
					getDiscPrice = disc.getDiscountPrice();
				}

			}
		}

		return getDiscPrice;

	}

}
