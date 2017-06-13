package com.tqmall.enmus;

/**
 * Created by Jay on 16/12/15.
 */

public enum ErrorEnum implements KeyService {


    NET_ERROR(400,"网络错误"),
    SERVICE_ERROR(500,"后端接口返回数据失败"),
    NETWORK(400,"网络错误"),
    CONVERSION(401,"GSON转换错误"),
    HTTP(402,"数据请求失败"),
    UNEXPECTED(403,"未知错误");

    private int key;

    private String Name;

    ErrorEnum(int key,String Name){
        this.key=key;
        this.Name=Name;
    }

    public static ErrorEnum getEnum(int key){
        for(ErrorEnum errorEnum:ErrorEnum.values()){
            if(errorEnum.key==key){
                return errorEnum;
            }
        }
        return null;
    }

    public static  String getName(int key){
        ErrorEnum errorEnum=ErrorEnum.getEnum(key);
        return errorEnum!=null?errorEnum.Name:null;
    }


    @Override
    public int getKey() {
        return key;
    }

    @Override
    public String getName() {
        return Name;
    }
}
