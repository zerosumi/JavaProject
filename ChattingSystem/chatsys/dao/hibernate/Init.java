package chatsys.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import chatsys.entity.User;


public class Init {

	public static void main(String[] args) {
		
		Session s=HibernateUtil.getSession();
		if(s==null)return;
		Transaction t=s.beginTransaction();
//		User user=new User("test",'F',"20","","3542");
//		s.save(user);
//		s.flush();
		
		String hql="from User";
		Query q=s.createQuery(hql);
		List<?> users=q.list();
		System.out.println("users:"+users.size());
		Iterator<?> it=users.iterator();
		while(it.hasNext()){
			User u=(User) it.next();
			System.out.println(u.getId()+":"+u.getName()+":"+u.getIcon());
		}
		t.commit();
	}

}
