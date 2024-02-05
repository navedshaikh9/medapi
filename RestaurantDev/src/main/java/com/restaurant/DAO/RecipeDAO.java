package com.restaurant.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.Recipe;
import com.restaurant.Entity.SaleItem;
import AllNotivications.AllNotifications;
import SettingsManagment.AllSettings;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RecipeDAO {

	

	public static boolean addRecipe(String recipeName, String rawMaterialName, String unit, double qty,
			boolean active) {
		boolean isSave = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();

			Recipe recipe = new Recipe();
			recipe.setRecipeName(recipeName);
			recipe.setRawMaterial(rawMaterialName);
			recipe.setUnit(unit);
			recipe.setQty(qty);
			recipe.setStatus(active);

			Transaction tx = session.beginTransaction();

			Integer us = (Integer) session.save(recipe);
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

	public static boolean updateRecipe(int id, String recipeName, String rawMaterialName, String unit, double qty,
			boolean active) {
		boolean isUpdate = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Recipe recipe = session.get(Recipe.class, id);

			if (recipe != null) {
				recipe.setRecipeName(recipeName);
				recipe.setRawMaterial(rawMaterialName);
				recipe.setUnit(unit);
				recipe.setQty(qty);
				recipe.setStatus(active);

				Transaction tx = session.beginTransaction();
				session.update(recipe);
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

	public static boolean deleteRecipe(int id) {

		Session session = null;
		Transaction transaction = null;
		boolean isDeleted = false;
		try {

			session = FactoryProvider.getFactory().openSession();
			transaction = session.beginTransaction();
			Recipe recipe = session.get(Recipe.class, id);

			if (recipe != null) {
				// If the entity exists, delete it
				session.delete(recipe);
				transaction.commit();
				isDeleted = true;
			} else {
				AllNotifications.error("Recipe", "Record Not Found", "");
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

	public static ObservableList<Recipe> getAllRecipe() {
		Session session = null;
		ObservableList<Recipe> depList = FXCollections.observableArrayList();
		try {
			session = FactoryProvider.getFactory().openSession();

			Query nq = session.createQuery("from Recipe");
			List<Recipe> list = nq.list();

			for (Recipe recipe : list) {

				depList.add(recipe);

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

	public static ObservableList<Recipe> getItem(String itemName) {
		Session session = null;
		ObservableList<Recipe> depList = FXCollections.observableArrayList();
		try {

			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Recipe where recipeName=:name");
			nq.setParameter("name", itemName);
			List<Recipe> list = nq.list();

			for (Recipe recipe : list) {

				if (itemName.equals(recipe.getRecipeName())) {

					depList.add(recipe);
				}

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

	public static void minimizStock(int saleId) {
		ObservableList<SaleItem> list = SaleItemDAO.getSaleItem(saleId);

		for (SaleItem item : list) {

			ObservableList<Recipe> recipeList = getItem(item.getItemName());

			for (Recipe recipeItem : recipeList) {
				double qty = InventoryDAO.getSubQty(recipeItem.getRawMaterial());

				double totalQty = item.getQty() * recipeItem.getQty();

				double subUnit = qty - totalQty;

				double kilograms = subUnit / 1000.0;

				InventoryDAO.stockUpdate(recipeItem.getRawMaterial(), subUnit, kilograms);
			}

		}
		
		if(AllSettings.getStockNotificationSettign()==true)
		{
		InventoryDAO.showStockQty();
		}
	}

}
