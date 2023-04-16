package com.ly.mall.common.constant;

public class WareConstant {
    public  enum PurchaseStatusEnum{
        CREATED(0,"新建"),ASSIGNED(1,"已分配"),
        RECEIVE(2,"已领取"),FINISHED(3,"已完成"),
        HASERROR(4,"异常");


        private int code;
        private String message;
        PurchaseStatusEnum(int code,String message){
            this.code=code;
            this.message=message;
        }

        public  int getCode(){return code;}
        public String getMessage(){return  message;}
    }

    public  enum PurchaseDetailsStatusEnum{
        CREATED(0,"新建"),ASSIGNED(1,"已分配"),
        BUYING(2,"正在采购"),FINISHED(3,"已完成"),
        HASERROR(4,"采购失败");


        private int code;
        private String message;
        PurchaseDetailsStatusEnum(int code,String message){
            this.code=code;
            this.message=message;
        }

        public  int getCode(){return code;}
        public String getMessage(){return  message;}
    }
}
