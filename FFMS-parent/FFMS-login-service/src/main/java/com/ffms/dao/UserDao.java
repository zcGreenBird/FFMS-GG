package com.ffms.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ffms.domain.User;
import com.ffms.service.UserService;
import com.ffms.utils.DBConnection;


/**
 * @author zc
 *UserDao.java
 * 2019年10月17日 下午2:51:24
 */
public class UserDao implements UserService {
	private Connection conn;
  public UserDao() {
	conn= new DBConnection().getConnection();
	
  }
/* 验证用户登陆
 * @see com.ffms.service.UserService#login(java.lang.String, java.lang.String)
 */
@Override
public User login(String name ,String password ) {
	 String sql="select id,name,password,type,email,phone_no,address, family_id,real_name from tb_user  where name=? and password=?";

	 try {
		PreparedStatement ps=conn.prepareStatement(sql);
		ps.setString(1, name);
		ps.setString(2, password);
		ResultSet rs=ps.executeQuery();
		if(rs.next()) {
			User user=new User();
			user.setId(rs.getInt(1));
			user.setName(rs.getString(2));
			user.setPassword(rs.getString(3));
			user.setType(rs.getInt(4));
			user.setEmail(rs.getString(5));
			user.setPhoneNo(rs.getString(6));
			user.setAddress(rs.getString(7));
			user.setFamilyId(rs.getInt(8));
			user.setRealName(rs.getString(9));
			return user;
		}

	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return  null;

}


// public static void main(String[] args) {
//	System.out.println(new UserDao().login("asd", "123"));
//}
}
