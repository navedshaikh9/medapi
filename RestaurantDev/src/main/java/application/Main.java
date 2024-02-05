package application;
	
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import com.restaurant.DAO.CategoryDAO;
import com.restaurant.DAO.MenuItemDAO;
import com.restaurant.DAO.TableManagmentDAO;
import com.restaurant.DAO.TaxManagmentDAO;
import com.restaurant.DAO.TrialDAO;
import com.restaurant.Entity.Trial;
import SettingsManagment.SettingController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;



public class Main extends Application {
	
	 Image image = new Image("/images/newLogo1.png");
	 
	@Override
	public void start(Stage stage) {
		try {
			Trial trayelStatus = TrialDAO.checkTrayel();
			if (trayelStatus != null) {
				boolean status = TrialDAO.checkTrayelStatus();
				if (status == true) {
	
			Parent root = FXMLLoader.load(getClass().getResource("AdminLogin.fxml"));
			Scene scene = new Scene(root,500,500);
		          
	           
	            stage.getIcons().add(image);
	            stage.initStyle(StageStyle.UNDECORATED);
		        stage.setTitle("Admin Login");
		        stage.setResizable(false);
		        stage.setScene(scene);
		        stage.show();
				} else {

					updatePlan();
				}
			} else {

				updatePlan();

			}
	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		 database();	
		loadAllClass();
		
        launch(args);
       
       
	}
	
	private static void loadAllClass()
	{
		
		// Wetting Creation 
		SettingController setting = new SettingController();
		setting.settingCreation();
				
		//LoadTable
  		TableManagmentDAO tbl = new TableManagmentDAO();
  		tbl.loadAllTable();
  		
  		//LoadMenu
  		MenuItemDAO menu = new MenuItemDAO();
  		menu.loadAllMenu();
  		
  		//LoadAll Category
  		CategoryDAO cate = new CategoryDAO();
  		cate.getAllCategory();
  		
  		//Load Tax And Discount
  		TaxManagmentDAO taxDiscount = new TaxManagmentDAO();
  		taxDiscount.getAllTax();
  		
  		
 	}
	
	
	public static void database()
	{
		String appFolderName = "medresto";

         File appFolder = new File(System.getProperty("user.home"), appFolderName);
     if (!appFolder.exists()) {
            if (appFolder.mkdir()) {
            } else {
                 return;
            }
        }

        String dbFileName = "medresto.db";
        String dbFilePath = appFolder.getAbsolutePath() + File.separator + dbFileName;

        File dbFile = new File(dbFilePath);
        
        if (dbFile.exists()) {
            return;
        }
  
	}
	
	private void updatePlan() {

		Trial check = TrialDAO.checkTrayel();
		if (check != null && check.getEnd().isBefore(LocalDate.now())) {
			if (check.isStatus() == true) {
				TrialDAO.update(check.getTrailId());
			} else {
				openStartWindow();
			}

		} else {
			openStartWindow();
		}
	}

	private void openStartWindow() {
		Stage stage = new Stage();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/InfoWindow.fxml"));
			Scene scene = new Scene(root, 450, 350);
			stage.setTitle("Start Trial");
			stage.setScene(scene);
			 stage.getIcons().add(image);
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	

}
