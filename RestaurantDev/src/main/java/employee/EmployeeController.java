package employee;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import com.restaurant.DAO.EmployeeDAO;
import com.restaurant.Entity.Employee;
import AllNotivications.AllNotifications;
import AllNotivications.CommenFunction;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import com.jfoenix.controls.JFXTextField;

public class EmployeeController implements Initializable {

// Declear Class
	EmployeeDAO employee = new EmployeeDAO();
	AllNotifications allNotifi = new AllNotifications();
	CommenFunction cf = new CommenFunction();

	private final ObservableList<Employee> data = employee.getAllEmployee();

	@FXML
	private AnchorPane EmployeePane;

	@FXML
	private CheckBox chbActive;

	@FXML
	private ComboBox<String> chb_shift;
	
	  @FXML
	    private ComboBox<String> chbRole;

	@FXML
	private TableColumn<Employee, Boolean> colActive;

	@FXML
	private TableColumn<Employee, Long> colEmployeeContact;

	@FXML
	private TableColumn<Employee, LocalDate> colEmployeeDateofJoining;

	@FXML
	private TableColumn<Employee, Integer> colEmployeeId;

	@FXML
	private TableColumn<Employee, Integer> srNo;

	@FXML
	private TableColumn<Employee, String> colEmployeeName;
	
    @FXML
    private TableColumn<Employee, String> colRole;

	@FXML
	private TableColumn<Employee, String> colShift;

	@FXML
	private DatePicker dateofjoining;

	@FXML
	private TableView<Employee> tblEmployee;

	  @FXML
	    private JFXTextField txtEmployeeName;

	    @FXML
	    private JFXTextField txtContact;
	    
	    @FXML
	    private JFXTextField txtSearch;
	    


	private int empId = 0;
	private String employeeName = null;
	private String role = null;
	private LocalDate joiningDate = null;
	private String shift = null;
	private Long contact;
	private boolean active;

	@FXML
	private void getSelectedRecord(MouseEvent event) {
		int index = tblEmployee.getSelectionModel().getSelectedIndex();
		if (index >= 0) {
			txtEmployeeName.setText(colEmployeeName.getCellData(index).toString());
			String d = colEmployeeDateofJoining.getCellData(index).toString();
			dateofjoining.setValue(cf.getLocalDate(d));
			chb_shift.setValue(colShift.getCellData(index).toString());
			txtContact.setText(colEmployeeContact.getCellData(index).toString());
			chbActive.setSelected(colActive.getCellData(index));

			empId = colEmployeeId.getCellData(index);

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
		chb_shift.getItems().addAll("Day", "Night");
		chbRole.getItems().addAll("Admin","Manager","Captain", "Chef","Water", "Receptionist","Assistant");
		showEmployee();
		search();

	}

	private void showEmployee() {
		ObservableList<Employee> list = employee.getAllEmployee();
		if (list != null) {
			srNo.setCellValueFactory(
					cellData -> new SimpleIntegerProperty(tblEmployee.getItems().indexOf(cellData.getValue()) + 1)
							.asObject());
			colEmployeeId.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("employeeId"));
			colEmployeeName.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeeName"));
			colRole.setCellValueFactory(new PropertyValueFactory<Employee, String>("role"));
			colEmployeeDateofJoining.setCellValueFactory(new PropertyValueFactory<Employee, LocalDate>("joiningDate"));
			colShift.setCellValueFactory(new PropertyValueFactory<Employee, String>("shift"));
			colEmployeeContact.setCellValueFactory(new PropertyValueFactory<Employee, Long>("contact"));
			colActive.setCellValueFactory(new PropertyValueFactory<Employee, Boolean>("status"));

			tblEmployee.setItems(list);
		}
	}

	private void clearFileds() {
		txtEmployeeName.clear();
		dateofjoining.setValue(null);
		txtContact.clear();
		chbActive.setSelected(false);
	}

	private void save() {
		employeeName = txtEmployeeName.getText().trim();
		role = chbRole.getValue();
		joiningDate = dateofjoining.getValue();
		shift = chb_shift.getValue();
		String contactno = txtContact.getText().trim();
		active = chbActive.isSelected();
		if (employeeName.isEmpty() || employeeName == null) {

			AllNotifications.error("Employee Management", "Enter Employee Name", "");
			return;
		}
		if (role == null) {
			AllNotifications.error("Employee Management", "Select Employee Role", "");
			return;
		}
		if (joiningDate == null) {

			joiningDate = LocalDate.now();
		}
		if (shift == null) {
			shift = "Day";
		}
		if (contactno.isEmpty() || contactno == null || !contactno.matches("-?\\d*")) {
			AllNotifications.error("Employee Management", "Enter Employee Contact", "");
			return;
		}

		try {

			contact = Long.parseLong(contactno);

			boolean st = employee.addEmployee(employeeName,role, joiningDate, shift, contact, active);
			if (st == true) {
				AllNotifications.showNotification("Employee Management", "Employee Detials Saved Successfully");
				showEmployee();
				clearFileds();

			} else {
				allNotifi.error("Employee Management", "Employee Detials Not Saved", "");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void update() {
		employeeName = txtEmployeeName.getText().trim();
		role = chbRole.getValue();
		joiningDate = dateofjoining.getValue();
		shift = chb_shift.getValue();
		String contactno = txtContact.getText().trim();
		active = chbActive.isSelected();

		if (employeeName.isEmpty() || employeeName == null) {
			AllNotifications.error("Employee Management", "Enter Employee Name", "");
			return;
		}
		if (joiningDate == null) {
			joiningDate = LocalDate.now();
		}
		if (role == null) {
			AllNotifications.error("Employee Management", "Select Employee Role", "");
			return;
		}
		if (shift == null) {
			shift = "Day";
		}
		if (contactno.isEmpty() || contactno == null || !contactno.matches("-?\\d*")) {
			AllNotifications.error("Employee Management", "Enter Employee Contact", "");
			return;
		}
		try {

			if (empId != 0) {
				Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Employee Details",
						"Are You Sure You want to Update this Employee Details", "");

				if (conf.get() == ButtonType.OK) {

					contact = Long.parseLong(contactno);

					boolean st = employee.updateEmployee(employeeName,role, joiningDate, shift, contact, active, empId);
					if (st == true) {
						allNotifi.showNotification("Employee Management", "Employee Details Updated Successfully");
						showEmployee();
						clearFileds();

					} else {
						allNotifi.error("Employee Management", "Employee Details Not Updated", "");
					}

				}
			} else {
				allNotifi.error("Employee Management", "Please Select Employee Details", "");

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void delete() {
		if (empId != 0) {
			Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Employee Details",
					"Are You Sure You want to Delete this Employee Details", "");

			if (conf.get() == ButtonType.OK) {
				boolean st = employee.deleteEmployee(empId);
				if (st == true) {
					showEmployee();
					allNotifi.showNotification("Employee Details", "Employee Delete Successfully");
					clearFileds();
				} else {
					allNotifi.error("Employee Management", "Employee Details Not Deleted", "");
				}

			}
		} else {
			allNotifi.error("Employee Management", "Please Select Employee Details", "");

		}
	}

	private void search() {
		txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			// Call a method to update the table based on the search text
			filterData(newValue);
		});
	}

	private void filterData(String searchText) {
		ObservableList<Employee> filteredData = FXCollections.observableArrayList();
		if (data != null) {
			for (Employee item : data) {
				if (item.getEmployeeName().toLowerCase().contains(searchText.toLowerCase())) {
					filteredData.add(item);
				}
			}

			tblEmployee.setItems(filteredData);
		}
	}

}
