package com.restaurant.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.Setting;
import database.FactoryProvider;

public class SettingDAO {

	

	// Insert Setting
	public static boolean insertSetting(String name, boolean settingstatus) {
		boolean status = false;
		try {
			Session s = FactoryProvider.getFactory().openSession();

			Setting se = new Setting();
			se.setSetting_name(name);
			se.setStatus(settingstatus);

			Transaction tx = s.beginTransaction();

			Integer us = (Integer) s.save(se);
			if (us > 0) {
				status = true;
			}

			tx.commit();
			s.close();

		} catch (Exception e) {

		}
		return status;

	}

	public static Setting getSetting(String name) {
		Setting st = null;
		Session s = FactoryProvider.getFactory().openSession();
		Query nq = s.createQuery("from Setting where setting_name=:sn");
		nq.setParameter("sn", name);
		List<Setting> list = nq.list();
		for (Setting b : list) {

			st = new Setting();
			st.setSetting_id(b.getSetting_id());
			st.setSetting_name(b.getSetting_name());
			st.setStatus(b.isStatus());
		}

		return st;

	}

	public static boolean checkSettingStatus(String name) {
		boolean status = false;
		Session s = FactoryProvider.getFactory().openSession();
		Query nq = s.createQuery("from Setting where setting_name=:sn");
		nq.setParameter("sn", name);
		List<Setting> list = nq.list();
		for (Setting b : list) {

			if (name.equals(b.getSetting_name())) {
				status = true;
			}
		}

		return status;

	}

	// Update Setting
	public static boolean updateSetting(int id, boolean status1, String name) {
		boolean status = false;
		try {

			Session s = FactoryProvider.getFactory().openSession();

			Query q = s.createQuery("update Setting set status=:st where setting_id=:id and setting_name=:sn");
			q.setParameter("st", status1);
			q.setParameter("id", id);
			q.setParameter("sn", name);

			Transaction tx = s.beginTransaction();
			int st = q.executeUpdate();
			if (st > 0) {
				status = true;
			}
			tx.commit();
			s.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return status;
	}

}
