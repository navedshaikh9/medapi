package dashboard;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.control.MenuItem;
import com.jfoenix.controls.JFXButton;
import com.restaurant.DAO.KOTDAO;
import com.restaurant.DAO.KOTItemDAO;
import com.restaurant.Entity.KOT;
import com.restaurant.Entity.KOTItem;
import AllNotivications.AllNotifications;
import AllNotivications.CommenFunction;
import BillManagment.BillFormat;
import SettingsManagment.AllSettings;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXRadioButton;

public class KOTController implements Initializable {

	AllNotifications allNotif = new AllNotifications();
	KOTDAO kot_dao = new KOTDAO();
	KOTItemDAO kot_item_dao = new KOTItemDAO();
	
	@FXML
	private TableColumn<Map, Integer> col_remove;

	@FXML
	private TableColumn<Map, String> col_ItemName;

	@FXML
	private TableColumn<Map, Double> col_ItemPrice;

	@FXML
	private TableColumn<Map, Integer> col_Qty;

	@FXML
	private TableColumn<Map, Integer> col_itemID;

	@FXML
	private TableColumn<Map, Double> col_total;
	 ToggleGroup toggleGroup = new ToggleGroup();


	@FXML
	private Label lbl_created;

	@FXML
	private Label lbl_kot_no;

	@FXML
	private Label lbl_order;

	@FXML
	private Label lbl_order_type;

	@FXML
	private Label lbl_total;

	@FXML
	private TableView<Map> tbl_kot;

	@FXML
	private TilePane tile_kot_order;

	private JFXButton btnKOT;
	
	private TextField amountTextField;
	

	private int kot_id = 0;

	private int menu_id = 0;
	
	 @FXML
	    private JFXRadioButton radioBtnPlaced = new JFXRadioButton("Placed");

	    @FXML
	    private JFXRadioButton radioBtnPreparing= new JFXRadioButton("Preparing");

	    @FXML
	    private JFXRadioButton radioBtnReady= new JFXRadioButton("Ready");

	    @FXML
	    private JFXRadioButton radioBtnServed= new JFXRadioButton("Served");
	    

	@FXML
	void btn_save(ActionEvent event) {

		kot_id = Integer.parseInt(lbl_kot_no.getText());
		if (kot_id != 0) {
			updateItems(kot_id);
		}
	}

	@FXML
	void sale_tbl_on_key_pressed(KeyEvent event) {
		if (event.getCode() == KeyCode.DELETE) {

			int index = tbl_kot.getSelectionModel().getSelectedIndex();
			if (index >= 0) {

				menu_id = col_itemID.getCellData(index);
				cancelKOTItem(menu_id);
			} else {
				return;
			}

		}
	}

	@FXML
	void btn_delete_kot(ActionEvent event) {

		if (kot_id != 0) {
			
		
		if(AllSettings.getCancelItemCommentSettign())
		{
			cancelKOT(kot_id);
		}
		else
		{
			Optional<ButtonType> conf = allNotif.ConfirmationNotify("KOT Cancel",
					"Are You Sure You Want To Cancel This KOT Order", "If Yes Click on Ok if No Click on Cancel");
			if (conf.get() == ButtonType.OK) {
				deleteKOT(kot_id,"");
			} else {
				allNotif.showErrorNotification("KOT Delete", "Selete KOT First");
			}
			
		}
		
		} else {
			allNotif.showErrorNotification("KOT Order", "Select KOT Order");
		}
		
	}

	@FXML
	void btn_print(ActionEvent event) {
		
		int kotId = Integer.parseInt(lbl_kot_no.getText());
		if (kotId != 0) {
			kotPrint(kotId);
		}
	}
	
	 @FXML
	    void onRadioBtnActionPlaced(ActionEvent event) {
		 updateOrderComment(kot_id,"Placed");
	    }

	    @FXML
	    void onRadioBtnActionPreparing(ActionEvent event) {
	    	updateOrderComment(kot_id,"Preparing");
	    }

	    @FXML
	    void onRadioBtnActionReady(ActionEvent event) {
	    	updateOrderComment(kot_id,"Ready");
	    }

	    @FXML
	    void onRadioBtnActionServed(ActionEvent event) {
	    	updateOrderComment(kot_id,"Served");
	    }

	    private void kotOrderAll() {
			tile_kot_order.getChildren().clear();
			List<KOT> list = kot_dao.getAllKOTActive();
			if (list != null) {
				for (KOT order : list) {
					String multilineText = "KOT." + order.getKotId() + "\n" + order.getOrderType() + "\n"
							+ order.getComment() + "\n" + order.getTotalPrice() + "\n" + CommenFunction.timeDifference(order.getTime());

					btnKOT = new JFXButton(multilineText);
					btnKOT.setPrefWidth(110);
					btnKOT.setPrefHeight(110);
					btnKOT.setMaxWidth(110);
					btnKOT.setMaxHeight(110);
					btnKOT.setId("" + order.getKotId());
					if (CommenFunction.timeDifferenceInMin(order.getTime())<=5) {
						btnKOT.setStyle("-fx-background-color: #59a61f; -fx-font-size:14; -fx-text-fill:#ffffff;");
					
						}
						if (CommenFunction.timeDifferenceInMin(order.getTime())>=6 && CommenFunction.timeDifferenceInMin(order.getTime())<=10 ) {
							btnKOT.setStyle("-fx-background-color: #FEDE00; -fx-font-size:14; -fx-text-fill:#ffffff;");
							
						}
						if (CommenFunction.timeDifferenceInMin(order.getTime())>=11) {
							btnKOT.setStyle("-fx-background-color: Red; -fx-font-size:14; -fx-text-fill:#ffffff;");
							}
					tile_kot_order.getChildren().add(btnKOT);
					tile_kot_order.setHgap(10);
					tile_kot_order.setVgap(10);

					btnKOT.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {

							String old_kot_id = ((Button) event.getSource()).getId();
							String tblName = ((Button) event.getSource()).getText();
							int kot = Integer.parseInt(old_kot_id);
							openKOTItem(kot);
							openKOTDetils(kot);

						}

					});

				}
			}
		}

	private void openKOTItem(int kot_item_kot_id) {
		tbl_kot.getItems().clear();
		ObservableList<KOTItem> list = kot_item_dao.getAll(kot_item_kot_id);
		if (list != null) {

			for (KOTItem item : list) {
				Map<String, Object> data = new HashMap<>();
				data.put("menu_id", item.getKotItemId());
				data.put("item_name", item.getItemName());
				data.put("item_price", item.getPrice());
				data.put("qty", item.getQty());
				data.put("item_total", item.getItemTotal());

				tbl_kot.getItems().addAll(data);

				qtyButtonAdd();
				removebtn();

			}
			kot_id = kot_item_kot_id;
			lbl_kot_no.setText("" + kot_item_kot_id);
		}
		countTotal();
	}

	private double countTotal() {
		double count = 0;

		for (int rowIndex = 0; rowIndex < tbl_kot.getItems().size(); rowIndex++) {
			int item_id = col_itemID.getCellData(rowIndex);
			int qty = col_Qty.getCellData(rowIndex);
			double price = col_ItemPrice.getCellData(rowIndex);

			double total = price * qty;
			count += total;
		}

		lbl_total.setText("" + count);

		return count;
	}

	private void loadBill() {
		col_itemID.setCellValueFactory(new MapValueFactory<>("menu_id"));
		col_ItemName.setCellValueFactory(new MapValueFactory<>("item_name"));
		col_Qty.setCellValueFactory(new MapValueFactory<>("qty"));
		col_ItemPrice.setCellValueFactory(new MapValueFactory<>("item_price"));
		col_total.setCellValueFactory(new MapValueFactory<>("item_total"));

		// countTotal();

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

		col_remove.setCellFactory(column -> new TableCell<Map, Integer>() {

			final Button remove = new Button("X");

			{
				remove.setOnAction(event -> {
					Map currentItem = getTableView().getItems().get(getIndex());
					menu_id = (int) currentItem.get("menu_id");
					
					if(AllSettings.getCancelItemCommentSettign())
					{
						cancelKOTItem(menu_id);
					}
					else
					{
						Optional<ButtonType> conf = allNotif.ConfirmationNotify("KOT Item Cancel",
								"Are You Sure You Want To Cancel This Item", "If Yes Click on Ok if No Click on Cancel");
						if (conf.get() == ButtonType.OK) {
							deleteKOTItem(menu_id,"");
						}
						
					}
			
				});

				remove.setStyle("-fx-background-color:RED; -fx-font-size:10; -fx-text-fill:#FFFFFF;");

			}

			@Override
			protected void updateItem(Integer item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(remove);
				}
			}
		});
	}

	

	private void updateItems(int updat_kotId) {
		if (updat_kotId != 0) {

			boolean status = false;

			for (int i = 0; i < tbl_kot.getItems().size(); i++) {
				int item_id = col_itemID.getCellData(i);
				String item_name = col_ItemName.getCellData(i);
				int qty = col_Qty.getCellData(i);
				double price = col_ItemPrice.getCellData(i);

				double total = price * qty;

				status = kot_item_dao.update(updat_kotId, item_id, item_name, qty, price, total);

			}

			if (status == true) {
				boolean st1 = kot_dao.updateKOT(updat_kotId, countTotal());
				if (st1 == true) {
					allNotif.showNotification("KOT", "KOT Update Successfully");
					clear();
					openKOTItem(updat_kotId);

				}

			}
		}
	}

	private void openKOTDetils(int kotNO) {
		
		KOT kot = kot_dao.openKOT(kotNO);
		lbl_created.setText(kot.getTime());
		lbl_order.setText(kot.getKotOrder());
		lbl_order_type.setText(kot.getOrderType());
		lbl_total.setText("" + kot.getTotalPrice());
		selectRadioButton(kot.getComment());

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		loadBill();
		countTotal();
		kotOrderAll();
		tbl_kot.setStyle("-fx-table-cell-border-color: transparent; -fx-font-size:14;"); // Hide cell borders
		tbl_kot.setPlaceholder(new Label("No Item Selected"));
		reloadAfterOneSecond();
	        radioBtnPlaced.setToggleGroup(toggleGroup);
        radioBtnPreparing.setToggleGroup(toggleGroup);
	        radioBtnReady.setToggleGroup(toggleGroup);
        radioBtnServed.setToggleGroup(toggleGroup);
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
            return "No selection"; // Or handle the case where no radio button is selected
        }
    }

	
	private void deleteKOT(int cancelKOTId, String comment) {
				boolean status = kot_dao.deleteKOT(cancelKOTId,comment);
				if (status == true) {		
					allNotif.showNotification("KOT Delete", "KOT Delete Successfully.");
					kot_item_dao.updateCancleKOTItemAll(cancelKOTId);
					clear();
				}
			
	}

	private void clear() {
		lbl_created.setText("");
		lbl_kot_no.setText("");
		lbl_order.setText("");
		lbl_order_type.setText("");
		lbl_total.setText("");
		tbl_kot.getItems().clear();
		kot_id = 0;

		loadBill();
		kotOrderAll();
	}

	public void kotPrint(int kot_id) {

		if (tbl_kot.getItems().isEmpty()) {

		} else {
			KOT kot = KOTDAO.openKOT(kot_id);
			ObservableList<KOTItem> item = KOTItemDAO.getAll(kot_id);
			if (item != null) {
				BillFormat.kotPrint(kot_id, kot.getKotOrder(), kot.getOrderType(), item, kot.getTime());
			}
		}

	}
	
	
	private void cancelKOTItem(int kotItemId) {

		Stage popupStage = new Stage();
	    popupStage.setTitle("Cancle KOT Item");
	    popupStage.setResizable(false);

	    GridPane gridPane = new GridPane();
	    gridPane.setPadding(new Insets(20));
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setStyle("-fx-background-color: #FFFFFF;");

	    Label label = new Label("Enter Comment");
	    TextField textField = new TextField();
	    textField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #EAECEE; -fx-font-family: Verdana ; -fx-font-size:14; -fx-border-width: 0px 0px 1px 0px;");
	    textField.setPrefWidth(550);

	    Button saveButton = new Button("Save");
	    saveButton.setPrefWidth(100);
	    saveButton.setPrefHeight(30);
	    saveButton.setStyle("-fx-background-color:#3b71ca; -fx-font-family: Verdana ; -fx-font-size:14; -fx-text-fill:#FFFFFF;");
	   
	    Button cancelButton = new Button("Cancel");
	    cancelButton.setPrefWidth(100);
	    cancelButton.setPrefHeight(30);
	    cancelButton.setStyle("-fx-background-color:#3b71ca; -fx-font-family: Verdana ; -fx-font-size:14; -fx-text-fill:#FFFFFF;");

	    saveButton.setOnAction(event -> {
	    String comment = textField.getText().trim();
	    deleteKOTItem(kotItemId, comment);
	    
	    popupStage.close();
	    countTotal();
	    });

	    cancelButton.setOnAction(event ->{ 
	    	
	    	popupStage.close();
	    	countTotal();
	    	
	    });

	    HBox buttonBox = new HBox(10); // spacing between buttons
	    buttonBox.getChildren().addAll(saveButton, cancelButton);
	    buttonBox.setAlignment(Pos.CENTER_RIGHT); // Align buttons to the right

	    gridPane.add(label, 1, 0);
	    gridPane.add(textField, 1, 1);
	    gridPane.add(buttonBox, 1, 2);

	    Scene scene = new Scene(gridPane, 550, 150);
	    popupStage.setScene(scene);

	    popupStage.centerOnScreen();
	    popupStage.show();
	}
	
	
private void deleteKOTItem(int itemId, String comment) {
		
		boolean st = kot_item_dao.checkKOT(kot_id, itemId);
		if (st == true) {
		boolean st1 = kot_item_dao.updateCancelKOTItem(kot_id, itemId, comment);
				
				if (st1 == true) {
					AllNotifications.showNotification("KOT Item Delete", "KOT Item Cancle Successfully");
					openKOTItem(kot_id);
					loadBill();
					updateItems(kot_id);
					boolean st2 = kot_item_dao.checkKOT(kot_id);
					if (st2 == false) {
					kot_dao.deleteKOT(kot_id,"");
					clear();
					}

				} else {
					AllNotifications.showErrorNotification("KOT Item Delete", "KOT Item Not Cancel");
				}
		} else {

			AllNotifications.showErrorNotification("KOT Item Delete", "KOT Item Is Not Available");
		}

		countTotal();
	}


private void cancelKOT(int kotId) {

	Stage popupStage = new Stage();
    popupStage.setTitle("Cancle KOT Item");
    popupStage.setResizable(false);

    GridPane gridPane = new GridPane();
    gridPane.setPadding(new Insets(20));
    gridPane.setHgap(10);
    gridPane.setVgap(10);
    gridPane.setStyle("-fx-background-color: #FFFFFF;");

    Label label = new Label("Enter Comment");
    TextField textField = new TextField();
    textField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #EAECEE; -fx-font-family: Verdana ; -fx-font-size:14; -fx-border-width: 0px 0px 1px 0px;");
    textField.setPrefWidth(550);

    Button saveButton = new Button("Save");
    saveButton.setPrefWidth(100);
    saveButton.setPrefHeight(30);
    saveButton.setStyle("-fx-background-color:#3b71ca; -fx-font-family: Verdana ; -fx-font-size:14; -fx-text-fill:#FFFFFF;");
   
    Button cancelButton = new Button("Cancel");
    cancelButton.setPrefWidth(100);
    cancelButton.setPrefHeight(30);
    cancelButton.setStyle("-fx-background-color:#3b71ca; -fx-font-family: Verdana ; -fx-font-size:14; -fx-text-fill:#FFFFFF;");

    saveButton.setOnAction(event -> {
    String comment = textField.getText().trim();
    deleteKOT(kotId,comment);
    
    popupStage.close();
    countTotal();
    });

    cancelButton.setOnAction(event ->{ 
    	
    	popupStage.close();
    	countTotal();
    	
    });

    HBox buttonBox = new HBox(10); // spacing between buttons
    buttonBox.getChildren().addAll(saveButton, cancelButton);
    buttonBox.setAlignment(Pos.CENTER_RIGHT); // Align buttons to the right

    gridPane.add(label, 1, 0);
    gridPane.add(textField, 1, 1);
    gridPane.add(buttonBox, 1, 2);

    Scene scene = new Scene(gridPane, 550, 150);
    popupStage.setScene(scene);

    popupStage.centerOnScreen();
    popupStage.show();
}

	private void updateOrderComment(int kotId,String comment)
	{
		if(kotId!=0 && comment !=null)
		{
			boolean updateStatus = KOTDAO.updateOrderComment(kotId, comment);
			if(updateStatus==true)
			{
				AllNotifications.showNotification("KOT", "Order Status Updated Successfully");
			}
			
		}
	}
	
	private void reloadAfterOneSecond()
	{
		 Timer timer = new Timer();

	        // Schedule the method call every 1 second
	        timer.scheduleAtFixedRate(new TimerTask() {
	            @Override
	            public void run() {
	                 Platform.runLater(() -> kotOrderAll());
	           
	               
	            }
	        }, 0, 1000); // 1000 milliseconds = 1 second
	}
}
