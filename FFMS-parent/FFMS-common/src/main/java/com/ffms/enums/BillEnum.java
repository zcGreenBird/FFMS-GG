package com.ffms.enums;

public class BillEnum {
    public enum ImportBillEnum{

        ZFB(1, "支付宝"),
        WX(2,"微信");
       private int type ;
       private String name="支付方式";
        ImportBillEnum(int type, String name) {
           this.type=type;
           this.name=name;

        }
    }
}
