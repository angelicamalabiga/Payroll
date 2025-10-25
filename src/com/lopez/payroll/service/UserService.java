package com.lopez.payroll.service;


import com.lopez.payroll.dao.UserDAO;
import com.lopez.payroll.model.User;


	public class UserService {
	    private UserDAO userDAO = new UserDAO();

	    public User login(String employeeId, String password) {
	        return userDAO.getUserByCredentials(employeeId, password);
	    }

	    public boolean isDeactivated(User user) {
	        return user != null && "deactivated".equalsIgnoreCase(user.getStatus());
	    }

	    public boolean isAdmin(User user) {
	        return user != null && "admin".equalsIgnoreCase(user.getRole());
	    }

	    public boolean isUser(User user) {
	        return user != null && "user".equalsIgnoreCase(user.getRole());
	    }
	}


