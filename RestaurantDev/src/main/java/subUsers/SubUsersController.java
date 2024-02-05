package subUsers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import com.restaurant.DAO.EmployeeDAO;
import com.restaurant.DAO.EmpAccessDAO;
import com.restaurant.Entity.EmpAccess;
import com.restaurant.Entity.Employee;
import AllNotivications.AllNotifications;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

public class SubUsersController implements Initializable {

// Declear Class
	private final ObservableList<EmpAccess> data = EmpAccessDAO.getAllSubUsers();

	 @FXML
	    private CheckBox chbStatus;

	    @FXML
	    private ComboBox<String> chbUser;

	    @FXML
	    private TableColumn<EmpAccess, Integer> colEmployeeAccessId;

	    @FXML
	    private TableColumn<EmpAccess, String> colEmployeeName;

	    @FXML
	    private TableColumn<EmpAccess, String> colPassword;

	    @FXML
	    private TableColumn<EmpAccess, Boolean> colStatus;

	    @FXML
	    private TableColumn<EmpAccess, String> colUsername;

	    @FXML
	    private TableColumn<EmpAccess, Integer> srNo;

	    @FXML
	    private TableView<EmpAccess> tblUserAccess;

	    @FXML
	    private JFXTextField txtUsername;

	    @FXML
	    private JFXPasswordField txtPassword;

	    @FXML
	    private JFXPasswordField txtConfirmPassword;

	    @FXML
	    private JFXTextField txtSearch;

	private int empId = 0;
	private String employeeName = null;
	private String username = null;
	private String password = null;
	private String confirmPassword = null;
	private boolean status;

	@FXML
	private void getSelectedRecord(MouseEvent event) {
		
		int index = tblUserAccess.getSelectionModel().getSelectedIndex();
		
		if (index >= 0) {
			
			chbUser.setValue(colEmployeeName.getCellData(index).toString());
			txtUsername.setText(colUsername.getCellData(index).toString());
			txtPassword.setText(colPassword.getCellData(index).toString());
			chbStatus.setSelected(colStatus.getCellData(index));
		empId = colEmployeeAccessId.getCellData(index);

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
			if (empId != 0) {
				update();
			} else {
				save();
			}
		}
	}

	@FXML
	void onTableKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.DELETE) {
			if (empId != 0) {
				delete();
			}

		}
	}

	// -----------------------------------------------------------------------------------------------

	public void initialize(URL arg0, ResourceBundle arg1) {
			showEmployee();
		search();
		loadSubUser();
	}

	private void showEmployee() {
		ObservableList<EmpAccess> list = EmpAccessDAO.getAllSubUsers();
		if (list != null) {
			srNo.setCellValueFactory(
					cellData -> new SimpleIntegerProperty(tblUserAccess.getItems().indexOf(cellData.getValue()) + 1)
							.asObject());
			colEmployeeAccessId.setCellValueFactory(new PropertyValueFactory<EmpAccess, Integer>("subUserid"));
			colEmployeeName.setCellValueFactory(new PropertyValueFactory<EmpAccess, String>("userName"));
			colUsername.setCellValueFactory(new PropertyValueFactory<EmpAccess, String>("userId"));
			colPassword.setCellValueFactory(new PropertyValueFactory<EmpAccess, String>("password"));
			colStatus.setCellValueFactory(new PropertyValueFactory<EmpAccess, Boolean>("status"));

			tblUserAccess.setItems(list);
		}
	}

	private void clearFileds() {
		txtUsername.clear();
		txtPassword.clear();
		txtConfirmPassword.clear();
		chbStatus.setSelected(false);
	}

	private void save() {
		employeeName = chbUser.getValue();
		username = txtUsername.getText().trim();
		password = txtPassword.getText().trim();
		confirmPassword = txtConfirmPassword.getText().trim();
		status = chbStatus.isSelected();
		
		if (employeeName == null) {

			AllNotifications.error("Employee Access", "Enter Employee Name", "");
			return;
		}
		if (username == null || username.isEmpty() || username.contains(" ")) {
			AllNotifications.error("Employee Access", "Enter User Name Without Space", "");
			return;
		}
		if (password == null || password.isEmpty() || password.contains(" ") || password.length() < 6) {
			AllNotifications.error("Employee Access", "Enter User Password Without Space and Greter than 6 digit", "");
			return;
		}
		
		if (!password.equals(confirmPassword)) {
		    AllNotifications.error("Employee Access", "Passwords do not match", "");
		    return;
		}

		try {

			if(EmpAccessDAO.getDuplicatUser(username)==true)
			{
				
			boolean st = EmpAccessDAO.save(employeeName, username, password, status);
			if (st == true) {
				AllNotifications.showNotification("Employee Access", "Employee Detials Saved Successfully");
				showEmployee();
				clearFileds();

			} else {
				AllNotifications.error("Employee Access", "Employee Detials Not Saved", "");
			}
			}
			else
			{
				AllNotifications.error("Employee Access", "This Username is Already Exist Please try with another username", "");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void update() {
		employeeName = chbUser.getValue();
		username = txtUsername.getText().trim();
		password = txtPassword.getText().trim();
		confirmPassword = txtConfirmPassword.getText().trim();
		status = chbStatus.isSelected();
		
		if (employeeName == null) {

			AllNotifications.error("Employee Access", "Enter Employee Name", "");
			return;
		}
		if (username == null || username.isEmpty() || username.contains(" ")) {
			AllNotifications.error("Employee Access", "Enter User Name Without Space", "");
			return;
		}
		if (password == null || password.isEmpty() || password.contains(" ") || password.length() < 6) {
			AllNotifications.error("Employee Access", "Enter User Password Without Space and Greter than 6 digit", "");
			return;
		}
		
		if (!password.equals(confirmPassword)) {
		    AllNotifications.error("Employee Access", "Passwords do not match", "");
		    return;
		}
		try {

			if (empId != 0) {
				Optional<ButtonType> conf = AllNotifications.ConfirmationNotify("Employee Access",
						"Are You Sure You want to Update this Employee Details", "");

				if (conf.get() == ButtonType.OK) {

					boolean st = EmpAccessDAO.update(empId, employeeName, username, password, status);
					if (st == true) {
						AllNotifications.showNotification("Employee Access", "Employee Details Updated Successfully");
						showEmployee();
						clearFileds();

					} else {
						AllNotifications.error("Employee Access", "Employee Details Not Updated", "");
					}

				}
			} else {
				AllNotifications.error("Employee Access", "Please Select Employee Details", "");

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void delete() {
		if (empId != 0) {
			Optional<ButtonType> conf = AllNotifications.ConfirmationNotify("Employee Access",
					"Are You Sure You want to Delete this Employee Details", "");

			if (conf.get() == ButtonType.OK) {
				boolean st = EmpAccessDAO.delete(empId);
				if (st == true) {
					showEmployee();
					AllNotifications.showNotification("Employee Access", "Employee Delete Successfully");
					clearFileds();
				} else {
					AllNotifications.error("Employee Access", "Employee Details Not Deleted", "");
				}

			}
		} else {
			AllNotifications.error("Employee Access", "Please Select Employee Details", "");

		}
	}

	private void search() {
		txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			// Call a method to update the table based on the search text
			filterData(newValue);
		});
	}

	private void filterData(String searchText) {
		ObservableList<EmpAccess> filteredData = FXCollections.observableArrayList();
		if (data != null) {
			for (EmpAccess item : data) {
				if (item.getUserName().toLowerCase().contains(searchText.toLowerCase())) {
					filteredData.add(item);
				}
			}

			tblUserAccess.setItems(filteredData);
		}
	}
	
	
	private void loadSubUser() {
		ObservableList<Employee> list = EmployeeDAO.getAllEmployee();

		ObservableList data = FXCollections.observableArrayList();

		for (Employee d : list) {

			if(d.isStatus()==true)
			{
			data.add(d.getEmployeeName());
			}
		}

		chbUser.setItems(data);
		
	}
	
	

}
