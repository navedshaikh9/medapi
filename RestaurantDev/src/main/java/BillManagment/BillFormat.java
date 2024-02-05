package BillManagment;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import com.restaurant.DAO.AdditionalChargesDAO;
import com.restaurant.DAO.BillFormateDAO;
import com.restaurant.DAO.PaymentMethodDAO;
import com.restaurant.DAO.SaleDAO;
import com.restaurant.DAO.SaleItemDAO;
import com.restaurant.DAO.SettingDAO;
import com.restaurant.DAO.TaxManagmentDAO;
import com.restaurant.Entity.AdditionalCharges;
import com.restaurant.Entity.BillFormate;
import com.restaurant.Entity.KOTItem;
import com.restaurant.Entity.Sale;
import com.restaurant.Entity.SaleItem;
import com.restaurant.Entity.Tax;
import AllNotivications.AllNotifications;
import SettingsManagment.AllSettings;
import javafx.collections.ObservableList;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.*;

public class BillFormat {

	AllNotifications allNotif = new AllNotifications();

	private static final String line = "---------------------------------------------------------";

	private static String restaurantName = "MedResto";
	private static String restaurantType = "Software Company";
	private static String restaurantAddress = "Hadapsar, Pune";
	private static String restaurantContact= "+91-9579999850";
	private static String endingMessage = "Thank You Visit Again!";

	
	public static void billView(int id) {
		SettingDAO settingDAO = new SettingDAO();
		BillFormate bf = BillFormateDAO.getHotelDetails();
		Sale sale = SaleDAO.openBill(id);

		if(bf!=null)
		{
			restaurantName = bf.getHotelName();
			restaurantType = bf.getHotelType();
			restaurantAddress =bf.getAddress();
			restaurantContact = bf.getContact();
			endingMessage =  bf.getMessage();
		}
		
		double finalGrandTotal = 0;
		
			if (sale != null) {
				JFrame frame = new JFrame();
				Container cp = frame.getContentPane();
				JTextPane billPane = new JTextPane();
				billPane.setContentType("text/html");

				finalGrandTotal = sale.getGrandTotal();
				double tax_percent = sale.getTaxPercent();
				double disc_amt = sale.getDiscountAmt();
				double taxAmt = TaxManagmentDAO.getTaxTotal(sale.getTotalAmt());
				double applyTax = TaxManagmentDAO.applyTaxOnTotal(sale.getTotalAmt());
				double finelTotal = applyTax - disc_amt;
				
				double sgstPercentage = tax_percent / 2;
				double cgstPercentage = tax_percent / 2;
				double sgstAmt = finelTotal * sgstPercentage / 100;
				double cgstAmt = finelTotal * cgstPercentage / 100;
				DecimalFormat decimalFormat = new DecimalFormat("0.0");

				// Format the SGST and CGST amounts
				String sgstAmount = decimalFormat.format(sgstAmt);
				String cgstAmount = decimalFormat.format(cgstAmt);
				String taxAmount = decimalFormat.format(taxAmt);

				String billText = String.format("<html><body style='font-family: 'Vendara', sans-serif;'><center>"
						+ "<p style='font-size:12px;  font-weight: bold;'>%s</p>" + "%s<br>" + "%s<br>" + "%s<br>"
						+ "%s<br>" + "%s<br>" + "<table>"
						+ "<tr'><td style='font-weight: bold; font-size:12px;'>Bill No.: %s</td> <td>%s</td> <td>%s</td> </tr>"
						+ "</table>" + "%s<br>" + "<table>"
						+ "<tr ><td style='font-weight: bold;'>Item Name</td><td style='font-weight: bold;'>Price</td><td style='font-weight: bold;'>Qty</td><td style='font-weight: bold;'>Total</td></tr>",
						restaurantName, restaurantType, restaurantAddress, restaurantContact,
						sale.getCreated() + "(" + sale.getTime() + ")", line, sale.getSaleId(), sale.getSaleOrder(),
						sale.getOrderType(), line);

				ObservableList<SaleItem> list = SaleItemDAO.getSaleItem(id);

				for (SaleItem sale_item : list) {
					billText += String.format(
							"<tr><td>%s</td><td style='text-align:center'>%s</td><td style='text-align:center'>%s</td><td style='text-align:center'>%s</td></tr>",
							sale_item.getItemName(), "" + sale_item.getPrice(), "" + sale_item.getQty(),
							"" + sale_item.getItemTotal());

				}

				billText += String.format("</table>" + "%s " + "<table style='width:200px;'>"
						+ "<tr> <td style='text-align:right; font-weight: bold;'>Total Amt: %s</td></tr>" + "</table>",
						line, sale.getTotalAmt());

					if (AllSettings.getOfferSettign()==true) {
						// Add offer

						billText += String.format("<table style='width:200px;'>"
								+ "<tr> <td style='text-align:right;font-weight: bold;'>%s: %s</td></tr>" + "</table>",
								"Discount", disc_amt);

					}
		
			
					if (AllSettings.getTaxSettign() == true) {
						// Get and Apply tax

						String taxType = "";
						Tax t = TaxManagmentDAO.getTax();
						if (t != null) {
							taxType = t.getTaxName();

						}
						// Add tax
						billText += String.format(
								"%s<table style='width:200px;'>"
										+ "<tr> <td style='text-align:right; font-weight: bold;'>%s</td> </tr>"
										+ "<tr> <td style='text-align:right;'>%s</td> </tr>"
										+ "<tr> <td style='text-align:right;'>%s</td> </tr>" + "</table>",
								line, taxType + ": (" + tax_percent + "%) " + taxAmount,
								"SGST: (" + sgstPercentage + "%) " + sgstAmount,
								"CGST: (" + cgstPercentage + "%) " + cgstAmount);

					}
			

				double additotal = 0;
					if (AllSettings.getAdditionalChargesSettign() == true) {
						// Add Addistion Charges

						billText += String.format("%s <table style='width:200px;'>", line);

						ObservableList<AdditionalCharges> aclis = AdditionalChargesDAO.getAllCharges();

						for (AdditionalCharges ac : aclis) {
							if (ac.isStatus() == true) {
								billText += String.format(
										"<tr> <td style='text-align:right;'>%s: %s</td></tr>",
										ac.getChargesName(), ac.getCharges());
								additotal = additotal + ac.getCharges();
							}
						}

						billText += String.format("</table>");
					}


				double grandTotal = finalGrandTotal + additotal;
				billText += String.format("%s <table style='width:200px;'>"
						+ "<tr> <td style='text-align:left; font-weight: bold;'>%s</td> <td style='text-align:right; font-weight: bold;'>Grand Total: %s</td></tr></table>"
						+ "<br>%s<br></center><br></body></html>", line, sale.getPayMethod(), grandTotal,
						endingMessage);

				billPane.setText(billText);

				// Add JTextPane to JFrame
				frame.getContentPane().add(billPane);

				JScrollPane scrollPane = new JScrollPane(billPane);
				cp.add(scrollPane, BorderLayout.CENTER);
				// Set JFrame properties
				billPane.setEditable(false);
				frame.setTitle("Restaurant Bill");
				frame.setSize(310, 600);
				// frame.setLocationRelativeTo(null);
				frame.setVisible(true);
					if (AllSettings.getPrintBillSettign() == true) {
						
						
						billPrint(billPane,grandTotal);
				}

			} else {
				AllNotifications.showErrorNotification("Bill", "Please Select Bill");
			}
		
	}
	
	

	public static void kotPrint(int kot_id, String kot_order, String order_type, ObservableList<KOTItem> list,
			String time) {
		
		if (kot_id != 0) {
			JFrame frame = new JFrame();
			Container cp = frame.getContentPane();
			JTextPane billPane = new JTextPane();
			billPane.setContentType("text/html");

			String billText = String.format("<html><body style='font-family: 'Vendara', sans-serif;'><center>"
					+ "<p style='font-size:12px;  font-weight: bold;'>KOT No. %s</p>" + "%s<br>" + "%s<br>"
					+ "<table style='width:200px;'>" + //
					"<tr><center><td><b> %s</b></td> <td>%s</td></center></tr>" + "</table style='width:200px;'>" + "%s"
					+ "<table style='width:180px;'>" + //
					"<tr><td style='font-weight: bold;'>Item Name</td><td style='font-weight: bold;'>Qty</td></tr>",
					kot_id, time, line, kot_order, order_type, line);

			for (KOTItem item : list) {
				billText += String.format("<tr><td>%s</td><td style='text-align:center' >%s</td></tr>",
						item.getItemName(), item.getQty());
				// style='height:5px;'
			}

			billText += String.format("</table> %s<br></center><br></body></html>", line);

			billPane.setText(billText);

			// Add JTextPane to JFrame
			frame.getContentPane().add(billPane);

			JScrollPane scrollPane = new JScrollPane(billPane);
			cp.add(scrollPane, BorderLayout.CENTER);
			// Set JFrame properties
			billPane.setEditable(false);
			frame.setResizable(false);
			frame.setTitle("Kitchen Order");
			frame.setSize(310, 600);
			frame.setVisible(true);

			if (AllSettings.getPrintKOTSettign() == true) {
				kotPrint(billPane);
			}

		} else {
			AllNotifications.showErrorNotification("KOT", "Please Select KOT");
		}
	}

	public static void kotPrint(JTextPane textPane) {
		try {
			SimpleAttributeSet attributes = new SimpleAttributeSet();
			StyleConstants.setFontSize(attributes, 14); // Adjust the font size as needed
			textPane.getStyledDocument().setCharacterAttributes(0, textPane.getDocument().getLength(), attributes,
					true);
			PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();

			if (defaultPrintService != null) {
				PrinterJob printerJob = PrinterJob.getPrinterJob();
				printerJob.setPrintService(defaultPrintService);

				// Create a PageFormat with the paper size and orientation
				PageFormat pageFormat = new PageFormat();
				Paper paper = new Paper();
				double paperWidth = 58.0; // Width in millimeters for a typical 58mm thermal printer
				double paperHeight = Double.POSITIVE_INFINITY; // Set a large height to fit content
				paper.setSize(paperWidth * 72.0 / 25.4, paperHeight * 72.0 / 25.4); // Convert to points (1 inch = 72
																					// points)
				paper.setImageableArea(0.0, 0.0, paperWidth * 72.0 / 25.4, paperHeight * 72.0 / 25.4);
				pageFormat.setPaper(paper);

				// Set other print attributes
				HashPrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();

				// Set the printable content and PageFormat to the PrinterJob
				printerJob.setPrintable(new Printable() {
					@Override
					public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
						if (pageIndex > 0) {
							return Printable.NO_SUCH_PAGE;
						}

						Graphics2D g2d = (Graphics2D) graphics;

						// Calculate the scaling factor to fit the content within the paper width
						double scaleX = pageFormat.getImageableWidth() / textPane.getPreferredSize().getWidth();

						// Apply the scaling transformation
						g2d.scale(scaleX, 0.8); // Only scale horizontally

						// Draw the JTextPane content to the Graphics object
						textPane.print(g2d);

						return Printable.PAGE_EXISTS;
					}
				}, pageFormat);

				// Initiate the print job
				printerJob.print();
			} else {
				AllNotifications.error("Printer", "No Default Printer", "");
			}
		} catch (PrinterException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void billPrint(JTextPane textPane, double amount) {
		
	    

	    try {
	        SimpleAttributeSet attributes = new SimpleAttributeSet();
	        StyleConstants.setFontSize(attributes, 14); // Adjust the font size as needed
	        textPane.getStyledDocument().setCharacterAttributes(0, textPane.getDocument().getLength(), attributes, true);
	        PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();

	        if (defaultPrintService != null) {
	            PrinterJob printerJob = PrinterJob.getPrinterJob();
	            printerJob.setPrintService(defaultPrintService);

	            // Create a PageFormat with the paper size and orientation
	            PageFormat pageFormat = new PageFormat();
	            Paper paper = new Paper();
	            double paperWidth = 58.0; // Width in millimeters for a typical 58mm thermal printer
	            double paperHeight = Double.POSITIVE_INFINITY; // Set a large height to fit content
	            paper.setSize(paperWidth * 72.0 / 25.4, paperHeight * 72.0 / 25.4); // Convert to points (1 inch = 72 points)
	            paper.setImageableArea(0.0, 0.0, paperWidth * 72.0 / 25.4, paperHeight * 72.0 / 25.4);
	            pageFormat.setPaper(paper);

	            // Set other print attributes
	            HashPrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();

	            	String uri = "upi://pay?pa=" + PaymentMethodDAO.getUPIId() + "&am=" + amount;
	   	            BufferedImage qrImage = generateQRCode(uri);
	           
	            
	            // Generate QR Code image
	         

	            // Set the printable content and PageFormat to the PrinterJob
	            printerJob.setPrintable(new Printable() {
	                @Override
	                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
	                    if (pageIndex == 0) {
	                        Graphics2D g2d = (Graphics2D) graphics;

	                        // Calculate the scaling factor to fit the content within the paper width
	                        double scaleX = pageFormat.getImageableWidth() / textPane.getPreferredSize().getWidth();

	                        // Apply the scaling transformation
	                        g2d.scale(scaleX, 0.8); // Only scale horizontally

	                        // Calculate the preferred size of the JTextPane
	                        Dimension preferredSize = textPane.getPreferredSize();
	                       
	                        // Ensure the JTextPane fits within the printable area
	                        while (textPane.getPreferredSize().getWidth() * scaleX > pageFormat.getImageableWidth()) {
	                            // Reduce the font size or apply a different scaling mechanism if necessary
	                            int fontSize = StyleConstants.getFontSize(attributes);
	                            StyleConstants.setFontSize(attributes, fontSize - 1); // Adjust the font size

	                            // Update the JTextPane attributes
	                            textPane.getStyledDocument().setCharacterAttributes(0, textPane.getDocument().getLength(), attributes, true);

	                            // Recalculate the preferred size and scaling factor
	                            preferredSize = textPane.getPreferredSize();
	                            scaleX = pageFormat.getImageableWidth() / preferredSize.getWidth();
	                         }

	                        // Draw the JTextPane content to the Graphics object
	                        textPane.print(g2d);

	                       
	                        int textPaneHeight = (int) textPane.getPreferredSize().getHeight();

	                     // Add space after printing textPane content
	                     int spaceAfterText = 1; // You can adjust this value as needed
	                     int qrCodeYPosition = textPaneHeight + spaceAfterText;

	                     if(PaymentMethodDAO.getUPIId()!=null && AllSettings.getQRCodeSettign()==true)
	                     {
	                     // Print QR Code image after the textPane content with space
	                     g2d.drawImage(qrImage, 0, qrCodeYPosition, null);
	                     }    
	                        return Printable.PAGE_EXISTS;
	                    } else {
	                        return Printable.NO_SUCH_PAGE;
	                    }
	                }
	            }, pageFormat);

	        
	                printerJob.print(); // If user confirms, start the print job
	            
	        } else {
	            AllNotifications.error("Printer", "No Default Printer", "");
	        }
	    } catch (PrinterException e) {
	        e.printStackTrace();
	    }
	}
	
	 
	 private static BufferedImage generateQRCode(String qrText) {
		    int size = 150;

		    Map<EncodeHintType, Object> hints = new HashMap<>();
		    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		    QRCodeWriter qrCodeWriter = new QRCodeWriter();

		    try {
		        BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, size, size, hints);
		        int width = bitMatrix.getWidth();
		        BufferedImage qrImage = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);

		        for (int x = 0; x < width; x++) {
		            for (int y = 0; y < width; y++) {
		                qrImage.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
		            }
		        }
		        return qrImage;
		    } catch (WriterException e) {
		        e.printStackTrace();
		    }
		    return null;
		}

	    
	 
	 
}
