package com.restaurant.DAO;

import java.time.LocalDate;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.restaurant.Entity.Employee;
import AllNotivications.AllNotifications;
import database.FactoryProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EmployeeDAO {


	
		
		// Add new Employee
		public static boolean addEmployee(String empName,String role, LocalDate date,String shift, Long contact, boolean active) {
			boolean isSave = false;
			Session session = null;
			try {
				session = FactoryProvider.getFactory().openSession();
				Employee emp = new Employee();
				emp.setEmployeeName(empName);
				emp.setRole(role);
				emp.setJoiningDate(date);
				emp.setShift(shift);
				emp.setContact(contact);
				emp.setStatus(active);

				Transaction tx = session.beginTransaction();

				Integer us = (Integer) session.save(emp);
				if (us > 0) {
					isSave = true;
				}

				tx.commit();

			} catch (Exception e) {
				if (session != null) {
					session.getTransaction().rollback();
				}
				e.printStackTrace();
			} finally {
				if (session != null && session.isOpen()) {
					session.close();
				}
			}
			return isSave;

		}	
		
		
	// Get all Employee List
		public static ObservableList<Employee> getAllEmployee() {
			try {

				ObservableList<Employee> empList = FXCollections.observableArrayList();
				Session s = FactoryProvider.getFactory().openSession();
				Query nq = s.createQuery("from Employee");
				List<Employee> list = nq.list();

				for (Employee employee : list) {

					
					empList.add(employee);
					

				}

				return empList;

			} catch (Exception e) {
				e.printStackTrace();
				return FXCollections.observableArrayList();
			}

		}
		
		
		// Update Employee
		public boolean updateEmployee(String empname,String role, LocalDate jdate,String shift,Long cnt,boolean ac,int empid) {
			boolean isUpdate = false;
			Session session = null;
			try {

				session = FactoryProvider.getFactory().openSession();
				Employee emp = session.get(Employee.class, empid);

				if (emp != null) {
					emp.setEmployeeName(empname);
					emp.setRole(role);
					emp.setJoiningDate(jdate);
					emp.setShift(shift);
					emp.setContact(cnt);
					emp.setStatus(ac);

					Transaction tx = session.beginTransaction();
					session.update(emp);
					tx.commit();
					isUpdate = true;
				}

			} catch (Exception e) {
				if (session != null) {
					session.getTransaction().rollback();
				}
				e.printStackTrace();
			} finally {
				if (session != null && session.isOpen()) {
					session.close();
				}
			}
			return isUpdate;
		}
		
		
		// Delete Employee
		public static boolean deleteEmployee(int empid) {
			Session session = null;
			Transaction transaction = null;
			boolean isDeleted = false;
			try {

				session = FactoryProvider.getFactory().openSession();
				transaction = session.beginTransaction();
				Employee c = session.get(Employee.class, empid);

				if (c != null) {
					// If the entity exists, delete it
					session.delete(c);
					transaction.commit();
					isDeleted = true;
				} else {
					AllNotifications.error("Employee", "Record Not Found", "");
				}

			} catch (Exception e) {
				if (transaction != null) {
					transaction.rollback();
				}
				e.printStackTrace();
			} finally {
				if (session != null && session.isOpen()) {
					session.close();
				}
			}

			return isDeleted;
		}	
		
		
		public static String getUserRole(String userName) {
			String userRole=null;
			Session session = null;
			try {

				session = FactoryProvider.getFactory().openSession();

				Query nq = session.createQuery("from Employee WHERE employeeName=:userName");
				nq.setParameter("userName", userName);
				List<Employee> list = nq.list();
				for (Employee b : list) {
					if (userName.equals(b.getEmployeeName())) {
						userRole=b.getRole();
					}
				}

			} catch (Exception e) {

				e.printStackTrace();
			} finally {
				if (session != null && session.isOpen()) {
					session.close();
				}
			}
			return userRole;

		}
		
		
		public static boolean checkEmployee() {
			List<Employee> list = null;
			boolean status = false;
			Session session = null;
			try {

				session = FactoryProvider.getFactory().openSession();
		Query nq = session.createQuery("FROM Employee");
			list = nq.list();
			if (list == null || list.isEmpty()) {
				status = true;
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
}
