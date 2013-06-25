package chatsys.dao.hibernate;

import java.util.List;

import org.hibernate.*;

import chatsys.entity.User;


public class ServiceDaoImHbn implements IServiceDao {

	public User addUser() {
		Session s = HibernateUtil.getSession();
		Transaction t = s.beginTransaction();
		User user = new User(" ", " ");
		//s.save(user);
		//s.flush();
		user.setName(String.valueOf(user.getId()));
		user.setPwd(String.valueOf(user.getId()));
		user.setIcon("1");
		s.save(user);
		t.commit();
		return user;
	}

	public void delUser(Long id) {
		Session s = HibernateUtil.getSession();
		Transaction t = s.beginTransaction();
		User user = this.getUser(id);
		if(user!=null){
			s.delete(user);
		}
		t.commit();
	}

	public List<User> getAllUser() {
		Session s = HibernateUtil.getSession();
		Transaction t = s.beginTransaction();
		Query q = s.createQuery("from User");
		List list = q.list();
		t.commit();
		return list;
	}

	public User getUser(Long id) {
		Session s = HibernateUtil.getSession();
		Transaction t = s.beginTransaction();
		User user = null;
		user = (User) s.createQuery("from User where id=? ").setLong(0, id)
				.uniqueResult();
		t.commit();
		return user;
	}

	public User getUser(Long id, String pwd) {
		Session s = HibernateUtil.getSession();
		Transaction t = s.beginTransaction();
		User user = null;
		user = (User) s.createQuery("from User where id=? and pwd=?")
					.setLong(0, id).setString(1, pwd)
					.uniqueResult();
		t.commit();
		return user;
	}

	public int getUserCount() {
		return getAllUser().size();
	}

	public void updatePwd(Long id, String oldPwd, String newPwd) {
		Session s = HibernateUtil.getSession();
		Transaction t = s.beginTransaction();
		User user = getUser(id, oldPwd);
		if (user != null) {
			user.setPwd(newPwd);
			try {
				s.saveOrUpdate(user);
				t.commit();
			} catch (HibernateException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
	}

	public void updateUser(User user) {
		Session s = HibernateUtil.getSession();
		Transaction t = s.beginTransaction();
		try {
			s.saveOrUpdate(user);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		t.commit();
	}
	public static void main(String[] args) {

		ServiceDaoImHbn dao = new ServiceDaoImHbn();
//		 User user=dao.addUser();
//		 System.out.println(user.getId()+":"+user.getName());
//		System.out.println(dao.getAllUser().size());
//		 dao.updatePwd(1002L, "3542", "11920");
//		 System.out.println(dao.getUser(1002L).getPwd());
		User user=dao.getUser(1000L);
		user.setIcon("22");
		dao.updateUser(user);
	}
}
