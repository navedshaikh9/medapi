package AllNotivications;

import java.util.Optional;

import org.controlsfx.control.Notifications;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class AllNotifications {
	
	
	public static Optional<ButtonType> ConfirmationNotify(String title,String header,String content)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		Optional<ButtonType> result=alert.showAndWait();
		
		return result;
		
	}
	
	
	
	public static void error(String title,String header,String content)
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
		
	}
	
	
	public static void info(String title,String header,String content)
	{ 	
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
		
	}
	
	public static void warning(String title,String header,String content)
	{ 	
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
		
	}
	
	public static void showNotification(String titel, String text) {
		Image img = new Image("/images/ok.png");
		ImageView imgView = new ImageView(img);
		imgView.setFitWidth(50);
		imgView.setFitHeight(50);
        Notifications.create()
                .title(titel)
                .text(text)
                .graphic(imgView)
                .darkStyle()
                .position(Pos.BOTTOM_LEFT)
                .hideAfter(javafx.util.Duration.seconds(2))
                .show();
    }
	
	
	public static void showErrorNotification(String titel, String text) {
		Image img = new Image("/images/notok.png");
		ImageView imgView = new ImageView(img);
		imgView.setFitWidth(50);
		imgView.setFitHeight(50);
        Notifications.create()
                .title(titel)
                .text(text)
                .graphic(imgView)
                .darkStyle()
                .position(Pos.BOTTOM_LEFT)
                .hideAfter(javafx.util.Duration.seconds(2))
                .show();
    }
	
	
}
