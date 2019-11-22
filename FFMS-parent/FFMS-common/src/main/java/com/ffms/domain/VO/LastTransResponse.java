package com.ffms.domain.VO;/*
  @auther WJW129
  @date 2019/11/10 - 13:52
*/

import lombok.Data;

import java.util.Date;

/**
 *  最后一次用户消费实体类
 *  useId:用户id
 *  name：消费名称
 *  time:消费时间
 *  price：消费金额
 */
@Data
public class LastTransResponse {
    private int userId;
    private String name;
    private Date time;
    private double price;
}
