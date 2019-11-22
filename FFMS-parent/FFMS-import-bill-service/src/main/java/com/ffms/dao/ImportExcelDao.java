package com.ffms.dao;

import com.ffms.domain.Bill;
import com.ffms.domain.Dump;
import com.ffms.service.BillImportService;
import com.ffms.utils.DBConnection;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImportExcelDao implements BillImportService {

    public static final String STANDARD_FORMAT = "yyyy-MM-ddHH:mm:ss";
    public static final String[]  eat ={"牛肉面","牛肉汤","可乐"};
    public static final String[]  life = {"食品","电子商务","鞋","电影","超市","商品"};
    public static final String[]   tripMode= {"地铁","酒店","宾馆","滴滴","火车票","高铁票","票"};
    private Connection conn;
    public ImportExcelDao() {
        conn= new DBConnection().getConnection();

    }
    /**
     *
     * @param file
     * @param num
     * @return
     */
    @Override
    public int importExcel(File file, int num,int userId) throws IOException {
        //测试
        int rs = 0;
        List<Dump> dump=ImportExcel.exportListFromExcel(file,num);
        //对list进行处理
        List<Dump> dumpList= new ArrayList<>();
        List<Bill> BillList= new ArrayList<>();
        for (Dump d : dump) {

            if(d.getConsumerName() == null &&d.getConsumerAmount()==null) break;
            if(d.getConsumerNameTime()==null){
                continue;
            }
             String str=d.getConsumerNameTime();
             Bill  bill = new Bill();
             bill.setUserId(userId);
             bill.setConsumerNameTime(str);
             bill.setConsumerName(d.getConsumerName());
             bill.setTradingParty(d.getTradingParty());
             bill.setConsumerAmount(Double.parseDouble(d.getConsumerAmount()));
             bill.setRemarks(d.getRemarks());
             if(d.getType().equals("收入")){
                 bill.setType(1);
             }
            if(d.getType().equals("支出")){
                bill.setType(2);
            }
            if(d.getType().equals("")){
                bill.setType(3);

            }
            if(d.getConsumerName()==null){
                continue;
            }
           List<String> wordList=IKAnalysis(d.getConsumerName());
            //分类

            for(String s : wordList){
                if(Arrays.asList(eat).contains(s)){
                    bill.setCategoryId(4);
                    break;
                }
                if(Arrays.asList(life).contains(s)){
                    bill.setCategoryId(1);
                    break;
                }
                if(Arrays.asList(tripMode).contains(s)){
                    bill.setCategoryId(2);
                    break;
                }
                else{
                    bill.setCategoryId(3);
                }

            }
            String sql="INSERT INTO tb_consumer(category_id, consumer_name, consumer_time, consume_amount, trading_party, type, remarks, user_id) VALUES (?,?,?,?,?,?,?,?)";
            try (
                    PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1,bill.getCategoryId());
                    ps.setString(2,bill.getConsumerName());

                    SimpleDateFormat sdf = new SimpleDateFormat(STANDARD_FORMAT);
                    Timestamp date =  java.sql.Timestamp.valueOf(bill.getConsumerNameTime());
                    ps.setTimestamp(3,date);
                    ps.setDouble(4,bill.getConsumerAmount());
                    ps.setString(5,bill.getTradingParty());
                    ps.setInt(6,bill.getType());
                    ps.setString(7,bill.getRemarks());
                    ps.setInt(8,bill.getUserId());
                     rs=ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            BillList.add(bill);
             dumpList.add(d);
        }
        System.out.println(BillList.toString());

        return rs;
    }

    public static void main(String[] args) throws IOException {
        new ImportExcelDao().importExcel(new File("e://asd.xls"),0,1);
////        System.out.println(new ImportExcelDao().IKAnalysis("我爱吃屎").toString());
//        String[]  eat ={"牛肉面","可乐"};
//        System.out.println(Arrays.asList(eat).contains("垃圾"));
    }

    public  List<String> IKAnalysis(String str) {
        List<String> keywordList = new ArrayList<String>();
        try {
            byte[] bt = str.getBytes();
            InputStream ip = new ByteArrayInputStream(bt);
            Reader read = new InputStreamReader(ip);
            IKSegmenter iks = new IKSegmenter(read, true);
            Lexeme t;
            while ((t = iks.next()) != null) {
                keywordList.add(t.getLexemeText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return keywordList;
    }
}
