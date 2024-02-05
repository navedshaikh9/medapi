package BillManagment;

import java.net.URL;
import java.util.ResourceBundle;
import com.restaurant.DAO.BillFormateDAO;
import com.restaurant.Entity.BillFormate;
import AllNotivications.AllNotifications;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import com.jfoenix.controls.JFXTextField;

public class BillDetailsController implements Initializable {

	@FXML
	private Label lbAddress;

	@FXML
	private Label lbContact;

	@FXML
	private Label lbHotelName;

	@FXML
	private Label lbHotelType;

	@FXML
	private Label lbMessage;


    @FXML
    private JFXTextField txtHotelName;

    @FXML
    private JFXTextField txtHotelType;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtContact;

    @FXML
    private JFXTextField txtMessage;

	private int billFormatId=0;
	private String hotelName=null;
	private String hotelType=null;
	private String address=null;
	private String contact=null;
	private String message=null;
	private boolean status=false;
	

	 @FXML
	    void btnViewBillFormat(ActionEvent event) {
		 viewHotelDetails();
	    }
	 
	@FXML
	private void btnUpdateBillFormate(ActionEvent event) {

		
		
			hotelName = txtHotelName.getText().trim();
			 hotelType = txtHotelType.getText().trim();
			 address = txtAddress.getText().trim();
			contact = txtContact.getText().trim();
		 message = txtMessage.getText().trim();
		 
		 if (hotelName.trim().isEmpty() || hotelName == null) {
				AllNotifications.error("Bill Foramat", "Enter Restaurant Name", "");
				return;
			}
		 if (hotelType.trim().isEmpty() || hotelType == null) {
				AllNotifications.error("Bill Foramat", "Enter Restaurant Type", "");
				return;
			}
		 if (address.trim().isEmpty() || address == null) {
				AllNotifications.error("Bill Foramat", "Enter Restaurant Address", "");
				return;
			}
		 if (contact.trim().isEmpty() || contact == null) {
				AllNotifications.error("Bill Foramat", "Enter Restaurant Contact", "");
				return;
			}
		 if (message.trim().isEmpty() || message == null) {
				AllNotifications.error("Bill Foramat", "Enter Restaurant Message", "");
				return;
			}
		
		 
			BillFormate billFormate = BillFormateDAO.getHotelDetails();
			if (billFormate == null) {

				boolean st = BillFormateDAO.insertHotelDetails(hotelName, hotelType, address, contact, message);
				if (st == true) {
					loadHotelDetails();
				}

			} else {
				boolean st = BillFormateDAO.update(hotelName, hotelType, address, contact, message);

				if (st == true) {
					loadHotelDetails();
				}

			}
	}

	private void loadHotelDetails() {
		BillFormate billFormate = BillFormateDAO.getHotelDetails();
		if (billFormate != null) {

			lbHotelName.setText(billFormate.getHotelName());
			lbHotelType.setText(billFormate.getHotelType());
			lbAddress.setText(billFormate.getAddress());
			lbContact.setText("" + billFormate.getContact());
			lbMessage.setText(billFormate.getMessage());

		} else {
			lbHotelName.setText("");
			lbHotelType.setText("");
			lbAddress.setText("");
			lbContact.setText("");
			lbMessage.setText("");
		}

	}

	private void viewHotelDetails() {
		BillFormate billFormate = BillFormateDAO.getHotelDetails();
		if (billFormate != null) {

			txtHotelName.setText(billFormate.getHotelName());
			txtHotelType.setText(billFormate.getHotelType());
			txtAddress.setText(billFormate.getAddress());
			txtContact.setText(billFormate.getContact());
			txtMessage.setText(billFormate.getMessage());

		} else {
			txtHotelName.setText("");
			txtHotelType.setText("");
			txtAddress.setText("");
			txtContact.setText("");
			txtMessage.setText("");
		}

	}

	
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		loadHotelDetails();
	}

}
