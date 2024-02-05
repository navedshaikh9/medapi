package Stock;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import com.restaurant.DAO.UnitDAO;
import com.restaurant.Entity.Unit;
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
import javafx.scene.layout.AnchorPane;

public class UnitController implements Initializable {

	UnitDAO udao = new UnitDAO();
	AllNotifications allNotifi = new AllNotifications();

	int unitId = 0;
	private String unit_name=null;
	private boolean status;
	
	 @FXML
	    private CheckBox active;


	    @FXML
	    private TableColumn<Unit, String> col_Unit;

	    @FXML
	    private TableColumn<Unit, Boolean> col_active;

	    @FXML
	    private TableColumn<Unit, Integer> col_id;
	    
	    @FXML
	    private TableColumn<Unit, Integer> srNo;

	    @FXML
	    private AnchorPane departmentPane;

	    @FXML
	    private TableView<Unit> tblUnit;

	    @FXML
	    private JFXTextField txtUnit;


	@FXML
	void getSelectedRcord(MouseEvent event) {
		int index = tblUnit.getSelectionModel().getSelectedIndex();
		if (index >= 0) {
			txtUnit.setText(col_Unit.getCellData(index).toString());
			active.setSelected(col_active.getCellData(index));
			unitId = col_id.getCellData(index);
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
			if(unitId!=0)
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
			if(unitId!=0)
			{
				delete();
			}
			
		}
    }
//-------------------------------------------------------------------------------------------------------------	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		showCategory();
	}
	
	private void showCategory() {
		ObservableList<Unit> list = udao.getAllUnit();
		if(list!=null)
		{
		srNo.setCellValueFactory(cellData -> new SimpleIntegerProperty(tblUnit.getItems().indexOf(cellData.getValue()) + 1).asObject());	
		col_id.setCellValueFactory(new PropertyValueFactory<Unit, Integer>("unitId"));
		col_Unit.setCellValueFactory(new PropertyValueFactory<Unit, String>("unitName"));
		col_active.setCellValueFactory(new PropertyValueFactory<Unit, Boolean>("status"));
		tblUnit.setItems(list);
		}
	}

	private void clearAllValues()
	{
		txtUnit.clear();
		active.setSelected(false);
	}

	
	private void save()
	{
			unit_name = txtUnit.getText().trim();
			status = active.isSelected();
			
			if(unit_name.isEmpty() || unit_name==null)
			{
				AllNotifications.error("Unit Management", "Enter Unit Name", "");
				return;
			}
			try {
				
				boolean st = udao.addUnit(unit_name, status);
				if (st == true) {
					allNotifi.showNotification("Unit Management", "Unit Added Successfully");
					clearAllValues();
					showCategory();
				} else {
					allNotifi.error("Unit Management", "Unit Not Added", "");
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		
	}
	
	private void update()
	{
		unit_name = txtUnit.getText().trim();
		status = active.isSelected();
		
		if(unit_name.isEmpty() || unit_name==null)
		{
			AllNotifications.error("Unit Management", "Enter Unit Name", "");
			return;
		}
		try {
			
			if (unitId != 0) {
				Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Unit Management", "Are You Sure You want to Update this Unit", "");
				if (conf.get() == ButtonType.OK) {
					
					boolean st = udao.updateUnit(unitId, unit_name, status);
					if (st == true) {
						allNotifi.showNotification("Unit Management", "Unit Updated Successfully");
						clearAllValues();
						showCategory();
					} else {
						allNotifi.error("Unit Management", "Unit Not Updated", "");
					}
				}
			}
			else
			{
				allNotifi.error("Unit Management", "Please Select Unit", "");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void delete()
	{
		if (unitId != 0) {
			Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Unit Management", "Are You Sure You want to Delete this Unit", "");
			if (conf.get() == ButtonType.OK) {
					boolean status = udao.deleteUnit(unitId);
				if (status == true) {
					allNotifi.showNotification("Unit Management", "Unit Deleted Successfully");
					clearAllValues();
					showCategory();
				} else {
					allNotifi.error("Unit Management", "Unit Not Deleted", "");
				}
			}
		}
		else
		{
			allNotifi.error("Unit Management", "Please Select Unit", "");
		}
	
	}
}
