package Stock;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import com.restaurant.DAO.InventoryDAO;
import com.restaurant.DAO.MenuItemDAO;
import com.restaurant.DAO.RecipeDAO;
import com.restaurant.DAO.UnitDAO;
import com.restaurant.Entity.Inventory;
import com.restaurant.Entity.MenuItem;
import com.restaurant.Entity.Recipe;
import com.restaurant.Entity.Unit;
import AllNotivications.AllNotifications;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.scene.control.ComboBox;
import com.jfoenix.controls.JFXTextField;

public class RecipeController implements Initializable {
	
	AllNotifications allNotifi = new AllNotifications();

	
	@FXML
    private CheckBox chbStatus;

    @FXML
    private ComboBox<String> chbRawMaterialName;

    @FXML
    private ComboBox<String> chbRecipeName;

    @FXML
    private ComboBox<String> chbUnit;

    @FXML
    private TableColumn<Recipe, Double> colQty;

    @FXML
    private TableColumn<Recipe, String> colRecipeName;
    
    @FXML
    private TableColumn<Recipe, String> colRawMaterial;

    @FXML
    private TableColumn<Recipe, Boolean> colStatus;

    @FXML
    private TableColumn<Recipe, String> colUnit;

    @FXML
    private TableColumn<Recipe, Integer> srNo;
    
    @FXML
    private TableColumn<Recipe, Integer> col_id;

    @FXML
    private TableView<Recipe> tblRecipe;

    @FXML
    private JFXTextField txtRecipeQty;
	
	int recipeId = 0;
	private String recipeName = null;
	private String rawMaterial = null;
	private String unit = null;
	private String qty = null;
	private boolean status = false;


	@FXML
	void getSelectedRcord(MouseEvent event) {
		int index = tblRecipe.getSelectionModel().getSelectedIndex();
		if (index >= 0) {
			chbRecipeName.setValue(colRecipeName.getCellData(index).toString());
			chbRawMaterialName.setValue(colRawMaterial.getCellData(index).toString());
			chbUnit.setValue(colUnit.getCellData(index).toString());
			txtRecipeQty.setText(colQty.getCellData(index).toString());
			chbStatus.setSelected(colStatus.getCellData(index));
			recipeId = col_id.getCellData(index);
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
			if(recipeId!=0)
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
			if(recipeId!=0)
			{
				delete();
			}
			
		}
    }
//-------------------------------------------------------------------------------------------------------------	

	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		loadRecipe();
		loadItem();
		loadRecipe();
		loadStock();
		loadUnit();
	}
	
	private void loadRecipe() {
		ObservableList<Recipe> list = RecipeDAO.getAllRecipe();
		if(list!=null)
		{
		srNo.setCellValueFactory(cellData -> new SimpleIntegerProperty(tblRecipe.getItems().indexOf(cellData.getValue()) + 1).asObject());	
		col_id.setCellValueFactory(new PropertyValueFactory<Recipe, Integer>("recipeId"));
		colRecipeName.setCellValueFactory(new PropertyValueFactory<Recipe, String>("recipeName"));
		colRawMaterial.setCellValueFactory(new PropertyValueFactory<Recipe, String>("rawMaterial"));
		colUnit.setCellValueFactory(new PropertyValueFactory<Recipe, String>("unit"));
		colQty.setCellValueFactory(new PropertyValueFactory<Recipe, Double>("qty"));
		colStatus.setCellValueFactory(new PropertyValueFactory<Recipe, Boolean>("status"));
		tblRecipe.setItems(list);
		}
	}

	private void clearAllValues()
	{
	chbRawMaterialName.getItems().clear();
	chbRecipeName.getItems().clear();
	chbUnit.getItems().clear();
	txtRecipeQty.clear();
	
	loadRecipe();
	loadItem();
	loadRecipe();
	loadStock();
	loadUnit();
	
	}

	
	private void save()
	{
			  recipeName = chbRecipeName.getValue();
			  rawMaterial = chbRawMaterialName.getValue();
			  unit = chbUnit.getValue();
			  qty = txtRecipeQty.getText().trim();
			  status = chbStatus.isSelected();
			 if(recipeName==null || recipeName.isEmpty())
			 {
				AllNotifications.error("Recipe Management", "Select Recipe Name", "");
				return;
			 }
			 
			 if(rawMaterial==null || rawMaterial.isEmpty())
			 {
				AllNotifications.error("Recipe Management", "Select Raw Material Name", "");
				return;
			 }
			 
			 if(unit==null || unit.isEmpty())
			 {
				AllNotifications.error("Recipe Management", "Select Unit", "");
				return;
			 }
			 if(qty==null || qty.isEmpty() || !qty.matches("-?\\d+(\\.\\d+)?"))
			 {
				AllNotifications.error("Recipe Management", "Enter Qty", "");
				return;
			 }
			 
			 
			 try {
				 double allQty  = Double.parseDouble(qty);
				 boolean st = RecipeDAO.addRecipe(recipeName, rawMaterial, unit, allQty, status);
					if (st == true) {
						allNotifi.showNotification("Recipe Management", "Recipe Added Successfully");
						loadRecipe();
						clearAllValues();
						
					} else {
						allNotifi.error("Recipe Management", "Recipe Not Added", "");
					}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		
	}
	
	private void update()
	{
		 recipeName = chbRecipeName.getValue();
		  rawMaterial = chbRawMaterialName.getValue();
		  unit = chbUnit.getValue();
		  qty = txtRecipeQty.getText().trim();
		  status = chbStatus.isSelected();
		 
		  if(recipeName==null || recipeName.isEmpty())
			 {
				AllNotifications.error("Recipe Management", "Select Recipe Name", "");
				return;
			 }
			 
			 if(rawMaterial==null || rawMaterial.isEmpty())
			 {
				AllNotifications.error("Recipe Management", "Select Raw Material Name", "");
				return;
			 }
			 
			 if(unit==null || unit.isEmpty())
			 {
				AllNotifications.error("Recipe Management", "Select Unit", "");
				return;
			 }
			 if(qty==null || !qty.matches("-?\\d+(\\.\\d+)?"))
			 {
				AllNotifications.error("Recipe Management", "Enter Qty", "");
				return;
			 }
		 try {
			 
			 if (recipeId != 0) {
				 
				 Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Recipe Management", "Are You Sure you want to Update this Recipe", "");
					if (conf.get() == ButtonType.OK) {		
						double allQty  = Double.parseDouble(qty);
						 boolean st = RecipeDAO.updateRecipe(recipeId, recipeName, rawMaterial, unit, allQty, status);
							if (st == true) {
								allNotifi.showNotification("Recipe Management", "Recipe Updated Successfully");
								loadRecipe();
								clearAllValues();
								
							} else {
								allNotifi.error("Recipe Management", "Recipe Not Updated", "");
							}
					}
				}
				else
				{
					allNotifi.error("Recipe Management", "Select Recipe", "");
				}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void delete()
	{
		if (recipeId != 0) {
			Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Recipe Management", "Are You Sure you want to Delete this Recipe", "");
			if (conf.get() == ButtonType.OK) {
				boolean status = RecipeDAO.deleteRecipe(recipeId);
				if (status == true) {
					allNotifi.showNotification("Recipe Management", "Recipe Delete Successfully");
					loadRecipe();
					clearAllValues();
				} else {
					allNotifi.error("Recipe Management", "Recipe Not Deleted", "");
				}
			}
		}
		else
		{
			allNotifi.error("Recipe Management", "Select Recipe", "");
		}
	}
	
private void loadItem() {
	chbRecipeName.getItems().clear();
	List<MenuItem> list = MenuItemDAO.loadAllMenu();
	ObservableList data = FXCollections.observableArrayList();
	for (MenuItem d : list) {

		data.add(d.getItemName());
	}
	
	FilteredList<String> filteredItems = new FilteredList<>(data, s -> true);

    chbRecipeName.setEditable(true);
    
    chbRecipeName.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
        final TextField editor = chbRecipeName.getEditor();
        final String selected = chbRecipeName.getSelectionModel().getSelectedItem();
        
        if (selected == null || !selected.equals(editor.getText())) {
            filteredItems.setPredicate(item -> item.toLowerCase().contains(newValue.toLowerCase().trim()));
        }
    });

    // Set the filtered items to the ComboBox
    chbRecipeName.setItems(filteredItems);
	

}

private void loadStock() {
	chbRawMaterialName.getItems().clear();
	List<Inventory> list = InventoryDAO.getAll();
	ObservableList data = FXCollections.observableArrayList();
	for (Inventory d : list) {

		data.add(d.getItemName());
	}
	
	FilteredList<String> filteredItems = new FilteredList<>(data, s -> true);

    chbRawMaterialName.setEditable(true);
    
    chbRawMaterialName.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
        final TextField editor = chbRawMaterialName.getEditor();
        final String selected = chbRawMaterialName.getSelectionModel().getSelectedItem();
        
        if (selected == null || !selected.equals(editor.getText())) {
            filteredItems.setPredicate(item -> item.toLowerCase().contains(newValue.toLowerCase().trim()));
        }
    });

    // Set the filtered items to the ComboBox
    chbRawMaterialName.setItems(filteredItems);
	

}

private void loadUnit() {
	chbUnit.getItems().clear();
	List<Unit> list = UnitDAO.getactiveUnit();

	ObservableList data = FXCollections.observableArrayList();

	for (Unit d : list) {

		data.add(d.getUnitName());
	}

	chbUnit.setItems(data);
	
	FilteredList<String> filteredItems = new FilteredList<>(data, s -> true);

	chbUnit.setEditable(true);
    
	chbUnit.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
        final TextField editor = chbUnit.getEditor();
        final String selected = chbUnit.getSelectionModel().getSelectedItem();
        
        if (selected == null || !selected.equals(editor.getText())) {
            filteredItems.setPredicate(item -> item.toLowerCase().contains(newValue.toLowerCase().trim()));
        }
    });

    // Set the filtered items to the ComboBox
	chbUnit.setItems(filteredItems);
}
}
