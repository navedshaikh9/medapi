package com.restaurant.DAO;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.MenuItem;
import AllNotivications.AllNotifications;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;


public class MenuItemDAO {

	AllNotifications allNoti = new AllNotifications();

	

	// Add new Menu Item
	public static boolean addMenuItem(String itemname, String item_code, String category, String subcategory,
			double item_price, boolean active) {
		boolean isSave = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();

			MenuItem menu = new MenuItem();
			menu.setItemName(itemname);
			menu.setItemCode(item_code);
			menu.setCategory(category);
			menu.setSubCategory(subcategory);
			menu.setItemPrice(item_price);
			menu.setStatus(active);
			Transaction tx = session.beginTransaction();

			Integer us = (Integer) session.save(menu);
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

	// Get Menu List List

	public static ObservableList<MenuItem> getAllMenuItem() {
		try {

			ObservableList<MenuItem> depList = FXCollections.observableArrayList();
			Session s = FactoryProvider.getFactory().openSession();
			Query nq = s.createQuery("from MenuItem");
			List<MenuItem> list = nq.list();

			for (MenuItem menuItem : list) {

				depList.add(menuItem);

			}

			return depList;

		} catch (Exception e) {
			e.printStackTrace();
			return FXCollections.observableArrayList();
		}

	}

	// Get LoadMenu List

	public static List<MenuItem> loadAllMenu() {
		try {

			Session s = FactoryProvider.getFactory().openSession();
			Query nq = s.createQuery("from MenuItem");
			List<MenuItem> list = nq.list();

			return list;

		} catch (Exception e) {
			e.printStackTrace();
			return FXCollections.observableArrayList();
		}

	}

	// Get Menu By MenuId

	public static MenuItem getMenu(int menuid) {
		Session session = null;
		MenuItem menu = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from MenuItem where menuId=:id");
			nq.setParameter("id", menuid);
			menu = (MenuItem) nq.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return menu;
	}

	// Update Menu Item
	public boolean updateMenuItem(String itemname, String item_code, String category, String subCategory,
			double item_price, boolean active, int menuId) {
		boolean isUpdate = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			MenuItem menu = session.get(MenuItem.class, menuId);
			if (menu != null) {
				menu.setItemName(itemname);
				menu.setItemCode(item_code);
				menu.setCategory(category);
				menu.setSubCategory(subCategory);
				menu.setItemPrice(item_price);
				menu.setStatus(active);

				Transaction tx = session.beginTransaction();
				session.update(menu);
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

	// Import All Menu Using Excel file
	public void importAllMenu() {

		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File("D:\\"));
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Files", "*.xlsx");
		fc.getExtensionFilters().add(extFilter);
		File selectedFile = fc.showOpenDialog(null);
		if (selectedFile != null) {
			try {
				FileInputStream file = new FileInputStream(selectedFile);
				Workbook workbook = new XSSFWorkbook(file);
				Sheet sheet = workbook.getSheetAt(0);
				int lastRowNumber = sheet.getLastRowNum();
				for (int i = 1; i <= lastRowNumber; i++) {
					Row row = sheet.getRow(i);

					if (row != null) {
						Cell itemCell = row.getCell(1);
						String itemName = itemCell.getStringCellValue().trim();
	
//						Cell shortCodeCell = row.getCell(2).toString();
//						String shortCode = shortCodeCell.getStringCellValue().trim();
						
						String shortCode = String.valueOf(new DataFormatter().formatCellValue(row.getCell(2)).toString());

						Cell cateCell = row.getCell(3);
						String category = cateCell.getStringCellValue().trim();

						Cell subCell = row.getCell(4);
						String sub = subCell.getStringCellValue().trim();

						String pri = String.valueOf(new DataFormatter().formatCellValue(row.getCell(5)).toString());

						String a = new DataFormatter().formatCellValue(row.getCell(6));
						double price = Double.parseDouble(pri);
						boolean active = Boolean.parseBoolean(a);
						
						addMenuItem(itemName, shortCode, category, sub, price, active);

					}

				}

				workbook.close();
				file.close();
			} catch (Exception e) {
				
				e.printStackTrace();
			} 
			

		} else {
			AllNotifications.error("Menu Import", "Please Select File", "");
		}

	}

	// Delete MenuItem
	public static boolean deleteMenuItem(int id) {
		Session session = null;
		Transaction transaction = null;
		boolean isDeleted = false;
		try {

			session = FactoryProvider.getFactory().openSession();
			transaction = session.beginTransaction();
			MenuItem c = session.get(MenuItem.class, id);

			if (c != null) {
				// If the entity exists, delete it
				session.delete(c);
				transaction.commit();
				isDeleted = true;
			} else {
				AllNotifications.error("MenuItem", "Record Not Found", "");
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

// Search Menu Item by Name

	public static List<MenuItem> searchMenuByName(String itemName) {
		Session session = null;
		List<MenuItem> list=null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from MenuItem where itemName LIKE'%" + itemName + "%'");

			list = nq.list();

			

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return list;
	}

	public static List<MenuItem> searchCategory(String category) {
		Session session = null;
		List<MenuItem> list=null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from MenuItem where category=:cat");
			nq.setParameter("cat", category);
			list = nq.list();

			

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return list;
	}

	public static List<MenuItem> searchSubCategory(String subCategory) {
		Session session = null;
		List<MenuItem> list=null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from MenuItem where subCategory=:scat");
			nq.setParameter("scat", subCategory);
			 list = nq.list();

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return list;

	}

	public static List<MenuItem> searchCategory(String category, String subCategory) {
		Session session = null;
		List<MenuItem> list=null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from MenuItem where category=:cat and subCategory=:scat");
			nq.setParameter("cat", category);
			nq.setParameter("scat", subCategory);
		
			list = nq.list();

		
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return list;

	}
	
	


}
