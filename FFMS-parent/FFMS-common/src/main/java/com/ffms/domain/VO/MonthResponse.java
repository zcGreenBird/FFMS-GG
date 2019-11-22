package com.ffms.domain.VO;/*
  @auther WJW129
  @date 2019/11/10 - 14:03
*/

import lombok.Data;

/**
 * 消费月份实体类
 * name：消费前三个月名称
 * price：消费总金额
 */
@Data
public class MonthResponse {
    private String name;
    private double price;
}
