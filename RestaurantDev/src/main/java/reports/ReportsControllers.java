package reports;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import org.xhtmlrenderer.pdf.ITextRenderer;
import com.restaurant.DAO.BillFormateDAO;
import com.restaurant.DAO.KOTDAO;
import com.restaurant.DAO.KOTItemDAO;
import com.restaurant.DAO.OrderTypeDAO;
import com.restaurant.DAO.PaymentMethodDAO;
import com.restaurant.DAO.SaleDAO;
import com.restaurant.DAO.SaleItemDAO;
import com.restaurant.DAO.TaxManagmentDAO;
import com.restaurant.Entity.BillFormate;
import com.restaurant.Entity.KOT;
import com.restaurant.Entity.KOTItem;
import com.restaurant.Entity.OrderType;
import com.restaurant.Entity.PaymentMethod;
import com.restaurant.Entity.Sale;
import com.restaurant.Entity.SaleItem;

import AllNotivications.AllNotifications;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReportsControllers implements Initializable{
	
	  @FXML
	    private ComboBox<String> chbOrderType;

	    @FXML
	    private ComboBox<String> chbPayment;

	    @FXML
	    private DatePicker dateFrom;

	    @FXML
	    private DatePicker dateTo;
	    
	    private static String restaurantName = "MedResto";
		private static String restaurantType = "Software Company";
		private static String restaurantAddress = "Hadapsar, Pune";
		private static String restaurantContact= "+91-9579999850";

		@FXML
	    void OnMouseClickedOrderType(MouseEvent event) {
			loadOrderType();
	    }

	    @FXML
	    void OnMouseClickedPayment(MouseEvent event) {
	    	loadPaymentMethod();
		    
	    }
	    
	    @FXML
	    void btnOnActionCancelItem(ActionEvent event) {
	    	cancelItem();
	    }

	    @FXML
	    void btnOnActionCancelOrder(ActionEvent event) {
	    	cancelKOT();
	    }

	    @FXML
	    void btnOnActionClear(ActionEvent event) {
	    	dateFrom.setValue(null);
	    	dateTo.setValue(null);
	    }

	    @FXML
	    void btnOnActionDelivery(ActionEvent event) {
	    	delivery();
	    }

	    @FXML
	    void btnOnActionDineIn(ActionEvent event) {
	    	dineIn();
	    }

	    @FXML
	    void btnOnActionDiscount(ActionEvent event) {
	    	discount();
	    }

	    @FXML
	    void btnOnActionHotSaleItem(ActionEvent event) {
	    	hotSaleItem();
	    }

	    @FXML
	    void btnOnActionOnOrderDelivery(ActionEvent event) {
	    	deliveryOderType();
	    }

	    @FXML
	    void btnOnActionOnPayment(ActionEvent event) {
	    	allSaleayment();
	    }

	    @FXML
	    void btnOnActionTakeaway(ActionEvent event) {
	    	takeaway();
	    }

	    @FXML
	    void btnOnActionTax(ActionEvent event) {
	    	tax();
	    }

	    @FXML
	    void btnOnActionTotalSale(ActionEvent event) {
	    	allSale();
	    }

	    @FXML
	    void onKeyPressed(KeyEvent event) {

	    }
	    
	    @Override
		public void initialize(URL location, ResourceBundle resources) {
			// TODO Auto-generated method stub
	    	loadPaymentMethod();
	    	loadOrderType();
		}
	    
	    public void loadPaymentMethod() {

			chbPayment.getItems().clear();

			List<PaymentMethod> list = PaymentMethodDAO.getAllPaymentMethod();
			ObservableList data = FXCollections.observableArrayList();

			for (PaymentMethod d : list) {

				data.add(d.getPaymentMethod());
			}

			chbPayment.setItems(data);

		}
	    
	    
	    public void loadOrderType() {

			chbOrderType.getItems().clear();

			List<OrderType> list = OrderTypeDAO.getAllOrderType();
			ObservableList data = FXCollections.observableArrayList();

			for (OrderType d : list) {

				data.add(d.getOrderType());
			}

			chbOrderType.setItems(data);

		}

	    
	    public void dineIn()
	    {
	    	LocalDate from = dateFrom.getValue();
	    	LocalDate to = dateTo.getValue();
	    	
	    	List<Sale> list = SaleDAO.getAllSale();
	    	
	    	List<Sale> salesWithinDateRange = null;
	    	
	    	if(list!=null)
	    	{
	    		
	    			salesWithinDateRange = new ArrayList<>();
	    			
	    			for(Sale sale:list)
	    			{
	    				if(sale.getSaleOrder().equals("Dine-In"))
	    				{
	    					LocalDate saleDate = sale.getCreated();
	    					 if ((from != null && to != null) && (saleDate.isEqual(from) || saleDate.isEqual(to) || (saleDate.isAfter(from) && saleDate.isBefore(to)))) 
	    					 {
	    	                        salesWithinDateRange.add(sale);
	    	                    } else if (from == null && to == null) {
	    	                        salesWithinDateRange.add(sale);
	    	                    }
	    				
	    			}
	    		}
	    		
	    		
	    		if(salesWithinDateRange!=null)
	    		{
	    			saleTemplet(salesWithinDateRange,"Dine-In");
	    		}
	    	}
	    	
	    	
	    }
	    
	    
	    public void delivery()
	    {
	    	LocalDate from = dateFrom.getValue();
	    	LocalDate to = dateTo.getValue();
	    	
	    	List<Sale> list = SaleDAO.getAllSale();
	    	
	    	List<Sale> salesWithinDateRange = null;
	    	
	    	if(list!=null)
	    	{
	    		
	    			salesWithinDateRange = new ArrayList<>();
	    			
	    			for(Sale sale:list)
	    			{
	    				if(sale.getSaleOrder().equals("Delivery"))
	    				{
	    					LocalDate saleDate = sale.getCreated();
	    					 if ((from != null && to != null) && (saleDate.isEqual(from) || saleDate.isEqual(to) || (saleDate.isAfter(from) && saleDate.isBefore(to)))) 
	    					 {
	    	                        salesWithinDateRange.add(sale);
	    	                    } else if (from == null && to == null) {
	    	                        salesWithinDateRange.add(sale);
	    	                    }
	    				
	    			}
	    		}
	    		
	    		
	    		if(salesWithinDateRange!=null)
	    		{
	    			saleTemplet(salesWithinDateRange,"Delivery");
	    		}
	    	}
	    	
	    	
	    }
	    
	    
	    public void deliveryOderType()
	    {
	    	LocalDate from = dateFrom.getValue();
	    	LocalDate to = dateTo.getValue();
	    	String orderType = chbOrderType.getValue();
	    	List<Sale> list = SaleDAO.getAllSale();
	    	
	    	List<Sale> salesWithinDateRange = null;
	    	
	    	if(list!=null)
	    	{
	    		
	    			salesWithinDateRange = new ArrayList<>();
	    			
	    			for(Sale sale:list)
	    			{
	    				if(sale.getSaleOrder().equals("Delivery"))
	    				{
	    					if(sale.getOrderType().equals(orderType) && orderType!=null)
	    					{
	    					LocalDate saleDate = sale.getCreated();
	    					 if ((from != null && to != null) && (saleDate.isEqual(from) || saleDate.isEqual(to) || (saleDate.isAfter(from) && saleDate.isBefore(to)))) 
	    					 {
	    	                        salesWithinDateRange.add(sale);
	    	                    } else if (from == null && to == null) {
	    	                        salesWithinDateRange.add(sale);
	    	                    }
	    				}
	    			}
	    		}
	    		
	    		
	    		if(salesWithinDateRange!=null)
	    		{
	    			saleTemplet(salesWithinDateRange, "Delivery_"+orderType);
	    		}
	    	}
	    	
	    	
	    }
	    
	    
	    public void takeaway()
	    {
	    	LocalDate from = dateFrom.getValue();
	    	LocalDate to = dateTo.getValue();
	    	
	    	List<Sale> list = SaleDAO.getAllSale();
	    	
	    	List<Sale> salesWithinDateRange = null;
	    	
	    	if(list!=null)
	    	{
	    		
	    			salesWithinDateRange = new ArrayList<>();
	    			
	    			for(Sale sale:list)
	    			{
	    				if(sale.getSaleOrder().equals("Takeaway"))
	    				{
	    					LocalDate saleDate = sale.getCreated();
	    					 if ((from != null && to != null) && (saleDate.isEqual(from) || saleDate.isEqual(to) || (saleDate.isAfter(from) && saleDate.isBefore(to)))) 
	    					 {
	    	                        salesWithinDateRange.add(sale);
	    	                    } else if (from == null && to == null) {
	    	                        salesWithinDateRange.add(sale);
	    	                    }
	    				
	    			}
	    		}
	    		
	    		
	    		if(salesWithinDateRange!=null)
	    		{
	    			saleTemplet(salesWithinDateRange,"Takeaway");
	    		}
	    	}
	    	
	    	
	    }
	    
	    
	    public void allSale()
	    {
	    	LocalDate from = dateFrom.getValue();
	    	LocalDate to = dateTo.getValue();
	    	
	    	List<Sale> list = SaleDAO.getAllSale();
	    	
	    	List<Sale> salesWithinDateRange = null;
	    	
	    	if(list!=null)
	    	{
	    		
	    			salesWithinDateRange = new ArrayList<>();
	    			
	    			for(Sale sale:list)
	    			{
	    				
	    					LocalDate saleDate = sale.getCreated();
	    					 if ((from != null && to != null) && (saleDate.isEqual(from) || saleDate.isEqual(to) || (saleDate.isAfter(from) && saleDate.isBefore(to)))) 
	    					 {
	    	                        salesWithinDateRange.add(sale);
	    	                    } else if (from == null && to == null) {
	    	                        salesWithinDateRange.add(sale);
	    	                    }
	    				
	    		}
	    		
	    		
	    		if(salesWithinDateRange!=null)
	    		{
	    			saleTemplet(salesWithinDateRange,"All");
	    		}
	    	}
	    	
	    	
	    }
	    
	    
	    public void tax()
	    {
	    	LocalDate from = dateFrom.getValue();
	    	LocalDate to = dateTo.getValue();
	    	
	    	List<Sale> list = SaleDAO.getAllSale();
	    	
	    	List<Sale> salesWithinDateRange = null;
	    	
	    	if(list!=null)
	    	{
	    		
	    			salesWithinDateRange = new ArrayList<>();
	    			
	    			for(Sale sale:list)
	    			{
	    				if(sale.getTaxPercent()!=0)
	    				{
	    					LocalDate saleDate = sale.getCreated();
	    					 if ((from != null && to != null) && (saleDate.isEqual(from) || saleDate.isEqual(to) || (saleDate.isAfter(from) && saleDate.isBefore(to)))) 
	    					 {
	    	                        salesWithinDateRange.add(sale);
	    	                    } else if (from == null && to == null) {
	    	                        salesWithinDateRange.add(sale);
	    	                    }
	    				
	    			}
	    		}
	    		
	    		
	    		if(salesWithinDateRange!=null)
	    		{
	    			saleTemplet(salesWithinDateRange,"Tax");
	    		}
	    	}
	    	
	    	
	    }
	    
	    
	    public void discount()
	    {
	    	LocalDate from = dateFrom.getValue();
	    	LocalDate to = dateTo.getValue();
	    	
	    	List<Sale> list = SaleDAO.getAllSale();
	    	
	    	List<Sale> salesWithinDateRange = null;
	    	
	    	if(list!=null)
	    	{
	    		
	    			salesWithinDateRange = new ArrayList<>();
	    			
	    			for(Sale sale:list)
	    			{
	    				if(sale.getDiscountAmt()!=0)
	    				{
	    					LocalDate saleDate = sale.getCreated();
	    					 if ((from != null && to != null) && (saleDate.isEqual(from) || saleDate.isEqual(to) || (saleDate.isAfter(from) && saleDate.isBefore(to)))) 
	    					 {
	    	                        salesWithinDateRange.add(sale);
	    	                    } else if (from == null && to == null) {
	    	                        salesWithinDateRange.add(sale);
	    	                    }
	    				
	    			}
	    		}
	    		
	    		
	    		if(salesWithinDateRange!=null)
	    		{
	    			saleTemplet(salesWithinDateRange,"Discount");
	    		}
	    	}
	    	
	    	
	    }
	    
	   
	    public void allSaleItem()
	    {
	    	LocalDate from = dateFrom.getValue();
	    	LocalDate to = dateTo.getValue();
	    	
	    	List<SaleItem> list = SaleItemDAO.getAllSaleItem();
	    	
	    	List<SaleItem> salesWithinDateRange = null;
	    	
	    	if(list!=null)
	    	{
	    		
	    			salesWithinDateRange = new ArrayList<>();
	    			
	    			for(SaleItem sale:list)
	    			{
	    					LocalDate saleDate = sale.getCreated();
	    					 if ((from != null && to != null) && (saleDate.isEqual(from) || saleDate.isEqual(to) || (saleDate.isAfter(from) && saleDate.isBefore(to)))) 
	    					 {
	    	                        salesWithinDateRange.add(sale);
	    	                    } else if (from == null && to == null) {
	    	                        salesWithinDateRange.add(sale);
	    	                    }
	    		
	    		}
	    		
	    		
	    		if(salesWithinDateRange!=null)
	    		{
	    			saleItemTemplet(salesWithinDateRange,"SaleItem");
	    		}
	    	}
	    	
	    	
	    }
	    
	    
	    public void hotSaleItem()
	    {
	    	LocalDate from = dateFrom.getValue();
	    	LocalDate to = dateTo.getValue();
	    	
	    	List<SaleItem> list = SaleItemDAO.getSaleItemDESC();
	    	
	    	List<SaleItem> salesWithinDateRange = null;
	    	
	    	if(list!=null)
	    	{
	    		
	    			salesWithinDateRange = new ArrayList<>();
	    			
	    			for(SaleItem sale:list)
	    			{
	    					LocalDate saleDate = sale.getCreated();
	    					 if ((from != null && to != null) && (saleDate.isEqual(from) || saleDate.isEqual(to) || (saleDate.isAfter(from) && saleDate.isBefore(to)))) 
	    					 {
	    	                        salesWithinDateRange.add(sale);
	    	                    } else if (from == null && to == null) {
	    	                        salesWithinDateRange.add(sale);
	    	                    }
	    		
	    		}
	    		
	    		
	    		if(salesWithinDateRange!=null)
	    		{
	    			saleItemTemplet(salesWithinDateRange,"Hot_Sale_Item");
	    		}
	    	}
	    	
	    	
	    }
	    
	    public void cancelItem()
	    {
	    	LocalDate from = dateFrom.getValue();
	    	LocalDate to = dateTo.getValue();
	    	
	    	List<KOTItem> list = KOTItemDAO.getAllKOTItem();
	    	
	    	List<KOTItem> kotWithinDateRange = null;
	    	
	    	if(list!=null)
	    	{
	    		
	    		kotWithinDateRange = new ArrayList<>();
	    			
	    			for(KOTItem sale:list)
	    			{

						 if(sale.isCancelStatus()==true)
						 {
	    					LocalDate saleDate = sale.getCreated();
	    					 if ((from != null && to != null) && (saleDate.isEqual(from) || saleDate.isEqual(to) || (saleDate.isAfter(from) && saleDate.isBefore(to)))) 
	    					 {
	    						 kotWithinDateRange.add(sale);
	    						
	    	                    } else if (from == null && to == null) {
	    	                    	kotWithinDateRange.add(sale);
	    	                    }
						 }
	    		}
	    		
	    		
	    		if(kotWithinDateRange!=null)
	    		{
	    			kotITemTemplet(kotWithinDateRange,"Cancel_Item");
	    		}
	    	}
	    	
	    	
	    }
	    
	    public void cancelKOT()
	    {
	    	LocalDate from = dateFrom.getValue();
	    	LocalDate to = dateTo.getValue();
	    	
	    	List<KOT> list = KOTDAO.getKOTOrder();
	    	
	    	List<KOT> salesWithinDateRange = null;
	    	
	    	if(list!=null)
	    	{
	    		
	    			salesWithinDateRange = new ArrayList<>();
	    			
	    			for(KOT sale:list)
	    			{
	    				if(sale.isCancelStatus()==true)
	    				{
	    					LocalDate saleDate = sale.getCreated();
	    					 if ((from != null && to != null) && (saleDate.isEqual(from) || saleDate.isEqual(to) || (saleDate.isAfter(from) && saleDate.isBefore(to)))) 
	    					 {
	    	                        salesWithinDateRange.add(sale);
	    	                    } else if (from == null && to == null) {
	    	                        salesWithinDateRange.add(sale);
	    	                    }
	    				
	    			}
	    		}
	    		
	    		
	    		if(salesWithinDateRange!=null)
	    		{
	    			kotOrderTemplet(salesWithinDateRange, "Cancel_KOT_Order");
	    		}
	    	}
	    	
	    	
	    }
	    
	    
	    public void allSaleayment()
	    {
	    	LocalDate from = dateFrom.getValue();
	    	LocalDate to = dateTo.getValue();
	    	String payment = chbPayment.getValue();
	    	List<Sale> list = SaleDAO.getAllSale();
	    	
	    	List<Sale> salesWithinDateRange = null;
	    	
	    	if(list!=null)
	    	{
	    		
	    			salesWithinDateRange = new ArrayList<>();
	    			
	    			for(Sale sale:list)
	    			{
	    				
	    					if(sale.getPayMethod().equals(payment) && payment!=null)
	    					{
	    					LocalDate saleDate = sale.getCreated();
	    					 if ((from != null && to != null) && (saleDate.isEqual(from) || saleDate.isEqual(to) || (saleDate.isAfter(from) && saleDate.isBefore(to)))) 
	    					 {
	    	                        salesWithinDateRange.add(sale);
	    	                    } else if (from == null && to == null) {
	    	                        salesWithinDateRange.add(sale);
	    	                    }
	    				}
	    		
	    		}
	    		
	    		
	    		if(salesWithinDateRange!=null)
	    		{
	    			saleTemplet(salesWithinDateRange, "All_Sale_"+payment);
	    		}
	    	}
	    	
	    	
	    }
	    
	    public void saleTemplet(List<Sale> list,String reportName)
	    {
	    	BillFormate bf = BillFormateDAO.getHotelDetails();
	    	
	    	double total_count = 0;
			double total_tax_count = 0;
			double total_disc_count = 0;
			double grand_total_count = 0;
	    	
	    	if(bf!=null)
			{
				restaurantName = bf.getHotelName();
				restaurantType = bf.getHotelType();
				restaurantAddress =bf.getAddress();
				restaurantContact = bf.getContact();
				
			}
	    	
	    	StringBuilder htmlBuilder = new StringBuilder();

	        htmlBuilder.append("<!DOCTYPE html>");
	        htmlBuilder.append("<html>");
	        htmlBuilder.append("<head>");
	        htmlBuilder.append("<title>Sales Report</title>");
	        htmlBuilder.append("<style>");
	        htmlBuilder.append("table {"
	        		+ "        border-collapse: collapse;"
	        		+ "        width: 100%;"
	        		+ "        border: 1px solid lightgray; "
	        		+ "    }"
	        		+ "    th, td {"
	        		+ "        border: 1px solid lightgray;"
	        		+ "        padding: 8px;"
	        		+ "        text-align: left;"
	        		+ "    }"
	        		+ "        th {"
	        		+ "            background-color: lightgray;"
	        		+ "        }"
	        		+ "         .company-info {"
	        		+ "            text-align: right;"
	        		+ "            font-size: 12px;"
	        		+ "            color: #777;"
	        		+ "            margin-bottom: 20px;"
	        		+ "        }"
	        		+ "        .additional-info {"
	        		+ "            margin-bottom: 20px;"
	        		+ "        }"
	        		+ "        .additional-info th {"
	        		+ "            background-color: lightgray;"
	        		+ "        }"
	        		+ "");
	        htmlBuilder.append("</style>");
	        htmlBuilder.append("</head>");
	        htmlBuilder.append("<body>");
	        
	        htmlBuilder.append("<h2>"+restaurantName+" "+restaurantType+"</h2>");
	        htmlBuilder.append("<p>"+restaurantAddress+"</p>");
	        htmlBuilder.append("<p>"+restaurantContact+"</p>");
	        htmlBuilder.append("<p>"+LocalDate.now()+"</p>");
		       
	        htmlBuilder.append("<h3>"+reportName+" Sales Report</h3>");
	        htmlBuilder.append("<table>");
	        htmlBuilder.append("<thead>");
	        htmlBuilder.append("<tr>");
	        htmlBuilder.append("<th>Bill No.</th>");
	        htmlBuilder.append("<th>Order</th>");
	        htmlBuilder.append("<th>Order Type</th>");
	        htmlBuilder.append("<th>Total Amt</th>");
	        htmlBuilder.append("<th>Tax Percent</th>");
	        htmlBuilder.append("<th>Discount</th>");
	        htmlBuilder.append("<th>Grand Total</th>");
	        htmlBuilder.append("<th>Pay Method</th>");
	        htmlBuilder.append("<th>Date</th>");
	        htmlBuilder.append("<th>Time</th>");
	        htmlBuilder.append("</tr>");
	        htmlBuilder.append("</thead>");
	        htmlBuilder.append("<tbody>");
	       
	  for(Sale sale : list)
	  {	
					
		  htmlBuilder.append("<tr>");
	        htmlBuilder.append("<td>"+sale.getSaleId()+"</td>");
	        htmlBuilder.append("<td>"+sale.getSaleOrder()+"</td>");
	        htmlBuilder.append("<td>"+sale.getOrderType()+"</td>");
	        htmlBuilder.append("<td>"+sale.getTotalAmt()+"</td>");
	        htmlBuilder.append("<td>"+sale.getTaxPercent()+"</td>");
	        htmlBuilder.append("<td>"+sale.getDiscountAmt()+"</td>");
	        htmlBuilder.append("<td>"+sale.getGrandTotal()+"</td>");
	        htmlBuilder.append("<td>"+sale.getPayMethod()+"</td>");
	        htmlBuilder.append("<td>"+sale.getCreated()+"</td>");
	        htmlBuilder.append("<td>"+sale.getTime()+"</td>");
	        htmlBuilder.append("</tr>");
	        
	        double total = sale.getTotalAmt();
			double tax = sale.getTaxPercent();
			double disc = sale.getDiscountAmt();
			double grand = sale.getGrandTotal();
			
			double taxAmtCount = TaxManagmentDAO.getTaxTotal(total, tax);
			total_count += total;
			total_tax_count += taxAmtCount;
			total_disc_count += disc;
			grand_total_count += grand;
	  }
		 DecimalFormat decimalFormat = new DecimalFormat("0.0");
		    
	        // Format the SGST and CGST amounts
	        String totalAmount = decimalFormat.format(total_count);
	        String taxAmount = decimalFormat.format(total_tax_count);
	        String discountAmount = decimalFormat.format(total_disc_count);
	        String grandTotalAmount = decimalFormat.format(grand_total_count);
	      
	       
	       htmlBuilder.append("</tbody>");
	        htmlBuilder.append("</table>");
	        
	        htmlBuilder.append(" <table>"
	        		+ "        <thead>"
	        		+ "            <tr>"
	        		+ "                <th>Total Amount</th>"
	        		+ "                <th>Tax</th>"
	        		+ "                <th>Tax Amount</th>"
	        		+ "                <th>Discount</th>"
	        		+ "                <th>Discount Amount</th>"
	        		+ "                <th>Grand Total</th>"             
	        		+ "            </tr>"
	        		+ "        </thead>"
	        		+ "        <tbody>"
	        		+ "            <tr>"
	        		+ "                <td>"+totalAmount+"</td>"
	        		+ "                <td>"+total_tax_count+"</td>"
	        		+ "                <td>"+taxAmount+"</td>"
	        		+ "                <td>"+total_disc_count+"</td>"
	        		+ "                <td>"+discountAmount+"</td>"
	        		+ "                <td>"+grandTotalAmount+"</td>"
	        		+ "            </tr>"
	        		+ "        </tbody>"
	        		+ "    </table>");
	        htmlBuilder.append("<div class=\"company-info\">");
	        htmlBuilder.append("MedResto Software");
	        htmlBuilder.append("</div>");

	        htmlBuilder.append("</body>");
	        htmlBuilder.append("</html>");
	        
	        generatePDF(htmlBuilder.toString(), reportName);
	    }
	    
	    
	    public void kotOrderTemplet(List<KOT> list,String reportName)
	    {
	    	BillFormate bf = BillFormateDAO.getHotelDetails();
	    	
	    	double total_count = 0;
		 	
	    	if(bf!=null)
			{
				restaurantName = bf.getHotelName();
				restaurantType = bf.getHotelType();
				restaurantAddress =bf.getAddress();
				restaurantContact = bf.getContact();
				
			}
	    	
	    	StringBuilder htmlBuilder = new StringBuilder();

	        htmlBuilder.append("<!DOCTYPE html>");
	        htmlBuilder.append("<html>");
	        htmlBuilder.append("<head>");
	        htmlBuilder.append("<title>Sales Report</title>");
	        htmlBuilder.append("<style>");
	        htmlBuilder.append("table {"
	        		+ "        border-collapse: collapse;"
	        		+ "        width: 100%;"
	        		+ "        border: 1px solid lightgray; "
	        		+ "    }"
	        		+ "    th, td {"
	        		+ "        border: 1px solid lightgray;"
	        		+ "        padding: 8px;"
	        		+ "        text-align: left;"
	        		+ "    }"
	        		+ "        th {"
	        		+ "            background-color: lightgray;"
	        		+ "        }"
	        		+ "         .company-info {"
	        		+ "            text-align: right;"
	        		+ "            font-size: 12px;"
	        		+ "            color: #777;"
	        		+ "            margin-bottom: 20px;"
	        		+ "        }"
	        		+ "        .additional-info {"
	        		+ "            margin-bottom: 20px;"
	        		+ "        }"
	        		+ "        .additional-info th {"
	        		+ "            background-color: lightgray;"
	        		+ "        }"
	        		+ "");
	        htmlBuilder.append("</style>");
	        htmlBuilder.append("</head>");
	        htmlBuilder.append("<body>");
	        
	        htmlBuilder.append("<h2>"+restaurantName+" "+restaurantType+"</h2>");
	        htmlBuilder.append("<p>"+restaurantAddress+"</p>");
	        htmlBuilder.append("<p>"+restaurantContact+"</p>");
	        htmlBuilder.append("<p>"+LocalDate.now()+"</p>");
		       
	        htmlBuilder.append("<h3>"+reportName+" Sales Report</h3>");
	        htmlBuilder.append("<table>");
	        htmlBuilder.append("<thead>");
	        htmlBuilder.append("<tr>");
	        htmlBuilder.append("<th>KOT No.</th>");
	        htmlBuilder.append("<th>Order</th>");
	        htmlBuilder.append("<th>Order Type</th>");
	        htmlBuilder.append("<th>Total Amt</th>");
	          htmlBuilder.append("<th>Comment</th>");
	        htmlBuilder.append("<th>Date</th>");
	        htmlBuilder.append("<th>Time</th>");
	        htmlBuilder.append("</tr>");
	        htmlBuilder.append("</thead>");
	        htmlBuilder.append("<tbody>");
	       
	  for(KOT sale : list)
	  {	
					
		  htmlBuilder.append("<tr>");
	        htmlBuilder.append("<td>"+sale.getKotId()+"</td>");
	        htmlBuilder.append("<td>"+sale.getKotOrder()+"</td>");
	        htmlBuilder.append("<td>"+sale.getOrderType()+"</td>");
	        htmlBuilder.append("<td>"+sale.getTotalPrice()+"</td>");
	        htmlBuilder.append("<td>"+sale.getComment()+"</td>");
	        htmlBuilder.append("<td>"+sale.getCreated()+"</td>");
	        htmlBuilder.append("<td>"+sale.getTime()+"</td>");
	        htmlBuilder.append("</tr>");
	        
	        double total = sale.getTotalPrice();
					
				total_count += total;
	  }
		 DecimalFormat decimalFormat = new DecimalFormat("0.0");
		    
	        // Format the SGST and CGST amounts
	        String totalAmount = decimalFormat.format(total_count);
	       
	       
	       htmlBuilder.append("</tbody>");
	        htmlBuilder.append("</table>");
	        
	        htmlBuilder.append("<h3>Grand Total</h3>");
	        htmlBuilder.append("<h3>"+totalAmount+"</h3>");
	        htmlBuilder.append("<div class=\"company-info\">");
	        htmlBuilder.append("MedResto Software");
	        htmlBuilder.append("</div>");

	        htmlBuilder.append("</body>");
	        htmlBuilder.append("</html>");
	        
	        generatePDF(htmlBuilder.toString(), reportName);
	    }

		
	    public void saleItemTemplet(List<SaleItem> list,String reportName)
	    {
	    	BillFormate bf = BillFormateDAO.getHotelDetails();
	    	
	   	double grand_total_count = 0;
	    	
	    	if(bf!=null)
			{
				restaurantName = bf.getHotelName();
				restaurantType = bf.getHotelType();
				restaurantAddress =bf.getAddress();
				restaurantContact = bf.getContact();
				
			}
	    	
	    	StringBuilder htmlBuilder = new StringBuilder();

	        htmlBuilder.append("<!DOCTYPE html>");
	        htmlBuilder.append("<html>");
	        htmlBuilder.append("<head>");
	        htmlBuilder.append("<title>Sales Report</title>");
	        htmlBuilder.append("<style>");
	        htmlBuilder.append("table {"
	        		+ "        border-collapse: collapse;"
	        		+ "        width: 100%;"
	        		+ "        border: 1px solid lightgray; "
	        		+ "    }"
	        		+ "    th, td {"
	        		+ "        border: 1px solid lightgray;"
	        		+ "        padding: 8px;"
	        		+ "        text-align: left;"
	        		+ "    }"
	        		+ "        th {"
	        		+ "            background-color: lightgray;"
	        		+ "        }"
	        		+ "         .company-info {"
	        		+ "            text-align: right;"
	        		+ "            font-size: 12px;"
	        		+ "            color: #777;"
	        		+ "            margin-bottom: 20px;"
	        		+ "        }"
	        		+ "        .additional-info {"
	        		+ "            margin-bottom: 20px;"
	        		+ "        }"
	        		+ "        .additional-info th {"
	        		+ "            background-color: lightgray;"
	        		+ "        }"
	        		+ "");
	        htmlBuilder.append("</style>");
	        htmlBuilder.append("</head>");
	        htmlBuilder.append("<body>");
	        
	        htmlBuilder.append("<h2>"+restaurantName+" "+restaurantType+"</h2>");
	        htmlBuilder.append("<p>"+restaurantAddress+"</p>");
	        htmlBuilder.append("<p>"+restaurantContact+"</p>");
	        htmlBuilder.append("<p>"+LocalDate.now()+"</p>");
		       
	        htmlBuilder.append("<h3>"+reportName+" Sales Report</h3>");
	        htmlBuilder.append("<table>");
	        htmlBuilder.append("<thead>");
	        htmlBuilder.append("<tr>");
	        htmlBuilder.append("<th>Sale Item Id</th>");
	        htmlBuilder.append("<th>Bill No.</th>");
	        htmlBuilder.append("<th>Item Name</th>");
	        htmlBuilder.append("<th>Qty</th>");
	        htmlBuilder.append("<th>Price</th>");
	        htmlBuilder.append("<th>Total</th>");
	        htmlBuilder.append("<th>Date</th>");
	        htmlBuilder.append("</tr>");
	        htmlBuilder.append("</thead>");
	        htmlBuilder.append("<tbody>");
	       
	  for(SaleItem sale : list)
	  {	
					
		  htmlBuilder.append("<tr>");
	        htmlBuilder.append("<td>"+sale.getSaleItemId()+"</td>");
	        htmlBuilder.append("<td>"+sale.getSaleId()+"</td>");
	        htmlBuilder.append("<td>"+sale.getItemName()+"</td>");
	        htmlBuilder.append("<td>"+sale.getQty()+"</td>");
	        htmlBuilder.append("<td>"+sale.getPrice()+"</td>");
	        htmlBuilder.append("<td>"+sale.getItemTotal()+"</td>");
	        htmlBuilder.append("<td>"+sale.getCreated()+"</td>");
	        htmlBuilder.append("</tr>");
	        
	        double total = sale.getItemTotal();
			
			grand_total_count += total;
	  }
		 DecimalFormat decimalFormat = new DecimalFormat("0.0");
		    
	          String grandTotalAmount = decimalFormat.format(grand_total_count);
	      
	       
	       htmlBuilder.append("</tbody>");
	        htmlBuilder.append("</table>");
	        htmlBuilder.append("<h3>Grand Total</h3>");
	        htmlBuilder.append("<h3>"+grandTotalAmount+"</h3>");
	       
	        htmlBuilder.append("<div class=\"company-info\">");
	        htmlBuilder.append("MedResto Software");
	        htmlBuilder.append("</div>");

	        htmlBuilder.append("</body>");
	        htmlBuilder.append("</html>");
	        
	        generatePDF(htmlBuilder.toString(), reportName);
	    }

	    public void kotITemTemplet(List<KOTItem> list,String reportName)
	    {
	    	BillFormate bf = BillFormateDAO.getHotelDetails();
	    	
	   	double grand_total_count = 0;
	    	
	    	if(bf!=null)
			{
				restaurantName = bf.getHotelName();
				restaurantType = bf.getHotelType();
				restaurantAddress =bf.getAddress();
				restaurantContact = bf.getContact();
				
			}
	    	
	    	StringBuilder htmlBuilder = new StringBuilder();

	        htmlBuilder.append("<!DOCTYPE html>");
	        htmlBuilder.append("<html>");
	        htmlBuilder.append("<head>");
	        htmlBuilder.append("<title>Sales Report</title>");
	        htmlBuilder.append("<style>");
	        htmlBuilder.append("table {"
	        		+ "        border-collapse: collapse;"
	        		+ "        width: 100%;"
	        		+ "        border: 1px solid lightgray; "
	        		+ "    }"
	        		+ "    th, td {"
	        		+ "        border: 1px solid lightgray;"
	        		+ "        padding: 8px;"
	        		+ "        text-align: left;"
	        		+ "    }"
	        		+ "        th {"
	        		+ "            background-color: lightgray;"
	        		+ "        }"
	        		+ "         .company-info {"
	        		+ "            text-align: right;"
	        		+ "            font-size: 12px;"
	        		+ "            color: #777;"
	        		+ "            margin-bottom: 20px;"
	        		+ "        }"
	        		+ "        .additional-info {"
	        		+ "            margin-bottom: 20px;"
	        		+ "        }"
	        		+ "        .additional-info th {"
	        		+ "            background-color: lightgray;"
	        		+ "        }"
	        		+ "");
	        htmlBuilder.append("</style>");
	        htmlBuilder.append("</head>");
	        htmlBuilder.append("<body>");
	        
	        htmlBuilder.append("<h2>"+restaurantName+" "+restaurantType+"</h2>");
	        htmlBuilder.append("<p>"+restaurantAddress+"</p>");
	        htmlBuilder.append("<p>"+restaurantContact+"</p>");
	        htmlBuilder.append("<p>"+LocalDate.now()+"</p>");
		       
	        htmlBuilder.append("<h3>"+reportName+" Sales Report</h3>");
	        htmlBuilder.append("<table>");
	        htmlBuilder.append("<thead>");
	        htmlBuilder.append("<tr>");
	        htmlBuilder.append("<th>KOT Item Id</th>");
	        htmlBuilder.append("<th>KOT Id</th>");
	        htmlBuilder.append("<th>Item Name</th>");
	        htmlBuilder.append("<th>Price</th>");
	        htmlBuilder.append("<th>Qty</th>");
	        htmlBuilder.append("<th>Total</th>");
	        htmlBuilder.append("<th>Comment</th>");
	        htmlBuilder.append("<th>Date</th>");
	        htmlBuilder.append("</tr>");
	        htmlBuilder.append("</thead>");
	        htmlBuilder.append("<tbody>");
	       
	  for(KOTItem sale : list)
	  {	
					
		  htmlBuilder.append("<tr>");
	        htmlBuilder.append("<td>"+sale.getKotItemId()+"</td>");
	        htmlBuilder.append("<td>"+sale.getKotId()+"</td>");
	        htmlBuilder.append("<td>"+sale.getItemName()+"</td>");
	        htmlBuilder.append("<td>"+sale.getPrice()+"</td>");
	        htmlBuilder.append("<td>"+sale.getQty()+"</td>");
	        htmlBuilder.append("<td>"+sale.getItemTotal()+"</td>");
	        htmlBuilder.append("<td>"+sale.getComment()+"</td>");
	        htmlBuilder.append("<td>"+sale.getCreated()+"</td>");
	        htmlBuilder.append("</tr>");
	        
	        double total = sale.getItemTotal();
			
			grand_total_count += total;
	  }
		 DecimalFormat decimalFormat = new DecimalFormat("0.0");
		    
	        // Format the SGST and CGST amounts
	          String grandTotalAmount = decimalFormat.format(grand_total_count);
	      
	       
	       htmlBuilder.append("</tbody>");
	        htmlBuilder.append("</table>");
	        htmlBuilder.append("<h3>Grand Total</h3>");
	        htmlBuilder.append("<h3>"+grandTotalAmount+"</h3>");
	       
	        htmlBuilder.append("<div class=\"company-info\">");
	        htmlBuilder.append("MedResto Software");
	        htmlBuilder.append("</div>");

	        htmlBuilder.append("</body>");
	        htmlBuilder.append("</html>");
	        
	        generatePDF(htmlBuilder.toString(), reportName);
	    }
	    
	    public static void generatePDF(String htmlContent, String fileName) {
	        try {
	            // Get the Downloads folder path
	        	String downloadDirectory = System.getProperty("user.home") + File.separator + "Downloads";

	            // Create the directory if it doesn't exist
	            Path directoryPath = Paths.get(downloadDirectory);
	            Files.createDirectories(directoryPath);

	            // Create a unique file name using timestamp
	            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	            String outputFile = downloadDirectory + File.separator + fileName + "_" + timestamp + ".pdf";

	            FileOutputStream os = new FileOutputStream(outputFile);
	            ITextRenderer renderer = new ITextRenderer();
	            renderer.setDocumentFromString(htmlContent);
	            renderer.layout();
	            renderer.createPDF(os);
	            os.close();

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        AllNotifications.showNotification("Report", "Report Downloaded Sucssesfuly");
	    }

}
