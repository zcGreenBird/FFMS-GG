package com.ffms.controller;

import com.ffms.dao.ResponseDao;

public class Test {
    public static void main(String[] args) {
        System.out.println(new ResponseDao().selectLastTrans(1));
        System.out.println(new ResponseDao().selectMonthConsume());
    }
}
