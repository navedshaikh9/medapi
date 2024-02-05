package menuItem;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import com.restaurant.DAO.CategoryDAO;
import com.restaurant.DAO.MenuItemDAO;
import com.restaurant.DAO.SubCategoryDAO;
import com.restaurant.Entity.Category;
import com.restaurant.Entity.MenuItem;
import com.restaurant.Entity.SubCategory;
import AllNotivications.AllNotifications;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import com.jfoenix.controls.JFXTextField;

public class MenuItemController implements Initializable {
	
	AllNotifications allNotifi = new AllNotifications();
	MenuItemDAO menuDAO = new MenuItemDAO();
	CategoryDAO catDAO = new CategoryDAO();
	SubCategoryDAO subCateDAO = new SubCategoryDAO();

	private final ObservableList<MenuItem> data = menuDAO.getAllMenuItem();

	int menuID = 0;
	private String itemName=null;
	private String itemCode=null;
	private String category=null;
	private String subCategory=null;
	private double itemPrice=0;
	private boolean status;
	
	

	@FXML
	private CheckBox chbActive;

	@FXML
	private TableColumn<MenuItem, Boolean> col_active;

	@FXML
	private TableColumn<MenuItem, String> col_category;

	@FXML
	private TableColumn<MenuItem, String> col_SubCategory;

	@FXML
	private TableColumn<MenuItem, String> col_item_code;

	@FXML
	private TableColumn<MenuItem, String> col_item_name;

	@FXML
	private TableColumn<MenuItem, Integer> col_menu_id;
	
	@FXML
    private TableColumn<MenuItem, Integer> srNo;

	@FXML
	private TableColumn<MenuItem, Double> col_price;

	@FXML
	private TableView<MenuItem> tblMenuItem;

	@FXML
	private ComboBox<String> chb_category;

	 @FXML
	    private JFXTextField txtItemName;

	    @FXML
	    private JFXTextField txtItemCode;

	    @FXML
	    private JFXTextField txtItemPrice;


	    @FXML
	    private JFXTextField txtSearch;

	@FXML
	private ComboBox<String> chb_sub_category;

	

	

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
			if(menuID!=0)
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
			if(menuID!=0)
			{
				delete();
			}
			
		}
    }
	
	@FXML
	void getSelectedRow(MouseEvent event) {
		int index = tblMenuItem.getSelectionModel().getSelectedIndex();
		if (index >= 0) {
			txtItemName.setText(col_item_name.getCellData(index).toString());
			txtItemCode.setText(col_item_code.getCellData(index).toString());
			chb_category.setValue(col_category.getCellData(index).toString());
			chb_sub_category.setValue(col_SubCategory.getCellData(index).toString());
			txtItemPrice.setText(col_price.getCellData(index).toString());
			chbActive.setSelected(col_active.getCellData(index));
			menuID = col_menu_id.getCellData(index);

		} else {
			return;
		}
	}

	@FXML
	private void btnImportExcel(ActionEvent event) {
		menuDAO.importAllMenu();
		showMenuItem();
	}

	

	// -------------------------------------------------------------------------------------------------

	private void showMenuItem() {
		ObservableList<MenuItem> list = menuDAO.getAllMenuItem();
		if (list != null) {
			srNo.setCellValueFactory(cellData -> new SimpleIntegerProperty(tblMenuItem.getItems().indexOf(cellData.getValue()) + 1).asObject());
			col_menu_id.setCellValueFactory(new PropertyValueFactory<MenuItem, Integer>("menuId"));
			col_item_name.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("itemName"));
			col_item_code.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("itemCode"));
			col_category.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("category"));
			col_SubCategory.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("subCategory"));
			col_price.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("itemPrice"));
			col_active.setCellValueFactory(new PropertyValueFactory<MenuItem, Boolean>("status"));
			tblMenuItem.setItems(list);
		}
	}

// Load Category on choice box
	private void loadCategory() {

		List<Category> list = catDAO.getActiveTrueCategory();
		ObservableList data = FXCollections.observableArrayList();

		for (Category d : list) {

			data.add(d.getCategory());
		}
		
		FilteredList<String> filteredItems = new FilteredList<>(data, s -> true);

		chb_category.setEditable(true);
        
		chb_category.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            final TextField editor = chb_category.getEditor();
            final String selected = chb_category.getSelectionModel().getSelectedItem();
            
            if (selected == null || !selected.equals(editor.getText())) {
                filteredItems.setPredicate(item -> item.toLowerCase().contains(newValue.toLowerCase().trim()));
            }
        });

        // Set the filtered items to the ComboBox
		chb_category.setItems(filteredItems);

	}

	
	private void loadSubCategory() {

		List<SubCategory> list = subCateDAO.getActiveTrueSubCategory();
		ObservableList data = FXCollections.observableArrayList();
		for (SubCategory d : list) {

			data.add(d.getSubCategory());
		}
		
		FilteredList<String> filteredItems = new FilteredList<>(data, s -> true);

        chb_sub_category.setEditable(true);
        
        chb_sub_category.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            final TextField editor = chb_sub_category.getEditor();
            final String selected = chb_sub_category.getSelectionModel().getSelectedItem();
            
            if (selected == null || !selected.equals(editor.getText())) {
                filteredItems.setPredicate(item -> item.toLowerCase().contains(newValue.toLowerCase().trim()));
            }
        });

        // Set the filtered items to the ComboBox
        chb_sub_category.setItems(filteredItems);
		

	}
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		showMenuItem();
		loadCategory();
		loadSubCategory();
		search();

	}

	private void clearAllValues() {
		menuID = 0;
		txtItemName.clear();
		txtItemCode.clear();
		txtItemPrice.clear();
		chbActive.setSelected(false);
		loadCategory();
		loadSubCategory();
	}

	private void search() {
		txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			// Call a method to update the table based on the search text
			filterData(newValue);
		});
	}

	private void filterData(String searchText) {
		ObservableList<MenuItem> filteredData = FXCollections.observableArrayList();
		if (data != null) {
			for (MenuItem item : data) {
				if (item.getItemName().toLowerCase().contains(searchText.toLowerCase())) {
					filteredData.add(item);
				}
			}

			tblMenuItem.setItems(filteredData);
		}
	}
	
	private void save()
	{
		itemName = txtItemName.getText().trim();
		String price =  txtItemPrice.getText().trim();
		 itemCode = txtItemCode.getText().trim();
		 category = chb_category.getValue();
		 subCategory = chb_sub_category.getValue();
		 status = chbActive.isSelected();
		 
		 if(itemName.isEmpty() || itemName==null)
		 {
			 AllNotifications.error("Menu Management", "Enter Item Name", "");
				return;
		 }
		 
		 if(itemCode.isEmpty() || itemCode==null)
		 {
			 itemCode="NA";
		 }
		 if(category==null || category.isEmpty() )
		 {
			 AllNotifications.error("Menu Management", "Select Category", "");
				return;
		 }
		 if(subCategory==null || subCategory.isEmpty())
		 {
			 AllNotifications.error("Menu Management", "Select Sub-Category", "");
				return;
		 }
		 if(price.isEmpty() || price==null || !price.matches("-?\\d+(\\.\\d+)?"))
		 {
			 AllNotifications.error("Menu Management", "Enter Item Price", "");
				return;
		 }
		 
		 try {
			 
			 itemPrice = Double.parseDouble(price);
			 
			 boolean st1 = menuDAO.addMenuItem(itemName, itemCode, category, subCategory, itemPrice, status);
				if (st1 == true) {
					showMenuItem();
					clearAllValues();
					allNotifi.showNotification("Menu Management", "Menu Saved Successfully");

				} else {
					allNotifi.error("Menu Management", "Menu Not Saved", "");
				}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		 
			
	}
	
	private void update()
	{
		itemName = txtItemName.getText().trim();
		String price =  txtItemPrice.getText().trim();
		 itemCode = txtItemCode.getText().trim();
		 category = chb_category.getValue();
		 subCategory = chb_sub_category.getValue();
		 status = chbActive.isSelected();
		 
		 if(itemName.isEmpty() || itemName==null)
		 {
			 AllNotifications.error("Menu Management", "Enter Item Name", "");
				return;
		 }
		 
		 if(itemCode.isEmpty() || itemCode==null)
		 {
			 itemCode="NA";
		 }
		 if(category==null || category.isEmpty() )
		 {
			 AllNotifications.error("Menu Management", "Select Category", "");
				return;
		 }
		 if(subCategory==null || subCategory.isEmpty())
		 {
			 AllNotifications.error("Menu Management", "Select Sub-Category", "");
				return;
		 }
		 if(price.isEmpty() || price==null || !price.matches("-?\\d+(\\.\\d+)?"))
		 {
			 AllNotifications.error("Menu Management", "Enter Item Price", "");
				return;
		 }
		 try {
			
				if (menuID != 0) {
					
					Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Menu Managment", "Are You Sure You want to Update this Menu Details", "");

					if (conf.get() == ButtonType.OK) {
					itemPrice = Double.parseDouble(price);
					boolean st1 = menuDAO.updateMenuItem(itemName, itemCode, category, subCategory, itemPrice, status, menuID);
						if (st1 == true) {
							showMenuItem();
							clearAllValues();
							allNotifi.showNotification("Menu Management", "Menu Updated Successfully");
						} else {
							allNotifi.error("Menu Management", "Menu Not Updated", "");
						}
					}	
						
				} else {
					allNotifi.error("Menu Management", "Please Select Menu", "");
				}
				
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void delete()
	{
		if (menuID != 0) {
			Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Menu Management", "Are You Sure You want to Delete this Menu", "");
			if (conf.get() == ButtonType.OK) {
				boolean st1 = menuDAO.deleteMenuItem(menuID);
				if (st1 == true) {
					showMenuItem();
					clearAllValues();
					allNotifi.showNotification("Menu Management", "Menu Deleted Successfully");
				} else {
					allNotifi.error("Menu Management", "Menu Not Deleted", "");
				}
			}
		} else {
			allNotifi.error("Menu Management", "Please Select Menu", "");
		}

	}
	
	
	//--------------------------------------------------------------
	
	 private void loadData() {
	        showProgressPopup();

	        // Simulate loading data in a separate thread
	        new Thread(() -> {
	            for (int i = 0; i <= 100; i += 10) {
	                try {
	                    Thread.sleep(500); // Simulate loading delay
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	                final double progress = i / 100.0;

	                // Update progress bar in the UI thread
	                Platform.runLater(() -> updateProgress(progress));
	            }

	            // Close the progress popup when data loading is complete
	            Platform.runLater(() -> closeProgressPopup());
	        }).start();
	    }

	    private void showProgressPopup() {
	        Stage popupStage = new Stage();
	        popupStage.initStyle(StageStyle.UNDECORATED);
	        popupStage.initModality(Modality.APPLICATION_MODAL);

	        ProgressBar progressBar = new ProgressBar();
	        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);

	        VBox vBox = new VBox(progressBar);
	        vBox.setStyle("-fx-background-color: white; -fx-padding: 20px;");

	        Scene scene = new Scene(vBox, 200, 100);
	        popupStage.setScene(scene);
	        popupStage.show();
	    }

	    private void updateProgress(double progress) {
	        // Update progress bar
	        // Access and update the progress bar in the popup stage
	    }

	    private void closeProgressPopup() {
	        // Close the progress popup
	        // Close the popup stage after data loading completes
	        Platform.runLater(() -> {
	         //   Stage stage = (Stage) loadButton.getScene().getWindow();
	           // stage.close();
	        });
	    }
	    
}
