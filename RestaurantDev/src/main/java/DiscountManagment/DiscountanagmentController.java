package DiscountManagment;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import com.restaurant.DAO.DiscountDAO;
import com.restaurant.Entity.Discount;
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
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class DiscountanagmentController implements Initializable {

	AllNotifications allNotifi = new AllNotifications();
	DiscountDAO disDAO = new DiscountDAO();

	int discountId = 0;
	private String discountName = null;
	private double discountPrice = 0;
	private double applyDiscount = 0;
	private boolean status;

	@FXML
	private CheckBox chaDisAppli;

	@FXML
	private TableColumn<Discount, Boolean> col_Active;

	@FXML
	private TableColumn<Discount, Double> col_ApplyDiscountAmt;

	@FXML
	private TableColumn<Discount, Double> col_DiscountAmt;

	@FXML
	private TableColumn<Discount, String> col_OfferName;

	   @FXML
	    private TableColumn<Discount, Integer> srNo;
	   
	@FXML
	private TableColumn<Discount, Integer> col_id;

	@FXML
	private TableView<Discount> tblDiscountManagment;

	 @FXML
	    private JFXTextField txtOffer;

	    @FXML
	    private JFXTextField txtDiscountAmt;

	    @FXML
	    private JFXTextField txtApplyDisAmt;

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
			if(discountId!=0)
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
			if(discountId!=0)
			{
				delete();
			}
			
		}
    }

	@FXML
	private void getSelectedRow(MouseEvent event) {
		int index = tblDiscountManagment.getSelectionModel().getSelectedIndex();
		if (index >= 0) {
			txtOffer.setText(col_OfferName.getCellData(index).toString());
			txtDiscountAmt.setText(col_DiscountAmt.getCellData(index).toString());
			txtApplyDisAmt.setText(col_ApplyDiscountAmt.getCellData(index).toString());
			chaDisAppli.setSelected(col_Active.getCellData(index));
			discountId = col_id.getCellData(index);

		} else {
			return;
		}
	}

	public void initialize(URL arg0, ResourceBundle arg1) {
		loadDiscountTbl();
	}

	// ---------------------------------------------

	private void clearFileds() {
		txtOffer.clear();
		txtDiscountAmt.clear();
		txtApplyDisAmt.clear();
		chaDisAppli.setSelected(false);
		discountId = 0;
	}

	private void loadDiscountTbl() {

		ObservableList<Discount> list = disDAO.loadAllDescount();
		if (list != null) {
			srNo.setCellValueFactory(cellData -> new SimpleIntegerProperty(tblDiscountManagment.getItems().indexOf(cellData.getValue()) + 1).asObject());
			col_id.setCellValueFactory(new PropertyValueFactory<Discount, Integer>("discountId"));
			col_OfferName.setCellValueFactory(new PropertyValueFactory<Discount, String>("discountName"));
			col_DiscountAmt.setCellValueFactory(new PropertyValueFactory<Discount, Double>("discountPrice"));
			col_ApplyDiscountAmt.setCellValueFactory(new PropertyValueFactory<Discount, Double>("applyDiscount"));
			col_Active.setCellValueFactory(new PropertyValueFactory<Discount, Boolean>("status"));
			tblDiscountManagment.setItems(list);
		}
	}

	private void save() {
		discountName = txtOffer.getText().trim();
		String dis_amt = txtDiscountAmt.getText().trim();
		String applydisAmt = txtApplyDisAmt.getText().trim();
		status = chaDisAppli.isSelected();

		if (discountName.isEmpty() || discountName == null) {
			discountName = "Offer";
		}
		if (dis_amt.isEmpty() || dis_amt == null || !dis_amt.matches("-?\\d+(\\.\\d+)?")) {
			AllNotifications.error("Discount Management", "Enter Discount Amount", "");
			return;
		}
		if (applydisAmt.isEmpty() || applydisAmt == null || !applydisAmt.matches("-?\\d+(\\.\\d+)?")) {
			AllNotifications.error("Discount Management", "Enter Discount Applicable Amount", "");
			return;
		}
		try {

			discountPrice = Double.parseDouble(dis_amt);
			applyDiscount = Double.parseDouble(applydisAmt);

			boolean st1 = disDAO.addDiscount(discountName, discountPrice, applyDiscount, status);
			if (st1 == true) {
				loadDiscountTbl();
				clearFileds();
				AllNotifications.showNotification("Discount Management", "Discount Added Successfuly");

			} else {
				allNotifi.error("Discount Management", "Discount Not Added", "");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void update() {
		discountName = txtOffer.getText().trim();
		String dis_amt = txtDiscountAmt.getText().trim();
		String applydisAmt = txtApplyDisAmt.getText().trim();
		status = chaDisAppli.isSelected();

		if (discountName.isEmpty() || discountName == null) {
			discountName = "Offer";
		}
		if (dis_amt.isEmpty() || dis_amt == null || !dis_amt.matches("-?\\d+(\\.\\d+)?")) {
			AllNotifications.error("Discount Management", "Enter Discount Amount", "");
			return;
		}
		if (applydisAmt.isEmpty() || applydisAmt == null || !applydisAmt.matches("-?\\d+(\\.\\d+)?")) {
			AllNotifications.error("Discount Management", "Enter Discount Applicable Amount", "");
			return;
		}
		try {

		
		if (discountId != 0) {

			Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Discount Management",
					"Are You Sure You want to Update this Discount", "");

			if (conf.get() == ButtonType.OK) {

				discountPrice = Double.parseDouble(dis_amt);
				applyDiscount = Double.parseDouble(applydisAmt);

				boolean st1 = disDAO.updateDiscount(discountId, discountName, discountPrice, applyDiscount, status);
				if (st1 == true) {
					loadDiscountTbl();
					clearFileds();
					AllNotifications.showNotification("Discount Management", "Discount Updated Successfuly");

				} else {
					allNotifi.error("Discount Management", "Discount Not Updated", "");
				}
			}
		} else {
			allNotifi.error("Discount Management", "Please Select Discount", "");
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void delete() {
		if (discountId != 0) {
			Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Discount Management",
					"Are You Sure You want to Delete this Discount", "");

			if (conf.get() == ButtonType.OK) {
				boolean st1 = disDAO.deleteDiscount(discountId);
				if (st1 == true) {
					loadDiscountTbl();
					clearFileds();
					AllNotifications.showNotification("Discount Management", "Discount Deleted Successfuly");

				} else {
					allNotifi.error("Discount Management", "Discount Not Deleted", "");
				}
			}
		} else {
			allNotifi.error("Discount Management", "Please Select Discount", "");
		}

	}

	
}
