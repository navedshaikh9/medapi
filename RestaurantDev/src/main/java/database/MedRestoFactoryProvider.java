package database;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class MedRestoFactoryProvider {


public static SessionFactory factory;

	
	public static SessionFactory getFactory() {
				
		if(factory==null)
		{
			/*
			String appFolderName = "medapp";
			String filePathdb = System.getProperty("user.home") + File.separator + appFolderName + File.separator + "medresta.db";
	        System.setProperty("user.home", filePathdb);
		*/
			factory =new Configuration().configure("medrestoHibernate.cfg.xml").buildSessionFactory();
		}
                
		return factory;
	}
	
	public void closeFactory() {
		if(factory.isOpen())
		{
			factory.close();
		}
	}
	


}
