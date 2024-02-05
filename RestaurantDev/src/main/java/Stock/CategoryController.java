package Stock;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import com.restaurant.DAO.InventoryCategoryDAO;
import com.restaurant.Entity.InventoryCaregory;
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
import javafx.scene.layout.AnchorPane;	

public class CategoryController implements Initializable {
	
	InventoryCategoryDAO categoryDAO = new InventoryCategoryDAO();
	AllNotifications allNotifi = new AllNotifications();

	
	@FXML
	private CheckBox active;


	@FXML
	private TableColumn<InventoryCaregory, Boolean> col_active;

	@FXML
	private TableColumn<InventoryCaregory, String> col_category;
	@FXML
    private TableColumn<InventoryCaregory, Integer> srNo;
	@FXML
	private TableColumn<InventoryCaregory, Integer> col_id;

	@FXML
	private AnchorPane departmentPane;

	@FXML
	private TableView<InventoryCaregory> tblCategory;

	@FXML
	private TextField txtCategorry;
	
	int catID = 0;
	private String category=null;
	private boolean status;


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
//-------------------------------------------------------------------------------------------------------------	

	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		showCategory();
	}
	
	private void showCategory() {
		ObservableList<InventoryCaregory> list = categoryDAO.getAllCategory();
		if(list!=null)
		{
		srNo.setCellValueFactory(cellData -> new SimpleIntegerProperty(tblCategory.getItems().indexOf(cellData.getValue()) + 1).asObject());
		col_id.setCellValueFactory(new PropertyValueFactory<InventoryCaregory, Integer>("categoryId"));
		col_category.setCellValueFactory(new PropertyValueFactory<InventoryCaregory, String>("category"));
		col_active.setCellValueFactory(new PropertyValueFactory<InventoryCaregory, Boolean>("status"));
		tblCategory.setItems(list);
		}
	}

	private void clearAllValues()
	{
		txtCategorry.setText("");
		active.setSelected(false);
	}

	
	private void save()
	{
			 category = txtCategorry.getText().trim();
			 status = active.isSelected();
			 
			 if(category.isEmpty() || category==null)
			 {
				AllNotifications.error("Category Management", "Enter Category Name", "");
				return;
			 }
			 
			 try {
				 
				 boolean st = categoryDAO.addCategory(category, status);
					if (st == true) {
						allNotifi.showNotification("Category Management", "Category Added Successfully");
						clearAllValues();
						showCategory();
					} else {
						allNotifi.error("Category Management", "Category Not Added", "");
					}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		
	}
	
	private void update()
	{
		category = txtCategorry.getText().trim();
		 status = active.isSelected();
		 
		 if(category.isEmpty() || category==null)
		 {
			AllNotifications.error("Category Management", "Enter Category Name", "");
			return;
		 }
		 try {
			 
			 if (catID != 0) {
				 Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Category Management", "Are You Sure you want to Update this Category", "");
					if (conf.get() == ButtonType.OK) {
						
						boolean st = categoryDAO.updateCategory(catID, category, status);
						if (st == true) {
							allNotifi.showNotification("Category Management", "Category Updated Successfully");
							clearAllValues();
							showCategory();
						} else {
							allNotifi.error("Category Management", "Category Not Updated", "");
						}
					}
				}
				else
				{
					allNotifi.error("Category Management", "Select Category", "");
				}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void delete()
	{
		if (catID != 0) {
			Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Category Management", "Are You Sure you want to Delete this Category", "");
			if (conf.get() == ButtonType.OK) {
				boolean status = categoryDAO.deleteCategory(catID);
				if (status == true) {
					allNotifi.showNotification("Category Management", "Category Delete Successfully");
					clearAllValues();
					showCategory();
				} else {
					allNotifi.error("Category Management", "Category Not Deleted", "");
				}
			}
		}
		else
		{
			allNotifi.error("Category Management", "Select Category", "");
		}
	}
}
