package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.restaurant.DAO.EmpAccessDAO;
import com.restaurant.DAO.TrialDAO;
import com.restaurant.Entity.Trial;
import AllNotivications.AllNotifications;
import AllNotivications.CommenFunction;
import dashboard.DashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class AdminLoginController implements Initializable {

	AllNotifications allNotify = new AllNotifications();
	CommenFunction cf = new CommenFunction();
	@FXML
	private AnchorPane adminloginPane;

	@FXML
	private Button btnClose;

	@FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXButton btnLogin;


	@FXML
	private Label lbl_logo;


	Image image = new Image("/images/newLogo1.png");
	ImageView imageView = new ImageView(new Image("/images/LoginWindowLogo.png"));

	@FXML
	private void onEnter(ActionEvent event) {
		login();
	}

	@FXML
	void btn_login_on_key_pressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			btnLogin.requestFocus();
		}
	}

	@FXML
	void on_key_pressed_pass(KeyEvent event) {
		if (event.getCode() == KeyCode.TAB) {
			btnLogin.requestFocus();
		}
	}

	@FXML
	void on_key_pressed_user(KeyEvent event) {
		if (event.getCode() == KeyCode.TAB) {
			txtPassword.requestFocus();
		}
	}

	@FXML
	public void btnClose(ActionEvent event) {

		System.exit(0);
	}

	@FXML
	private void btnsubmit(ActionEvent event) {

		login();

	}

	@FXML
	void btnXClose(ActionEvent event) {
		Stage adminLoginStage = (Stage) adminloginPane.getScene().getWindow();
		adminLoginStage.close();
	}

	private void login() {
		String username = txtUsername.getText();
		String password = txtPassword.getText();

		if (EmpAccessDAO.getUserLoginStatus(username, password) == true) {
			DashboardController.setUser(username);
			try {
				Stage s = new Stage();
				Parent root = FXMLLoader.load(getClass().getResource("/dashboard/dashboard.fxml"));
				Scene scene = new Scene(root, 1380, 700);
				s.setTitle("Dashboard");
				s.setScene(scene);
				s.getIcons().add(image);
				s.show();
				Stage adminLoginStage = (Stage) adminloginPane.getScene().getWindow();
				adminLoginStage.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			AllNotifications.error("MedResto", "Please Enter Correct User and Password", "");
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		lbl_logo.setGraphic(imageView);
		imageView.setFitWidth(120);// Set the width of the image
		imageView.setFitHeight(120);
	}

	private void openStartWindow() {
		Stage stage = new Stage();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/InfoWindow.fxml"));
			Scene scene = new Scene(root, 450, 350);
			stage.setTitle("Start Trial");
			stage.getIcons().add(image);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

}
