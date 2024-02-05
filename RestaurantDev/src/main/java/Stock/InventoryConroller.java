package Stock;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import com.restaurant.DAO.InventoryCategoryDAO;
import com.restaurant.DAO.InventoryDAO;
import com.restaurant.DAO.UnitDAO;
import com.restaurant.Entity.Inventory;
import com.restaurant.Entity.InventoryCaregory;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.input.MouseEvent;

public class InventoryConroller implements Initializable {

	private final ObservableList<Inventory> data = InventoryDAO.getAll();

	@FXML
	private ComboBox<String> chb_category;

	@FXML
	private ComboBox<String> chb_unit;

	@FXML
	private CheckBox che_status;

	@FXML
	private TableColumn<Inventory, String> col_category;

	@FXML
	private TableColumn<Inventory, LocalDate> col_date;
	
    @FXML
    private TableColumn<Inventory, Integer> srNo;

	@FXML
	private TableColumn<Inventory, Integer> col_id;

	@FXML
	private TableColumn<Inventory, String> col_item_name;

	@FXML
	private TableColumn<Inventory, Double> col_price;

	@FXML
	private TableColumn<Inventory, Double> col_qty;

	@FXML
	private TableColumn<Inventory, Double> colSubUnit;

	@FXML
	private TableColumn<Inventory, Boolean> col_status;

	@FXML
	private TableColumn<Inventory, Double> col_tax;

	@FXML
	private TableColumn<Inventory, Double> col_total;

	@FXML
	private TableColumn<Inventory, String> col_unit;

	@FXML
	private DatePicker stock_entry_date;

	@FXML
	private TableView<Inventory> tbl_inventory;

	@FXML
    private JFXTextField txt_item_name;

    @FXML
    private JFXTextField txt_purches_price;

    @FXML
    private JFXTextField txt_qty;

    @FXML
    private JFXTextField txt_tax;
    
    @FXML
    private JFXTextField txtSearch;

	private int inventoryId = 0;
	private double total = 0;
	private String category = null;
	private String itemName = null;
	private String unit = null;
	private double price = 0;
	private double qty = 0;
	private double tax = 0;
	private LocalDate date = LocalDate.now();
	private boolean chbstatus = false;

	@FXML
	void btnDelete(ActionEvent event) {
		delete();
	}

	@FXML
	void btnAdd(ActionEvent event) {
		save();
	}

	@FXML
	void btnUpdate(ActionEvent event) {
		update();
	}
	
	@FXML
    void onKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			if(inventoryId!=0)
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
			if(inventoryId!=0)
			{
				delete();
			}
			
		}
    }

	@FXML
	void get_selected_row(MouseEvent event) {
		getSelected();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		loadUnit();
		loadCategory();
		inventoryLoad();
		search();
	}

	private void loadCategory() {
		List<InventoryCaregory> list = InventoryCategoryDAO.getActiveTrueCategory();

		ObservableList data = FXCollections.observableArrayList();

		for (InventoryCaregory d : list) {

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

	private void loadUnit() {
		List<Unit> list = UnitDAO.getactiveUnit();

		ObservableList data = FXCollections.observableArrayList();

		for (Unit d : list) {

			data.add(d.getUnitName());
		}

		chb_unit.setItems(data);

		FilteredList<String> filteredItems = new FilteredList<>(data, s -> true);

		chb_unit.setEditable(true);

		chb_unit.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
			final TextField editor = chb_unit.getEditor();
			final String selected = chb_unit.getSelectionModel().getSelectedItem();

			if (selected == null || !selected.equals(editor.getText())) {
				filteredItems.setPredicate(item -> item.toLowerCase().contains(newValue.toLowerCase().trim()));
			}
		});

		// Set the filtered items to the ComboBox
		chb_unit.setItems(filteredItems);
	}

	private void save() {

		category = chb_category.getValue();
		itemName = txt_item_name.getText();
		unit = chb_unit.getValue();
		String pr = txt_purches_price.getText().trim();
		String pqty = txt_qty.getText().trim();
		String ptx = txt_tax.getText().trim();
		date = stock_entry_date.getValue();
		chbstatus = che_status.isSelected();

		if (itemName.trim().isEmpty() || itemName == null) {
			AllNotifications.error("Inventory Management", "Enter Item Name", "");
			return;
		}
		if (unit == null || unit.trim().isEmpty()) {
			AllNotifications.error("Inventory Management", "Select Unit", "");
			return;
		}
		if (pr.trim().isEmpty() || pr == null || !pr.matches("-?\\d+(\\.\\d+)?")) {
			AllNotifications.error("Inventory Management", "Enter Price", "");
			return;
		}
		if (pqty.trim().isEmpty() || pqty == null || !pqty.matches("-?\\d+(\\.\\d+)?")) {
			AllNotifications.error("Inventory Management", "Enter Qty", "");
			return;
		}
		if (ptx.trim().isEmpty() || ptx == null || !ptx.matches("-?\\d+(\\.\\d+)?")) {
			ptx = "0";
		}
		if (date == null || date.equals("")) {
			date = LocalDate.now();
		}

		try {

			price = Double.parseDouble(pr);
			qty = Double.parseDouble(pqty);
			tax = Double.parseDouble(ptx);

			double gramsPerKilogram = 1000.0;

			double subUnit = qty * gramsPerKilogram;

			double priceQtyTotal = price * qty;

			double gstAmount = (priceQtyTotal * tax) / (100 + tax);

			total = Math.round(priceQtyTotal + gstAmount);

			boolean st1 = InventoryDAO.add(category, itemName, unit, price, qty, subUnit, tax, total, date, chbstatus);
			if (st1 == true) {
				inventoryLoad();
				AllNotifications.showNotification("Inventory Management", "New Stock Add Successfully");

			} else {
				AllNotifications.error("Inventory Management", "New Stock Not Added", "");
			}

		} catch (NumberFormatException e) {

		}
	}

	private void update() {
		category = chb_category.getValue();
		itemName = txt_item_name.getText();
		unit = chb_unit.getValue();
		String pr = txt_purches_price.getText().trim();
		String pqty = txt_qty.getText().trim();
		String ptx = txt_tax.getText().trim();
		date = stock_entry_date.getValue();
		chbstatus = che_status.isSelected();

		if (itemName.trim().isEmpty() || itemName.equals("") || itemName == null) {
			AllNotifications.error("Inventory Management", "Enter Item Name", "");
			return;
		}
		if (unit == null || unit.trim().isEmpty()) {
			AllNotifications.error("Inventory Management", "Select Unit", "");
			return;
		}
		if (pr.trim().isEmpty() || pr.equals("") || pr == null || !pr.matches("-?\\d+(\\.\\d+)?")) {
			AllNotifications.error("Inventory Management", "Enter Price", "");
			return;
		}
		if (pqty.trim().isEmpty() || pqty.equals("") || pqty == null || !pqty.matches("-?\\d+(\\.\\d+)?")) {
			AllNotifications.error("Inventory Management", "Enter Qty", "");
			return;
		}
		if (ptx.trim().isEmpty() || ptx.equals("") || ptx == null || !ptx.matches("-?\\d+(\\.\\d+)?")) {
			ptx = "0";
		}
		if (date == null || date.equals("")) {
			date = LocalDate.now();
		}
		try {
			if (inventoryId != 0) {
				Optional<ButtonType> conf = AllNotifications.ConfirmationNotify("Inventory Management",
						"Are You Sure you want to Update This Stock", "");
				if (conf.get() == ButtonType.OK) {

					price = Double.parseDouble(pr);
					qty = Double.parseDouble(pqty);
					tax = Double.parseDouble(ptx);

					double gramsPerKilogram = 1000.0;

					double subUnit = qty * gramsPerKilogram;

					double priceQtyTotal = price * qty;

					double gstAmount = (priceQtyTotal * tax) / (100 + tax);

					total = Math.round(priceQtyTotal + gstAmount);

					boolean st1 = InventoryDAO.update(inventoryId, category, itemName, unit, price, qty, subUnit, tax,
							total, date, chbstatus);
					if (st1 == true) {
						inventoryLoad();

						AllNotifications.showNotification("Inventory Management", "Stock Updated Successfully");
					} else {
						AllNotifications.error("Inventory Management", "Stock Not Updated", "");
					}

				} else {
					AllNotifications.error("Inventory Management", "Please Select Stock", "");
				}
			}
		} catch (NumberFormatException e) {

		}
	}

	private void delete() {
		if (inventoryId != 0) {
			Optional<ButtonType> conf = AllNotifications.ConfirmationNotify("Inventory Management",
					"Are You Sure you want to Delete This Stock", "");
			if (conf.get() == ButtonType.OK) {
				boolean st1 = InventoryDAO.delete(inventoryId);
				if (st1 == true) {
					inventoryLoad();
					AllNotifications.showNotification("Inventory Management", "Stock Deleted Successfully");
				} else {
					AllNotifications.error("Inventory Management", "Stock Not Deleted", "");
				}
			}
		} else {
			AllNotifications.error("Inventory Management", "Please Select Stock", "");
		}

	}

	private void inventoryLoad() {
		ObservableList<Inventory> list = InventoryDAO.getAll();
		if (list != null) {
			srNo.setCellValueFactory(cellData -> new SimpleIntegerProperty(tbl_inventory.getItems().indexOf(cellData.getValue()) + 1).asObject());
			col_id.setCellValueFactory(new PropertyValueFactory<Inventory, Integer>("inventoryId"));
			col_category.setCellValueFactory(new PropertyValueFactory<Inventory, String>("category"));
			col_item_name.setCellValueFactory(new PropertyValueFactory<Inventory, String>("itemName"));
			col_unit.setCellValueFactory(new PropertyValueFactory<Inventory, String>("unit"));
			col_price.setCellValueFactory(new PropertyValueFactory<Inventory, Double>("price"));
			col_qty.setCellValueFactory(new PropertyValueFactory<Inventory, Double>("qty"));
			colSubUnit.setCellValueFactory(new PropertyValueFactory<Inventory, Double>("subUnit"));
			col_tax.setCellValueFactory(new PropertyValueFactory<Inventory, Double>("tax"));
			col_total.setCellValueFactory(new PropertyValueFactory<Inventory, Double>("total"));
			col_date.setCellValueFactory(new PropertyValueFactory<Inventory, LocalDate>("date"));
			col_status.setCellValueFactory(new PropertyValueFactory<Inventory, Boolean>("status"));
			tbl_inventory.setItems(list);
		}
	}

	private void getSelected() {
		int index = tbl_inventory.getSelectionModel().getSelectedIndex();
		if (index >= 0) {
			chb_category.setValue(col_category.getCellData(index).toString());
			txt_item_name.setText(col_item_name.getCellData(index).toString());
			chb_unit.setValue(col_unit.getCellData(index).toString());
			txt_purches_price.setText(col_price.getCellData(index).toString());
			txt_qty.setText(col_qty.getCellData(index).toString());
			txt_tax.setText(col_tax.getCellData(index).toString());
			stock_entry_date.setValue(col_date.getCellData(index));
			che_status.setSelected(col_status.getCellData(index));
			inventoryId = col_id.getCellData(index);

		} else {
			return;
		}
	}

	private void search() {
		txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			// Call a method to update the table based on the search text
			filterData(newValue);
		});
	}

	private void filterData(String searchText) {
		ObservableList<Inventory> filteredData = FXCollections.observableArrayList();
		if (data != null) {
			for (Inventory item : data) {
				if (item.getItemName().toLowerCase().contains(searchText.toLowerCase())) {
					filteredData.add(item);
				}
			}

			tbl_inventory.setItems(filteredData);
		}
	}

}
