package com.esri.arcgisruntime.container.monitoring.global;


import android.os.Environment;

public  class Constants {


    //默认定位位置

    //-11.5, 17.5, 安哥拉位置
//    public static double latitude = -11.5;
//    public static double longitude = 17.5;

    public static double testlatitude = -11.5;
    public static double testlongitude = 17.5;

    //上海
    public static double latitude = 31.229886;
    public static double longitude = 121.478881;
    public static int initScale = 1500000;

//    [self creatDicWithCode:@"1001" Msg:@"请检查您的网络"]; // 网络连接异常
//    /// 接口返回状态码
//    [self creatDicWithCode:@"201" Msg:@"请求服务器失败"]; // 您的请求缺少必要的参数，请重新登陆
//    [self creatDicWithCode:@"202" Msg:@"请求服务器失败"]; // 您的请求缺少必要的参数值，请重新登陆
//    [self creatDicWithCode:@"203" Msg:@"请求服务器失败"]; // 您的请求是非法的(没有对应的key值)，请重新登陆
//    [self creatDicWithCode:@"204" Msg:@"请求服务器失败"]; // 您的请求是非法的(安全校验没有通过)，请重新登陆
//    [self creatDicWithCode:@"301" Msg:@"请求服务器失败"]; // 系统异常，登录失败
//    [self creatDicWithCode:@"302" Msg:@"用户名或密码错误"]; // 用户名或密码错误，登录失败
//    [self creatDicWithCode:@"303" Msg:@"请求服务器失败"]; // 系统异常，登出失败
//    [self creatDicWithCode:@"602" Msg:@"暂无可用数据"]; // 所有单据查询无结果

//    [self creatDicWithCode:@"604" Msg:@"未查询到单据详情信息"]; // 未查询到单据详情信息
//    [self creatDicWithCode:@"605" Msg:@"查询的编号不存在"]; // 集装箱编号不存在
//    [self creatDicWithCode:@"606" Msg:@"查询的编号不存在"]; // 报关单编号不存在
//    [self creatDicWithCode:@"607" Msg:@"查询失败"]; // 起点查询失败
//    [self creatDicWithCode:@"608" Msg:@"查询失败"]; // 终点查询失败
//    [self creatDicWithCode:@"609" Msg:@"未查询到指定路线"]; // 未查询到指定路线
//    [self creatDicWithCode:@"610" Msg:@"暂无可用数据"]; // 集装箱编号实时监控查询无结果
//    [self creatDicWithCode:@"611" Msg:@"查询的编号不存在"]; // 实时监控查询编号不存在

//    [self creatDicWithCode:@"612" Msg:@"暂无可用数据"]; // 关锁编号实时监控查询无结果
//    [self creatDicWithCode:@"613" Msg:@"暂无可用数据"]; // 全部站点关锁频率统计升序未查询到任何信息
//    [self creatDicWithCode:@"614" Msg:@"暂无可用数据"]; // 全部站点关锁频率统计降序未查询到任何信息
//    [self creatDicWithCode:@"615" Msg:@"暂无可用数据"]; // 全部站点路线使用频率统计升序未查询到任何信息
//    [self creatDicWithCode:@"616" Msg:@"暂无可用数据"]; // 全部站点路线使用频率统计降序未查询到任何信息
//    [self creatDicWithCode:@"617" Msg:@"暂无可用数据"]; // 指定站点关锁频率统计升序未查询到任何信息
//    [self creatDicWithCode:@"618" Msg:@"暂无可用数据"]; // 指定站点关锁频率统计降序未查询到任何信息
//    [self creatDicWithCode:@"619" Msg:@"暂无可用数据"]; // 指点站点路线使用频率统计升序未查询到任何信息
//    [self creatDicWithCode:@"620" Msg:@"暂无可用数据"]; // 指定站点路线使用频率统计降序未查询到任何信息

    //请求成功状态码 100
    public static final int SUCCESS_STATUS_CODE = 100;

    public static final int SUCCESS_STATUS_CODE_302 = 302;  //用户名或密码错误
    public static final int SUCCESS_STATUS_CODE_1001 = 1001;  //请检查您的网络

    public static final int SUCCESS_STATUS_CODE_201 = 201;  //请求服务器失败
    public static final int SUCCESS_STATUS_CODE_202 = 202;  //请求服务器失败
    public static final int SUCCESS_STATUS_CODE_203 = 203;  //请求服务器失败
    public static final int SUCCESS_STATUS_CODE_204 = 204;  //请求服务器失败
    public static final int SUCCESS_STATUS_CODE_301 = 301;  //请求服务器失败
    public static final int SUCCESS_STATUS_CODE_303 = 303;  //请求服务器失败

    public static final int SUCCESS_STATUS_CODE_602 = 602;  //暂无可用数据
    public static final int SUCCESS_STATUS_CODE_610 = 610;  //暂无可用数据
    public static final int SUCCESS_STATUS_CODE_612 = 612;  //暂无可用数据
    public static final int SUCCESS_STATUS_CODE_613 = 613;  //暂无可用数据
    public static final int SUCCESS_STATUS_CODE_614 = 614;  //暂无可用数据
    public static final int SUCCESS_STATUS_CODE_615 = 615;  //暂无可用数据
    public static final int SUCCESS_STATUS_CODE_616 = 616;  //暂无可用数据
    public static final int SUCCESS_STATUS_CODE_617 = 617;  //暂无可用数据
    public static final int SUCCESS_STATUS_CODE_618 = 618;  //暂无可用数据
    public static final int SUCCESS_STATUS_CODE_619 = 619;  //暂无可用数据
    public static final int SUCCESS_STATUS_CODE_620 = 620;  //暂无可用数据


    public static final int SUCCESS_STATUS_CODE_604 = 604;  //未查询到单据详情信息

    public static final int SUCCESS_STATUS_CODE_605 = 605;  //查询的编号不存在
    public static final int SUCCESS_STATUS_CODE_606 = 606;  //查询的编号不存在
    public static final int SUCCESS_STATUS_CODE_611 = 611;  //查询的编号不存在

    public static final int SUCCESS_STATUS_CODE_607 = 607;  //查询失败
    public static final int SUCCESS_STATUS_CODE_608 = 608;  //查询失败
    public static final int SUCCESS_STATUS_CODE_609 = 609;  //未查询到指定路线

    /**
     * Created by 李波 on 2017/3/29.
     * 选择照片相关
     */
    public static final String IMAGE_UNSPECIFIED = "image/*";
    public static final int PIC_PHOTO = 0x00000400;
    public static final int PIC_CAMERA = 0x00000401;
    public static final int PIC_ZOOM = 0x00000402;

    /**
     * Created by 李波 on 2017/5/15.
     * 文件上传的临时地址
     */
    public static final String APP_EXTERNAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cmUploadTemp";

    public static final int CH = 1;  //中文
    public static final int EN = 2;  //英文
    public static final int PT = 3;  //葡萄牙

    //-----------Acache-------------
    public static final String KEY_ACACHE_USER = "acache_user"; //登录信息缓存
    public static final String KEY_ACACHE_NUMBERCACHE = "NumberCache"; //实时监控搜索的编号缓存 10条
    public static final String KEY_ACACHE_USERINFO = "userInfo"; //用户详情信息



}
