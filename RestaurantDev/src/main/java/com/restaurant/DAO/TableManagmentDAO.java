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
import com.restaurant.Entity.Category;
import com.restaurant.Entity.HotelTable;
import AllNotivications.AllNotifications;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

public class TableManagmentDAO {

	

	// Add new table
	public static boolean addTable(String name, boolean active) {
		boolean isSave = false;
		Session session = null;
		try {
			session = FactoryProvider.getFactory().openSession();

			HotelTable tm = new HotelTable();
			tm.setTableName(name);
			tm.setStatus(active);

			Transaction tx = session.beginTransaction();

			Integer us = (Integer) session.save(tm);
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

	// Get Table List

	public static ObservableList<HotelTable> getAllTable() {
		Session session = null;
		try {

			ObservableList<HotelTable> depList = FXCollections.observableArrayList();
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from HotelTable");
			List<HotelTable> list = nq.list();

			for (HotelTable hotelTable : list) {

				depList.add(hotelTable);

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

	// load all Table

	public static List<HotelTable> loadAllTable() {
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from HotelTable");
			List<HotelTable> list = nq.list();

			return list;

		} catch (Exception e) {
			e.printStackTrace();
			return FXCollections.observableArrayList();
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	// Update Table
	public boolean updateTable(int id, String name, boolean active) {
		boolean isUpdate = false;
		Session session = null;
		try {

			session = FactoryProvider.getFactory().openSession();
			HotelTable hotelTable = session.get(HotelTable.class, id);

			if (hotelTable != null) {
				hotelTable.setTableName(name);
				hotelTable.setStatus(active);

				Transaction tx = session.beginTransaction();
				session.update(hotelTable);
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

	// Delete Table
	public static boolean deleteTable(int id) {

		Session session = null;
		Transaction transaction = null;
		boolean isDeleted = false;
		try {

			session = FactoryProvider.getFactory().openSession();
			transaction = session.beginTransaction();
			HotelTable hotelTable = session.get(HotelTable.class, id);

			if (hotelTable != null) {
				// If the entity exists, delete it
				session.delete(hotelTable);
				transaction.commit();
				isDeleted = true;
			} else {
				AllNotifications.error("Table", "Record Not Found", "");
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
	public static ObservableList<HotelTable> getActiveTable() {
		Session session = null;
		ObservableList<HotelTable> depList = FXCollections.observableArrayList();

		try {

			Session s = FactoryProvider.getFactory().openSession();
			Category cat = new Category();
			Query q = s.createQuery("from HotelTable");
			List<HotelTable> list = q.list();

			for (HotelTable hotelTable : list) {

				if (hotelTable.isStatus() == true) {

					depList.add(hotelTable);

				}
			}

			s.close();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return depList;

	}

	public void importTable() {

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
						String table_name = itemCell.getStringCellValue().trim();
						String a = new DataFormatter().formatCellValue(row.getCell(2));
						boolean active = Boolean.parseBoolean(a);
						addTable(table_name, active);
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
