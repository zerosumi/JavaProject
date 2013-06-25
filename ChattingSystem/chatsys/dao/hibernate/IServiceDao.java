package chatsys.dao.hibernate;

import java.util.List;

import chatsys.entity.User;


public interface IServiceDao {
	
	List<User> getAllUser();
	User getUser(Long id);
	User getUser(Long id,String pwd); 
	int getUserCount();
	User addUser();
	void updateUser(User user);
	void delUser(Long id);
	void updatePwd(Long id,String oldPwd,String newPwd);
}	

