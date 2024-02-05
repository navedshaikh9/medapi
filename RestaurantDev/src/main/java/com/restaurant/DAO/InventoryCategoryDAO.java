package com.restaurant.DAO;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.InventoryCaregory;
import AllNotivications.AllNotifications;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InventoryCategoryDAO {
	

	

	// Add new Category
		public static boolean addCategory(String category, boolean active) {
			boolean isSave = false;
			Session session = null;
			try {
				session = FactoryProvider.getFactory().openSession();

				InventoryCaregory cate = new InventoryCaregory();
				cate.setCategory(category);
				cate.setStatus(active);

				Transaction tx = session.beginTransaction();

				Integer us = (Integer) session.save(cate);
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

		public static ObservableList<InventoryCaregory> getAllCategory() {
			try {

				ObservableList<InventoryCaregory> depList = FXCollections.observableArrayList();
				Session s = FactoryProvider.getFactory().openSession();
				Query nq = s.createQuery("from InventoryCaregory");
				List<InventoryCaregory> list = nq.list();

				for (InventoryCaregory inventoryCaregory : list) {

					
					depList.add(inventoryCaregory);

				}

				return depList;

			} catch (Exception e) {
				e.printStackTrace();
				return FXCollections.observableArrayList();
			}

		}

	// Update Category
		public boolean updateCategory(int id, String category, boolean active) {
			boolean isUpdate = false;
			Session session = null;
			try {

				session = FactoryProvider.getFactory().openSession();
				InventoryCaregory cate = session.get(InventoryCaregory.class, id);

				if (cate != null) {
					cate.setCategory(category);
					cate.setStatus(active);

					Transaction tx = session.beginTransaction();
					session.update(cate);
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

	// Delete Category
		public static boolean deleteCategory(int id) {

			Session session = null;
			Transaction transaction = null;
			boolean isDeleted = false;
			try {

				session = FactoryProvider.getFactory().openSession();
				transaction = session.beginTransaction();
				InventoryCaregory cate = session.get(InventoryCaregory.class, id);

				if (cate != null) {
					// If the entity exists, delete it
					session.delete(cate);
					transaction.commit();
					isDeleted = true;
				} else {
					AllNotifications.error("Employee", "Record Not Found", "");
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

		// Load Cateogry in Choicebox
		public static List<InventoryCaregory> getActiveTrueCategory() {
			Session session = null;
			List<InventoryCaregory> list = new ArrayList<InventoryCaregory>();
			try {

				session = FactoryProvider.getFactory().openSession();
				Query q = session.createQuery("FROM InventoryCaregory WHERE status=:st");
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
