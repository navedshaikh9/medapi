package com.restaurant.DAO;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.restaurant.Entity.Sale;
import com.restaurant.Entity.SaleItem;

import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SaleReportDAO {

	public static ObservableList<Sale> getSaleAll() {
		Session session = null;
		try {

			ObservableList<Sale> depList = FXCollections.observableArrayList();
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Sale");
			List<Sale> list = nq.list();

			for (Sale sale : list) {

				depList.add(sale);

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

	public static ObservableList<Sale> getSaleAll(LocalDate start, LocalDate end) {
		Session session = null;
		try {

			ObservableList<Sale> depList = FXCollections.observableArrayList();
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Sale WHERE created BETWEEN :startDate AND :endDate");
			nq.setParameter("startDate", start);
			nq.setParameter("endDate", end);
			List<Sale> list = nq.list();

			for (Sale sale : list) {

				depList.add(sale);

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

	public static ObservableList<Sale> getSaleByOrder(String order) {
		Session session = null;
		try {

			ObservableList<Sale> depList = FXCollections.observableArrayList();
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Sale where saleOrder=:order");
			nq.setParameter("order", order);

			List<Sale> list = nq.list();

			for (Sale sale : list) {
				if (order.equals(sale.getSaleOrder())) {

					depList.add(sale);
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

	public static ObservableList<Sale> getSaleByOrder(String order, LocalDate start, LocalDate end) {
		Session session = null;
		try {

			ObservableList<Sale> depList = FXCollections.observableArrayList();
			session = FactoryProvider.getFactory().openSession();
			Query nq = session
					.createQuery("from Sale where saleOrder=:order and created BETWEEN :startDate AND :endDate");
			nq.setParameter("order", order);
			nq.setParameter("startDate", start);
			nq.setParameter("endDate", end);
			List<Sale> list = nq.list();

			for (Sale sale : list) {
				if (order.equals(sale.getSaleOrder())) {

					depList.add(sale);
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

	public static ObservableList<Sale> getTaxReport() {
		Session session = null;
		try {

			ObservableList<Sale> depList = FXCollections.observableArrayList();
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Sale");
			List<Sale> list = nq.list();
			for (Sale sale : list) {
				if (0 != sale.getTaxPercent()) {

					depList.add(sale);
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

	public static ObservableList<Sale> getTaxReport(LocalDate start, LocalDate end) {
		Session session = null;
		try {

			ObservableList<Sale> depList = FXCollections.observableArrayList();
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Sale WHERE created BETWEEN :startDate AND :endDate");
			nq.setParameter("startDate", start);
			nq.setParameter("endDate", end);
			List<Sale> list = nq.list();
			for (Sale sale : list) {
				if (0 != sale.getTaxPercent()) {

					depList.add(sale);
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

	public static ObservableList<Sale> getDiscountReport() {
		Session session = null;
		try {

			ObservableList<Sale> depList = FXCollections.observableArrayList();
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Sale");
			List<Sale> list = nq.list();
			for (Sale sale : list) {
				if (0 != sale.getDiscountAmt()) {

					depList.add(sale);
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

	public static ObservableList<Sale> getDiscountReport(LocalDate start, LocalDate end) {
		Session session = null;
		try {

			ObservableList<Sale> depList = FXCollections.observableArrayList();
			session = FactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from Sale WHERE created BETWEEN :startDate AND :endDate");
			nq.setParameter("startDate", start);
			nq.setParameter("endDate", end);
			List<Sale> list = nq.list();
			for (Sale sale : list) {
				if (0 != sale.getDiscountAmt()) {

					depList.add(sale);
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
	
	
	 public static void getSaleItemReport() {
		 List<SaleItem> list= SaleItemDAO.getAllSaleItem();
	        StringBuilder htmlBuilder = new StringBuilder();

	        // HTML report template
	        htmlBuilder.append("<!DOCTYPE html>");
	        htmlBuilder.append("<html>");
	        htmlBuilder.append("<head>");
	        htmlBuilder.append("<title>Sales Report</title>");
	        htmlBuilder.append("<style>");
	        htmlBuilder.append("/* Your CSS styles here */");
	        htmlBuilder.append("</style>");
	        htmlBuilder.append("</head>");
	        htmlBuilder.append("<body>");
	        htmlBuilder.append("<h1>Sales Report</h1>");
	        htmlBuilder.append("<table>");
	        htmlBuilder.append("<thead>");
	        htmlBuilder.append("<tr>");
	        htmlBuilder.append("<th>Sale Item Id</th>");
	        htmlBuilder.append("<th>Sale Id</th>");
	        htmlBuilder.append("<th>Item Name</th>");
	        htmlBuilder.append("<th>Price</th>");
	        htmlBuilder.append("<th>Qty</th>");
	        htmlBuilder.append("<th>Total</th>");
	        htmlBuilder.append("<th>Status</th>");
	        htmlBuilder.append("</tr>");
	        htmlBuilder.append("</thead>");
	        htmlBuilder.append("<tbody>");

	        // Populate table rows with sales data
	        for (SaleItem sale : list) {
	            htmlBuilder.append("<tr>");
	            htmlBuilder.append("<td>").append(sale.getSaleItemId()).append("</td>");
	            htmlBuilder.append("<td>").append(sale.getSaleId()).append("</td>");
	            htmlBuilder.append("<td>").append(sale.getItemName()).append("</td>");
	            htmlBuilder.append("<td>").append(sale.getPrice()).append("</td>");
	            htmlBuilder.append("<td>").append(sale.getQty()).append("</td>");
	            htmlBuilder.append("<td>").append(sale.getItemTotal()).append("</td>");
	            htmlBuilder.append("</tr>");
	        }

	        htmlBuilder.append("</tbody>");
	        htmlBuilder.append("</table>");
	        htmlBuilder.append("</body>");
	        htmlBuilder.append("</html>");
	        
	       // Generate PDF from HTML content
	        String filePath = "C:\\Users\\A\\Downloads\\sales_report.pdf";
	        generatePDF(htmlBuilder.toString(), filePath);

	    }
	 
	 public static void generatePDF(String htmlContent, String outputFile) {
	        try {
	            FileOutputStream os = new FileOutputStream(outputFile);
	            ITextRenderer renderer = new ITextRenderer();
	            renderer.setDocumentFromString(htmlContent);
	            renderer.layout();
	            renderer.createPDF(os);
	            os.close();

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
