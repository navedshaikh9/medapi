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
import com.restaurant.Entity.SubCategory;
import AllNotivications.AllNotifications;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

public class SubCategoryDAO {

	

	// Add new SubCategory
	public static boolean addSubCategory(String subcategory, boolean active) {
		boolean isSave = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();

			SubCategory cate = new SubCategory();
			cate.setSubCategory(subcategory);
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

	// Get SubCategory List

	public static ObservableList<SubCategory> getAllSubCategory() {
		Session session = null;
		try {

			ObservableList<SubCategory> depList = FXCollections.observableArrayList();
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from SubCategory");
			List<SubCategory> list = nq.list();

			for (SubCategory subCategory : list) {

				depList.add(subCategory);

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

	// Update SubCategory
	public boolean updateSubCategory(int id, String subcategory, boolean active) {
		boolean isUpdate = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			SubCategory cate = session.get(SubCategory.class, id);

			if (cate != null) {
				cate.setSubCategory(subcategory);
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

	// Delete SubCategory
	public static boolean deleteSubCategory(int id) {

		Session session = null;
		Transaction transaction = null;
		boolean isDeleted = false;
		try {

			session = FactoryProvider.getFactory().openSession();
			transaction = session.beginTransaction();
			SubCategory cate = session.get(SubCategory.class, id);

			if (cate != null) {
				// If the entity exists, delete it
				session.delete(cate);
				transaction.commit();
				isDeleted = true;
			} else {
				AllNotifications.error("SubCategory", "Record Not Found", "");
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

	// Load Sub Cateogry in Choicebox
	public static List<SubCategory> getActiveTrueSubCategory() {
		Session session = null;
		List<SubCategory> list = new ArrayList<SubCategory>();
		try {

			session = FactoryProvider.getFactory().openSession();
			SubCategory cat = new SubCategory();
			Query q = session.createQuery("from SubCategory where status=:act");
			q.setParameter("act", true);
			list = q.list();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;

	}

	public static List<SubCategory> getSubCategory(String category) {
		Session session = null;
		List<SubCategory> list = new ArrayList<SubCategory>();
		try {

			session = FactoryProvider.getFactory().openSession();
			SubCategory cat = new SubCategory();
			Query q = session.createQuery("from SubCategory where category=:category and status=:act");
			q.setParameter("category", category);
			q.setParameter("act", true);
			list = q.list();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;

	}

	public void importSubCategory() {

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
						String subcategory = itemCell.getStringCellValue().trim();
						String a = new DataFormatter().formatCellValue(row.getCell(2));
						boolean active = Boolean.parseBoolean(a);

						addSubCategory(subcategory, active);

					}

				}
				workbook.close();
				file.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			AllNotifications.error("Table Import", "Please Select File", "");
		}

	}

}
