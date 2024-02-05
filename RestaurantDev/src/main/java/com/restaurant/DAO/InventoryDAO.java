package com.restaurant.DAO;


import java.time.LocalDate;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.Inventory;
import com.restaurant.Entity.MenuItem;
import AllNotivications.AllNotifications;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InventoryDAO {
	
	
	

	public static boolean add(String category,String item_name,String unit, double price, double qty,double subUnit,double tax, double total,LocalDate date,  boolean chbstatus) {
		boolean isSave = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();

			Inventory in = new Inventory();
			in.setCategory(category);
			in.setItemName(item_name);
			in.setUnit(unit);
			in.setPrice(price);
			in.setQty(qty);
			in.setSubUnit(subUnit);
			in.setTax(tax);
			in.setTotal(total);
			in.setDate(date);
			in.setStatus(chbstatus);
			Transaction tx = session.beginTransaction();

			Integer us = (Integer) session.save(in);
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

	public static ObservableList<Inventory> getAll() {
		try {

			ObservableList<Inventory> depList = FXCollections.observableArrayList();
			Session s = FactoryProvider.getFactory().openSession();
			Query nq = s.createQuery("from Inventory");
			List<Inventory> list = nq.list();

			for (Inventory inventory : list) {

				depList.add(inventory);

			}

			return depList;

		} catch (Exception e) {
			e.printStackTrace();
			return FXCollections.observableArrayList();
		}

	}

	public static List<MenuItem> loadAllMenu() {
	    List<MenuItem> list = null;
	    Session session = null;

	    try {
	        session = FactoryProvider.getFactory().openSession();
	        Query query = session.createQuery("from MenuItem"); // Assuming MenuItem is your entity class
	        list = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }

	    return list;
	}


	public static Inventory getInventory(int id) {
		Session session = null;
		Inventory in = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Inventory where inventoryId=:id");
			nq.setParameter("id", id);
			in = (Inventory) nq.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return in;
	}

	

	public static boolean update(int id,String category,String item_name,String unit, double price, double qty,double subUnit,double tax, double total, LocalDate date,  boolean chbstatus) {
		boolean isUpdate = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Inventory in = session.get(Inventory.class, id);
			if (in != null) {
				in.setCategory(category);
				in.setItemName(item_name);
				in.setUnit(unit);
				in.setPrice(price);
				in.setQty(qty);
				in.setSubUnit(subUnit);
				in.setTax(tax);
				in.setTotal(total);
				in.setDate(date);
				in.setStatus(chbstatus);

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

	public static boolean delete(int id) {
		Session session = null;
		Transaction transaction = null;
		boolean isDeleted = false;
		try {

			session = FactoryProvider.getFactory().openSession();
			transaction = session.beginTransaction();
			Inventory c = session.get(Inventory.class, id);

			if (c != null) {
				// If the entity exists, delete it
				session.delete(c);
				transaction.commit();
				isDeleted = true;
			} else {
				AllNotifications.error("Inventory", "Record Not Found", "");
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

	
	public static double getSubQty(String stockItem) {
		double us = 0;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("select subUnit from Inventory where itemName=:itemName");
			nq.setParameter("itemName", stockItem);
			double u = (double) nq.uniqueResult();
			if ( u != 0) {
				us= u;
				
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
	
	public static boolean stockUpdate(String rawMaterialName, double subUnit, double kilogram) {
	    boolean status = false;
	    Session session = null;

	    try {
	        session = FactoryProvider.getFactory().openSession();
	        Query query = session.createQuery("update Inventory set subUnit = :subUnit, qty = :qty WHERE itemName = :rawMaterial AND status=:status");
	        query.setParameter("subUnit", subUnit);
	        query.setParameter("qty", kilogram);
	        query.setParameter("rawMaterial", rawMaterialName);
	        query.setParameter("status", true);
	        Transaction tx = session.beginTransaction();
	        int rowCount = query.executeUpdate();

	        if (rowCount > 0) {
	            status = true;
	        }
	      
	        tx.commit();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (session != null && session.isOpen()) {
	            session.close();
	
	        }
	    }

	    return status;
	}
	
	
	public static void showStockQty()
	{
		ObservableList<Inventory> inventory =  getAll();
		String lowStockItems="";
		for(Inventory stock:inventory)
		{
			if(stock.getQty()<=5 && stock.isStatus()==true)
			{
				lowStockItems += stock.getItemName() + ": Qty " + stock.getQty() + "\n"; // Concatenating strings
		          		        			       
			}
		}
		
		if(lowStockItems!=null)
		{
			AllNotifications.warning("Low Stock Items", "Please check this item will Out Of Stock\n"+lowStockItems.trim(), "");
		}
	}
	
	
	
	
}
