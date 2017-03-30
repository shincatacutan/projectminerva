package com.optum.technology.minerva.dao;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.optum.technology.minerva.entity.UserRole;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-web-config.xml")
@Transactional
@WebAppConfiguration
public class UserDaoImplTest {

	@Autowired
	UserDao userDao;


	@Test
	public void getUsersByRole() throws SQLException{
		UserRole role = new UserRole();
		role.setRole("ROLE_ADMIN");
		List<UserRole> users = userDao.getUsersByRole(role);
		for (UserRole h : users) {
			System.out.println(h.toString());
		}
		Assert.isTrue(users.size() > 0);
		
		role.setRole("ROLE_INQUIRY_ADMIN");
		List<UserRole> inquiryAdmins = userDao.getUsersByRole(role);
		for (UserRole h : inquiryAdmins) {
			System.out.println(h.toString());
		}
		Assert.isTrue(inquiryAdmins.size() > 0);
	}
}
