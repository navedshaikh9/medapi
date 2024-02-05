package dashboard;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXButton;
import com.restaurant.DAO.*;
import com.restaurant.Entity.*;
import AllNotivications.AllNotifications;
import AllNotivications.CommenFunction;
import BillManagment.BillFormat;
import SettingsManagment.SettingName;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.RadioButton;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.scene.layout.VBox;

public class OrderViewController implements Initializable {
	AllNotifications allNotif = new AllNotifications();
	MenuItemDAO menuDAO = new MenuItemDAO();
	TableManagmentDAO tblDAO = new TableManagmentDAO();
	AllNotifications allNotifi = new AllNotifications();
	TaxManagmentDAO taxDeDAO = new TaxManagmentDAO();
	DiscountDAO disDAO = new DiscountDAO();
	CustomerDAO cusDAO = new CustomerDAO();
	OrderTypeDAO otDAO = new OrderTypeDAO();
	PaymentMethodDAO pmDAO = new PaymentMethodDAO();
	CategoryDAO catDAO = new CategoryDAO();
	SubCategoryDAO subCateDAO = new SubCategoryDAO();
	KOTDAO kot_dao = new KOTDAO();
	KOTItemDAO kot_item_dao = new KOTItemDAO();
	SaleDAO saledao = new SaleDAO();
	SaleItemDAO sale_item_dao = new SaleItemDAO();
	
	 
	Image image = new Image("/images/windowlogo.png");

	private double total = 0;
	private int kot_id = 0;
	private int sale_id = 0;

	@FXML
	private ComboBox<String> chbCategory;

	@FXML
	private ComboBox<String> chb_cash_and_delivery;

	@FXML
	public TilePane paneMenuSection, paneTable, sub_category_tile_pane;

	@FXML
	public Pane paneOrderView, paneSearchSection;

	@FXML
	private TextField txtSearchItemName;

	private TextField amountTextField;
	@FXML
	private ScrollPane paneScrolSection;

	@FXML
	private TableColumn<Map, String> col_ItemName;

	@FXML
	private TableColumn<Map, Double> col_ItemPrice;

	@FXML
	private TableColumn<Map, Integer> col_Qty;

	@FXML
	private TableColumn<Map, Integer> col_itemID;

	@FXML
	private TableColumn<Map, Button> col_remove;

	@FXML
	private TableView<Map> sale_tbl;

	@FXML
    private JFXRadioButton radiusBtnDineIn = new JFXRadioButton("Dine-In");
	
	@FXML
	private JFXRadioButton radiusBtnDelivery = new JFXRadioButton("Delivery");
	
	@FXML
	private JFXRadioButton radiusBtnTakeaway = new JFXRadioButton("Takeaway");
	
	 ToggleGroup toggleGroup = new ToggleGroup();
    
	@FXML
	private RadioButton btn_radious_payment;

	@FXML
	private Label lable_table, lable_total_sale,lbl_kot_no;

	@FXML
	public TilePane tile_pane_kot_order;

	public JFXButton btnTable;

	@FXML
	void btnRefresh(ActionEvent event) {
	//	refresh();
		SyncDataDAO.dataSynch();
	}

	public void refresh() {
		allSubCategory();
		loadCategory();
		allTables();
		kotOrderAll();
		allMenu(1, "");
		radiusBtnDineIn.setSelected(true);
		chb_cash_and_delivery.setVisible(false);
		lable_table.setText("");
		lbl_kot_no.setText("" + kot_dao.kotId());
		sale_tbl.getItems().clear();
		countTotal();
		kot_id = 0;
		lbl_kot_no.setText("" + kot_dao.kotId());
	}
	

	public void load() {
		kotOrderAll();
		allSubCategory();
		loadCategory();
		allTables();
		allMenu(1, "");
		loadBill();
		sale_tbl.setStyle("-fx-table-cell-border-color: transparent; -fx-font-size:14;"); // Hide cell borders
		sale_tbl.setPlaceholder(new Label("No Item Selected"));
		lbl_kot_no.setText("" + kot_dao.kotId());
		radiusBtnDineIn.setToggleGroup(toggleGroup);
		radiusBtnDelivery.setToggleGroup(toggleGroup);
		radiusBtnTakeaway.setToggleGroup(toggleGroup);
		radiusBtnDineIn.setSelected(true);
	}

// Initialize all tables and menu onLoad
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		load();
		reloadAfterOneSecond();
	}

	@FXML
	void btn_radious_payment_on_action(ActionEvent event) {

		boolean st = btn_radious_payment.isSelected();
		if (st == true) {
			chb_cash_and_delivery.setVisible(false);

		} else {
			loadPaymentMethod();
		}
	}
	
	private void selectRadioButton(String value) {
        toggleGroup.getToggles().forEach(toggle -> {
        	JFXRadioButton radioButton = (JFXRadioButton) toggle;
            if (radioButton.getText().equals(value)) {
                radioButton.setSelected(true);
            }
        });
    }
	
	private String getSelectedRadioButtonValue(ToggleGroup toggleGroup) {
		JFXRadioButton selectedRadioButton = (JFXRadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            return selectedRadioButton.getText();
        } else {
            return "Dine-In"; // Or handle the case where no radio button is selected
        }
    }

	 @FXML
	    void onActionRadiusBtnDelivery(ActionEvent event) {
		
		 loadOrderType();
		 lable_table.setText("");
	    }

	    @FXML
	    void onActionRadiusBtnDineIn(ActionEvent event) {
	    	chb_cash_and_delivery.setVisible(false);
	    }

	    @FXML
	    void onActionRadiusBtnTakeaway(ActionEvent event) {
	    	lable_table.setText("");
			chb_cash_and_delivery.setVisible(false);
	    }
	

// Save Bill Quick Bill

	@FXML
	void btn_kot(ActionEvent event) {
		allNotif.showNotification("KOT Saved", "This Is KOT Button");
	}

	@FXML
	void btn_save(ActionEvent event) {
		saveNewKOT();

	}

	@FXML
	void btn_settled(ActionEvent event) {
		if(kot_id!=0)
		{
			String calculatWindow = SettingName.amountCalculater;
		Setting ofsetting = SettingDAO.getSetting(calculatWindow);
			
			if (ofsetting != null) {
				if (ofsetting.isStatus() == true) {
					amtSettled(kot_id);
				}
				else
				{
					billSetteled(kot_id);
				}
			}
			}
		else
		{
			AllNotifications.error("Bill Setteled", "Please Select Bill", "");
		}
	}

	@FXML
	void btn_customer(ActionEvent event) {
		try {
			Stage s = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/CustomerManagment/Customer.fxml"));
			Scene scene = new Scene(root, 1100, 600);
			s.setTitle("Customer Management");
			s.setScene(scene);
			// s.getIcons().add(image);
			s.setResizable(false);
			s.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	void btn_settings(ActionEvent event) {

		try {
			Stage s = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/SettingsManagment/Setting.fxml"));
			Scene scene = new Scene(root, 600, 500);
			s.setTitle("Settings");
			// s.getIcons().add(image);
			s.setResizable(false);
			s.setScene(scene);
			s.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//----------------------- Table Managment Start ------------------

	public void allTables() {
		paneTable.getChildren().clear();
		List<HotelTable> list = tblDAO.getActiveTable();
		if (list != null) {
			for (HotelTable tbl : list) {
				btnTable = new JFXButton(tbl.getTableName());
				btnTable.setId("" + tbl.getTableId());
				btnTable.setPrefWidth(100);
				btnTable.setPrefHeight(35);
				btnTable.setMaxWidth(100);
				btnTable.setMaxHeight(35);
				btnTable.setStyle("-fx-background-color:#59A61F; -fx-font-size:14; -fx-text-fill:#ffff;");
				paneTable.getChildren().add(btnTable);
				paneTable.setHgap(10);
				paneTable.setVgap(10);
				checkTable(btnTable.getText());

				btnTable.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {

						String tableID = ((Button) event.getSource()).getId();
						String tblName = ((Button) event.getSource()).getText();

						boolean status = kot_dao.checkTable(tblName);
						if (status == true) {
							allNotif.showErrorNotification("Dine-In",
									tblName + " Already Booked Please Select Another Table");
							
						} else {
							selectTable(tableID, tblName);
							checkTable(tblName);
						}

					}

				});

			}
		}
	}

	// Check Table
	private void checkTable(String tbl) {

		List<KOT> list = kot_dao.getBookTable();

		for (KOT b : list) {
			if (tbl.equals(b.getOrderType())) {
				btnTable.setStyle("-fx-background-color:RED;-fx-font-size:14; -fx-text-fill:#ffff;");
			}

		}

	}

//----------------------- Table Managment End ------------------
	public void kotOrderAll() {
		tile_pane_kot_order.getChildren().clear();
		List<KOT> list = kot_dao.getAllKOTActive();
		if (list != null) {
			for (KOT order : list) {
				String multilineText = "KOT." + order.getKotId() + "\n" + order.getOrderType() + "\n" + order.getComment()
						+ "\n" + order.getTotalPrice() + "\n" + CommenFunction.timeDifference(order.getTime());

				JFXButton btnKOT = new JFXButton(multilineText);
				btnKOT.setPrefWidth(110);
				btnKOT.setPrefHeight(110);
				btnKOT.setMaxWidth(110);
				btnKOT.setMaxHeight(110);
				btnKOT.setId("" + order.getKotId());
				tile_pane_kot_order.getChildren().add(btnKOT);
				tile_pane_kot_order.setHgap(10);
				tile_pane_kot_order.setVgap(10);

				if (CommenFunction.timeDifferenceInMin(order.getTime())<=5) {
				btnKOT.setStyle("-fx-background-color: #59a61f; -fx-font-size:14; -fx-text-fill:#ffffff;");
			
				}
				if (CommenFunction.timeDifferenceInMin(order.getTime())>=6 && CommenFunction.timeDifferenceInMin(order.getTime())<=10 ) {
					btnKOT.setStyle("-fx-background-color: #FEDE00; -fx-font-size:14; -fx-text-fill:#ffffff;");
					
				}
				if (CommenFunction.timeDifferenceInMin(order.getTime())>=11) {
					btnKOT.setStyle("-fx-background-color: Red; -fx-font-size:14; -fx-text-fill:#ffffff;");
					}

				btnKOT.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {

						String old_kot_id = ((Button) event.getSource()).getId();
						String tblName = ((Button) event.getSource()).getText();
						int kot = Integer.parseInt(old_kot_id);
						kot_id = kot;
						lbl_kot_no.setText(old_kot_id);
						setKOT(kot_id);

					}

				});

			}
		}
	}

// get Item name on search and set item name for search
	@FXML
	private void getSearchItemName(KeyEvent event) {
		String itemName = txtSearchItemName.getText().trim();
		allMenu(2, itemName);

	}

	@FXML
	private void getCategory(ActionEvent event) {
		String category = chbCategory.getValue();
		allMenu(4, category);

	}

// Load Menu	

	public void allMenu(int no, String itemName) {
		paneMenuSection.getChildren().clear();

		if (no != 0) {

			List<MenuItem> list = null;

			switch (no) {

			case 1:
				list = menuDAO.loadAllMenu();

				break;

			case 2:
				list = menuDAO.searchMenuByName(itemName);

				break;

			case 3:
				String category = chbCategory.getValue();
				if (category != null) {

					list = menuDAO.searchCategory(category, itemName);
				} else {
					list = menuDAO.searchSubCategory(itemName);
				}

				break;

			case 4:
				String category1 = chbCategory.getValue();

				if (category1 != null) {
					list = menuDAO.searchCategory(itemName);
				}

				break;
			}

			if (list != null) {
				for (MenuItem m : list) {
					JFXButton btn = new JFXButton();
					btn.setText(m.getItemName() + " (" + m.getItemPrice() + ")");
					btn.setId("" + m.getMenuId());
					btn.setPrefWidth(210);
					btn.setPrefHeight(70);
					btn.setMaxWidth(210);
					btn.setMaxHeight(70);
					btn.setStyle(
							"-fx-background-color:#ffff; -fx-font-size:16; -fx-border-color:#59A61F; -fx-border-radius:5px;-fx-text-fill:#000000;");// #566573
					btn.setWrapText(true);
					btn.setTextOverrun(OverrunStyle.CLIP);
					paneMenuSection.getChildren().add(btn);
					paneMenuSection.setHgap(10);
					paneMenuSection.setVgap(10);

					btn.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {

							String t = ((Button) event.getSource()).getId();
							int menuid = Integer.parseInt(t);
							addItem(menuid);

						}
					});
				}

				no = 0;
			}
		}
	}

	public void allSubCategory() {
		sub_category_tile_pane.getChildren().clear();
		List<SubCategory> list = subCateDAO.getActiveTrueSubCategory();

		if (list != null) {
			for (SubCategory tbl : list) {
				JFXButton btnsubcategory = new JFXButton(tbl.getSubCategory());
				btnsubcategory.setId("" + tbl.getSubCategory());
				btnsubcategory.setPrefWidth(175);
				btnsubcategory.setPrefHeight(20);
				btnsubcategory.setMaxWidth(175);
				btnsubcategory.setMaxHeight(20);
				btnsubcategory.setWrapText(true);
				btnsubcategory.setTextOverrun(OverrunStyle.CLIP);
				sub_category_tile_pane.setVgap(5);
				sub_category_tile_pane.getChildren().add(btnsubcategory);

				
				btnsubcategory.setStyle(
						"-fx-background-color:#ffff; -fx-font-size:14; -fx-border-color: #EAECEE; -fx-border-width: 0px 0px 1px 0px;-fx-text-fill:#000000;");// #566573

				btnsubcategory.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {

						String tableID = ((Button) event.getSource()).getId();
						String categoryname = ((Button) event.getSource()).getText();
						allMenu(3, categoryname);

					}

				});

			}
		}
	}

// load bill item on BillTable

	private void addItem(int menuID) {

		MenuItem menu = menuDAO.getMenu(menuID);

		Map<String, Object> data = new HashMap<>();
		data.put("menu_id", menu.getMenuId());
		data.put("item_name", menu.getItemName());
		data.put("item_price", menu.getItemPrice());
		data.put("qty", 1);

		sale_tbl.getItems().addAll(data);

		qtyButtonAdd();
		removebtn();
		countTotal();
	}

	private void openKOTItem(int kot_item_kot_id) {
		sale_tbl.getItems().clear();
		ObservableList<KOTItem> list = kot_item_dao.getAll(kot_item_kot_id);
		if (list != null) {

			for (KOTItem item : list) {
				Map<String, Object> data = new HashMap<>();
				data.put("menu_id", item.getKotItemId());
				data.put("item_name", item.getItemName());
				data.put("item_price", item.getPrice());
				data.put("qty", item.getQty());

				sale_tbl.getItems().addAll(data);

				qtyButtonAdd();
				removebtn();
			}

		}
		countTotal();
	}

	@FXML
	void sale_tbl_on_key_pressed(KeyEvent event) {
		if (event.getCode() == KeyCode.DELETE) {

			int item_id = 0;
			int index = sale_tbl.getSelectionModel().getSelectedIndex();
			if (index >= 0) {

				item_id = col_itemID.getCellData(index);

				Map selectedPerson = sale_tbl.getSelectionModel().getSelectedItem();
				sale_tbl.getItems().remove(selectedPerson);

				qtyButtonAdd();
				removebtn();
				countTotal();

			} else {
				return;
			}

		}
	}

	private void loadBill() {

		col_itemID.setCellValueFactory(new MapValueFactory<>("menu_id"));
		col_ItemName.setCellValueFactory(new MapValueFactory<>("item_name"));
		col_ItemPrice.setCellValueFactory(new MapValueFactory<>("item_price"));
		col_Qty.setCellValueFactory(new MapValueFactory<>("qty"));
		// countTotal();

	}

// Count Total Amount of items on Bill	
	private double countTotal() {
		double count = 0;

		for (int rowIndex = 0; rowIndex < sale_tbl.getItems().size(); rowIndex++) {
			int item_id = col_itemID.getCellData(rowIndex);
			int qty = col_Qty.getCellData(rowIndex);
			double price = col_ItemPrice.getCellData(rowIndex);

			double total = price * qty;
			count += total;
		}

		lable_total_sale.setText("" + count);

		return count;
	}

	// Load Category on choice box
	public void loadCategory() {

		chbCategory.getItems().clear();

		List<Category> list = catDAO.getActiveTrueCategory();
		ObservableList data = FXCollections.observableArrayList();

		for (Category d : list) {

			data.add(d.getCategory());
		}

		chbCategory.setItems(data);

	}

	private void selectTable(String tbl_id, String tbl_name) {
		if (tbl_id != null) {
			lable_table.setText(tbl_name);
			lable_table.setId(tbl_id);
			radiusBtnDineIn.setSelected(true);
			chb_cash_and_delivery.setVisible(false);
		} else {
			allNotifi.showErrorNotification("Dine-In Order", "Please Select a Table");
		}
	}

	private void newKOTOrder() {

		if (sale_tbl.getItems().isEmpty()) {
			allNotif.showErrorNotification("Save KOT", "Please Selecte Item First");
		} else {
			int new_kot_id = kot_dao.kotId();
			total = Double.parseDouble(lable_total_sale.getText());
			boolean status = kot_dao.kotGenrate(new_kot_id, getSelectedRadioButtonValue(toggleGroup), getOrderType(), total);
			if (status == true) {
				saveItems(new_kot_id);
				allTables();
				chb_cash_and_delivery.setVisible(false);

			} else {
				allNotif.showErrorNotification("KOT Genrate", "KOT Not Saved Please Try Again");
			}

		}
	}




	private String getOrderType() {
		String order_type = "";

		boolean dine_ine = radiusBtnDineIn.isSelected();
		boolean dilivery = radiusBtnDelivery.isSelected();
		boolean takeaway = radiusBtnTakeaway.isSelected();

		if (dine_ine == true) {
			order_type = lable_table.getText().trim();

		} else if (dilivery == true) {
			String ord = chb_cash_and_delivery.getValue();
			if (ord != null) {
				order_type = ord;
				
			} else {
				order_type = "";
			}

		} else if (takeaway == true) {
			order_type = "Parcel";

		}

		return order_type;

	}

	
	private void saveItems(int new_kotid) {
		boolean status = false;

		for (int i = 0; i < sale_tbl.getItems().size(); i++) {

			int item_id = col_itemID.getCellData(i);
			String item_name = col_ItemName.getCellData(i);
			int qty = col_Qty.getCellData(i);
			double price = col_ItemPrice.getCellData(i);

			double total = price * qty;

			status = kot_item_dao.save(new_kotid, item_name, qty, price, total);

		}

		if (status == true) {
			loadFunction();
			allNotif.showNotification("KOT Genrate", "KOT Saved Successfully");
			kotPrint(new_kotid);
			clearFiled();

		}
	}

	private void updateItems(int updat_kotId) {
		if (updat_kotId != 0) {
			boolean status = false;
			boolean kotStatus = false;
			for (int i = 0; i < sale_tbl.getItems().size(); i++) {
				int item_id = col_itemID.getCellData(i);
				String item_name = col_ItemName.getCellData(i);
				int qty = col_Qty.getCellData(i);
				double price = col_ItemPrice.getCellData(i);

				double total = price * qty;

				kotStatus = kot_item_dao.checkKOT(updat_kotId, item_id);
				if (kotStatus == true) {
					status = kot_item_dao.update(updat_kotId, item_id, item_name, qty, price, total);
				} else {
					status = kot_item_dao.save(updat_kotId, item_name, qty, price, total);
				}

			}
			KOT oldKOT = kot_dao.openKOT(updat_kotId);
			boolean st1 = kot_dao.update(updat_kotId, getSelectedRadioButtonValue(toggleGroup), getOrderType(),
					oldKOT.getTotalPrice() + countTotal());
			if (st1 == true) {

				allNotif.showNotification("KOT", "KOT Update Successfully");
				loadFunction();
				kotPrint(updat_kotId);
				clearFiled();

			}
		}
	}

	private void saveNewKOT() {
		if (kot_id != 0) {
			boolean kot_status = kot_dao.checkKOTID(kot_id);
			if (kot_status == true) {
				Optional<ButtonType> conf = allNotifi.ConfirmationNotify("KOT Update",
						"Are You Sure You Want To Update KOT", "If Yes Click on Ok if No Click on Cancle");
				if (conf.get() == ButtonType.OK) {

					updateItems(kot_id);

				}

			} else {

			}
		} else {
			newKOTOrder();

		}
	}

	

	private void qtyButtonAdd() {
		col_Qty.setCellFactory(column -> new TableCell<Map, Integer>() {

			final Button plusButton = new Button("+");
			final Label quantityLabel = new Label();
			final Button minusButton = new Button("-");

			{
				HBox buttons = new HBox(5, plusButton, quantityLabel, minusButton); // Space between buttons
				buttons.setAlignment(Pos.CENTER);
				setGraphic(buttons);

				plusButton.setOnAction(event -> {
					Map item = getTableView().getItems().get(getIndex());
					int newQuantity = (int) item.get("qty") + 1;
					item.put("qty", newQuantity);
					quantityLabel.setText(String.valueOf(newQuantity));
					countTotal();
				});

				minusButton.setOnAction(event -> {
					Map item = getTableView().getItems().get(getIndex());
					int currentQuantity = (int) item.get("qty");
					if (currentQuantity > 1) { // Minimum quantity is 1
						int newQuantity = currentQuantity - 1;
						item.put("qty", newQuantity);
						quantityLabel.setText(String.valueOf(newQuantity));
						countTotal();
					} else {

					}

				});

				plusButton.setStyle("-fx-background-color: #3b71ca;  -fx-text-fill:#ffff;");
				minusButton.setStyle("-fx-background-color: #3b71ca; -fx-text-fill:#ffff;");
				// -fx-font-size:14;
			}

			@Override
			protected void updateItem(Integer item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					Map currentItem = getTableView().getItems().get(getIndex());
					quantityLabel.setText(String.valueOf(currentItem.get("qty")));
					quantityLabel.setAlignment(Pos.CENTER);
				}
			}
		});
	}

	private void removebtn() {

		col_remove.setCellFactory(column -> new TableCell<Map, Button>() {

			final Button remove = new Button("X");

			{
				remove.setOnAction(event -> {
					Map currentItem = getTableView().getItems().get(getIndex());
					sale_tbl.getItems().remove(currentItem);

					qtyButtonAdd();
					countTotal();

				});

				remove.setStyle("-fx-background-color:RED; -fx-font-size:10; -fx-text-fill:#FFFFFF;");

			}

			@Override
			protected void updateItem(Button item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(remove);
				}
			}
		});
	}

	public void loadPaymentMethod() {

		chb_cash_and_delivery.getItems().clear();

		List<PaymentMethod> list = pmDAO.getTrueAll();
		ObservableList data = FXCollections.observableArrayList();

		for (PaymentMethod d : list) {

			data.add(d.getPaymentMethod());
		}

		chb_cash_and_delivery.setItems(data);
		chb_cash_and_delivery.setValue("Payment");
		chb_cash_and_delivery.setVisible(true);
	}

	public void loadOrderType() {

		chb_cash_and_delivery.getItems().clear();
		List<OrderType> list = otDAO.getTrueOrderType();
		ObservableList data = FXCollections.observableArrayList();

		for (OrderType d : list) {

			data.add(d.getOrderType());
		}

		chb_cash_and_delivery.setItems(data);
		chb_cash_and_delivery.setValue("Order Type");
		chb_cash_and_delivery.setVisible(true);

	}

	private void clearFiled() {
		lable_table.setText("");
		radiusBtnDineIn.setSelected(true);
		lable_total_sale.setText("");
		chb_cash_and_delivery.setVisible(false);
		btn_radious_payment.setSelected(true);
		sale_tbl.getItems().clear();
		kot_id = 0;
		sale_id = 0;
		lbl_kot_no.setText("" + kot_dao.kotId());
	}

	private void loadFunction() {
		allTables();
		kotOrderAll();
	}

	private String getPayment() {

		String paymethod = "";

		boolean pay = btn_radious_payment.isSelected();

		if (pay == true) {
			paymethod = "Cash";

		} else {
			paymethod = chb_cash_and_delivery.getValue();

		}

		return paymethod;

	}

	private void billSetteled(int kotid) {
		if (kotid != 0) {
			sale_id = saledao.saleId();
			KOT kot = kot_dao.openKOT(kotid);
			double kotTotal = kot.getTotalPrice();
			double discountAMT = disDAO.getDiscountAmount(kotTotal);
			double taxAMT = taxDeDAO.getTaxTotal(kotTotal);
			double taxPercent = taxDeDAO.taxGet();
			double grandtotal = kotTotal - discountAMT + taxAMT;
			boolean st = saledao.add(sale_id, kot.getKotOrder(), kot.getOrderType(), getPayment(), kotTotal,
					discountAMT, taxPercent, grandtotal);
			if (st == true) {
				boolean st1 = kot_dao.updateKOTStatus(kotid);
				if (st1 == true) {
					kot_item_dao.updateKOTItemStatus(kotid);
					billItemSetteld(sale_id, kotid);
				}

			}
		} else {
			allNotif.showErrorNotification("Bill Details", "Please Select ORDER");
		}

	}

	private void billItemSetteld(int sale_id, int kotid) {

		if (kotid != 0) {
			boolean status = false;
			ObservableList<KOTItem> list = kot_item_dao.getAll(kotid);
			if (list != null) {

				for (KOTItem item : list) {
					status = sale_item_dao.save(sale_id, item.getItemName(), item.getQty(), item.getPrice(),
							item.getItemTotal());

				}

				if (status == true) {
					kot_item_dao.updateKOTItemStatus(kotid);
					allNotif.showNotification("Bill Setteled", "Bill Setteled Successfully");
					loadFunction();
					BillFormat.billView(sale_id);
					RecipeDAO.minimizStock(sale_id);
					clearFiled();

				}

			}
		}
	}

	public void kotPrint(int kot_id) {

		if (sale_tbl.getItems().isEmpty()) {

		} else {
			ObservableList<KOTItem> depList = FXCollections.observableArrayList();

			for (int i = 0; i < sale_tbl.getItems().size(); i++) {
				String item_name = col_ItemName.getCellData(i);
				int qty = col_Qty.getCellData(i);

				KOTItem kot_item = new KOTItem();
				kot_item.setItemName(item_name);
				kot_item.setQty(qty);

				depList.add(kot_item);

			}

			BillFormat.kotPrint(kot_id, getSelectedRadioButtonValue(toggleGroup), getOrderType(), depList, CommenFunction.getTime());

		}

	}

	

	private void setKOT(int kotId) {
		KOT kot = kot_dao.openKOT(kotId);
		lable_total_sale.setText("" + kot.getTotalPrice());
		selectRadioButton(kot.getKotOrder());
		
		if (kot.getKotOrder().equals("Dine-In")) {
			lable_table.setText(kot.getOrderType());
			chb_cash_and_delivery.setVisible(false);

		} else if (kot.getKotOrder().equals("Takeaway")) {
			chb_cash_and_delivery.setVisible(false);
			lable_table.setText("");

		} else if (kot.getKotOrder().equals("Delivery")) {
			chb_cash_and_delivery.setVisible(true);
			loadOrderType();
			chb_cash_and_delivery.setValue(kot.getOrderType());
			lable_table.setText("");
			
		}
		
	}

	private void amtSettled(int kotId) {

		KOT sale = KOTDAO.openKOT(kotId);

		Stage popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.initStyle(StageStyle.UTILITY);
		popupStage.setTitle("Payment");

		Label lableTotalAmt = new Label("Total Bill");
		lableTotalAmt.setStyle("-fx-font-size:14;");
		
		double discountAMT = disDAO.getDiscountAmount(sale.getTotalPrice());
		double taxAMT = taxDeDAO.getTaxTotal(sale.getTotalPrice());
		double chagrges = AdditionalChargesDAO.getTotalCharges();
		double grandtotal = sale.getTotalPrice() - discountAMT + taxAMT +chagrges;
		
		Label lableTotalAmt1 = new Label("" + grandtotal);
		lableTotalAmt1.setStyle("-fx-font-size:18;");

		Label amountLabel = new Label("Customer Pay Amount");
		amountLabel.setStyle("-fx-font-size:14;");

		amountTextField = new TextField();
		amountTextField.setMaxWidth(200);

		Label lblreturnAmount = new Label();
		lblreturnAmount.setText("Return to Customer Amount");
		lblreturnAmount.setStyle("-fx-font-size:14;");

		Label returnAmount = new Label("" + 0);
		returnAmount.setStyle("-fx-font-size:18;");

		amountTextField.setOnKeyReleased(event -> {
			String payAmt = amountTextField.getText().trim();
			if (!payAmt.matches("-?\\d+(\\.\\d+)?") || payAmt.isEmpty()) {
				returnAmount.setText("0");
				return;

			}

			else {
				double getPayAmt = Double.parseDouble(amountTextField.getText().trim());
				double returnPayAmt = getPayAmt - grandtotal;
				returnAmount.setText("" + returnPayAmt);

			}

		});

		Button payButton = new Button("Pay Settled");
		payButton.setStyle("-fx-background-color:RED; -fx-font-family: Verdana ; -fx-font-size:14; -fx-text-fill:#FFFFFF;");
		payButton.setMinWidth(175);
		payButton.setMinHeight(35);

		payButton.setOnAction(event -> {

			String payAmt = amountTextField.getText().trim();

			if (payAmt.isEmpty() || payAmt.matches("-?\\d+(\\.\\d+)?")) {
				popupStage.close();
				billSetteled(kotId);
				
			}
			else
			{
				AllNotifications.error("Customer Payment", "Please Enter Customer Payment", "");
				return;
			
			}
			
		});

		VBox popupLayout = new VBox(10);
		popupLayout.getChildren().addAll(lableTotalAmt, lableTotalAmt1, amountLabel, amountTextField, lblreturnAmount,
				returnAmount, payButton);
		popupLayout.setAlignment(Pos.CENTER);
		popupLayout.setStyle("-fx-background-color: white;");

		Scene popupScene = new Scene(popupLayout, 300, 250);
		popupStage.setScene(popupScene);
		popupStage.show();
	}
	
	private void reloadAfterOneSecond()
	{
		 Timer timer = new Timer();

	        // Schedule the method call every 1 second
	        timer.scheduleAtFixedRate(new TimerTask() {
	            @Override
	            public void run() {
	                 Platform.runLater(() -> kotOrderAll());
	                Platform.runLater(() -> allTables());
	               
	               
	            }
	        }, 0, 1000); // 1000 milliseconds = 1 second
	}
//kotOrderAll(); allMenu(1, "");
}
