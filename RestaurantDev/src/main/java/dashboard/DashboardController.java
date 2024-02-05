package dashboard;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import com.restaurant.DAO.EmpAccessDAO;
import com.restaurant.DAO.EmployeeDAO;
import AllNotivications.AllNotifications;
import SettingsManagment.SettingName;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;


public class DashboardController implements Initializable {

		
	private static String userId;
	
	 @FXML
	    private HBox hboxMainMenu;
	

	@FXML
	private MenuItem category;

	@FXML
	private Label lblUser;

	@FXML
	private Label lbl_nav_logo;

	@FXML
	private MenuItem menu;

	@FXML
	private AnchorPane paneDashboard;

	@FXML
	public AnchorPane paneOrder;

	@FXML
	public StackPane stackPane;

	@FXML
	private Label lbl_dashboard;

	@FXML
	private Label lbl_order;

	@FXML
	private Label lbl_order_manage;

	@FXML
	private Label lbl_report;

	@FXML
	private Label lbl_logo_dashboard;

	Image image = new Image("/images/newLogo1.png");
	ImageView imageView = new ImageView(new Image("/images/nav-logo.png"));
	ImageView imageView2 = new ImageView(new Image("/images/welcomelogo.png"));

	public void initialize(URL arg0, ResourceBundle arg1) {
				 
		lbl_nav_logo.setGraphic(imageView);
		imageView.setFitWidth(130);
		imageView.setFitHeight(40);

		lbl_logo_dashboard.setGraphic(imageView2);
		imageView2.setFitWidth(700);
		imageView2.setFitHeight(150);
		lblUser.setText(userId);
		
	}
	
	

	@FXML
	private void btnDashBoard(ActionEvent event) {
		openDashboard();
		
	}

	@FXML
	private void btnOrder(ActionEvent event) {
		openOrder();
		
	}

	@FXML
	void btnKOT(ActionEvent event) {
		openKOT();
	}
	
	@FXML
	private void btnReport(ActionEvent event) {
		openReport();
		
		
	}

	@FXML
	private void openCategory(ActionEvent event) {
		openCategory();
		
	}

	@FXML
	private void openSubCategory(ActionEvent event) {
		openSubCategory();
	}

	@FXML
	private void openMenu(ActionEvent event) {
		openMenu();
	}

	@FXML
	private void openTableManagment(ActionEvent event) {
		openTable();
	}

	@FXML
	private void openEmployeeManagment(ActionEvent event) {
		openEmployee();
	}

	@FXML
	private void openTaxManagment(ActionEvent event) {
		openTax();
	}

	@FXML
	private void openDiscountManagment(ActionEvent event) {
		openDiscount();
	}

	@FXML
	private void openCustomerManagment(ActionEvent event) {
		openCustomer();
	}

	@FXML
	private void openOrderType(ActionEvent event) {
		openOrderType();
	}

	@FXML
	void openPaymentMethod(ActionEvent event) {
		openPayment();
		
	}

	@FXML
	void btnLogOut(ActionEvent event) {

		Optional<ButtonType> conf = AllNotifications.ConfirmationNotify("LogOut", "Are You Sure You Want To LogOut",
				"If Yes Click on Ok if No Click on Cancle");
		if (conf.get() == ButtonType.OK) {
			System.exit(0);
		}
	}

	@FXML
	void openBillFormate(ActionEvent event) {
		openBillFormat();
	}

	@FXML
	void openSettings(ActionEvent event) {
		openSettings();
	}

	@FXML
	void openAdditionalCharges(ActionEvent event) {
		openAdditionalCharges();
	}

	@FXML
	void openUnit(ActionEvent event) {
		openUnint();
	}

	@FXML
	void openInventoryCategory(ActionEvent event) {
		openInventoryCategory();
	}

	@FXML
	void openInventory(ActionEvent event) {
		openInventory();
	}

	@FXML
	void openRecipeManagment(ActionEvent event) {
		openRecipe();
	}

	

	@FXML
	void open_contact(ActionEvent event) {
		openHelp();
	}

	@FXML
	void openUserAccess(ActionEvent event) {
		 openUserAccess();		
	}
	
	
	public static void setUser(String user)
	{
		userId=user;
	}
	
	public String getRole()
	{
		
		String userName = EmpAccessDAO.getUserName(lblUser.getText().trim());
		
		 String userRole = EmployeeDAO.getUserRole(userName);
		 
		return userRole;
	}
	
	private void openDashboard()
	{	
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole))
		{
			try {
				Parent fxml = FXMLLoader.load(getClass().getResource("DashboardView.fxml"));
				stackPane.getChildren().removeAll();
				stackPane.getChildren().setAll(fxml);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	
	
	private void openOrder()
	{	 
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
		{
			try {
				Parent fxml = FXMLLoader.load(getClass().getResource("orderView.fxml"));
				stackPane.getChildren().removeAll();
				stackPane.getChildren().setAll(fxml);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	
	private void openKOT()
	{	 
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
		{
			try {
				Parent fxml = FXMLLoader.load(getClass().getResource("kot.fxml"));
				stackPane.getChildren().removeAll();
				stackPane.getChildren().setAll(fxml);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	
	private void openReport()
	{
		 
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole))
		{
			try {
				Parent fxml = FXMLLoader.load(getClass().getResource("reportView.fxml"));
				stackPane.getChildren().removeAll();
				stackPane.getChildren().setAll(fxml);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	private void openCategory()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
		{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/category/category.fxml"));
				Scene scene = new Scene(root, 800, 450);
				s.setTitle("Category Management");
				s.getIcons().add(image);
				s.setScene(scene);
				s.setResizable(false);
				s.show();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	private void openSubCategory()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
		{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/SubCategory/subcategory.fxml"));
				Scene scene = new Scene(root, 800, 450);
				s.setTitle("Sub Sub Category Management");
				s.setScene(scene);
				s.getIcons().add(image);
				s.setResizable(false);
				s.show();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	private void openMenu()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
		{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/menuItem/menuitem.fxml"));
				Scene scene = new Scene(root, 1100, 550);
				s.setTitle("Menu Management");
				s.setScene(scene);
				s.getIcons().add(image);
				s.setResizable(false);
				s.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	private void openTable()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
		{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/TableManagment/table.fxml"));
				Scene scene = new Scene(root, 800, 450);
				s.setTitle("Table Management");
				s.setScene(scene);
				s.getIcons().add(image);
				s.setResizable(false);
				s.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	private void openEmployee()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole))
		{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/employee/AddEmployee.fxml"));
				Scene scene = new Scene(root, 1000, 550);
				s.setTitle("Employee Management");
				s.setScene(scene);
				s.getIcons().add(image);
				s.setResizable(false);
				s.show();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	private void openTax()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
		{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/TaxManagment/taxManagment.fxml"));
				Scene scene = new Scene(root, 700, 400);
				s.setTitle("Tax Management");
				s.getIcons().add(image);
				s.setScene(scene);
				s.setResizable(false);
				s.show();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	
	private void openDiscount()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
		{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/DiscountManagment/discountManagment.fxml"));
				Scene scene = new Scene(root, 800, 400);
				s.setTitle("Discount Management");
				s.setScene(scene);
				s.getIcons().add(image);
				s.setResizable(false);
				s.show();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	private void openCustomer()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
		{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/CustomerManagment/Customer.fxml"));
				Scene scene = new Scene(root, 1100, 550);
				s.setTitle("Customer Management");
				s.setScene(scene);
				s.getIcons().add(image);
				s.setResizable(false);
				s.show();
			} catch (IOException e) {
			
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	
	private void openOrderType()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
		{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/OnlineOrderType/onlineOrderType.fxml"));
				Scene scene = new Scene(root, 700, 400);
				s.setTitle("Order Management");
				s.setScene(scene);
				s.getIcons().add(image);
				s.setResizable(false);
				s.show();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	
	private void openPayment()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
		{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/paymentManagment/payment.fxml"));
				Scene scene = new Scene(root, 900, 400);
				s.setTitle("Payment Method Management");
				s.setScene(scene);
				s.getIcons().add(image);
				s.setResizable(false);
				s.show();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	private void openBillFormat()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
				{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/BillManagment/BillDetails.fxml"));
				Scene scene = new Scene(root, 600, 500);
				s.setTitle("Bill Formate");
				s.setResizable(false);
				s.setScene(scene);
				s.getIcons().add(image);
				s.show();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	
	private void openSettings()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
					{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/SettingsManagment/Setting.fxml"));
				Scene scene = new Scene(root, 600, 500);
				s.setTitle("Settings");
				s.getIcons().add(image);
				s.setResizable(false);
				s.setScene(scene);
				s.show();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	
	private void openAdditionalCharges()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
				{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/AdditionalChargesManagment/additionalCharges.fxml"));
				Scene scene = new Scene(root, 675, 400);
				s.setTitle("Additional Charges Management");
				s.setScene(scene);
				s.getIcons().add(image);
				s.setResizable(false);
				s.show();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	
	private void openUnint()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
				{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/Stock/Unit.fxml"));
				Scene scene = new Scene(root, 700, 400);
				s.setTitle("Unit Management");
				s.setScene(scene);
				s.getIcons().add(image);
				s.setResizable(false);
				s.show();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	
	private void openInventoryCategory()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
					{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/Stock/category.fxml"));
				Scene scene = new Scene(root, 700, 400);
				s.setTitle("Inventory Category Management");
				s.setScene(scene);
				s.getIcons().add(image);
				s.setResizable(false);
				s.show();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	
	private void openInventory()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
				{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/Stock/inventory.fxml"));
				Scene scene = new Scene(root, 1100, 550);
				s.setTitle("Inventory Management");
				s.setScene(scene);
				s.getIcons().add(image);
				s.setResizable(false);
				s.show();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	private void openRecipe()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
				{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/Stock/recipe.fxml"));
				Scene scene = new Scene(root, 850, 500);
				s.setTitle("Recipe Management");
				s.setScene(scene);
				s.getIcons().add(image);
				s.setResizable(false);
				s.show();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	
	private void openHelp()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole) || getRole().equals(SettingName.captainRole) || getRole().equals(SettingName.chefRole) || getRole().equals(SettingName.waterRole) || getRole().equals(SettingName.receptionistRole) || getRole().equals(SettingName.assistantRole))
					{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/help/contact.fxml"));
				Scene scene = new Scene(root, 600, 400);
				s.setTitle("Help Desk");
				s.setScene(scene);
				s.getIcons().add(image);
				s.setResizable(false);
				s.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
	
	private void openUserAccess()
	{
		if(getRole().equals(SettingName.adminRole) || getRole().equals(SettingName.managerRole))
		{
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/subUsers/subusers.fxml"));
				Scene scene = new Scene(root, 1000, 550);
				s.setTitle("User Access");
				s.setScene(scene);
				s.getIcons().add(image);
				s.setResizable(false);
				s.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			AllNotifications.error("User Access", "You Don't have Access for "+getRole()+" Role","");
		}
	}
	
}
