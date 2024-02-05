package paymentManagment;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import com.restaurant.DAO.PaymentMethodDAO;
import com.restaurant.Entity.PaymentMethod;
import AllNotivications.AllNotifications;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class PaymentController implements Initializable {

	AllNotifications allNotifi = new AllNotifications();
	PaymentMethodDAO da = new PaymentMethodDAO();
	
	int paymentId = 0;
	private String paymentMethod=null;
	private String upiId = null;
	private boolean upiIdStatus= false;
	private boolean status;
	
    @FXML
    private CheckBox active;
    
    @FXML
    private CheckBox chbUPIStatus;

    @FXML
    private TableColumn<PaymentMethod, Boolean> col_active;
    
    @FXML
    private TableColumn<PaymentMethod, String> colUPIId;

    @FXML
    private TableColumn<PaymentMethod, Boolean> colUPIStatus;

    @FXML
    private TableColumn<PaymentMethod, String> col_paymentMethod;

    @FXML
    private TableColumn<PaymentMethod, Integer> srNo;
    
    @FXML
    private TableColumn<PaymentMethod, Integer> col_id;

    @FXML
    private TableView<PaymentMethod> tblPaymentMethod;

    @FXML
    private TextField txtPayment;

    @FXML
    private TextField txtUPIId;

	@FXML
	void getSelectedRcord(MouseEvent event) {
		int index = tblPaymentMethod.getSelectionModel().getSelectedIndex();
		if (index >= 0) {
			txtPayment.setText(col_paymentMethod.getCellData(index).toString());
			active.setSelected(col_active.getCellData(index));
			paymentId = col_id.getCellData(index);
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
			if(paymentId!=0)
			{
				update();
			}
			else
			{
				save();
			}
		}
    }
	
	@FXML
    void onTableKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.DELETE) {
			if(paymentId!=0)
			{
				delete();
			}
			
		}
    }
//-------------------------------------------------------------------------------------------------------------	

	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		loadPaymentMethodTbl();
	}
	
	private void loadPaymentMethodTbl() {
		ObservableList<PaymentMethod> list = da.getAllPaymentMethod();
		if(list!=null)
		{
		srNo.setCellValueFactory(cellData -> new SimpleIntegerProperty(tblPaymentMethod.getItems().indexOf(cellData.getValue()) + 1).asObject());
		col_id.setCellValueFactory(new PropertyValueFactory<PaymentMethod, Integer>("paymentId"));
		col_paymentMethod.setCellValueFactory(new PropertyValueFactory<PaymentMethod, String>("paymentMethod"));
		colUPIId.setCellValueFactory(new PropertyValueFactory<PaymentMethod, String>("upiId"));
		colUPIStatus.setCellValueFactory(new PropertyValueFactory<PaymentMethod, Boolean>("upiIdStatus"));
		col_active.setCellValueFactory(new PropertyValueFactory<PaymentMethod, Boolean>("status"));
		tblPaymentMethod.setItems(list);
		}
	}

	private void clearAllValues()
	{
		txtPayment.clear();
		active.setSelected(false);
	}
	
	private void save()
	{
		paymentMethod = txtPayment.getText().trim();
		upiId = txtUPIId.getText().trim();
		upiIdStatus = chbUPIStatus.isSelected();
			status = active.isSelected();
			
			if(paymentMethod.isEmpty() || paymentMethod==null)
			{
				AllNotifications.error("Payment Method Management", "Enter Payment Method", "");
				return;
			}
			
			try {
				
				boolean s1 = da.addPaymentMethod(paymentMethod, upiId, upiIdStatus, status);
				if (s1 == true) {
					allNotifi.showNotification("Payment Method Management", "Payment Method Added Successfully");
					clearAllValues();
					loadPaymentMethodTbl();
				} else {
					allNotifi.error("Payment Method Management", "Payment Method Not Added", "");
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		
	}
	
	private void update()
	{
		paymentMethod = txtPayment.getText().trim();
		upiId = txtUPIId.getText().trim();
		upiIdStatus = chbUPIStatus.isSelected();
		status = active.isSelected();
		
		if(paymentMethod.isEmpty() || paymentMethod==null)
		{
			AllNotifications.error("Payment Method Management", "Enter Payment Method", "");
			return;
		}
		
		try {
			
			if (paymentId != 0) {
				Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Payment Method Management", "Are You Sure You want to Update this Payment Method", "");
				if (conf.get() == ButtonType.OK) {
					boolean s1 = da.updatePaymentMethod(paymentId, paymentMethod, upiId, upiIdStatus, status);
					if (s1 == true) {
						allNotifi.showNotification("Payment Method Management", "Payment Method Updated Successfully");
						clearAllValues();
						loadPaymentMethodTbl();
					} else {
						allNotifi.error("Payment Method Management", "Payment Method Not Updated", "");
					}
				}
			}
			else
			{
				allNotifi.error("Payment Method Management", "Please Select Payment Method", "");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	private void delete()
	{
		if (paymentId != 0) {
			Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Payment Method Management", "Are You Sure You want to Delete this Payment Method", "");
			if (conf.get() == ButtonType.OK) {
				boolean status = da.deletePaymentMethod(paymentId);
				if (status == true) {
					allNotifi.showNotification("Payment Method Management", "Payment Method Deleted Successfully");
					clearAllValues();
					loadPaymentMethodTbl();
				} else {
					allNotifi.error("Payment Method Management", "Payment Method Not Deleted", "");
				}
			}
		}
		else
		{
			allNotifi.error("Payment Method Management", "Please Select Payment Method", "");
		}
	
	}

}
