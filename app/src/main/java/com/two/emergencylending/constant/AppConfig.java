package com.two.emergencylending.constant;

import android.text.TextUtils;

import com.two.emergencylending.bean.CodeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forever on 2016/2/17.
 */
public class AppConfig {
    public static final int SHARE_AD = 0;
    public static final int SHARE_BONUS = 1;
    public static final int SHARE_WITHDRAW = 2;

    public static final boolean DEBUG = false;
    public static final String LOGINKEY = "login_key";//用户登录状态
    public static final String GESTUREPASSWORD = "GesturePassword";//手势密码保存
    public static final String CLEAN = "clean";//true:手势密码开启；false：手势密码关闭
    public static final int limitSetPwdNum = 2;//设置手势密码限制次数
    public static final int limitloginPwdNum = 5;//登录手势密码限制次数
    public static String platform_type = "1";

    public static List<CodeBean> degree() {
        List<CodeBean> degree = new ArrayList();
        degree.add(new CodeBean(0, "6401", "研究生"));
        degree.add(new CodeBean(1, "6402", "本科"));
        degree.add(new CodeBean(2, "6403", "专科"));
        degree.add(new CodeBean(3, "6404", "中专"));
        degree.add(new CodeBean(4, "6405", "技校"));
        degree.add(new CodeBean(5, "6406", "高中"));
        degree.add(new CodeBean(6, "6407", "小学"));
        degree.add(new CodeBean(7, "6408", "文盲"));
        return degree;
    }

    public static List<CodeBean> marriageStatus() {
        List<CodeBean> marriageStatus = new ArrayList();
        marriageStatus.add(new CodeBean(0, "801", "未婚"));
        marriageStatus.add(new CodeBean(1, "802", "已婚"));
//        marriageStatus.add(new CodeBean(2, "803", "再婚"));
//        marriageStatus.add(new CodeBean(3, "804", "复婚"));
        marriageStatus.add(new CodeBean(4, "805", "丧偶"));
        marriageStatus.add(new CodeBean(5, "806", "离婚"));
        return marriageStatus;
    }

    public static List<CodeBean> bank() {
        List<CodeBean> bank = new ArrayList();
        bank.add(new CodeBean(0, "2", "中国工商银行"));
        bank.add(new CodeBean(1, "4", "中国农业银行"));
        bank.add(new CodeBean(2, "1", "中国银行"));
        bank.add(new CodeBean(3, "3", "中国建设银行"));
        bank.add(new CodeBean(4, "8", "中信银行"));
        bank.add(new CodeBean(5, "10", "中国光大银行"));
        bank.add(new CodeBean(7, "10", "兴业银行"));
        bank.add(new CodeBean(8, "29", "中国邮政储蓄银行"));
        return bank;
    }

    public static List<CodeBean> openBank() {
        List<CodeBean> openBank = new ArrayList();
        openBank.add(new CodeBean(0, "4105840", "平安银行"));
        openBank.add(new CodeBean(1, "102", "中国工商银行"));
        openBank.add(new CodeBean(2, "103", "中国农业银行"));
        openBank.add(new CodeBean(3, "104", "中国银行"));
        openBank.add(new CodeBean(4, "105", "中国建设银行"));
        openBank.add(new CodeBean(5, "201", "国家开发银行"));
        openBank.add(new CodeBean(6, "202", "中国进出口银行"));
        openBank.add(new CodeBean(7, "203", "中国农业发展银行"));
        openBank.add(new CodeBean(8, "301", "交通银行"));
        openBank.add(new CodeBean(9, "302", "中信银行"));
        openBank.add(new CodeBean(10, "303", "中国光大银行"));
        openBank.add(new CodeBean(11, "304", "华夏银行"));
        openBank.add(new CodeBean(12, "305", "中国民生银行"));
        openBank.add(new CodeBean(13, "306", "广东发展银行"));
        openBank.add(new CodeBean(14, "307", "深圳发展银行"));
        openBank.add(new CodeBean(15, "308", "招商银行"));
        openBank.add(new CodeBean(16, "309", "兴业银行"));
        openBank.add(new CodeBean(17, "310", "上海浦东发展银行"));
        openBank.add(new CodeBean(18, "313", "城市商业银行"));
        openBank.add(new CodeBean(19, "403", "中国邮政储蓄银行"));
        return openBank;
    }

    public static List<CodeBean> clientOrigin() {
        List<CodeBean> clientOrigin = new ArrayList();
        clientOrigin.add(new CodeBean(0, "0", "宣传单"));
        clientOrigin.add(new CodeBean(1, "1", "短信"));
        clientOrigin.add(new CodeBean(2, "2", "网络"));
        clientOrigin.add(new CodeBean(3, "3", "市场/小区推广"));
        clientOrigin.add(new CodeBean(4, "4", "电销"));
        clientOrigin.add(new CodeBean(5, "5", "同业渠道"));
        clientOrigin.add(new CodeBean(6, "6", "其他"));
        clientOrigin.add(new CodeBean(7, "7", "线下续借用户"));
        return clientOrigin;
    }

    public static List<CodeBean> proFession() {
        List<CodeBean> proFessionList = new ArrayList();
        proFessionList.add(new CodeBean(0, "1501", "国家机关、党群组织、企业、职业单位负责人"));
        proFessionList.add(new CodeBean(1, "1502", "专业技术人员"));
        proFessionList.add(new CodeBean(2, "1503", "办事人员和有关人员"));
        proFessionList.add(new CodeBean(3, "1504", "商业、服务业人员"));
        proFessionList.add(new CodeBean(4, "1505", "农、林、牧、渔、水利业生产人员"));
        proFessionList.add(new CodeBean(5, "1506", "生产、运输设备操作人员及有关人员"));
        proFessionList.add(new CodeBean(6, "1507", "军人"));
        proFessionList.add(new CodeBean(7, "1508", "不便分类的其他从业人员"));
        proFessionList.add(new CodeBean(8, "1500", "无业"));
        return proFessionList;
    }

    public static List<CodeBean> month_salary() {
        List<CodeBean> month_salary = new ArrayList();
        month_salary.add(new CodeBean(0, "0", "2000元以下"));
        month_salary.add(new CodeBean(1, "6", "2000-2999元"));
        month_salary.add(new CodeBean(2, "7", "3000-3999元"));
        month_salary.add(new CodeBean(3, "8", "4000-4999元"));
        month_salary.add(new CodeBean(4, "9", "5000-5999元"));
        month_salary.add(new CodeBean(4, "10", "6000-6999元"));
        month_salary.add(new CodeBean(3, "11", "7000-7999元"));
        month_salary.add(new CodeBean(4, "12", "8000-8999元"));
        month_salary.add(new CodeBean(4, "13", "9000-9999元"));
        month_salary.add(new CodeBean(4, "14", "10000-10999元"));
        month_salary.add(new CodeBean(4, "15", "11000-11999元"));
        month_salary.add(new CodeBean(5, "5", "12000元以上"));
        return month_salary;
    }

    public static List<CodeBean> getWorkInfo() {
        List<CodeBean> codeBeanList = new ArrayList();
        codeBeanList.add(new CodeBean(0, "1000", "农、林、牧、渔业"));
        codeBeanList.add(new CodeBean(1, "1001", "建筑业"));
        codeBeanList.add(new CodeBean(2, "1002", "批发和零售业"));
        codeBeanList.add(new CodeBean(3, "1003", "交通运输、仓储和邮政业"));
        codeBeanList.add(new CodeBean(4, "1004", "住宿和餐饮业"));
        codeBeanList.add(new CodeBean(5, "1005", "信息传输、软件和信息技术服务业"));
        codeBeanList.add(new CodeBean(6, "1006", "信息传输、计算机服务和软件业"));
        codeBeanList.add(new CodeBean(7, "1007", "房地产业"));
        codeBeanList.add(new CodeBean(8, "1008", "租赁和商务服务业"));
        codeBeanList.add(new CodeBean(9, "1009", "采矿业"));
        codeBeanList.add(new CodeBean(10, "1010", "电力、燃气及水的生产和供应业"));
        codeBeanList.add(new CodeBean(11, "1011", "金融业"));
        codeBeanList.add(new CodeBean(12, "1012", "科学研究、技术服务和地质勘查业"));
        codeBeanList.add(new CodeBean(13, "1013", "水利、环境和公共设施管理业"));
        codeBeanList.add(new CodeBean(14, "1014", "居民服务和其他服务业"));
        codeBeanList.add(new CodeBean(15, "1015", "教育"));
        codeBeanList.add(new CodeBean(16, "1016", "卫生和社会工作"));
        codeBeanList.add(new CodeBean(17, "1017", "卫生、社会保障和社会福利业"));
        codeBeanList.add(new CodeBean(18, "1018", "文化、体育和娱乐业"));
        codeBeanList.add(new CodeBean(19, "1019", "公共管理、社会保障和社会组织"));
        codeBeanList.add(new CodeBean(20, "1020", "公共管理和社会组织"));
        codeBeanList.add(new CodeBean(21, "1021", "国际组织"));
        codeBeanList.add(new CodeBean(22, "1022", "制造业"));
        return codeBeanList;
    }

    public static List<CodeBean> payWay() {
        List<CodeBean> payWay = new ArrayList();
        payWay.add(new CodeBean(0, "1601", "网银"));
        payWay.add(new CodeBean(1, "1602", "现金"));
        payWay.add(new CodeBean(2, "1603", "网银+现金"));
        return payWay;
    }

    public static List<CodeBean> unitType() {
        List<CodeBean> unitType = new ArrayList();
        unitType.add(new CodeBean(0, "1201", "机关事业"));
        unitType.add(new CodeBean(1, "1202", "国有企业"));
        unitType.add(new CodeBean(2, "1203", "外资"));
        unitType.add(new CodeBean(3, "1204", "合资"));
        unitType.add(new CodeBean(4, "1205", "民营"));
        unitType.add(new CodeBean(5, "1206", "私营"));
        unitType.add(new CodeBean(6, "1207", "个体"));
        return unitType;
    }

    public static List<CodeBean> job() {
        List<CodeBean> job = new ArrayList();
        job.add(new CodeBean(0, "1403", "部门经理"));
        job.add(new CodeBean(1, "1418", "企业主"));
        job.add(new CodeBean(2, "1401", "一般员工"));
        job.add(new CodeBean(3, "1402", "主管"));
        job.add(new CodeBean(4, "1404", "总监"));
        job.add(new CodeBean(5, "1405", "助理"));
        job.add(new CodeBean(6, "1406", "副总经理"));
        job.add(new CodeBean(7, "1407", "总经理/总裁/董事长"));
        job.add(new CodeBean(8, "1408", "部长"));
        job.add(new CodeBean(9, "1409", "厂长"));
        job.add(new CodeBean(10, "1410", "书记"));
        job.add(new CodeBean(11, "1411", "主任"));
        job.add(new CodeBean(12, "1412", "科长"));
        job.add(new CodeBean(13, "1413", "所长"));
        job.add(new CodeBean(14, "1414", "村长"));
        job.add(new CodeBean(15, "1415", "校长"));
        job.add(new CodeBean(16, "1416", "教师"));
        job.add(new CodeBean(17, "1417", "农民"));
        return job;
    }


    public static List<CodeBean> contactRelation() {
        List<CodeBean> contactRelation = new ArrayList();
        contactRelation.add(new CodeBean(0, "2201", "父子（女）"));
        contactRelation.add(new CodeBean(1, "2202", "母子（女）"));
        contactRelation.add(new CodeBean(2, "2203", "配偶"));
        contactRelation.add(new CodeBean(3, "2204", "子女"));
//        contactRelation.add(new CodeBean(4,"2205","其他亲属"));
//        contactRelation.add(new CodeBean(5,"2206","同事"));
//        contactRelation.add(new CodeBean(6,"2207","朋友"));
//        contactRelation.add(new CodeBean(7,"2208","兄弟姐妹"));
//        contactRelation.add(new CodeBean(8,"2209","其他"));
        return contactRelation;
    }

    public static List<CodeBean> contactRelation1() {
        List<CodeBean> contactRelation = new ArrayList();
        contactRelation.add(new CodeBean(0, "2201", "父子（女）"));
        contactRelation.add(new CodeBean(1, "2202", "母子（女）"));
        contactRelation.add(new CodeBean(2, "2203", "配偶"));
        contactRelation.add(new CodeBean(3, "2204", "子女"));
//        contactRelation.add(new CodeBean(4,"2205","其他亲属"));
//        contactRelation.add(new CodeBean(5,"2206","同事"));
//        contactRelation.add(new CodeBean(6,"2207","朋友"));
//        contactRelation.add(new CodeBean(7, "2208", "兄弟姐妹"));
//        contactRelation.add(new CodeBean(8,"2209","其他"));
        return contactRelation;
    }

    public static List<CodeBean> youzhi() {
        List<CodeBean> contactRelation = new ArrayList();
        contactRelation.add(new CodeBean(0, "2201", "父子（女）"));
        contactRelation.add(new CodeBean(1, "2202", "母子（女）"));
//        contactRelation.add(new CodeBean(2, "2203", "配偶"));
        contactRelation.add(new CodeBean(3, "2204", "子女"));
        contactRelation.add(new CodeBean(7, "2208", "兄弟姐妹"));
        return contactRelation;
    }

    public static List<CodeBean> no_Online() {
        List<CodeBean> contactRelation = new ArrayList();
        contactRelation.add(new CodeBean(0, "2201", "父子（女）"));
        contactRelation.add(new CodeBean(1, "2202", "母子（女）"));
        contactRelation.add(new CodeBean(2, "2203", "配偶"));
        contactRelation.add(new CodeBean(5, "2206", "同事"));
        contactRelation.add(new CodeBean(3, "2204", "子女"));
        contactRelation.add(new CodeBean(6, "2207", "朋友"));
        contactRelation.add(new CodeBean(7, "2208", "兄弟姐妹"));
        return contactRelation;
    }

    public static List<CodeBean> yihun1() {
        List<CodeBean> contactRelation = new ArrayList();
        contactRelation.add(new CodeBean(2, "2203", "配偶"));
        return contactRelation;
    }

    public static List<CodeBean> yihun2() {
        List<CodeBean> contactRelation = new ArrayList();
        contactRelation.add(new CodeBean(0, "2201", "父子（女）"));
        contactRelation.add(new CodeBean(1, "2202", "母子（女）"));
        contactRelation.add(new CodeBean(3, "2204", "子女"));
        contactRelation.add(new CodeBean(7, "2208", "兄弟姐妹"));
        return contactRelation;
    }


    public static List<CodeBean> contactRelation2() {
        List<CodeBean> contactRelation = new ArrayList();
        contactRelation.add(new CodeBean(0, "2201", "父子（女）"));
        contactRelation.add(new CodeBean(1, "2202", "母子（女）"));
        contactRelation.add(new CodeBean(2, "2203", "配偶"));
        contactRelation.add(new CodeBean(3, "2204", "子女"));
//        contactRelation.add(new CodeBean(4, "2205", "其他亲属"));
        contactRelation.add(new CodeBean(5, "2206", "同事"));
        contactRelation.add(new CodeBean(6, "2207", "朋友"));
        contactRelation.add(new CodeBean(7, "2208", "兄弟姐妹"));
//        contactRelation.add(new CodeBean(8,"2209","其他"));
        return contactRelation;
    }


    public static List<CodeBean> loanName() {
        List<CodeBean> loanName = new ArrayList();
        loanName.add(new CodeBean(0, "0", "借款名义"));
        loanName.add(new CodeBean(1, "1", "申请信用卡名义"));
        loanName.add(new CodeBean(2, "2", "背景调查名义"));
        return loanName;
    }

    public static List<CodeBean> liveStatu() {
        List<CodeBean> liveStatu = new ArrayList();
        liveStatu.add(new CodeBean(0, "901", "自置"));
        liveStatu.add(new CodeBean(1, "902", "按揭"));
        liveStatu.add(new CodeBean(2, "903", "亲属楼宇"));
        liveStatu.add(new CodeBean(3, "904", "集体宿舍"));
        liveStatu.add(new CodeBean(4, "905", "租房"));
        liveStatu.add(new CodeBean(5, "906", "共用住宅"));
        liveStatu.add(new CodeBean(6, "907", "其他"));
        return liveStatu;
    }

    /**
     * 第一个联系人选择
     *
     * @param code
     * @return
     */
    public static List<CodeBean> getOneContacts(String code) {
        List<CodeBean> liveStatu = new ArrayList();
        switch (code) {
            case "802"://已婚
                liveStatu.add(new CodeBean(2, "2203", "配偶"));
                break;
            case "801"://未婚
                liveStatu.add(new CodeBean(0, "2201", "父子（女）"));
                liveStatu.add(new CodeBean(1, "2202", "母子（女）"));
                break;
            case "806"://离异
                liveStatu.add(new CodeBean(0, "2201", "父子（女）"));
                liveStatu.add(new CodeBean(1, "2202", "母子（女）"));
                liveStatu.add(new CodeBean(3, "2204", "子女"));
                break;
            case "805"://丧偶
                liveStatu.add(new CodeBean(0, "2201", "父子（女）"));
                liveStatu.add(new CodeBean(1, "2202", "母子（女）"));
                liveStatu.add(new CodeBean(3, "2204", "子女"));
                break;
        }
        return liveStatu;
    }

    /**
     * 第二个选择
     *
     * @param code
     * @return
     */
    public static List<CodeBean> getTowContacts(String code) {
        List<CodeBean> liveStatu = new ArrayList();
        switch (code) {
            case "802"://已婚
                liveStatu.add(new CodeBean(0, "2201", "父子（女）"));
                liveStatu.add(new CodeBean(1, "2202", "母子（女）"));
                liveStatu.add(new CodeBean(3, "2204", "子女"));
                liveStatu.add(new CodeBean(7, "2208", "兄弟姐妹"));
                break;
            case "801"://未婚
                liveStatu.add(new CodeBean(0, "2201", "父子（女）"));
                liveStatu.add(new CodeBean(1, "2202", "母子（女）"));
                liveStatu.add(new CodeBean(3, "2204", "子女"));
                liveStatu.add(new CodeBean(6, "2207", "朋友"));
                liveStatu.add(new CodeBean(7, "2208", "兄弟姐妹"));
                liveStatu.add(new CodeBean(5, "2206", "同事"));
                break;
            case "806"://离异
                liveStatu.add(new CodeBean(0, "2201", "父子（女）"));
                liveStatu.add(new CodeBean(1, "2202", "母子（女）"));
                liveStatu.add(new CodeBean(3, "2204", "子女"));
                liveStatu.add(new CodeBean(6, "2207", "朋友"));
                liveStatu.add(new CodeBean(7, "2208", "兄弟姐妹"));
                liveStatu.add(new CodeBean(5, "2206", "同事"));
                break;
            case "805"://丧偶
                liveStatu.add(new CodeBean(0, "2201", "父子（女）"));
                liveStatu.add(new CodeBean(1, "2202", "母子（女）"));
                liveStatu.add(new CodeBean(3, "2204", "子女"));
                liveStatu.add(new CodeBean(6, "2207", "朋友"));
                liveStatu.add(new CodeBean(7, "2208", "兄弟姐妹"));
                liveStatu.add(new CodeBean(5, "2206", "同事"));
                break;
        }
        return liveStatu;
    }

    /**
     * 根据名称获取联系人code
     *
     * @param conName
     */
    public static String getContCode(String conName) {
        if (TextUtils.isEmpty(conName)) {
            return "";
        }
        String conde = "";
        switch (conName) {
            case "父子（女）":
                conde = "2201";
                break;
            case "母子（女）":
                conde = "2202";
                break;
            case "配偶":
                conde = "2203";
                break;
            case "子女":
                conde = "2204";
                break;
            case "同事":
                conde = "2206";
                break;
            case "朋友":
                conde = "2207";
                break;
            case "兄弟姐妹":
                conde = "2208";
                break;

        }
        return conde;
    }

    /**
     * 根据名称获取联系人关系name
     *
     * @param conCode
     */
    public static String getContNmae(String conCode) {
        if (TextUtils.isEmpty(conCode)) {
            return "";
        }
        String conde = "";
        switch (conCode) {
            case "2201":
                conde = "父子（女）";
                break;
            case "2202":
                conde = "母子（女）";
                break;
            case "2203":
                conde = "配偶";
                break;
            case "2204":
                conde = "子女";
                break;
            case "2206":
                conde = "同事";
                break;
            case "2207":
                conde = "朋友";
                break;
            case "2208":
                conde = "兄弟姐妹";
                break;

        }
        return conde;
    }

    /**
     * @param cont      联系人1还是联系人2
     * @param code      联系人CODE
     * @param contStaes 婚姻状态
     * @return
     */
    public static String getCodeName(int cont, String code, String contStaes) {
        String conName = "";
        //没有穿联系人，或者没有穿联系人CODE 获取没穿婚姻状态都返回空
        if (cont == 0 || TextUtils.isEmpty(code) || TextUtils.isEmpty(contStaes)) {
            return conName;
        }
        List<CodeBean> mLists = null;
        if (cont == 1) {
            mLists = getOneContacts(contStaes);
        } else if (cont == 2) {
            mLists = getTowContacts(contStaes);

        }
        if (mLists == null || mLists.size() < 1) {
            return conName;
        }
        for (CodeBean mCodeBean : mLists) {
            if (code.equals(mCodeBean.getCode())) {//查找到相同的
                conName = mCodeBean.getName();
                break;
            }
        }
        return conName;
    }
}
