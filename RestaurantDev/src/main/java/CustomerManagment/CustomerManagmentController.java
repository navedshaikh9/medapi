package CustomerManagment;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import com.restaurant.DAO.CustomerDAO;
import com.restaurant.DAO.SaleDAO;
import com.restaurant.Entity.Customer;
import com.restaurant.Entity.Sale;
import AllNotivications.AllNotifications;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.CheckBox;

public class CustomerManagmentController implements Initializable {

// Declear Class
	AllNotifications allNotifi = new AllNotifications();
	CustomerDAO cuDAO = new CustomerDAO();

	private final ObservableList<Customer> data = cuDAO.getAllCustomer();
	
	private static String billNo = null;

	@FXML
	private CheckBox chbActive;

	@FXML
	private TableColumn<Customer, Integer> col_bill_id;

	@FXML
	private TableColumn<Customer, Boolean> colActive;

	@FXML
	private TableColumn<Customer, String> colAddress;

	@FXML
	private TableColumn<Customer, String> colEmail;

	@FXML
	private TableColumn<Customer, Integer> srNo;

	@FXML
	private TableColumn<Customer, Integer> col_customer_id;

	@FXML
	private TableColumn<Customer, Long> col_Contact;

	@FXML
	private TableColumn<Customer, String> col_CustomerName;

	@FXML
	private TableColumn<Customer, Double> col_pending_amt;

	@FXML
	private TableColumn<Customer, Double> col_total_amt;

	@FXML
	private AnchorPane customerPane;

	@FXML
	private TableView<Customer> tblCustomer;

	@FXML
	private TextField txtAddress;

	@FXML
	private TextField txtContact;

	@FXML
	private TextField txtCustomerName;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txt_bill_no;

	@FXML
	private TextField txt_pay_amt;

	@FXML
	private TextField txtSearch;

	private int customerId = 0;
	private int saleId = 0;
	private String customerName = null;
	private Long contact;
	private String email = null;
	private String address = null;
	private double totalAmount = 0;
	private double pendingAmount = 0;
	private double payAmt = 0;
	private boolean status;

	@FXML
	private void getSelectedRecord(MouseEvent event) {

		int index = tblCustomer.getSelectionModel().getSelectedIndex();
		if (index >= 0) {
			txt_bill_no.setText(col_bill_id.getCellData(index).toString());
			txtCustomerName.setText(col_CustomerName.getCellData(index).toString());
			txtContact.setText(col_Contact.getCellData(index).toString());
			txtEmail.setText(colEmail.getCellData(index).toString());
			txtAddress.setText(colAddress.getCellData(index).toString());
			chbActive.setSelected(colActive.getCellData(index));

			customerId = col_customer_id.getCellData(index);

		} else {
			return;
		}

	}

	@FXML
	private void btnAdd(ActionEvent event) {
		save();

	}

	@FXML
	private void btnDelete(ActionEvent event) {
		delete();

	}

	@FXML
	private void btnUpdate(ActionEvent event) {
		update();
	}

	@FXML
	void onKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			if (customerId != 0) {
				update();
			} else {
				save();
			}
		}
	}

	@FXML
	void onTableKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.DELETE) {
			if (customerId != 0) {
				delete();
			}

		}
	}
	// -----------------------------------------------------------------------------------------------

	public void initialize(URL arg0, ResourceBundle arg1) {
		loadCustomerTbl();
		search();
	}

	private void loadCustomerTbl() {
		ObservableList<Customer> list = cuDAO.getAllCustomer();
		if (list != null) {
			srNo.setCellValueFactory(cellData -> new SimpleIntegerProperty(tblCustomer.getItems().indexOf(cellData.getValue()) + 1).asObject()); 
			col_customer_id.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("customerId"));
			col_bill_id.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("saleId"));
			col_CustomerName.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
			col_Contact.setCellValueFactory(new PropertyValueFactory<Customer, Long>("contact"));
			colEmail.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));
			colAddress.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
			col_total_amt.setCellValueFactory(new PropertyValueFactory<Customer, Double>("totalAmount"));
			col_pending_amt.setCellValueFactory(new PropertyValueFactory<Customer, Double>("pendingAmount"));
			colActive.setCellValueFactory(new PropertyValueFactory<Customer, Boolean>("status"));

			tblCustomer.setItems(list);
		}
	}

	private void clearFileds() {
		txt_bill_no.clear();
		txtCustomerName.clear();
		txtContact.clear();
		txtEmail.clear();
		txtAddress.clear();
		txt_pay_amt.clear();
		chbActive.setSelected(false);
		customerId = 0;

	}

	private void save() {
		String billno = txt_bill_no.getText().trim();
		customerName = txtCustomerName.getText().trim();
		String contactno = txtContact.getText().trim();
		email = txtEmail.getText().trim();
		address = txtAddress.getText().trim();
		String amt = txt_pay_amt.getText().trim();
		status = chbActive.isSelected();

		if (billno.isEmpty() || billno == null || billno.startsWith("0") || !billno.matches("-?\\d*")) {
			AllNotifications.error("Customer Management", "Enter Customer Bill No.", "");
			return;
		}
		if (contactno.isEmpty() || contactno == null || !contactno.matches("-?\\d*")) {
			AllNotifications.error("Customer Management", "Enter Customer Contact Number", "");
			return;
		}
		if (!amt.matches("-?\\d+(\\.\\d+)?")) {
			amt="0";
		}

		try {

			contact = Long.parseLong(contactno);
			saleId = Integer.parseInt(billno);
			payAmt = Double.parseDouble(amt);
			
			 double total = getTotalAmt(saleId);
			double pendingAmt =  total - payAmt;
			
			if(payAmt==0)
			{
				pendingAmt=0;
			}
			
			Sale sale = SaleDAO.openBill(saleId);
		if(sale!=null)
		{
			if(saleId==sale.getSaleId())
			{
				if(payAmt <= total)
				{
				boolean st = cuDAO.addCustomer(saleId,customerName, contact, email, address,total,pendingAmt, status);
				if (st == true) {
					allNotifi.showNotification("Customer Management", "Customer Details Save Successfully");
					loadCustomerTbl();
					clearFileds();

				} else {
					allNotifi.error("Customer Management", "Customer Details Not Save Please Try Again","");
				}
				}
				else
				{
					allNotifi.error("Customer Management", "Please Check Total Amount and Enter Correct Amount in Pay Amount","");
				}
			}else {
				allNotifi.error("Customer Management", "Enter Correct Bill No","");
			}
					
		}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void update() {
		String billno = txt_bill_no.getText().trim();
		customerName = txtCustomerName.getText().trim();
		String contactno = txtContact.getText().trim();
		email = txtEmail.getText().trim();
		address = txtAddress.getText().trim();
		String amt = txt_pay_amt.getText().trim();
		status = chbActive.isSelected();

		if (billno.isEmpty() || billno == null || billno.startsWith("0") || !billno.matches("-?\\d*")) {
			AllNotifications.error("Customer Management", "Enter Customer Bill No.", "");
			return;
		}
		if (customerName.isEmpty() || customerName == null) {

		}
		if (contactno.isEmpty() || contactno == null) {
			AllNotifications.error("Customer Management", "Enter Customer Contact Number", "");
			return;
		}
		if (email.isEmpty() || email == null) {

		}
		if (address.isEmpty() || address == null) {

		}
		if (amt.isEmpty() || amt == null || amt.startsWith("0")) {

			AllNotifications.error("Customer Management", "Enter Customer Pay Amount", "");
			return;
		}

		try {

			if (customerId != 0) {

				Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Customer Management",
						"Are You Sure You want to Update this Customer Details", "");

				if (conf.get() == ButtonType.OK) {

					contact = Long.parseLong(contactno);
					saleId = Integer.parseInt(billno);
					payAmt = Double.parseDouble(amt);

					double total = getTotalAmt(saleId);
					double lastPendingAmt = getPendingAmt(customerId);
					double pendingAmt = lastPendingAmt - payAmt;

					if (payAmt <= lastPendingAmt) {
						boolean st = cuDAO.updateCustomer(customerId, saleId, customerName, contact, email, address,
								total, pendingAmt, status);
						if (st == true) {
							allNotifi.showNotification("Customer Management", "Customer Detials Updated Successfully");
							loadCustomerTbl();
							clearFileds();

						} else {
							allNotifi.error("Customer Management", "Customer Detials Not Updated", "");
						}
					} else {
						allNotifi.error("Customer Management",
								"Please Check Total Amount and Enter Correct Amount in Pay Amount", "");
					}

				}
			} else {
				allNotifi.error("Customer Management", "Please Select Customer", "");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void delete() {
		if (customerId != 0) {
			Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Customer Management",
					"Are You Sure You want to delete this Customer Details", "");

			if (conf.get() == ButtonType.OK) {
				boolean st = cuDAO.deleteCustomer(customerId);
				if (st == true) {
					loadCustomerTbl();
					allNotifi.showNotification("Customer Management", "Customer Detials Delete Successfully");
					clearFileds();
				} else {
					allNotifi.error("Customer Management", "Customer Detials Not Deleted", "");
				}

			}
		} else {
			allNotifi.error("Customer Management", "Please Select Customer", "");

		}
	}

	private double getTotalAmt(int bill) {
		double total = 0;
		Sale sale = SaleDAO.openBill(bill);

		if (sale != null) {
			total = sale.getGrandTotal();
		} 

		return total;
	}

	private double getPendingAmt(int customerID) {
		double amt = 0;
		Customer customer = cuDAO.getCustomer(customerID);

		if (customer != null) {
			amt = customer.getPendingAmount();
		} else {
			allNotifi.error("Customer Detials", "Please Enter Correct Customer Bill No", "");
		}

		return amt;
	}

	private void search() {
		txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			// Call a method to update the table based on the search text
			filterData(newValue);
		});
	}

	private void filterData(String searchText) {
		ObservableList<Customer> filteredData = FXCollections.observableArrayList();
		if (data != null) {
			for (Customer item : data) {
				if (item.getCustomerName().toLowerCase().contains(searchText.toLowerCase())) {
					filteredData.add(item);
				}
			}

			tblCustomer.setItems(filteredData);
		}
	}
	
}
