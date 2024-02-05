package TableManagment;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import com.restaurant.DAO.TableManagmentDAO;
import com.restaurant.Entity.HotelTable;
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
public class TableManagmentController implements Initializable {
	
	AllNotifications allNotifi = new AllNotifications();
	TableManagmentDAO tblDAO = new TableManagmentDAO();
	
	int tblID = 0;
	private String table_name;
	private boolean status;
	
	@FXML
    private CheckBox active;

    @FXML
    private TableColumn<HotelTable, String> col_Table_name;

    @FXML
    private TableColumn<HotelTable, Boolean> col_active;

    @FXML
    private TableColumn<HotelTable, Integer> srNo;
    @FXML
    private TableColumn<HotelTable, Integer> col_id;

    @FXML
    private AnchorPane departmentPane;

    @FXML
    private TableView<HotelTable> tblTableManagment;

    @FXML
    private JFXTextField txtTable;

    @FXML
    void btnAdd(ActionEvent event) {
    	save();
    }

    @FXML
    void btnDelete(ActionEvent event) {
    	delete();
    }

    @FXML
    void btnUpdate(ActionEvent event) {
    	update();
    }
    
    @FXML
    void onKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			if(tblID!=0)
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
			if(tblID!=0)
			{
				delete();
			}
			
		}
    }
    
    @FXML
    void btn_import(ActionEvent event) {
    	tblDAO.importTable();
    	loadTAble();
    }


    @FXML
    void getSelectedRcord(MouseEvent event) {
    	int index = tblTableManagment.getSelectionModel().getSelectedIndex();
		if (index >= 0) {
			txtTable.setText(col_Table_name.getCellData(index).toString());
			active.setSelected(col_active.getCellData(index));
			tblID = col_id.getCellData(index);
		} else {
			return;
		}
    }
    
    private void loadTAble(){
		ObservableList<HotelTable> list = tblDAO.getAllTable();
		if(list!=null)
		{
		srNo.setCellValueFactory(cellData -> new SimpleIntegerProperty(tblTableManagment.getItems().indexOf(cellData.getValue()) + 1).asObject());	
		col_id.setCellValueFactory(new PropertyValueFactory<HotelTable, Integer>("tableId"));
		col_Table_name.setCellValueFactory(new PropertyValueFactory<HotelTable, String>("tableName"));
		col_active.setCellValueFactory(new PropertyValueFactory<HotelTable, Boolean>("status"));
		tblTableManagment.setItems(list);
		}
	}

    private void clearAllValues()
	{
    	tblID = 0;
    	txtTable.clear();
		active.setSelected(false);
	}



	public void initialize(URL arg0, ResourceBundle arg1) {
		loadTAble();
		
	}
	
	private void save()
	{	
		table_name = txtTable.getText().trim();
		status = active.isSelected();
		
		if(table_name.isEmpty() || table_name==null)
		{
			AllNotifications.error("Table Management", "Enter Table", "");
			return;
		}
		try {
			
			boolean st1 = tblDAO.addTable(table_name, status);
			if (st1 == true) {
				allNotifi.showNotification("Table Management", "Table Added Successfully");
				clearAllValues();
				loadTAble();
			} else {
				allNotifi.error("Table Management", "Table Not Added", "");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
			
		
	}
	
	
	private void update()
	{
		table_name = txtTable.getText().trim();
		status = active.isSelected();
		
		if(table_name.isEmpty() || table_name==null)
		{
			AllNotifications.error("Table Managment", "Enter Table", "");
			return;
		}
		try {
			
			if (tblID != 0) {
				 Optional<ButtonType> conf = AllNotifications.ConfirmationNotify("Table Management",
							"Are You Sure you want to Update This Table", "");
					if (conf.get() == ButtonType.OK) {
					
					boolean st1 = tblDAO.updateTable(tblID, table_name, status);
					if (st1 == true) {
						allNotifi.showNotification("Table Management", "Table Updated Successfully");
						clearAllValues();
						loadTAble();
					} else {
						allNotifi.error("Table Management", "Table Not Updated", "");
					}
				}
			}
			else
			{
				allNotifi.error("Table Management", "Select Table", "");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	private void delete()
	{
		if (tblID != 0) {
			Optional<ButtonType> conf = AllNotifications.ConfirmationNotify("Table Management",
					"Are You Sure you want to Delete This Table", "");
			if (conf.get() == ButtonType.OK) {
				boolean status = tblDAO.deleteTable(tblID);
				if (status == true) {
					allNotifi.showNotification("Table Management", "Table Deleted Successfully");
					clearAllValues();
					loadTAble();
				} else {
					allNotifi.error("Table Management", "Table Not Deleted", "");
				}
			}
		}
		else
		{
			allNotifi.error("Table Management", "Select Table", "");
		}
	}


}
