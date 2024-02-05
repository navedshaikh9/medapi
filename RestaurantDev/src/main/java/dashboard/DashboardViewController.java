package dashboard;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import com.restaurant.DAO.PaymentMethodDAO;
import com.restaurant.DAO.SaleDAO;
import com.restaurant.DAO.SaleItemDAO;
import com.restaurant.Entity.PaymentMethod;
import com.restaurant.Entity.Sale;
import AllNotivications.AllNotifications;
import AllNotivications.CommenFunction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.control.DatePicker;
import javafx.event.ActionEvent;

public class DashboardViewController implements Initializable{

	CommenFunction cf = new CommenFunction();
	
	  @FXML
	    private Label lbl_delivery;

	    @FXML
	    private Label lbl_dine_in;

	    @FXML
	    private Label lbl_discount;

	    @FXML
	    private Label lbl_sale;

	    @FXML
	    private Label lbl_takeaway;

	    @FXML
	    private Label lbl_tax;

	    @FXML
	    private Pane paneDashboardView;

	    @FXML
	    private PieChart sale_pie_chart;
	    
	    @FXML
	    private BarChart<String, Number> bar_chart;
	    
	    @FXML
	    private TilePane tile_pane_payment;
	    
	    @FXML
	    private Label lbl_delivery_total;
	    
	    @FXML
	    private Label lbl_dine_in_total;
	    
	    @FXML
	    private Label lbl_takeaway_total;
	    

	    @FXML
	    private Label lbl_ip_address;
	    
	    @FXML
	    private DatePicker bar_chart_end_date;

	    @FXML
	    private DatePicker bar_chart_start_date;
	    
	    @FXML
	    private DatePicker pie_chart_end_date;

	    @FXML
	    private DatePicker pie_chart_start_date;
	    
	  
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		loadAll();
    	
	}
	
	private void loadAll()
	{
		loadDashboard();
    	pay();
    	 salePaieChart();
    	 saleBarChart();
	}
	
	@FXML
    void btn_chart_search(ActionEvent event) {
		barChartSearch();
    }

    @FXML
    void btn_pie_chart_search(ActionEvent event) {
    	pieChartSearch();
    }

    @FXML
    void btnHotTenItem(ActionEvent event) {
    	 topSaleItem();
    }
   
    private void loadDashboard()
    {	
    	int dine_in = SaleDAO.getOrderTotal("Dine-In");
    	int delivery = SaleDAO.getOrderTotal("Delivery");
    	int takeaway = SaleDAO.getOrderTotal("Takeaway");
       	
    	lbl_dine_in.setText(""+dine_in);
    	lbl_delivery.setText(""+delivery);
    	lbl_takeaway.setText(""+takeaway);
    	
    	double delivery_total = SaleDAO.getOrderSale("Delivery");
    	double dine_in_total = SaleDAO.getOrderSale("Dine-In");
    	double takeaway_total = SaleDAO.getOrderSale("Takeaway");
    	lbl_delivery_total.setText(""+delivery_total);
    	lbl_dine_in_total.setText(""+dine_in_total);
    	lbl_takeaway_total.setText(""+takeaway_total);
    	
    	double taxAmt = SaleDAO.getTotalWithTax();
    	double discount= SaleDAO.getDiscount();
    	double total = SaleDAO.getTotal();
    	
    	  DecimalFormat decimalFormat = new DecimalFormat("0.0");
    	  String taxAmount = decimalFormat.format(taxAmt);
    	  String discountAmount = decimalFormat.format(discount);
    	  String totalAmount = decimalFormat.format(total);
    	  
    	lbl_tax.setText(""+taxAmount);
    	lbl_discount.setText(""+discountAmount);
    	lbl_sale.setText(""+totalAmount);
    }
	

    
    private void pay()
    {
    	tile_pane_payment.getChildren().clear();
    	
    	List<PaymentMethod> list = PaymentMethodDAO.getAllPaymentMethod();
    	
  		for (PaymentMethod d : list) {
  			
  			double total = SaleDAO.getPayMethodTotal(d.getPaymentMethod());
  			String multilineText = "" + d.getPaymentMethod() + "\n" + total;
			Button btnKOT = new Button(multilineText);
			btnKOT.setPrefWidth(130);
			btnKOT.setPrefHeight(50);
			btnKOT.setMaxWidth(130);
			btnKOT.setMaxHeight(50);
			tile_pane_payment.getChildren().add(btnKOT);
			tile_pane_payment.setHgap(5);
			tile_pane_payment.setVgap(5);

			btnKOT.setStyle("-fx-background-color:#59a61f; -fx-font-size:14; -fx-text-fill:#ffffff;");
			
		}
    }
    
    
    
    
    
    private void salePaieChart()
    {
    	Map<String, Double> dataMap = SaleDAO.fetchPieChartData();
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        final double[] totalAmount = {0.0}; 
        for (Double amount : dataMap.values()) {
        	 totalAmount[0] += amount;
        }
        
        dataMap.forEach((sale_order, total) -> {
        	double percentage = (total / totalAmount[0]) * 100;
        	pieChartData.add(new PieChart.Data(sale_order + " (" + String.format("%.2f", percentage) + "%)", total));
        });
        
        sale_pie_chart.setData(pieChartData);
      //  sale_pie_chart.setTitle("Today's Sales");
    }
   
    
    private void saleBarChart()
    {
    	 CategoryAxis xAxis = new CategoryAxis();
         NumberAxis yAxis = new NumberAxis();
         BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
     //    barChart.setTitle("Monthly Sales");

         XYChart.Series<String, Number> series = new XYChart.Series<>();
     //    series.setName("Sales");

         List<Sale> monthlySales = SaleDAO.getAllSale();
         List<String> dateSummaries = new ArrayList<>();
      	  
  	    // Create a set to keep track of processed dates
  	    Set<LocalDate> processedDates = new HashSet<>();
  	  
  	    for (Sale sale : monthlySales) {
  	        LocalDate saleDate = sale.getCreated();
  	  
  	        // Check if the date is already processed
  	        if (!processedDates.contains(saleDate)) {
  	            int orderCount = 0;
  	            double totalAmount = 0.0;

  	            for (Sale record : monthlySales) {
  	                if (saleDate.isEqual(record.getCreated())) {
  	                    orderCount++;
  	                    totalAmount += record.getGrandTotal();
  	                }
  	            }
  	          series.getData().add(new XYChart.Data<>(""+totalAmount+" ("+orderCount+")",orderCount));
  	        }
  	    }
  	      
          bar_chart.getData().addAll(series);
          series.setName("Sales");
          
    }
    
    
    private void pieChartSearch()
    {
    	LocalDate from = pie_chart_start_date.getValue();
    	LocalDate to = pie_chart_end_date.getValue();
    	
    	if(from==null)
    	{
    		AllNotifications.error("Dashboard","Please Select From Date", null);
    		return;
    	}
    	if(to==null)
    	{
    		AllNotifications.error("Dashboard","Please Select To Date", null);
    		return;
    	}
    	
    	try {
    		
    		Map<String, Double> dataMap = SaleDAO.fetchPieChartData(from,to);
            
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            
            final double[] totalAmount = {0.0}; 
            for (Double amount : dataMap.values()) {
            	 totalAmount[0] += amount;
            }
            
            dataMap.forEach((sale_order, total) -> {
            	double percentage = (total / totalAmount[0]) * 100;
            	pieChartData.add(new PieChart.Data(sale_order + " (" + String.format("%.2f", percentage) + "%)", total));
            });
            
            sale_pie_chart.setData(pieChartData);
          //  sale_pie_chart.setTitle("Sales");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
    
    
    private void barChartSearch()
    {
    	LocalDate from = bar_chart_start_date.getValue();
    	LocalDate to = bar_chart_end_date.getValue();
    	
    	if(from==null)
    	{
    		AllNotifications.error("Dashboard","Please Select From Date", null);
    		return;
    	}
    	if(to==null)
    	{
    		AllNotifications.error("Dashboard","Please Select To Date", null);
    		return;
    	}
    	try {
    		 bar_chart.getData().clear();
    		 CategoryAxis xAxis = new CategoryAxis();
             NumberAxis yAxis = new NumberAxis();
             BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            // barChart.setTitle("Monthly Sales");

             XYChart.Series<String, Number> series = new XYChart.Series<>();
          //   series.setName("Sales");

             List<Sale> monthlySales = SaleDAO.getAllSale(from,to);
             
             
             
             List<String> dateSummaries = new ArrayList<>();
       	  
     	    // Create a set to keep track of processed dates
     	    Set<LocalDate> processedDates = new HashSet<>();
     	  
     	    for (Sale sale : monthlySales) {
     	        LocalDate saleDate = sale.getCreated();
     	  
     	        // Check if the date is already processed
     	        if (!processedDates.contains(saleDate)) {
     	            int orderCount = 0;
     	            double totalAmount = 0.0;

     	            for (Sale record : monthlySales) {
     	                if (saleDate.isEqual(record.getCreated())) {
     	                    orderCount++;
     	                    totalAmount += record.getGrandTotal();
     	                }
     	            }
     	           series.getData().add(new XYChart.Data<>(""+totalAmount+" ("+orderCount+")",orderCount));
     	        }
     	    }
             
             
             bar_chart.getData().addAll(series);
             series.setName("Sales");
		} catch (Exception e) {
			// TODO: handle exception
		}
    	

    }
    
    
    private void topSaleItem()
    {
    	
    	Map<String, Integer> menuItems = SaleItemDAO.getItemQty();
    	
    	if(menuItems!=null)
    	{
    	ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    	
    	List<Map.Entry<String, Integer>> entryList = new ArrayList<>(menuItems.entrySet());

        // Sort the list based on sales data (quantity sold)
        entryList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

     
        int count = 0;
        for (Map.Entry<String, Integer> entry : entryList) {
        	pieChartData.add(new PieChart.Data(entry.getKey()+"("+entry.getValue()+")", entry.getValue()));
            
            count++;
            if (count >= 10) {
                break;
            }
        }
        
        sale_pie_chart.setData(pieChartData);
        
    	}
    }
    
}
