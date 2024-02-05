package database;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FactoryProvider {

	public static SessionFactory factory;

	public static SessionFactory getFactory() {

		if (factory == null) {

			String appFolderName = "medresto";
			String filePathdb = System.getProperty("user.home") + File.separator + appFolderName + File.separator + "medresto.db";
			System.setProperty("databasePath", filePathdb);

			factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
		}

		return factory;
	}

	public void closeFactory() {
		if (factory.isOpen()) {
			factory.close();
		}
	}

}
