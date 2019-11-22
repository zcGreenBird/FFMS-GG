package com.ffms.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Dump {


    /**
     *消费名称
     */
    private String consumerName;
    /**
     *消费时间
     */
    private String consumerNameTime;
    /**
     *消费金额
     */
    private  String consumerAmount;

    /**
     *交易对方
     */
    private String tradingParty;
    /**
     *交易类型 1.收入 2.支出 3.资金转账
     */
    private String type;
    /**
     *备注
     */
    private String remarks;
}
