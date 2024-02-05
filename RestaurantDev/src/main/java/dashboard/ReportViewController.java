package dashboard;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.restaurant.DAO.SaleItemDAO;
import com.restaurant.DAO.SaleReportDAO;
import com.restaurant.DAO.TaxManagmentDAO;
import com.restaurant.Entity.Sale;
import com.restaurant.Entity.SaleItem;

import AllNotivications.AllNotifications;
import BillManagment.BillFormat;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Label;


public class ReportViewController implements Initializable {

	AllNotifications allNotifi = new AllNotifications();

	@FXML
	private TableColumn<Sale, String> col_Created;

	@FXML
	private TableColumn<Sale, Double> col_Discount;

	@FXML
	private TableColumn<Sale, Double> col_GrandTotal;

	@FXML
	private TableColumn<Sale, String> col_Order;

	@FXML
	private TableColumn<Sale, String> col_PayMethod;

	@FXML
	private TableColumn<Sale, String> col_Stauts;

	@FXML
	private TableColumn<Sale, Double> col_TaxPercent;

	@FXML
	private TableColumn<Sale, Double> col_TotalAmt;

	@FXML
	private TableColumn<Sale, Integer> col_billId;
	
    @FXML
    private TableColumn<Sale, Integer> srNo;

	@FXML
	private TableColumn<Sale, String> col_order_type;

	@FXML
	private TableColumn<Sale, String> col_time;


	@FXML
	private TableView<Sale> tblSales;

	@FXML
	private DatePicker dateEnd;

	@FXML
	private DatePicker dateStart;
	
	 @FXML
	    private Label lbl_grand_total;

	    @FXML
	    private Label lbl_total;

	    @FXML
	    private Label lbl_total_discount;

	    @FXML
	    private Label lbl_total_tax;
	    
	    //item table

	    @FXML
	    private TableColumn<SaleItem, String> colItemName;

	    @FXML
	    private TableColumn<SaleItem, Double> colItemPrice;

	    @FXML
	    private TableColumn<SaleItem, Integer> colItemQty;

	    @FXML
	    private TableColumn<SaleItem, Integer> colItemSrNo;

	    @FXML
	    private TableColumn<SaleItem, Boolean> colItemStatus;

	    @FXML
	    private TableColumn<SaleItem, Double> colItemTotal;
	    
	    @FXML
	    private TableView<SaleItem> tblItem;


	int billid = 0;
	
	

	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		loadSalesTbl();
		columnLoad();
		loadSaleItem();
	}
	
	 @FXML
	    void btnOnActionDownloadReport(ActionEvent event) {
		 try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/reports/reports.fxml"));
				Scene scene = new Scene(root,800,450);
				s.setTitle("All Reports");
				s.setScene(scene);
				s.setResizable(false);
				s.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	@FXML
	private void getSelectedRow(MouseEvent event) {
		int index = tblSales.getSelectionModel().getSelectedIndex();
		if (index >= 0) {

			billid = col_billId.getCellData(index);
			loadItem(billid);

		} else {
			return;
		}

	}

	@FXML
	void btn_delivery_report(ActionEvent event) {
		//SaleReportDAO.getSaleItemReport();
		setDeliveryReport();
	}

	@FXML
	void btn_descount_report(ActionEvent event) {
		setDiscountReport();
	}

	@FXML
	void btn_dine_in_report(ActionEvent event) {
		setDineInReport();
	}

	@FXML
	void btn_sale_report(ActionEvent event) {
		loadSalesTbl();
	}

	@FXML
	void btn_takeaway_report(ActionEvent event) {
		setTakeawayReport();
	}

	@FXML
	void btn_tax_report(ActionEvent event) {
		setTaxReport();
	}
	
	  @FXML
	    void btn_today_report(ActionEvent event) {
		  todaysReport();
	    }
	  
	  
	  @FXML
	    void btn_print(ActionEvent event) {
		  int index = tblSales.getSelectionModel().getSelectedIndex();
			if (index >= 0) {
					int sale_id = col_billId.getCellData(index);
					BillFormat.billView(sale_id);
			} else {
				return;
			}
		  
	    }
	  
	  
	  private void todaysReport() {
		  ObservableList<Sale> list =  SaleReportDAO.getSaleAll(LocalDate.now(), LocalDate.now());

			if (list != null) {

				columnLoad();
				tblSales.setItems(list);
			}
			getTotal();
		}

	private void loadSalesTbl() {
		ObservableList<Sale> list= null;
		
		if(getStartDate()!=null || getEndDate()!=null)
		{
				list = SaleReportDAO.getSaleAll(getStartDate(), getEndDate());

			if (list != null) {

				columnLoad();
				tblSales.setItems(list);
			}
		}
		else
		{
		list = SaleReportDAO.getSaleAll();
		if (list != null) {

			columnLoad();
			tblSales.setItems(list);
		}
		}
		
		getTotal();
	}

	private void setDineInReport() {
		tblSales.getItems().clear();		
		ObservableList<Sale> list= null;
		if(getStartDate()!=null || getEndDate()!=null)
		{
				list = SaleReportDAO.getSaleByOrder("Dine-In", getStartDate(), getEndDate());

			if (list != null) {

				columnLoad();
				tblSales.setItems(list);
			}
		}
		else
		{
		list = SaleReportDAO.getSaleByOrder("Dine-In");
		if (list != null) {

			columnLoad();
			tblSales.setItems(list);
		}
		}
		
		getTotal();
	}
	
	private void setDeliveryReport() {
		tblSales.getItems().clear();
		ObservableList<Sale> list = null;
		if(getStartDate()!=null || getEndDate()!=null)
		{
			list = SaleReportDAO.getSaleByOrder("Delivery", getStartDate(), getEndDate());
			if (list != null) {
				columnLoad();
				tblSales.setItems(list);
			}
		}
		else
		{
			list = SaleReportDAO.getSaleByOrder("Delivery");
			if (list != null) {
				columnLoad();
				tblSales.setItems(list);
			}
		}
		
		getTotal();
	}
	
	
	private void setTakeawayReport() {
		tblSales.getItems().clear();
		ObservableList<Sale> list = null;
		if(getStartDate()!=null || getEndDate()!=null)
		{
			list = SaleReportDAO.getSaleByOrder("Takeaway", getStartDate(), getEndDate());
			if (list != null) {
				columnLoad();
				tblSales.setItems(list);
			}
		}
		else
		{
			list = SaleReportDAO.getSaleByOrder("Takeaway");
			if (list != null) {
				columnLoad();
				tblSales.setItems(list);
			}
		}
		
		getTotal();
	}
	
	private void setTaxReport() {
		tblSales.getItems().clear();
		ObservableList<Sale> list = null;
		if(getStartDate()!=null || getEndDate()!=null)
		{
			list = SaleReportDAO.getTaxReport(getStartDate(), getEndDate());
			if (list != null) {
				columnLoad();
				tblSales.setItems(list);
			}
		}
		else
		{
			list = SaleReportDAO.getTaxReport();
			if (list != null) {
				columnLoad();
				tblSales.setItems(list);
			}
		}
		
		getTotal();
	}
	
	private void setDiscountReport() {
		tblSales.getItems().clear();
		ObservableList<Sale> list = null;
		if(getStartDate()!=null || getEndDate()!=null)
		{
			list = SaleReportDAO.getDiscountReport(getStartDate(), getEndDate());
			if (list != null) {
				columnLoad();
				tblSales.setItems(list); 
			}
		}
		else
		{
			list = SaleReportDAO.getDiscountReport();
			if (list != null) {
				columnLoad();
				tblSales.setItems(list);
			}
		}
		
		getTotal();
	}

	private void columnLoad() {
		
		srNo.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(tblSales.getItems().indexOf(cellData.getValue()) + 1)
						.asObject());

		col_billId.setCellValueFactory(new PropertyValueFactory<Sale, Integer>("saleId"));
		col_Order.setCellValueFactory(new PropertyValueFactory<Sale, String>("saleOrder"));

		col_order_type.setCellValueFactory(new PropertyValueFactory<Sale, String>("orderType"));
		col_TotalAmt.setCellValueFactory(new PropertyValueFactory<Sale, Double>("totalAmt"));

		col_TaxPercent.setCellValueFactory(new PropertyValueFactory<Sale, Double>("taxPercent"));

		col_Discount.setCellValueFactory(new PropertyValueFactory<Sale, Double>("discountAmt"));

		col_GrandTotal.setCellValueFactory(new PropertyValueFactory<Sale, Double>("grandTotal"));
		col_PayMethod.setCellValueFactory(new PropertyValueFactory<Sale, String>("payMethod"));

		col_Created.setCellValueFactory(new PropertyValueFactory<Sale, String>("created"));
		col_time.setCellValueFactory(new PropertyValueFactory<Sale, String>("time"));
		col_Stauts.setCellValueFactory(new PropertyValueFactory<Sale, String>("status"));
	}
	
	
private void loadSaleItem() {
		
		colItemSrNo.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(tblItem.getItems().indexOf(cellData.getValue()) + 1)
						.asObject());

		colItemName.setCellValueFactory(new PropertyValueFactory<SaleItem, String>("itemName"));
		colItemPrice.setCellValueFactory(new PropertyValueFactory<SaleItem, Double>("price"));
		colItemQty.setCellValueFactory(new PropertyValueFactory<SaleItem, Integer>("qty"));
		colItemTotal.setCellValueFactory(new PropertyValueFactory<SaleItem, Double>("itemTotal"));
		colItemStatus.setCellValueFactory(new PropertyValueFactory<SaleItem, Boolean>("status"));


	}

	
	
	
	
	private LocalDate getStartDate()
	{
		LocalDate start = dateStart.getValue();
		
		return start;
		
	}
	
	
	private LocalDate getEndDate()
	{
		LocalDate end = dateEnd.getValue();
		
		return end;
	}
	
	private void getTotal() {
		double total_count = 0;
		double total_tax_count = 0;
		double total_disc_count = 0;
		double grand_total_count = 0;
		

		for (int rowIndex = 0; rowIndex < tblSales.getItems().size(); rowIndex++) {
			double total = col_TotalAmt.getCellData(rowIndex);
			double tax = col_TaxPercent.getCellData(rowIndex);
			double disc = col_Discount.getCellData(rowIndex);
			double grand = col_GrandTotal.getCellData(rowIndex);
			
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
	        	
		lbl_total.setText("" + totalAmount);
		lbl_total_tax.setText("" +taxAmount);
		lbl_total_discount.setText("" + discountAmount);
		lbl_grand_total.setText("" +grandTotalAmount);
	}
	
	public void loadItem(int saleId)
	{
		tblItem.getItems().clear();
		ObservableList<SaleItem> list = SaleItemDAO.getSaleItem(saleId);
		if(list!=null)
		{
			loadSaleItem();
			tblItem.setItems(list);
			
		}
	}
	
}
