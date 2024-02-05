package TaxManagment;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import com.restaurant.DAO.TaxManagmentDAO;
import com.restaurant.Entity.Tax;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import com.jfoenix.controls.JFXTextField;

public class TaxManagmentController implements Initializable {

	AllNotifications allNotifi = new AllNotifications();
	TaxManagmentDAO tdDAO = new TaxManagmentDAO();
	
	int taxId = 0;
	private String tax_name;
	private double taxPercent;
	private boolean status;

	@FXML
	private CheckBox chbTaxActive;

	@FXML
	private TableColumn<Tax, Double> col_Tax;

	@FXML
	private TableColumn<Tax, Boolean> col_TaxActive;;

	@FXML
	private TableColumn<Tax, String> col_taxName;

    @FXML
    private TableColumn<Tax, Integer> srNo;
    
	@FXML
	private TableColumn<Tax, Integer> col_id;

    @FXML
    private TableView<Tax> tblTaxManagment;

    @FXML
    private JFXTextField txttaxName;

    @FXML
    private JFXTextField txtTaxPercent;

	

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
				if(taxId!=0)
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
				if(taxId!=0)
				{
					delete();
				}
				
			}
	    }
	  
	@FXML
	private void getSelectedRow(MouseEvent event) {
		int index = tblTaxManagment.getSelectionModel().getSelectedIndex();
		if (index >= 0) {
			txttaxName.setText(col_taxName.getCellData(index).toString());
			txtTaxPercent.setText(col_Tax.getCellData(index).toString());
			chbTaxActive.setSelected(col_TaxActive.getCellData(index));
			taxId = col_id.getCellData(index);

		} else {
			return;
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		showTaxAndDescount();
	}

	// ---------------------------------------------
	
	private void clearFileds()
	{
		txttaxName.clear();
		txtTaxPercent.clear();
		chbTaxActive.setSelected(false);
		
	}

	private void showTaxAndDescount() {
		ObservableList<Tax> list = tdDAO.getAllTax();
		if(list!=null)
		{
		srNo.setCellValueFactory(cellData -> new SimpleIntegerProperty(tblTaxManagment.getItems().indexOf(cellData.getValue()) + 1).asObject());	
		col_id.setCellValueFactory(new PropertyValueFactory<Tax, Integer>("taxId"));
		col_taxName.setCellValueFactory(new PropertyValueFactory<Tax, String>("taxName"));
		col_Tax.setCellValueFactory(new PropertyValueFactory<Tax, Double>("taxPercent"));
		col_TaxActive.setCellValueFactory(new PropertyValueFactory<Tax, Boolean>("status"));
		tblTaxManagment.setItems(list);
		}
	}
	
	
	private void save()
	{
		tax_name = txttaxName.getText().trim();
		String percent = txtTaxPercent.getText().trim();
		status = chbTaxActive.isSelected();
		
		if(tax_name.isEmpty() || tax_name==null)
		{
			AllNotifications.error("Tax Management", "Enter Tax Name", "");
			return;
		}
		if(percent.isEmpty() || percent==null || !percent.matches("-?\\d+(\\.\\d+)?"))
		{
			AllNotifications.error("Tax Management", "Enter Tax Percent", "");
			return;
		}
		try {
			taxPercent = Double.parseDouble(percent);
			boolean st1 = tdDAO.addTax(tax_name, taxPercent, status);
			if (st1 == true) {
				showTaxAndDescount();
				clearFileds();
				allNotifi.showNotification("Tax Management", "Tax Added Successfully");

			} else {
				allNotifi.error("Tax Management", "Tax Not Added","");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
			
			
		
	}
	
	
	private void update()
	{
		tax_name = txttaxName.getText().trim();
		String percent = txtTaxPercent.getText().trim();
		status = chbTaxActive.isSelected();
		
		if(tax_name.isEmpty() || tax_name==null)
		{
			AllNotifications.error("Tax Management", "Enter Tax", "");
			return;
		}
		if(percent.isEmpty() || percent==null || !percent.matches("-?\\d+(\\.\\d+)?"))
		{
			AllNotifications.error("Tax Management", "Enter Tax Percent", "");
			return;
		}
		try {
			
			if (taxId != 0) {
				Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Tax Management", "Are You Sure you Want to Update This Record", "");
				if (conf.get() == ButtonType.OK) {		
					taxPercent = Double.parseDouble(percent);
					boolean st1 = tdDAO.updateTax(taxId,tax_name, taxPercent, status);
					if (st1 == true) {
						showTaxAndDescount();
						clearFileds();
						allNotifi.showNotification("Tax Management", "Tax Updated Successfully");
						

					} else {
						allNotifi.error("Tax Management", "Tax Not Updated","");
					}
			} else {
				allNotifi.error("Tax Management", "Select Tax", "");
			}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	private void delete()
	{
		if (taxId != 0) {
			Optional<ButtonType> conf = allNotifi.ConfirmationNotify("Tax Management", "Are You Sure you Want to Delete This Record", "");
				if (conf.get() == ButtonType.OK) {
				boolean st1 = tdDAO.deleteTax(taxId);
				if (st1 == true) {
					showTaxAndDescount();
					clearFileds();
					allNotifi.showNotification("Tax Management", "Tax Deleted Successfully");

				} else {
					allNotifi.error("Tax Management", "Tax Not Deleted","");
				}
			}
		} else {
			allNotifi.error("Tax Management", "Select Tax", "");
		}

	}
	
}
