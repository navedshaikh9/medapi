package com.restaurant.DAO;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import com.google.gson.Gson;
import com.restaurant.Entity.KOT;
import com.restaurant.Entity.KOTItem;
import com.restaurant.Entity.Sale;
import com.restaurant.Entity.SaleItem;


public class SyncDataDAO {

	public static void syncData() {
		boolean isConnected = isInternetConnected();
		if (isConnected) {
			String currentIPAddress = currentIpAddress();

			boolean ipAddressStatus = UserDAO.checkIpAddressInDatabase(currentIPAddress);

			if (ipAddressStatus == true) {
				String userId = UserDAO.getAccountId(currentIPAddress);
				if (userId != null) {
				} else {
				}

			}

		} else {
			System.out.println("Internate Not Connected ");
		}
	}

	public static boolean isInternetConnected() {
		try {
			// Try to open a socket to a reliable and fast server (e.g., Google DNS)
			InetSocketAddress address = new InetSocketAddress("8.8.8.8", 53);
			Socket socket = new Socket();
			socket.connect(address, 500); // 2000 milliseconds (2 seconds) timeout

			// If the socket connection is successful, the internet is connected
			socket.close();
			return true;
		} catch (IOException e) {
			// An exception was thrown, indicating no internet connection
			return false;
		}
	}

	public static String currentIpAddress() {
		String ipAddress = null;
		try {
			 URL ifconfigMe = new URL("http://ifconfig.me/ip");
	            BufferedReader reader = new BufferedReader(new InputStreamReader(ifconfigMe.openStream()));

	            // Read the response from the service
	            ipAddress = reader.readLine();

	            // Close the reader
	            reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipAddress;
	}
	
	
	public static void dataSynch()
	{
		sales();
		saleItem();
	
		kots();
		kotItem();
	}
	
	
	private static void sales() {
        List<Sale> saleList = SaleDAO.getAllSale();

        try {
            String apiUrl = "https://medresto.com/api/sale";
            URL url = new URL(apiUrl);

            // Bypass hostname verification (not recommended for production)
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JSONArray jsonArray = new JSONArray();

            // Convert Sale objects to JSONObjects and add to JSONArray
            for (Sale sale : saleList) {
	            JSONObject saleJson = new JSONObject();
	            saleJson.put("accountId", TrialDAO.getAccountId());
	            saleJson.put("outletId", TrialDAO.getOutLetId());
	            saleJson.put("saleId", sale.getSaleId());
	            saleJson.put("saleOrder", sale.getSaleOrder());
	            saleJson.put("orderType", sale.getOrderType());
	            saleJson.put("totalAmt", sale.getTotalAmt());
	            saleJson.put("taxPercent", sale.getTaxPercent());
	            saleJson.put("discountAmt", sale.getDiscountAmt());
	            saleJson.put("grandTotal", sale.getGrandTotal());
	            saleJson.put("payMethod", sale.getPayMethod());
	            saleJson.put("created", sale.getCreated());
	            saleJson.put("time", sale.getTime());
	            saleJson.put("status", sale.isStatus());
	            
	            // Replace with your Sale properties
	            // Add other properties accordingly
	            jsonArray.put(saleJson);
	        }

            // Convert JSONArray to String and write to output stream
            String jsonInputString = jsonArray.toString();
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(jsonInputString);
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                System.out.println("Sales list successfully posted to API");
            } else {
                System.out.println("Error: Failed to post sales list to API. Response code: " + responseCode);

                // Print response body for more details on the error
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String line;
                StringBuilder errorResponse = new StringBuilder();
                while ((line = errorReader.readLine()) != null) {
                    errorResponse.append(line);
                }
                errorReader.close();
                System.out.println("Error Response: " + errorResponse.toString());
            }
            connection.disconnect();
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }


	
	private static void saleItem()
	{
		List<SaleItem> saleItemList= SaleItemDAO.getAllSaleItem();
		
		try {	
			 URL url = new URL("https://medresto.com/api/saleItem");
			 HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JSONArray jsonArray = new JSONArray();

            // Convert Sale objects to JSONObjects and add to JSONArray
            for (SaleItem sale : saleItemList) {
	            JSONObject saleJson = new JSONObject();
	            saleJson.put("accountId", TrialDAO.getAccountId());
	            saleJson.put("outletId", TrialDAO.getOutLetId());
	            saleJson.put("saleItemId", sale.getSaleItemId());
	            saleJson.put("saleId", sale.getSaleId());
	            saleJson.put("itemName", sale.getItemName());
	            saleJson.put("qty", sale.getQty());
	            saleJson.put("price", sale.getPrice());
	            saleJson.put("itemTotal", sale.getItemTotal());
	            saleJson.put("created", sale.getCreated());
	           
	            
	            // Replace with your Sale properties
	            // Add other properties accordingly
	            jsonArray.put(saleJson);
	        }

            // Convert JSONArray to String and write to output stream
            String jsonInputString = jsonArray.toString();
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(jsonInputString);
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                System.out.println("Sales list successfully posted to API");
            } else {
                System.out.println("Error: Failed to post sales list to API");
            }
            connection.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	
	
	private static void kots() {
        List<KOT> kotList = KOTDAO.getAll();

        try {
            String apiUrl = "https://medresto.com/api/kot";
            URL url = new URL(apiUrl);

            // Bypass hostname verification (not recommended for production)
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JSONArray jsonArray = new JSONArray();

            // Convert Sale objects to JSONObjects and add to JSONArray
            for (KOT kot : kotList) {
	            JSONObject kotJson = new JSONObject();
	            kotJson.put("kotId", kot.getKotId());
	            kotJson.put("accountId", TrialDAO.getAccountId());
	            kotJson.put("outletId", TrialDAO.getOutLetId());
	            kotJson.put("kotOrder", kot.getKotOrder());
	            kotJson.put("orderType", kot.getOrderType());
	            kotJson.put("totalPrice", kot.getTotalPrice());
	            kotJson.put("created", kot.getCreated());
	            kotJson.put("time", kot.getTime());
	            kotJson.put("comment", kot.getComment());
	            kotJson.put("cancelStatus", kot.isCancelStatus());
	            kotJson.put("status", kot.isStatus());
	            
	            // Replace with your Sale properties
	            // Add other properties accordingly
	            jsonArray.put(kotJson);
	        }

            // Convert JSONArray to String and write to output stream
            String jsonInputString = jsonArray.toString();
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(jsonInputString);
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                System.out.println("KOTs list successfully posted to API");
            } else {
                System.out.println("Error: Failed to post sales list to API. Response code: " + responseCode);

                // Print response body for more details on the error
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String line;
                StringBuilder errorResponse = new StringBuilder();
                while ((line = errorReader.readLine()) != null) {
                    errorResponse.append(line);
                }
                errorReader.close();
                System.out.println("Error Response: " + errorResponse.toString());
            }
            connection.disconnect();
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
	
	
	private static void kotItem()
	{
		List<KOTItem> kotItemList= KOTItemDAO.getAllKOTItem();
		
		try {	
			 URL url = new URL("https://medresto.com/api/kotItem");
			 HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JSONArray jsonArray = new JSONArray();

            // Convert Sale objects to JSONObjects and add to JSONArray
            for (KOTItem kotItem : kotItemList) {
	            JSONObject kotItemJson = new JSONObject();
	            kotItemJson.put("kotItemId", kotItem.getKotItemId());
	            kotItemJson.put("accountId", TrialDAO.getAccountId());
	            kotItemJson.put("outletId", TrialDAO.getOutLetId());
	            kotItemJson.put("kotId", kotItem.getKotId());
	            kotItemJson.put("itemName", kotItem.getItemName());
	            kotItemJson.put("qty", kotItem.getQty());
	            kotItemJson.put("price", kotItem.getPrice());
	            kotItemJson.put("itemTotal", kotItem.getItemTotal());
	            kotItemJson.put("comment", kotItem.getComment());
	            kotItemJson.put("cancelStatus", kotItem.isCancelStatus());
	            kotItemJson.put("status", kotItem.isStatus());
	            kotItemJson.put("created", kotItem.getCreated());
	            
	            // Replace with your Sale properties
	            // Add other properties accordingly
	            jsonArray.put(kotItemJson);
	        }

            // Convert JSONArray to String and write to output stream
            String jsonInputString = jsonArray.toString();
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(jsonInputString);
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                System.out.println("KOT Items list successfully posted to API");
            } else {
                System.out.println("Error: Failed to post sales list to API");
            }
            connection.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
}
