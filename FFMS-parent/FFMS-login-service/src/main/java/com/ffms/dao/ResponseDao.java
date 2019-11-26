package com.ffms.dao;/*
  @auther WJW129
  @date 2019/11/10 - 14:15
*/

import com.ffms.domain.VO.*;
import com.ffms.domain.*;
import com.ffms.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResponseDao {
    private Connection conn;

    public ResponseDao() {
        conn = new DBConnection().getConnection();
    }

    /**
     * 查询所有分类
     * @return List<CategoryResponse></>
     */

    public List<CategoryResponse> ClassifiedInformation(User user) {
        List<CategoryResponse> classList = new ArrayList();
        int familyId = user.getFamilyId();
        String sql = "select * from tb_consumer where consumer_time >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH) and family_id= ?";
        try {
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, familyId);
            ResultSet rs = pStmt.executeQuery();
            double []cost = new double [4];
            String []name = {"食品","出行","生活","其他"};
            while (rs.next()){
                switch (rs.getInt(2)){
                    case 1: cost[0] += rs.getDouble(5);break;
                    case 2: cost[1] += rs.getDouble(5);break;
                    case 3: cost[2] += rs.getDouble(5);break;
                    default: cost[3] += rs.getDouble(5);break;
                }
            }
            double totalCost = cost[0]+cost[1]+cost[2]+cost[3];
            for (int i= 0; i < 4; i++){
                CategoryResponse cate = new CategoryResponse();
                cate.setAccount((cost[i]/totalCost)*100);
                cate.setName(name[i]);
                cate.setAllPrice(totalCost);
                cate.setId(i+1);
                classList.add(cate);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classList;

    }

    /**
     * 查询最近一次的消费记录
     */
    public List<LastTransResponse> selectLastTrans(int familyId) {
        String sql = "SELECT user_id,consumer_name,consumer_time,consumer_amount FROM tb_consumer where family_id = "+familyId+" GROUP BY user_id ORDER BY consumer_time DESC";
        List<LastTransResponse> list = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                LastTransResponse ltr = new LastTransResponse();
                ltr.setUserId(rs.getInt(1));
                ltr.setName(rs.getString(2));
                ltr.setTime(rs.getTime(3));
                ltr.setPrice(rs.getByte(4));
                list.add(ltr);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 查询三个月每个月的总消费
     */
    public List<MonthResponse> selectMonthConsume() {
        String sql = "SELECT MONTH(consumer_time) AS '月份',SUM(consumer_amount) AS '金额' FROM tb_consumer GROUP BY MONTH(consumer_time) DESC; ";
        List<MonthResponse> list = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int i = 1;
            while (rs.next() && i <= 3) {
                MonthResponse mr = new MonthResponse();
                mr.setName(rs.getString(1));
                mr.setPrice(rs.getByte(2));
                list.add(mr);
                i++;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 查看上一周的消费记录
     * @return
     */
    public List<WeekResponse> selectWeekConsume(){
        String sql = "select DATE_FORMAT(consumer_time,'%w') as week,SUM(consumer_amount),consumer_time\n" +
                "from tb_consumer  WHERE consumer_time >= DATE_SUB( DATE_ADD(curdate(),interval -day(curdate())+1 day) , INTERVAL  1  WEEK )\n" +
                "GROUP BY DATE_FORMAT(consumer_time,'%w')  ORDER BY DATE_FORMAT(consumer_time,'%w')";
        List<WeekResponse> list = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            WeekResponse wr = new WeekResponse();
            while(rs.next()){
                if(rs.getInt(1)==0){
                    wr.setSunday(rs.getFloat(2)+"");
                }else if(rs.getInt(1)==1){
                    wr.setMonday(rs.getFloat(2)+"");
                }else if(rs.getInt(1)==2){
                    wr.setTuesday(rs.getFloat(2)+"");
                }else if(rs.getInt(1)==3){
                    wr.setWednesday(rs.getFloat(2)+"");
                }else if(rs.getInt(1)==4){
                    wr.setThursday(rs.getFloat(2)+"");
                }else if(rs.getInt(1)==5){
                    wr.setFriday(rs.getFloat(2)+"");
                }else if(rs.getInt(1)==6){
                    wr.setSaturday(rs.getFloat(2)+"");
                }
            }
            if(wr.getMonday()==null){
                wr.setMonday("0");
            }
            if(wr.getTuesday()==null){
                wr.setTuesday("0");
            }
            if(wr.getWednesday()==null){
                wr.setWednesday("0");
            }
            if(wr.getThursday()==null){
                wr.setThursday("0");
            }
            if(wr.getFriday()==null){
                wr.setFriday("0");
            }
            if(wr.getSaturday()==null){
                wr.setSaturday("0");
            }
            if(wr.getSunday()==null) {
                wr.setSunday("0");
            }
            list.add(wr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 更新消费记录
     * @param bill
     * @return
     */
    public boolean updateConsumptionInformation(Bill bill) {
        String sql = "update tb_consumer set category_id=?,consumer_name=?,consumer_time=?,consumer_amount=?,trading_party=?,type=?,remarks=?,user_id=?,family_id=? where id=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bill.getCategoryId());
            pstmt.setString(2, bill.getConsumerName());
            pstmt.setString(3, bill.getConsumerNameTime());
            pstmt.setDouble(4, bill.getConsumerAmount());
            pstmt.setString(5, bill.getTradingParty());
            pstmt.setInt(6, bill.getType());
            pstmt.setString(7, bill.getRemarks());
            pstmt.setInt(8, bill.getUserId());
            pstmt.setInt(9, bill.getFamilyId());
            pstmt.setInt(10,bill.getId());
            int rs = pstmt.executeUpdate();
            return rs > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 插入消费记录
     * @param bill
     * @return
     */
    public boolean insertConsumptionInformation(Bill bill) {
        String sql = "insert into tb_consumer(category_id,consumer_name,consumer_time,consumer_amount,trading_party,type,remarks,user_id,family_id)values(?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bill.getCategoryId());
            pstmt.setString(2, bill.getConsumerName());
            pstmt.setString(3, bill.getConsumerNameTime());
            pstmt.setDouble(4, bill.getConsumerAmount());
            pstmt.setString(5, bill.getTradingParty());
            pstmt.setInt(6, bill.getType());
            pstmt.setString(7, bill.getRemarks());
            pstmt.setInt(8, bill.getUserId());
            pstmt.setInt(9, bill.getFamilyId());
            int rs = pstmt.executeUpdate();
            return rs>0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除消费记录
     * @param id
     * @return
     */
    public boolean deleteConsumptionInformation(int id) {
        String sql = "delete from tb_consumer where id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int rs = pstmt.executeUpdate();
            return rs > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}