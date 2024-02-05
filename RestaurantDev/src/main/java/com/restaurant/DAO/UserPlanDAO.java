package com.restaurant.DAO;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.UserPlan;
import database.MedRestoFactoryProvider;

public class UserPlanDAO {

	public static UserPlan getUserPlan(String userId) {
		Session session = null;
		UserPlan r = null;
		try {
			List<UserPlan> list = new ArrayList<UserPlan>();
			session = MedRestoFactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("FROM UserPlan WHERE accountId=:userId AND status=:st");
			nq.setParameter("userId", userId);
			nq.setParameter("st", true);
			list = nq.list();

			for (UserPlan li : list) {
				r = new UserPlan();
				r.setId(li.getId());
				r.setAccountId(li.getAccountId());
				r.setOutletId(li.getOutletId());
				r.setPlan(li.getPlan());
				r.setPlanStartDate(li.getPlanStartDate());
				r.setPlanEndDate(li.getPlanEndDate());
				r.setUserLicenseKey(li.getUserLicenseKey());
				r.setStatus(li.isStatus());
				r.setCreated(li.getCreated());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return r;
	}

	public static UserPlan getUserAllPlan(String userId, String key) {
		Session session = null;
		UserPlan r = null;
		try {
			List<UserPlan> list = new ArrayList<UserPlan>();
			session = MedRestoFactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("FROM UserPlan WHERE accountId=:userId AND userLicenseKey=:key AND status=:st");
			nq.setParameter("userId", userId);
			nq.setParameter("key", key);
			nq.setParameter("st", true);
			list = nq.list();

			for (UserPlan li : list) {
				r = new UserPlan();
				r.setId(li.getId());
				r.setAccountId(li.getAccountId());
				r.setOutletId(li.getOutletId());
				r.setPlan(li.getPlan());
				r.setPlanStartDate(li.getPlanStartDate());
				r.setPlanEndDate(li.getPlanEndDate());
				r.setUserLicenseKey(li.getUserLicenseKey());
				r.setStatus(li.isStatus());
				r.setCreated(li.getCreated());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return r;
	}

	public static boolean userLicenceKeyStatus(String userid, String key) {
		Session session = null;
		boolean status = false;
		try {

			session = MedRestoFactoryProvider.getFactory().openSession();
			Query nq = session.createQuery("FROM UserPlan WHERE accountId=:userId AND userLicenseKey=:key AND status=:st");
			nq.setParameter("userId", userid);
			nq.setParameter("key", key);
			nq.setParameter("st", true);
			List<UserPlan> list = nq.list();
			for (UserPlan li : list) {
				if (key.equals(li.getUserLicenseKey()) && li.isStatus() == true) {
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

	
	public static boolean updateLiceneKey(int id,String userId, String key) {
		Session session = null;
		boolean isUpdate = false;
		try {

			session = MedRestoFactoryProvider.getFactory().openSession();

			Query q = session.createQuery("update UserPlan set status=:st where accountId=:userId and userLicenseKey=:key and id=:id");
			q.setParameter("st", false);
			q.setParameter("userId", userId);
			q.setParameter("key", key);
			q.setParameter("id", id);

			Transaction tx = session.beginTransaction();
			int st = q.executeUpdate();
			if (st > 0) {
				isUpdate = true;
			}
			tx.commit();
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return isUpdate;
	}
}
