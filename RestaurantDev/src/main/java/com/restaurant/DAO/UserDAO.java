package com.restaurant.DAO;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import com.restaurant.Entity.User;
import database.MedRestoFactoryProvider;

public class UserDAO {

	public static boolean checkIpAddressInDatabase(String ipAddress) {
		Session session = null;
		boolean status = false;
		String ip = null;
		try {
			session = MedRestoFactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("FROM User WHERE userIPAddress=:ipaddress");
			nq.setParameter("ipaddress", ipAddress);
			List<User> list = nq.list();
			for (User li : list) {
				if (ipAddress.equals(li.getUserIPAddress())) {
					status = true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return status;

	}

	public static User getUser(String ipAddress) {
		User user = null;
		Session session = null;
		try {
			List<User> list = new ArrayList<User>();
			session = MedRestoFactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("FROM User WHERE userIPAddress=:ipaddress");
			nq.setParameter("ipAddress", ipAddress);
			list = nq.list();

			for (User li : list) {
				user = new User();
				user.setId(li.getId());
				user.setAccountId(li.getAccountId());
				user.setUserName(li.getUserName());
				user.setBusinessName(li.getBusinessName());
				user.setContact(li.getContact());
				user.setUserIPAddress(li.getUserIPAddress());
				user.setCountry(li.getCountry());
				user.setState(li.getState());
				user.setCity(li.getCity());
				user.setCreated(li.getCreated());
				user.setStatus(li.isStatus());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return user;

	}

	public static String getAccountId(String ipAddress) {
		Session session = null;
		String accountId = null;
		try {
			session = MedRestoFactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("from User where userIPAddress=:ipaddress AND status=:st");
			nq.setParameter("ipaddress", ipAddress);
			nq.setParameter("st", true);
			List<User> list = nq.list();
			for (User li : list) {
				if (ipAddress.equals(li.getUserIPAddress())) {
					accountId = li.getAccountId();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return accountId;

	}

}
