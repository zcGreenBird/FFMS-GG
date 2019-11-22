package com.ffms.dao;/*
  @auther WJW129
  @date 2019/10/29 - 14:25
*/

import com.ffms.domain.*;
import com.ffms.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDao {
    private Connection conn;
    public MemberDao() {
        conn = new DBConnection().getConnection();
    }
    /**
     * 查询该用户的所有的家庭成员
     */
    public List<User> selectAllMember(User user){
         List<User>  users = new ArrayList<>();
         int familyId =  user.getFamilyId();
         String sql ="select id,name,password,type,email,phone_no,address,familyid,realname from tb_user  where familyid = ?";
        try {
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1,familyId);
            ResultSet rs = pStmt.executeQuery();
            while(rs.next()){
                User u = new User();
                u.setId(rs.getInt(1));
                u.setName(rs.getString(2));
                u.setPassword(rs.getString(3));
                u.setType(rs.getInt(4));
                u.setEmail(rs.getString(5));
                u.setPhoneNo(rs.getString(6));
                u.setAddress(rs.getString(7));
                u.setFamilyId(rs.getInt(8));
                u.setRealName(rs.getString(9));
                users.add(u);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
    /**
     * 查询某个家庭近3个月的消费情况
     */
    public List<Bill> selectThreeMonthsAmount(User user) {
        List<Bill> consumerList = new ArrayList();
        int familyid = user.getFamilyId();
        String sql ="SELECT  id ,category_id,consumer_name,consumer_time,consume_amount,trading_party,type,remarks,user_id,family_id FROM tb_consumer where consumer_time >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH) and family_id= ?";
        try {
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1,familyid);
            ResultSet rs = pStmt.executeQuery();
            while(rs.next()){
                Bill bill = new Bill();
                bill.setId(rs.getInt(1));
                bill.setCategoryId(rs.getInt(2));
                bill.setConsumerName(rs.getString(3));
                bill.setConsumerNameTime(rs.getString(4));
                bill.setConsumerAmount(rs.getDouble(5));
                bill.setTradingParty(rs.getString(6));
                bill.setType(rs.getInt(7));
                bill.setRemarks(rs.getString(8));
                bill.setFamilyId(rs.getInt(9));
                consumerList.add(bill);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return consumerList;
    }
}
