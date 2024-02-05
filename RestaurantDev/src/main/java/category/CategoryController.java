package category;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import com.restaurant.DAO.CategoryDAO;
import com.restaurant.Entity.Category;
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

public class CategoryController implements Initializable {

	CategoryDAO categoryDAO = new CategoryDAO();
	AllNotifications allNotifi = new AllNotifications();

	int catID = 0;
	private String category=null;
	private boolean status;

	@FXML
	private CheckBox active;

	@FXML
	private TableColumn<Category, Boolean> col_active;

	@FXML
	private TableColumn<Category, String> col_category;

	@FXML
	private TableColumn<Category, Integer> col_id;
	
	@FXML
    private TableColumn<Category, Integer> srNo;

	@FXML
	private AnchorPane departmentPane;

	@FXML
	private TableView<Category> tblCategory;

	@FXML
    private JFXTextField txtCategorry;

	
	@FXML
	void getSelectedRcord(MouseEvent event) {
		int index = tblCategory.getSelectionModel().getSelectedIndex();
		if (index >= 0) {
			txtCategorry.setText(col_category.getCellData(index).toString());
			active.setSelected(col_active.getCellData(index));
			catID = col_id.getCellData(index);
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
			if(catID!=0)
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
			if(catID!=0)
			{
				delete();
			}
			
		}
    }
	
	 @FXML
	    void btn_import(ActionEvent event) {
		 categoryDAO.importCategory();
		 showCategory();
	    }
//-------------------------------------------------------------------------------------------------------------	

	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		showCategory();
	}

	private void showCategory() {
		ObservableList<Category> list = categoryDAO.getAllCategory();
		if (list != null) {
			srNo.setCellValueFactory(cellData -> new SimpleIntegerProperty(tblCategory.getItems().indexOf(cellData.getValue()) + 1).asObject());
			col_id.setCellValueFactory(new PropertyValueFactory<Category, Integer>("categoryId"));
			col_category.setCellValueFactory(new PropertyValueFactory<Category, String>("category"));
			col_active.setCellValueFactory(new PropertyValueFactory<Category, Boolean>("status"));
			tblCategory.setItems(list);
		}
	}

	private void clearAllValues() {
		txtCategorry.clear();
		active.setSelected(false);
	}

	private void save() {
		category = txtCategorry.getText().trim();
		status = active.isSelected();
		if (category.trim().isEmpty() || category == null) {
			AllNotifications.error("Category Management", "Enter Category", "");
			return;
		}
		try {

			boolean status1 = categoryDAO.addCategory(category, status);
			if (status1 == true) {
				AllNotifications.showNotification("Category Management", "Category Added Successfully");
				clearAllValues();
				showCategory();
			} else {
				allNotifi.error("Category Management", "Category Not Added", "");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void update() {
		category = txtCategorry.getText().trim();
		status = active.isSelected();
		if (category.trim().isEmpty() || category == null) {
			AllNotifications.error("Category Management", "Enter Category", "");
			return;
		}
		try {

			if (catID != 0) {
				Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Category Management",
						"Are You Sure you want to Update This Category", "");
				if (conf.get() == ButtonType.OK) {
					boolean status1 = categoryDAO.updateCategory(catID, category, status);
					if (status1 == true) {
						AllNotifications.showNotification("Category Management", "Category Updated Successfully");
						clearAllValues();
						showCategory();
					} else {
						allNotifi.error("Category Management", "Category Not Updated", "");
					}
				}
			} else {
				allNotifi.error("Category Management", "Select Category", "");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void delete() {
		if (catID != 0) {
			Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Category Management",
					"Are You Sure you want to Delete This Category", "");
			if (conf.get() == ButtonType.OK) {
				boolean status = categoryDAO.deleteCategory(catID);
				if (status == true) {
					AllNotifications.showNotification("Category Management", "Category Deleted Successfully");
					clearAllValues();
					showCategory();
				} else {
					allNotifi.error("Category Management", "Category Not Deleted", "");
				}
			}
		} else {
			allNotifi.error("Category Management", "Select Category", "");
		}
	}
}
