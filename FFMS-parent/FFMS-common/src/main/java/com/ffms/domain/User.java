package com.ffms.domain;

import com.ffms.utils.DBConnection;
import lombok.Data;

/**
 * @author zc
 *User.java 实体类
 * 2019年10月17日 下午2:51:44
 */
@Data
public class User {
  private int id;
  private String name;
  private String password;
  private int  familyId;
  private int type;
  private String email;
  private  String address;
  private  String phoneNo;
  private  String realName;



}
