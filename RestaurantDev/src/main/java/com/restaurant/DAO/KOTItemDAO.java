package com.restaurant.DAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.KOTItem;
import AllNotivications.CommenFunction;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class KOTItemDAO {

	CommenFunction cf = new CommenFunction();

	// Genrate Id

	

	public boolean save(int kot_id, String item, int qty, double price, double total_price) {
		boolean isSave = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();

			KOTItem kot_item = new KOTItem();
			kot_item.setKotId(kot_id);
			kot_item.setItemName(item);
			kot_item.setQty(qty);
			kot_item.setPrice(price);
			kot_item.setItemTotal(total_price);
			kot_item.setStatus(true);
			kot_item.setCancelStatus(false);
			kot_item.setCreated(LocalDate.now());

			Transaction tx = session.beginTransaction();

			Integer us = (Integer) session.save(kot_item);
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
	
	public static List<KOTItem> getAllKOTItem() {

		List<KOTItem> list = new ArrayList();

		try {

			Session s = FactoryProvider.getFactory().openSession();
			Query nq = s.createQuery("from KOTItem");
			 list = nq.list();

			return list;

		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}

	}

	public static ObservableList<KOTItem> getAll(int kot_id) {
		try {

			ObservableList<KOTItem> depList = FXCollections.observableArrayList();
			Session s = FactoryProvider.getFactory().openSession();
			Query nq = s.createQuery("from KOTItem where kotId=:id AND status=:status");
			nq.setParameter("id", kot_id);
			nq.setParameter("status", true);
			List<KOTItem> list = nq.list();

			for (KOTItem kotItem : list) {
				depList.add(kotItem);

			}

			return depList;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	public boolean update(int kot_id, int kot_item_id, String item_name, int qty, double price, double total) {
		boolean isUpdate = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			KOTItem kot_item = session.get(KOTItem.class, kot_item_id);
			if (kot_item != null && kot_id == kot_item.getKotId() && kot_item_id == kot_item.getKotItemId()) {
				kot_item.setItemName(item_name);
				kot_item.setQty(qty);
				kot_item.setPrice(price);
				kot_item.setItemTotal(total);

				Transaction tx = session.beginTransaction();
				session.update(kot_item);
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

	

	public boolean checkKOT(int kot_id, int kot_item_id) {
		boolean status = false;
		Session s = FactoryProvider.getFactory().openSession();
		Query nq = s.createQuery("from KOTItem where kotId=:kot_id and kotItemId=:kot_item_id AND status=:status");
		nq.setParameter("kot_id", kot_id);
		nq.setParameter("kot_item_id", kot_item_id);
		nq.setParameter("status", true);
		List<KOTItem> list = nq.list();
		for (KOTItem b : list) {
			if (kot_id == b.getKotId() && kot_item_id == b.getKotItemId()) {
				status = true;
			}
		}

		return status;

	}

	public boolean checkKOT(int kot_id) {
		boolean status = false;
		Session s = FactoryProvider.getFactory().openSession();
		Query nq = s.createQuery("from KOTItem where kotId=:kotid AND status=:status");
		nq.setParameter("kotid", kot_id);
		nq.setParameter("status", true);
		
		List<KOTItem> list = nq.list();
		for (KOTItem b : list) {
			if (kot_id == b.getKotId()) {
				status = true;
			}
		}
		return status;

	}

	// Delete
	public static boolean delete(int kot_id, int kot_item_id) {

		boolean status = false;
		try {

			Session s = FactoryProvider.getFactory().openSession();

			Query q = s.createQuery("delete from KOTItem where kotId=:kot_id and kotItemId=:kot_item_id");
			q.setParameter("kot_id", kot_id);
			q.setParameter("kot_item_id", kot_item_id);
			Transaction tx = s.beginTransaction();
			int st = q.executeUpdate();
			if (st > 0) {
				status = true;
			}

			tx.commit();
			s.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return status;
	}

	// Delete KOT Item
	public static boolean deleteKOTItem(int id) {
		boolean status = false;
		try {

			Session s = FactoryProvider.getFactory().openSession();

			Query q = s.createQuery("delete from KOTItem where kotId=:id");
			q.setParameter("id", id);

			Transaction tx = s.beginTransaction();
			int st = q.executeUpdate();
			if (st > 0) {
				status = true;
			}

			tx.commit();
			s.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return status;
	}

	public boolean updateKOTItemStatus(int kot_id) {
		boolean isUpdate = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			KOTItem in = session.get(KOTItem.class, kot_id);

			if (in != null) {
				in.setStatus(false);
				Transaction tx = session.beginTransaction();
				session.update(in);
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
	
	
	public boolean updateCancleKOTItemAll(int kotId) {
		 boolean isUpdate = false;
		    Session session = null;
		    try {
		        session = FactoryProvider.getFactory().openSession();

		        Transaction tx = session.beginTransaction();

		        // Update the comment and status of KOTItem where both kot_id and itemId match
		        int updatedRows = session.createQuery("UPDATE KOTItem SET status = :status, cancelStatus= :cancelStatus WHERE kotId = :kot_id")
		               .setParameter("status", false)
		               .setParameter("cancelStatus", true)
		                 .setParameter("kot_id", kotId)
		                .executeUpdate();

		        tx.commit();

		        isUpdate = updatedRows > 0;

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
	
	
	
	public boolean updateCancelKOTItem(int kot_id, int itemId, String comment) {
	    boolean isUpdate = false;
	    Session session = null;
	    try {
	        session = FactoryProvider.getFactory().openSession();

	        Transaction tx = session.beginTransaction();

	        // Update the comment and status of KOTItem where both kot_id and itemId match
	        int updatedRows = session.createQuery("UPDATE KOTItem SET comment = :comment, status = :status, cancelStatus= :cancelStatus WHERE kotItemId = :itemId AND kotId = :kot_id")
	                .setParameter("comment", comment)
	                .setParameter("status", false)
	                .setParameter("cancelStatus", true)
	                .setParameter("itemId", itemId)
	                .setParameter("kot_id", kot_id)
	                .executeUpdate();

	        tx.commit();

	        isUpdate = updatedRows > 0;
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

	

}
