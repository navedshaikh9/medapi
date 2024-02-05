package com.restaurant.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.AdditionalCharges;
import com.restaurant.Entity.Setting;
import AllNotivications.AllNotifications;
import SettingsManagment.SettingName;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AdditionalChargesDAO {

	
	// Add new Charges
	public static boolean addCharges(String disName, double amt, boolean active) {
		boolean status = false;
		Session session =null;
		try {
			session = FactoryProvider.getFactory().openSession();

			AdditionalCharges ac = new AdditionalCharges();
			ac.setChargesName(disName);
			ac.setCharges(amt);
			ac.setStatus(active);

			Transaction tx = session.beginTransaction();

			Integer us = (Integer) session.save(ac);
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
	        if (session != null && session.isOpen()) {
	            session.close();
	        }
	    }
		return status;

	}

	
	public boolean update(int id, String cname, double amt, boolean sts) {
	    boolean status = false;
	    Session session = null;

	    try {
	    	session = FactoryProvider.getFactory().openSession();
	        
	        // Load the AdditionalCharges entity by its ID
	        AdditionalCharges additionalCharges = session.get(AdditionalCharges.class, id);

	        if (additionalCharges != null) {
	            // Data exists, update it
	            additionalCharges.setChargesName(cname);
	            additionalCharges.setCharges(amt);
	            additionalCharges.setStatus(sts);

	            Transaction tx = session.beginTransaction();
	            session.update(additionalCharges);
	            tx.commit();
	            status = true;
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

	    return status;
	}



	public boolean delete(int id) {
	    Session session = null;
	    Transaction transaction = null;
	    boolean isDeleted = false;

	    try {
	        session = FactoryProvider.getFactory().openSession();
	        transaction = session.beginTransaction();

	        // Load the entity by its ID
	        AdditionalCharges dditionalChargesDelete = session.get(AdditionalCharges.class, id);

	        if (dditionalChargesDelete != null) {
	            // If the entity exists, delete it
	            session.delete(dditionalChargesDelete);
	            transaction.commit();
	            isDeleted = true;
	        } else {
	            AllNotifications.error("Additional Charges", "Record Not Found", "");
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



	
	public static ObservableList<AdditionalCharges> getAllCharges() {
	    try {
	        ObservableList<AdditionalCharges> depList = FXCollections.observableArrayList();
	        Session session = FactoryProvider.getFactory().openSession();

	        Query query = session.createQuery("from AdditionalCharges");
	        List<AdditionalCharges> list = query.list();

	        for (AdditionalCharges entity : list) {
	            // Add each entity to the observable list
	            depList.add(entity);
	        }

	        return depList;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return FXCollections.observableArrayList(); // Return an empty list on error
	    }
	}
	
	
	public static double getTotalCharges()
	{
		
		String addtitionalCharges = SettingName.showAdditionalCharges;
		Setting acsetting = SettingDAO.getSetting(addtitionalCharges);
		double additotal = 0;
		
		if (acsetting.isStatus() == true) {
			// Add Addistion Charges

			ObservableList<AdditionalCharges> aclis = AdditionalChargesDAO.getAllCharges();

			for (AdditionalCharges ac : aclis) {
				if (ac.isStatus() == true) {
					additotal = additotal + ac.getCharges();
				}
			}

		}

		
		return additotal;
		
	}


}
