package com.restaurant.DAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.KOT;
import AllNotivications.AllNotifications;
import AllNotivications.CommenFunction;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class KOTDAO {

	// Genrate Id
	
	public static int kotId() {
		int us = 0;
		Session session = null;

		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("select MAX(kotId) from KOT");
			Integer maxId = (Integer) nq.uniqueResult();

			if (maxId != null) {
				us = maxId + 1;
			} else {
				us = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return us;
	}

	public boolean kotGenrate(int kot_id, String order, String order_type, double total_price) {
		boolean isSave = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();

			KOT kot = new KOT();
			kot.setKotId(kot_id);
			kot.setKotOrder(order);
			kot.setOrderType(order_type);
			kot.setTotalPrice(total_price);
			kot.setComment("Placed");
			kot.setCreated(LocalDate.now());
			kot.setTime(CommenFunction.getTime());
			kot.setStatus(true);

			Transaction tx = session.beginTransaction();

			Integer us = (Integer) session.save(kot);
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

	public static ObservableList<KOT> getAllKOT() {
		try {

			ObservableList<KOT> depList = FXCollections.observableArrayList();
			Session s = FactoryProvider.getFactory().openSession();
			Query nq = s.createQuery("from KOT WHERE status=: status AND ORDER BY kotId DESC");
			nq.setParameter("status", true);
			List<KOT> list = nq.list();

			for (KOT kot: list) {

				
				depList.add(kot);

			}

			return depList;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}
	
	
	public static ObservableList<KOT> getAll() {
		try {

			ObservableList<KOT> depList = FXCollections.observableArrayList();
			Session s = FactoryProvider.getFactory().openSession();
			Query nq = s.createQuery("from KOT");
		
			List<KOT> list = nq.list();

			for (KOT kot: list) {

				
				depList.add(kot);

			}

			return depList;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}
	
	
public static List<KOT> getKOTOrder() {
		
		List<KOT> list = null;
	
		Session session = null;
		
		try {

			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from KOT");
			list = nq.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		return list;
		
	}
	

	public static ObservableList<KOT> getAllKOTActive() {
		try {

			ObservableList<KOT> depList = FXCollections.observableArrayList();
			Session s = FactoryProvider.getFactory().openSession();
			Query nq = s.createQuery("from KOT where status =:st");
			nq.setParameter("st", true);
			List<KOT> list = nq.list();

			for (KOT kot : list) {

				depList.add(kot);

			}

			return depList;

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	public static KOT openKOT(int id) {
		Session session = null;
		KOT kot = null;
		try {
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("FROM KOT WHERE kotId=:id AND status=:status");
			nq.setParameter("id", id);
			nq.setParameter("status", true);
			kot = (KOT) nq.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return kot;
	}

	// Update
	public boolean update(int kot_id, String order, String order_type, double total_price) {
		boolean isUpdate = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			KOT kot = session.get(KOT.class, kot_id);
			
			if (kot != null) {
				kot.setKotOrder(order);
				kot.setOrderType(order_type);
				kot.setTotalPrice(total_price);
				kot.setCreated(LocalDate.now());
				kot.setTime(CommenFunction.getTime());
				kot.setStatus(true);

				Transaction tx = session.beginTransaction();
				session.update(kot);
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
	
	// Update
		public static boolean updateOrderComment(int kot_id, String comment) {
			boolean isUpdate = false;
			Session session = null;
			try {

				session = FactoryProvider.getFactory().openSession();
				KOT kot = session.get(KOT.class, kot_id);
				
				if (kot != null) {
					kot.setComment(comment);
					Transaction tx = session.beginTransaction();
					session.update(kot);
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
	
	
	public boolean updateKOT(int kot_id, double total_price) {
		boolean isUpdate = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			KOT kot = session.get(KOT.class, kot_id);

			if (kot != null) {
				kot.setTotalPrice(total_price);
				Transaction tx = session.beginTransaction();
				session.update(kot);
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

	public boolean checkKOTID(int id) {
		boolean status = false;
		Session s = FactoryProvider.getFactory().openSession();
		Query nq = s.createQuery("from KOT where kotId=:id AND status=:status");
		nq.setParameter("id", id);
		nq.setParameter("status", true);
		List<KOT> list = nq.list();
		for (KOT b : list) {
			if (id == b.getKotId()) {
				status = true;
			}
		}

		return status;

	}

	// Check Table
	public List<KOT> getBookTable() {
		try {
			List<KOT> list = new ArrayList<KOT>();
			Session s = FactoryProvider.getFactory().openSession();
			Query nq = s.createQuery("from KOT where kotOrder=:ot and status=:st");
			nq.setParameter("ot", "Dine-In");
			nq.setParameter("st", true);
			list = nq.list();
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	
	
	public boolean checkTable(String table) {
		boolean status = false;
		Session s = FactoryProvider.getFactory().openSession();
		Query nq = s.createQuery("from KOT where kotOrder=:ot and orderType=:table and status=:st");
		nq.setParameter("ot", "Dine-In");
		nq.setParameter("table", table);
		nq.setParameter("st", true);		
		List<KOT> list = nq.list();
		for (KOT b : list) {
			if (table.equals(b.getOrderType()))
			{
				status = true;
			}
		}
		return status;

	}

	public static boolean deleteKOT(int kotId, String comment) {
	    Session session = null;
	    Transaction transaction = null;
	    boolean isDeleted = false;
	    try {
	        session = FactoryProvider.getFactory().openSession();
	        transaction = session.beginTransaction();
	        KOT kot = session.get(KOT.class, kotId);

	        if (kot != null) {
	        	kot.setComment(comment);
	            kot.setStatus(false);
	            kot.setCancelStatus(true);
	            session.update(kot);
	            transaction.commit();
	            isDeleted = true;
	        } else {
	            // Display an error message when KOT is not found
	            AllNotifications.error("KOT", "Record Not Found", "");
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

	
	
	public boolean updateKOTStatus(int kot_id) {
		boolean isUpdate = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			KOT kot = session.get(KOT.class, kot_id);
		if (kot != null) {
			kot.setStatus(false);
			Transaction tx = session.beginTransaction();
			session.update(kot);
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

}
