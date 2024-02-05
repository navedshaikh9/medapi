package com.restaurant.DAO;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
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
import com.restaurant.Entity.Category;

import AllNotivications.AllNotifications;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

public class CategoryDAO {

	

	// Add new Category
	public static boolean addCategory(String category, boolean st) {
		boolean status = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();

			Category cate = new Category();
			cate.setCategory(category);
			cate.setStatus(st);

			Transaction tx = session.beginTransaction();

			Integer us = (Integer) session.save(cate);
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

	// Get Category List

	public static ObservableList<Category> getAllCategory() {

		ObservableList<Category> depList = FXCollections.observableArrayList();

		try {

			Session s = FactoryProvider.getFactory().openSession();
			Query nq = s.createQuery("from Category");
			List<Category> list = nq.list();

			for (Category category : list) {

				depList.add(category);

			}

			return depList;

		} catch (Exception e) {
			e.printStackTrace();
			return FXCollections.observableArrayList();
		}

	}

	// Update Category
	public boolean updateCategory(int id, String category, boolean activ) {
		boolean status = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Category cate = session.get(Category.class, id);

			if (cate != null) {
				cate.setCategory(category);
				cate.setStatus(activ);

				Transaction tx = session.beginTransaction();
				session.update(cate);
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

	// Delete Category
	public static boolean deleteCategory(int id) {
		Session session = null;
		Transaction transaction = null;
		boolean isDeleted = false;

		try {

			session = FactoryProvider.getFactory().openSession();
			transaction = session.beginTransaction();
			Category cate = session.get(Category.class, id);
			if (cate != null) {
				// If the entity exists, delete it
				session.delete(cate);
				transaction.commit();
				isDeleted = true;
			} else {
				AllNotifications.error("Category", "Record Not Found", "");
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
	public static List<Category> getActiveTrueCategory() {
		Session session = null;
		List<Category> list = new ArrayList<Category>();

		try {
			session = FactoryProvider.getFactory().openSession();

			Query query = session.createQuery("from Category where status = :isActive");
			query.setParameter("isActive", true);

			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return list;
	}

	public void importCategory() {
		Session session = null;
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File("D:\\"));
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Files", "*.xlsx");
		fc.getExtensionFilters().add(extFilter);
		File selectedFile = fc.showOpenDialog(null);
		if (selectedFile != null) {
			session = FactoryProvider.getFactory().openSession();

			try {
				FileInputStream file = new FileInputStream(selectedFile);
				Workbook workbook = new XSSFWorkbook(file);
				Sheet sheet = workbook.getSheetAt(0);
				int lastRowNumber = sheet.getLastRowNum();
				for (int i = 1; i <= lastRowNumber; i++) {
					Row row = sheet.getRow(i);

					if (row != null) {
						Cell itemCell = row.getCell(1);
						String category = itemCell.getStringCellValue().trim();
						String a = new DataFormatter().formatCellValue(row.getCell(2));
						boolean active = Boolean.parseBoolean(a);
						addCategory(category, active);

					}

				}
				workbook.close();
				file.close();
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
			// Close the SessionFactory after you're done.

		} else {
			AllNotifications.error("Category Import", "Please Select File", "");
		}

	}

}
