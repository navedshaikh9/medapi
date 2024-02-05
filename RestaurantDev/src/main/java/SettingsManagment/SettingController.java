package SettingsManagment;

import java.net.URL;
import java.util.ResourceBundle;
import com.restaurant.DAO.SettingDAO;
import com.restaurant.Entity.Setting;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;

public class SettingController implements Initializable{
	SettingDAO settingDAO = new SettingDAO();
	
	  @FXML
	   private CheckBox chbApplyTax,chbAdditionalCharges,chbCalculateWindow,chbOffer,chb_bill_print,chb_kot_print,chbStockNotification,chbCancelItemComment,chbPaymentQRCodeOnBill;
	   
	@FXML
	private void chbApplyTaxAction(ActionEvent event) {
		
		boolean applyTax =chbApplyTax.isSelected();
		
		String settingName = SettingName.applyTaxSetting;
		Setting setting = settingDAO.getSetting(settingName);
		if (setting != null) 
		{
			settingDAO.updateSetting(setting.getSetting_id(),applyTax,settingName);	
		} 
		

		loadSetting();
	}
	
	
	  @FXML
	   private void chbAdditionalCharges(ActionEvent event) {

		  boolean additionalCharges =chbAdditionalCharges.isSelected();
			String settingName = SettingName.showAdditionalCharges;
			Setting setting = settingDAO.getSetting(settingName);
			if (setting != null) 
			{
				settingDAO.updateSetting(setting.getSetting_id(),additionalCharges,settingName);	
			} 
			
			loadSetting();
	    }
	  
	  
	  @FXML
	  private void chbShowOffer(ActionEvent event) {

		  boolean offer =chbOffer.isSelected();
			
			String settingName = SettingName.showOffer;
			Setting setting = settingDAO.getSetting(settingName);
			if (setting != null) 
			{
				settingDAO.updateSetting(setting.getSetting_id(),offer,settingName);	
			} 

			loadSetting();
			
	    }

	  
	  @FXML
	    void chb_on_action_bill_prinnt(ActionEvent event) {
		  boolean billPrint =chb_bill_print.isSelected();
			
			String settingName = SettingName.printBill;
			Setting setting = settingDAO.getSetting(settingName);
			if (setting != null) 
			{
				settingDAO.updateSetting(setting.getSetting_id(),billPrint,settingName);	
			} 
			

			loadSetting();
	    }

	    @FXML
	    void chb_on_action_kot_print(ActionEvent event) {
	    	 boolean kotPrint =chb_kot_print.isSelected();
				
				String settingName = SettingName.printKOT;
				Setting setting = settingDAO.getSetting(settingName);
				if (setting != null) 
				{
					settingDAO.updateSetting(setting.getSetting_id(),kotPrint,settingName);	
				} 
				

				loadSetting();
	    }

	    
	    @FXML
	    void onActionChbCalculateWindow(ActionEvent event) {
	    	 boolean calcullateWindow =chbCalculateWindow.isSelected();
				
				String settingName = SettingName.amountCalculater;
				Setting setting = settingDAO.getSetting(settingName);
				if (setting != null) 
				{
					settingDAO.updateSetting(setting.getSetting_id(),calcullateWindow,settingName);	
				} 
				

				loadSetting();
	    }
	    
	    
	    
	    @FXML
	    void onActionChbStockNotification(ActionEvent event) {
	    	 boolean stockNotification =chbStockNotification.isSelected();
				
				String settingName = SettingName.stockNotificationShow;
				Setting setting = settingDAO.getSetting(settingName);
				if (setting != null) 
				{
					settingDAO.updateSetting(setting.getSetting_id(),stockNotification,settingName);	
				} 
				

				loadSetting();
	    }
	    
	    
	    @FXML
	    void onActionChbCancelItemComment(ActionEvent event) {
	    	boolean cancelItemComment =chbCancelItemComment.isSelected();
			
			String settingName = SettingName.cancelItemComment;
			Setting setting = settingDAO.getSetting(settingName);
			if (setting != null) 
			{
				settingDAO.updateSetting(setting.getSetting_id(),cancelItemComment,settingName);	
			} 
			

			loadSetting();
	    }
	    
	    @FXML
	    void onActionChbPaymentQRCodeOnBill(ActionEvent event) {
	    	boolean showQRCode =chbPaymentQRCodeOnBill.isSelected();
			
			String settingName = SettingName.showQRCode;
			Setting setting = settingDAO.getSetting(settingName);
			if (setting != null) 
			{
				settingDAO.updateSetting(setting.getSetting_id(),showQRCode,settingName);	
			} 
			

			loadSetting();
	    }
	    

	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		loadSetting();
		settingCreation();
	}
	
	
	private void loadSetting()
	{
		String taxShow = SettingName.applyTaxSetting;
		Setting txsetting = settingDAO.getSetting(taxShow);
		if(txsetting!=null)
		{
		chbApplyTax.setSelected(txsetting.isStatus());
		}
		
		String  addtitionalCharges = SettingName.showAdditionalCharges;
		Setting acsetting = settingDAO.getSetting(addtitionalCharges);
		if(acsetting!=null)
		{
		chbAdditionalCharges.setSelected(acsetting.isStatus());
		}
		
		String offer = SettingName.showOffer;
		Setting orsetting = settingDAO.getSetting(offer);
		if(orsetting!=null)
		{
		chbOffer.setSelected(orsetting.isStatus());
		}	
		
		String printBill = SettingName.printBill;
		Setting printBill1 = settingDAO.getSetting(printBill);
		if(printBill1!=null)
		{
		chb_bill_print.setSelected(printBill1.isStatus());
		}	
		
		
		String printKOT = SettingName.printKOT;
		Setting printKOT1 = settingDAO.getSetting(printKOT);
		if(printKOT1!=null)
		{
		chb_kot_print.setSelected(printKOT1.isStatus());
		}	
		
		String calculatWindow = SettingName.amountCalculater;
		Setting calculatWindow1 = settingDAO.getSetting(calculatWindow);
		if(calculatWindow1!=null)
		{
		chbCalculateWindow.setSelected(calculatWindow1.isStatus());
		}	
		
		String stockNotification = SettingName.stockNotificationShow;
		Setting stockNotification1 = settingDAO.getSetting(stockNotification);
		if(stockNotification1!=null)
		{
		chbStockNotification.setSelected(stockNotification1.isStatus());
		}	
		
		String cancelItemComment = SettingName.cancelItemComment;
		Setting cancelItemComment1 = settingDAO.getSetting(cancelItemComment);
		if(cancelItemComment1!=null)
		{
		chbCancelItemComment.setSelected(cancelItemComment1.isStatus());
		}	
		
		String showQRCode = SettingName.showQRCode;
		Setting showQRCode1 = settingDAO.getSetting(showQRCode);
		if(showQRCode1!=null)
		{
		chbPaymentQRCodeOnBill.setSelected(showQRCode1.isStatus());
		}	
		
	}
	
	
	public void settingCreation()
	{
		String printKOT = SettingName.printKOT;
		Setting printKOTSetting = settingDAO.getSetting(printKOT);
		if (printKOTSetting == null) 
		{
			 settingDAO.insertSetting(printKOT,false);
		}
		
		String printBill = SettingName.printBill;
		Setting printBillSetting = settingDAO.getSetting(printBill);
		if (printBillSetting == null) 
		{
			 settingDAO.insertSetting(printBill,false);
		} 
		
		String showOffer = SettingName.showOffer;
		Setting showOfferSetting = settingDAO.getSetting(showOffer);
		if (showOfferSetting == null) 
		{
			 settingDAO.insertSetting(showOffer,false);
		} 
		
		String showAdditionalCharges = SettingName.showAdditionalCharges;
		Setting showAdditionalChargesSetting = settingDAO.getSetting(showAdditionalCharges);
		if (showAdditionalChargesSetting == null) 
		{
			 settingDAO.insertSetting(showAdditionalCharges,false);
		} 
		
		
		String applyTax = SettingName.applyTaxSetting;
		Setting applyTaxSetting = settingDAO.getSetting(applyTax);
		if (applyTaxSetting == null) 
		{
			 settingDAO.insertSetting(applyTax,false);
		} 
		
		String amountCalculater = SettingName.amountCalculater;
		Setting amountCalculaterSetting = settingDAO.getSetting(amountCalculater);
		if (amountCalculaterSetting == null) 
		{
			 settingDAO.insertSetting(amountCalculater,false);
		} 
		
		String stockNotification = SettingName.stockNotificationShow;
		Setting stockNotificationSetting = settingDAO.getSetting(stockNotification);
		if (stockNotificationSetting == null) 
		{
			 settingDAO.insertSetting(stockNotification,false);
		} 
		
		String cancelItemComment = SettingName.cancelItemComment;
		Setting cancelItemCommentSetting = settingDAO.getSetting(cancelItemComment);
		if (cancelItemCommentSetting == null) 
		{
			 settingDAO.insertSetting(cancelItemComment,false);
		} 
		
		String showQRCode = SettingName.showQRCode;
		Setting showQRCodeSetting = settingDAO.getSetting(showQRCode);
		if (showQRCodeSetting == null) 
		{
			 settingDAO.insertSetting(showQRCode,false);
		} 
	}

}
