package application;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import com.restaurant.DAO.EmpAccessDAO;
import com.restaurant.DAO.EmployeeDAO;
import com.restaurant.DAO.SyncDataDAO;
import com.restaurant.DAO.TrialDAO;
import com.restaurant.DAO.UserDAO;
import com.restaurant.DAO.UserPlanDAO;
import com.restaurant.Entity.UserPlan;
import javafx.scene.control.Label;
import AllNotivications.AllNotifications;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class TrialController implements Initializable {
	 
    @FXML
    private TextField txtLicenceKey;
    
    @FXML
    private Label lbIPAddress;
    
    

    @FXML
    void btnVerify(ActionEvent event) {
    	
    	verifyKey();

    }

    
    public void initialize(URL arg0, ResourceBundle arg1) {
		
    	setIpAddress();
    	reloadAfterOneSecond();
	}
	private void verifyKey()
	{
		
		String licenceKey =  txtLicenceKey.getText().trim();
			
		if(licenceKey.isEmpty() || licenceKey==null)
		{
			
			AllNotifications.error("MedResto Licence Key", "Please Enter Correct Licence Key", "Connect with MedResto Support Team for Licence Key");
			return;
		}
		
		boolean internateStatus = SyncDataDAO.isInternetConnected();
		
		if(internateStatus==true)
		{
			
		String ipAddress = SyncDataDAO.currentIpAddress();
			
		boolean ipAddressStatus = UserDAO.checkIpAddressInDatabase(ipAddress);
		
		if(ipAddressStatus==true)
		{
		
			String userId = UserDAO.getAccountId(ipAddress);
			UserPlan user = UserPlanDAO.getUserAllPlan(userId, licenceKey);
			boolean licenceKeyStatus = UserPlanDAO.userLicenceKeyStatus(userId, licenceKey);
			if(licenceKeyStatus==true)
			{
				boolean duplicatLicenceKey = TrialDAO.checkDuplicatLicenceKeyStatus(licenceKey);
				
				if(duplicatLicenceKey==false)
				{
					
				boolean keyRegisterStatus = TrialDAO.startPlan(userId,user.getOutletId(), ipAddress, licenceKey,user.getPlanStartDate(),user.getPlanEndDate());
				
				if(keyRegisterStatus == true)
				{
					AllNotifications.info("MedResto Licence Key", "Congratulations Your Registration is Successful", "");
					boolean updateStatus = UserPlanDAO.updateLiceneKey(user.getId(),user.getAccountId(), user.getUserLicenseKey());
					if(updateStatus==true)
					{
						createEmp();
						createAdminUser();
						System.exit(0);
					}
				}
				else
				{
					AllNotifications.error("MedResto Licence Key", "Not Register Connect With MedResto Support Team", "");
				}
			}
				else
				{
					AllNotifications.error("MedResto Licence Key", "This Licence Key Already Used Use New Licence Key", "");
				}
			}
			else
			{
				AllNotifications.error("MedResto Licence Key", "We are not Getting this Licence Key you Have to Genrate New Licence Key", "Please Connect With MedResto Suport Team");
				
			}
			
		}
		else
		{
			AllNotifications.error("MedResto Licence Key", "You are Not Register Please Connect With MedResto Support Team", "");
		}
			
		}
		else
		{
			
			AllNotifications.error("MedResto Licence Key", "Your Internate is Not Connected Please Connect With Internate", "");
		}
		
		
	}
	
	
	 private static void createAdminUser() 
	 { 
		 String userid= "admin";
		 String username="Admin";
		 String password = "12345"; 
		 if(EmpAccessDAO.checkUserAccess()==true) 
		 {
			 EmpAccessDAO.save(username, userid, password, true);
			 
		 }
		 
		 }
	 private static void createEmp() 
	 { 
		 String employeName= "Admin";
		 String role= "Admin"; 
		 boolean status = true; 
		 if(EmployeeDAO.checkEmployee()==true) 
		 {
			 EmployeeDAO.addEmployee(employeName, role, LocalDate.now(), "", null, status);
		 }
		 
		 }
 
	 
	 private void setIpAddress()
	 {
		 boolean isConnected = SyncDataDAO.isInternetConnected();
			if (isConnected) {
				lbIPAddress.setText(SyncDataDAO.currentIpAddress());
			}
			else
			{
				lbIPAddress.setText("please connect with Internet");
			}
	 }
	 
	 
	 private void reloadAfterOneSecond()
		{
			 Timer timer = new Timer();

		        // Schedule the method call every 1 second
		        timer.scheduleAtFixedRate(new TimerTask() {
		            @Override
		            public void run() {
		                 Platform.runLater(() -> setIpAddress());
		               
		               
		            }
		        }, 0, 1000); // 1000 milliseconds = 1 second
		}
	
}
 