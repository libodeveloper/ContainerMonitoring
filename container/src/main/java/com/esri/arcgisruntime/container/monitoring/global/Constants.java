package com.esri.arcgisruntime.container.monitoring.global;


public  class Constants {


    //默认定位位置

    //-11.5, 17.5, 安哥拉位置
//    public static double latitude = -11.5;
//    public static double longitude = 17.5;

    //上海
    public static double latitude = 31.229886;
    public static double longitude = 121.478881;
    public static int initScale = 1500000;


    //请求成功状态码 100
    public static final int SUCCESS_STATUS_CODE=100;

    public static final int CH = 1;  //中文
    public static final int EN = 2;  //英文
    public static final int PT = 3;  //葡萄牙

    //-----------Acache-------------
    public static final String KEY_ACACHE_USER = "acache_user"; //登录信息缓存
    public static final String KEY_ACACHE_NUMBERCACHE = "NumberCache"; //实时监控搜索的编号缓存 10条




}
