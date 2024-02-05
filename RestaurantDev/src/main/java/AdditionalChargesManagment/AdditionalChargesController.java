package AdditionalChargesManagment;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import com.restaurant.DAO.AdditionalChargesDAO;
import com.restaurant.Entity.AdditionalCharges;
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
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import com.jfoenix.controls.JFXTextField;

public class AdditionalChargesController implements Initializable {

	AllNotifications allNotifi = new AllNotifications();
	AdditionalChargesDAO ac = new AdditionalChargesDAO();
		

    @FXML
    private CheckBox chbChargesStatus;

    @FXML
    private TableColumn<AdditionalCharges, Double> col_Amount;

    @FXML
    private TableColumn<AdditionalCharges, String> col_ChargesName;

    @FXML
    private TableColumn<AdditionalCharges, Integer> col_id;
    
    @FXML
    private TableColumn<AdditionalCharges, Integer> srNo;

    @FXML
    private TableColumn<AdditionalCharges, Boolean> col_status;

    @FXML
    private TableView<AdditionalCharges> tblCharges;

    @FXML
    private JFXTextField txtChargesName;

    @FXML
    private JFXTextField txtChargesAmount;
    
    private int id = 0;
 	private String chargesName=null;
	private double charges=0;
	private boolean status;
        
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
			if(id!=0)
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
			if(id!=0)
			{
				delete();
			}
			
		}
    }

	@FXML
	private void getSelectedRow(MouseEvent event) {
		int index = tblCharges.getSelectionModel().getSelectedIndex();
		if (index >= 0) {
			txtChargesName.setText(col_ChargesName.getCellData(index).toString());
			txtChargesAmount.setText(col_Amount.getCellData(index).toString());
			chbChargesStatus.setSelected(col_status.getCellData(index));
			id = col_id.getCellData(index);

		} else {
			return;
		}
	}


	public void initialize(URL arg0, ResourceBundle arg1) {
		loadDiscountTbl();
		setIntegerTextFormat();
	}

// ---------------------------------------------
	
	private void clearFileds()
	{
		txtChargesName.clear();
		txtChargesAmount.clear();
		chbChargesStatus.setSelected(false);
		id=0;
	}

	private void loadDiscountTbl() {
	
		ObservableList<AdditionalCharges> list = ac.getAllCharges();
		
		if(list!=null)
		{
			srNo.setCellValueFactory(cellData -> new SimpleIntegerProperty(tblCharges.getItems().indexOf(cellData.getValue()) + 1).asObject());
			col_id.setCellValueFactory(new PropertyValueFactory<>("chargesId"));
			col_ChargesName.setCellValueFactory(new PropertyValueFactory<>("chargesName"));
			col_Amount.setCellValueFactory(new PropertyValueFactory<>("charges"));
			col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
			
			tblCharges.getColumns().setAll(srNo, col_id, col_ChargesName, col_Amount, col_status);

			
			tblCharges.setItems(list);
	}
		}
	
	
	private void save()
	{
		chargesName = txtChargesName.getText().trim();
		String charge = txtChargesAmount.getText().trim();
		 status = chbChargesStatus.isSelected();
		 
		 if(chargesName.trim().isEmpty() || chargesName==null)
		 {
			 chargesName = "Charges";
		 }
		 if(charge.trim().isEmpty() || charge==null || !charge.matches("-?\\d+(\\.\\d+)?"))
		 {
			 AllNotifications.error("Additional Charges", "Enter Charges", "");
				return;
		 }
		 try {
			 
			 charges = Double.parseDouble(charge);
			 
			 boolean st1 = ac.addCharges(chargesName, charges, status);
				if (st1 == true) {
					loadDiscountTbl();
					clearFileds();
					AllNotifications.showNotification("Additional Charges", "Additional Charges Added Successfully");

				} else {
					allNotifi.error("Additional Charges", "Additional Charges Not Added", "");
				}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		 
		
	}
	
	private void update()
	{
		
		chargesName = txtChargesName.getText().trim();
		String charge = txtChargesAmount.getText().trim();
		 status = chbChargesStatus.isSelected();
		 if(chargesName.trim().isEmpty() || chargesName==null)
		 {
			 chargesName = "Charges";
		 }
		 if(charge.trim().isEmpty() || charge==null || !charge.matches("-?\\d+(\\.\\d+)?"))
		 {
			 AllNotifications.error("Additional Charges", "Enter Charges", "");
				return;
		 }
		 try {
			 
			 if (id != 0) {
					Optional<ButtonType> conf = AllNotifications.ConfirmationNotify("Additional Charges",
							"Are You Sure you want to Update This Charges", "");
					if (conf.get() == ButtonType.OK) {
					
						charges = Double.parseDouble(charge);
						
						boolean st1 = ac.update(id, chargesName, charges,status);
						if (st1 == true) {
							loadDiscountTbl();
							clearFileds();
							AllNotifications.showNotification("Additional Charges", "Additional Charges Updated Successfully");
						} else {
							allNotifi.error("Additional Charges", "Additional Charges Not Updated", "");
						}
				} 
			 }
			 else {
					allNotifi.error("Additional Charges", "Please Select Additional Charges", "");
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		 
		
	}
	
	private void delete()
	{
		if (id != 0) {
			Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Additional Charges",
					"Are You Sure you want to Delete This Charges", "");
			if (conf.get() == ButtonType.OK) {
				boolean st1 =ac.delete(id);
				if (st1 == true) {
					loadDiscountTbl();
					clearFileds();
					AllNotifications.showNotification("Additional Charges", "Additional Charges Deleted Successfully");

				} else {
					allNotifi.error("Additional Charges", "Additional Charges Not Deleted", "");
				}
			}
		} else {
			allNotifi.error("Additional Charges", "Please Select Additional Charges", "");
		}

	}
	
	public TextFormatter<String> createIntegerTextFormatter() {
		TextFormatter<String> integerTextFormatter = new TextFormatter<>(change -> {
			if (!change.isContentChange()) {
				return change;
			}

			if (!change.getControlNewText().matches("-?\\d*\\.?\\d*")) { // matches("-?\\d*\\.?\\d*")
				// Replace this line with your actual notification mechanism
				AllNotifications.error("Additional Charges", "Please Enter Number Only", "");

				return null; // Reject the change if it contains non-integer characters
			}

			return change;
		});

		return integerTextFormatter;
	}

	private void setIntegerTextFormat() {
		TextFormatter<String> formatter1 = createIntegerTextFormatter();

		txtChargesAmount.setTextFormatter(formatter1);

		

	}
}
