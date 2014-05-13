package com.keymetic.flicking.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.keymetic.flicking.core.entity.User;

@Repository
public class UserDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional(readOnly=true)
	public User getUserByEmail(String email) throws DataAccessException{
		List<User> users = new ArrayList<User>();		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("email", email));
		users = criteria.list();		
		if(users.size() > 0){
			return users.get(0);
		}else{
			return null;
		}

	}	

	@Transactional
	public User addUser(User user) throws DataAccessException{
		sessionFactory.getCurrentSession().save(user);
		return user;		
	}

	@Transactional(readOnly=true)
	public User getUserById(Long id) throws DataAccessException{
		return (User)sessionFactory.getCurrentSession().get(User.class,id);
	}

	@Transactional
	public User updateUser(User user) throws DataAccessException{
		User uUser = (User) sessionFactory.getCurrentSession().get(User.class,user.getId());
		uUser.setFirstName(user.getFirstName());
		uUser.setLastName(user.getLastName());
		uUser.setEnabled(user.isEnabled());		
		sessionFactory.getCurrentSession().update(uUser);;
		return uUser;
	}

	@Transactional
	public Boolean deleteUser(Long id) throws DataAccessException{
		User user = (User) sessionFactory.getCurrentSession().get(User.class,id);
		if(user != null){
			sessionFactory.getCurrentSession().delete(user);
			return true;
		}
		else{
			return false;
		}
	}

	@Transactional
	public User disableEnableUser(Long id,boolean value) throws DataAccessException{
		User user = (User)sessionFactory.getCurrentSession().get(User.class,id);
		user.setEnabled(value);
		sessionFactory.getCurrentSession().update(user);
		return user;
	}
	
	
	@Transactional
	public User updatePassword(Long id,String password) throws DataAccessException{
		User user = (User)sessionFactory.getCurrentSession().get(User.class,id);
		user.setPassword(password);
		sessionFactory.getCurrentSession().update(user);
		return user;		
	}


	@Transactional(readOnly=true)
	public List<User> getUsers(int limit,int offset) throws DataAccessException{
		List<User> users = null;		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(limit);				
		users = criteria.list();
		return users;
	}



}
