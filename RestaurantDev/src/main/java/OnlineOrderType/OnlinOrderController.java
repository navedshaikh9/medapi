package OnlineOrderType;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import com.restaurant.DAO.OrderTypeDAO;
import com.restaurant.Entity.OrderType;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.input.MouseEvent;

public class OnlinOrderController implements Initializable {

	OrderTypeDAO otDAO = new OrderTypeDAO();
	AllNotifications allNotifi = new AllNotifications();

	int orderTypeId = 0;
	private String orderType = null;
	private boolean status;

	@FXML
	private CheckBox active;

	@FXML
	private TableColumn<OrderType, String> col_Order_type;

	@FXML
	private TableColumn<OrderType, Boolean> col_active;

	@FXML
	private TableColumn<OrderType, Integer> col_id;

	@FXML
    private TableColumn<OrderType, Integer> srNo;
	
	@FXML
	private TableView<OrderType> tblOnlineOrderType;

	 @FXML
	    private JFXTextField txtOrderType;


	@FXML
	void getSelectedRcord(MouseEvent event) {
		int index = tblOnlineOrderType.getSelectionModel().getSelectedIndex();
		if (index >= 0) {
			txtOrderType.setText(col_Order_type.getCellData(index).toString());
			active.setSelected(col_active.getCellData(index));
			orderTypeId = col_id.getCellData(index);
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
			if(orderTypeId!=0)
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
			if(orderTypeId!=0)
			{
				delete();
			}
			
		}
    }
//-------------------------------------------------------------------------------------------------------------	

	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		loadOrderTypeTbl();
	}

	private void loadOrderTypeTbl() {
		ObservableList<OrderType> list = otDAO.getAllOrderType();
		if (list != null) {
			srNo.setCellValueFactory(cellData -> new SimpleIntegerProperty(tblOnlineOrderType.getItems().indexOf(cellData.getValue()) + 1).asObject());
			col_id.setCellValueFactory(new PropertyValueFactory<OrderType, Integer>("orderTypeId"));
			col_Order_type.setCellValueFactory(new PropertyValueFactory<OrderType, String>("orderType"));
			col_active.setCellValueFactory(new PropertyValueFactory<OrderType, Boolean>("status"));
			tblOnlineOrderType.setItems(list);
		}
	}

	private void clearAllValues() {
		txtOrderType.clear();
		active.setSelected(false);
		orderTypeId = 0;
	}

	private void save() {
		orderType = txtOrderType.getText().trim();
		status = active.isSelected();

		if (orderType.isEmpty() || orderType == null) {
			AllNotifications.error("Order Type Management", "Enter Order Type", "");
			return;
		}

		try {

			boolean s1 = otDAO.addOrderType(orderType, status);
			if (s1 == true) {
				loadOrderTypeTbl();
				allNotifi.showNotification("Order Type Management", "Orde Type Added Successfully");
				clearAllValues();

			} else {
				allNotifi.error("Order Type Management", "Orde Type Not Added", "");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void update() {
		orderType = txtOrderType.getText().trim();
		status = active.isSelected();

		if (orderType.isEmpty() || orderType == null) {
			AllNotifications.error("Order Type Management", "Enter Order Type", "");
			return;
		}

		try {

			if (orderTypeId != 0) {
				Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Order Type Management",
						"Are You Sure You want to Update this Order Type", "");

				if (conf.get() == ButtonType.OK) {
					boolean s1 = otDAO.updateOrderType(orderTypeId, orderType, status);
					if (s1 == true) {
						loadOrderTypeTbl();
						allNotifi.showNotification("Order Type Management", "Orde Type Updated Successfully");
						clearAllValues();
					} else {
						allNotifi.error("Order Type Management", "Orde Type Not Updated", "");
					}
				}
			}

			else {
				allNotifi.error("Order Type Management", "Please Select Order Type", "");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void delete() {
		if (orderTypeId != 0) {
			Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Order Type Management",
					"Are You Sure You want to Delete this Order Type", "");
			if (conf.get() == ButtonType.OK) {
				boolean status = otDAO.deleteOrderType(orderTypeId);
				if (status == true) {
					loadOrderTypeTbl();
					allNotifi.showNotification("Order Type Management", "Orde Type Deleted Successfully");
					clearAllValues();
				} else {
					allNotifi.error("Order Type Management", "Orde Type Not Deleted", "");
				}
			}
		} else {
			allNotifi.error("Order Type Management", "Please Select Order Type", "");
		}

	}
}
