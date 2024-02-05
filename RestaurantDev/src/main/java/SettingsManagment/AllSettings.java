package SettingsManagment;

import com.restaurant.DAO.SettingDAO;
import com.restaurant.Entity.Setting;

public class AllSettings {
	
	
	public static boolean getOfferSettign()
	{
		boolean status=false;
		
		String offer = SettingName.showOffer;
		Setting ofsetting = SettingDAO.getSetting(offer);
		if(ofsetting != null)
		{
			if (ofsetting.isStatus() == true)
			{
				status=true;
			}
		}
		return status;
	}
	
	
	public static boolean getTaxSettign()
	{
		boolean status=false;
		
		String taxs = SettingName.applyTaxSetting;
		Setting txsetting = SettingDAO.getSetting(taxs);
		if(txsetting != null)
		{
			if (txsetting.isStatus() == true)
			{
				status=true;
			}
		}
		return status;
	}
	
	
	public static boolean getAdditionalChargesSettign()
	{
		boolean status=false;
		
		String addtitionalCharges = SettingName.showAdditionalCharges;
		Setting acsetting = SettingDAO.getSetting(addtitionalCharges);
		if(acsetting != null)
		{
			if (acsetting.isStatus() == true)
			{
				status=true;
			}
		}
		return status;
	}
	
	
	public static boolean getPrintBillSettign()
	{
		boolean status=false;
		
		String printBill = SettingName.printBill;
		Setting printBill1 = SettingDAO.getSetting(printBill);
		if(printBill1 != null)
		{
			if (printBill1.isStatus() == true)
			{
				status=true;
			}
		}
		return status;
	}
	
	
	public static boolean getPrintKOTSettign()
	{
		boolean status=false;
		
		String printKOT = SettingName.printKOT;
		Setting printKOT1 = SettingDAO.getSetting(printKOT);
		if(printKOT1 != null)
		{
			if (printKOT1.isStatus() == true)
			{
				status=true;
			}
		}
		return status;
	}
	
	
	public static boolean getStockNotificationSettign()
	{
		boolean status=false;
		
		String stockNotification = SettingName.stockNotificationShow;
		Setting stockNotification1 = SettingDAO.getSetting(stockNotification);
		if(stockNotification1 != null)
		{
			if (stockNotification1.isStatus() == true)
			{
				status=true;
			}
		}
		return status;
	}
	
	
	public static boolean getCancelItemCommentSettign()
	{
		boolean status=false;
		
		String cancelItemComment = SettingName.cancelItemComment;
		Setting cancelItemComment1 = SettingDAO.getSetting(cancelItemComment);
		if(cancelItemComment1 != null)
		{
			if (cancelItemComment1.isStatus() == true)
			{
				status=true;
			}
		}
		return status;
	}
	
	
	public static boolean getQRCodeSettign()
	{
		boolean status=false;
		
		String showQRCode = SettingName.showQRCode;
		Setting showQRCode1 = SettingDAO.getSetting(showQRCode);
		if(showQRCode1 != null)
		{
			if (showQRCode1.isStatus() == true)
			{
				status=true;
			}
		}
		return status;
	}
	

}
