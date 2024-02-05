package SubCategory;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import com.restaurant.DAO.CategoryDAO;
import com.restaurant.DAO.SubCategoryDAO;
import com.restaurant.Entity.SubCategory;
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

public class SubCategoryController implements Initializable {
	CategoryDAO catDAO = new CategoryDAO();
	SubCategoryDAO subcatedao = new SubCategoryDAO();
	AllNotifications allNotifi = new AllNotifications();

	int catID = 0;
	private String subcategory;
	private boolean status;
	
	 @FXML
	    private CheckBox active;

	    @FXML
	    private TableColumn<SubCategory, Boolean> col_active;
   
	    @FXML
	    private TableColumn<SubCategory, String> col_sub_category;

	    @FXML
	    private TableColumn<SubCategory, Integer> srNo;
	    
	    @FXML
	    private TableColumn<SubCategory, Integer> col_id;

	    @FXML
	    private TableView<SubCategory> tblSubCategory;
	 
	    @FXML
	    private JFXTextField txtSubCategory;

	
	@FXML
	void getSelectedRcord(MouseEvent event) {
		int index = tblSubCategory.getSelectionModel().getSelectedIndex();
		if (index >= 0) {
			txtSubCategory.setText(col_sub_category.getCellData(index).toString());
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
		subcatedao.importSubCategory();
		showSubCategory();
    }

	
//-------------------------------------------------------------------------------------------------------------	

	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		showSubCategory();
		
	}
	
	private void showSubCategory() {
		ObservableList<SubCategory> list = subcatedao.getAllSubCategory();
		if(list!=null)
		{
		srNo.setCellValueFactory(cellData -> new SimpleIntegerProperty(tblSubCategory.getItems().indexOf(cellData.getValue()) + 1).asObject());	
		col_id.setCellValueFactory(new PropertyValueFactory<SubCategory, Integer>("subCategoryId"));
		col_sub_category.setCellValueFactory(new PropertyValueFactory<SubCategory, String>("subCategory"));
		col_active.setCellValueFactory(new PropertyValueFactory<SubCategory, Boolean>("status"));
		tblSubCategory.setItems(list);
		}
	}

	private void clearAllValues()
	{	txtSubCategory.clear();
		active.setSelected(false);
	}


	private void save()
	{
			 subcategory = txtSubCategory.getText().trim();
			 status = active.isSelected();
			 
			 if(subcategory.isEmpty() || subcategory==null)
			 {
				 AllNotifications.error("Sub-Category Management", "Enter Sub-Category", "");
					return;
			 }
			 
			 try {
				 
				 boolean st = subcatedao.addSubCategory(subcategory, status);
					if (st == true) {
						allNotifi.showNotification("Sub-Category Management", "Sub-Category Added Successfully");
						clearAllValues();
						showSubCategory();
					} else {
						allNotifi.error("Sub-Category Management", "Sub-Category Not Added", "");
					}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		
	}
	
	private void update()
	{
		 subcategory = txtSubCategory.getText().trim();
		 status = active.isSelected();
		 
		 if(subcategory.isEmpty() || subcategory==null)
		 {
			 AllNotifications.error("Sub-Category Management", "Enter Sub-Category", "");
				return;
		 }
		 
		 try {
			 
			 if (catID != 0) {
				 Optional<ButtonType> conf = AllNotifications.ConfirmationNotify("Sub-Category Managment",
							"Are You Sure you want to Update This Sub-Category", "");
					if (conf.get() == ButtonType.OK) {
						
							boolean st1 = subcatedao.updateSubCategory(catID, subcategory, status);
						if (st1 == true) {
							allNotifi.showNotification("Sub-Category Management", "Sub-Category Updated Successfully");
							clearAllValues();
							showSubCategory();
						} else {
							allNotifi.error("Sub-Category Management", "Sub-Category Not Updated", "");
						}
					}
				}
				else
				{
					allNotifi.error("Sub-Category Management", "Please Select Sub-Category", "");
				}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void delete()
	{
		if (catID != 0) {
			 Optional<ButtonType> conf = AllNotifications.ConfirmationNotify("Sub-Category Management",
						"Are You Sure you want to Delete This Sub-Category", "");
				if (conf.get() == ButtonType.OK) {
				boolean status = subcatedao.deleteSubCategory(catID);
				if (status == true) {
					allNotifi.showNotification("Sub-Category Management", "Sub-Category Deleted Successfully");
					clearAllValues();
					showSubCategory();
				} else {
					allNotifi.error("Sub-Category Management", "Sub-Category Not Deleted", "");
				}
			}
		}
		else
		{
			allNotifi.error("Sub-Category Management", "Please Select Sub-Category", "");
		}
	}
}
