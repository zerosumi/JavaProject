package chatsys.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil {
	private static SessionFactory sf;
	static{
		try {
			//Hibernate 4.x
			Configuration configuration = new Configuration();    
            configuration.configure();    
            ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()    
                    .applySettings(configuration.getProperties())    
                    .buildServiceRegistry();    
            sf = configuration.buildSessionFactory(serviceRegistry);
			
			
			//Hibernate 3.x
			//sf=new Configuration().configure().buildSessionFactory();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
	
	public static Session getSession(){
		if(sf!=null){
			return sf.openSession();
		}else{
			return null;
		}
	}
}
