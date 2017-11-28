package com.two.emergencylending.manager;

import com.two.emergencylending.utils.StringUtil;

/**
 * 项目名称：jijietong1.08
 * 类描述：借款状态管理
 * 创建人：szx
 * 创建时间：2017/2/17 21:40
 * 修改人：szx
 * 修改时间：2017/2/17 21:40
 * 修改备注：
 */
public class BorrowStatusManager {
    public final static int LOAN_STATUS_APPLYING = 0;//申请中
    public final static int LOAN_STATUS_APPLY_FAIL = LOAN_STATUS_APPLYING + 1;//申请失败
    public final static int LOAN_STATUS_REPAY = LOAN_STATUS_APPLY_FAIL + 1;//还款中
    public final static int LOAN_STATUS_SQUARED = LOAN_STATUS_REPAY + 1;//申已结清


    public final static int PAGE_STATUS_LOAN = 0;
    public final static int PAGE_STATUS_AUTH = PAGE_STATUS_LOAN + 1;
    public final static int PAGE_STATUS_PROGRESS = PAGE_STATUS_AUTH + 1;
    public final static int PAGE_STATUS_CONFIRM_MONEY = PAGE_STATUS_PROGRESS + 1;
    public final static int PAGE_STATUS_VIEW_ORDER = PAGE_STATUS_CONFIRM_MONEY + 1;
    public final static int PAGE_STATUS_ELECTRONIC_AGREEMENT = PAGE_STATUS_VIEW_ORDER + 1;

    public final static int CLERK_STATUS_ALL = 0;//全部
    public final static int CLERK_STATUS_APPLY = CLERK_STATUS_ALL + 1;//可申请
    public final static int CLERK_STATUS_AUDIT = CLERK_STATUS_APPLY + 1;//审核中
    public final static int CLERK_STATUS_AUDIT_FAIL = CLERK_STATUS_AUDIT + 1;//审核失败
    public final static int CLERK_STATUS_AUDIT_SUCESS = CLERK_STATUS_AUDIT_FAIL + 1;//审核通过
    public final static int CLERK_STATUS_CERTIFICATE = CLERK_STATUS_AUDIT_SUCESS + 1;//认证中
    public final static int CLERK_STATUS_ELECTRONIC_SIGN = CLERK_STATUS_CERTIFICATE + 1;//电子签约
    public final static int CLERK_STATUS_SIGN_FAIL = CLERK_STATUS_ELECTRONIC_SIGN + 1;//签约失败
    public final static int CLERK_STATUS_LOANING = CLERK_STATUS_SIGN_FAIL + 1;//放款中
    public final static int CLERK_STATUS_LOANING_FAIL = CLERK_STATUS_LOANING + 1;//放款失败
    public final static int CLERK_STATUS_LOANING_SUCCESS = CLERK_STATUS_LOANING_FAIL + 1;//放款成功
    public final static int CLERK_STATUS_CLOSED_ACCOUNT = CLERK_STATUS_LOANING_SUCCESS + 1;//已结清


    private final static String STATUS_NULL = "";//立即借款!
    private final static String STATUS_FRIST_AUDIT_IN = "0";//初审中（FXD系统审核中）!
    private final static String STATUS_FRIST_AUDIT_REFUSE = "1";//初审未通过!
    private final static String STATUS_OFFLINE_AUDIT_IN = "2";//线下审核中!（FXD人工审核中）
    private final static String STATUS_OFFLINE_AUDIT_REFUSE = "3";//线下审核未通过!
    private final static String STATUS_REPAYMENT_IN = "4";//已放款!（FXD正常还款）
    private final static String STATUS_ALREADY_SETTLE = "5";//已结清!
    private final static String STATUS_FRIST_AUDIT_PASS_NOT_JXL_AUTH = "6";//初审通过,聚信立未认证!
    private final static String STATUS_END_AUDIT_PASS = "7";//终审通过!（FXD审核结束）
    private final static String STATUS_OFFLINE_AUTH_SUCCESS = "8";//线下认证通过，待保存借款信息
    private final static String STATUS_AUTH_SUCCESS = "9";//认证通过，待推送
    private final static String STATUS_STORE_SIGN = "12";//门店已面签!
    private final static String STATUS_EC_SIGN = "13";//电子签名成功！并放款中
    private final static String STATUS_FACE_VAL_NO_AUTH_SUC = "14";//聚信立认证通过，人脸未识别-优质续贷或非优质续贷，线上放款用户!
    private final static String STATUS_FACE_VAL_SUC_AUTH_SUC = "15";//人脸识别成功!
    private final static String STATUS_USER_REFUSE_TO_LEND = "16";//用户拒绝提款
    private final static String STATUS_JXL_AUTH_ING = "17";//聚信立认证中
    private final static String STATUS_EC_SIGN_ING = "18";//电子签名中!
    private final static String STATUS_PAY_FAIL = "19";//放款失败
    private final static String BORROW_TIME_OUT = "20";//用户超时未领取申请件
    private final static String SIGN_TIMEOUT = "21";//用户签约超时
    public static final String SIGN_FAIL = "22";//拒签
    public static final String OFF_LINE_PAY = "23";//线上签约成功，线下/FXD 放款
    public static final String OUT_TIME_SIGN_FAIL = "24";//签约超时，签约失败

    //我的借款状态
    public static int getLoanStatus(String status) {
        if (StringUtil.isNullOrEmpty(status)) {
            status = STATUS_NULL;
        }
        if (status.equals(STATUS_FRIST_AUDIT_IN) || status.equals(STATUS_OFFLINE_AUDIT_IN) ||
                status.equals(STATUS_FRIST_AUDIT_PASS_NOT_JXL_AUTH) || status.equals(STATUS_END_AUDIT_PASS) ||
                status.equals(STATUS_OFFLINE_AUTH_SUCCESS) || status.equals(STATUS_STORE_SIGN) ||
                status.equals(STATUS_FACE_VAL_NO_AUTH_SUC) || status.equals(STATUS_FACE_VAL_SUC_AUTH_SUC) ||
                status.equals(STATUS_JXL_AUTH_ING) || status.equals(STATUS_EC_SIGN_ING) || status.equals(STATUS_EC_SIGN)) {
            return LOAN_STATUS_APPLYING;
        } else if (status.equals(SIGN_TIMEOUT) || status.equals(BORROW_TIME_OUT) ||
                status.equals(STATUS_FRIST_AUDIT_REFUSE) || status.equals(STATUS_OFFLINE_AUDIT_REFUSE) ||
                status.equals(STATUS_USER_REFUSE_TO_LEND) || status.equals(STATUS_PAY_FAIL)) {
            return LOAN_STATUS_APPLY_FAIL;
        } else if (status.equals(STATUS_REPAYMENT_IN)) {
            return LOAN_STATUS_REPAY;
        } else if (status.equals(STATUS_ALREADY_SETTLE)) {
            return LOAN_STATUS_SQUARED;
        }
        return LOAN_STATUS_APPLYING;
    }

    //首页借款状态
    public static int getBorrowStatus(String status) {
        if (StringUtil.isNullOrEmpty(status)) {
            status = STATUS_NULL;
        }

        if (status.equals(STATUS_NULL) || status.equals(BORROW_TIME_OUT) || status.equals(STATUS_FRIST_AUDIT_IN) ||
                status.equals(STATUS_FRIST_AUDIT_REFUSE) || status.equals(STATUS_OFFLINE_AUDIT_REFUSE) ||
                status.equals(STATUS_ALREADY_SETTLE) || status.equals(STATUS_USER_REFUSE_TO_LEND) || status.equals(SIGN_TIMEOUT)) {    //可以借款的状态
            return PAGE_STATUS_LOAN;
        } else if (status.equals(STATUS_FRIST_AUDIT_IN) ||
                status.equals(STATUS_FRIST_AUDIT_PASS_NOT_JXL_AUTH) ||
                status.equals(STATUS_OFFLINE_AUTH_SUCCESS) || status.equals(STATUS_FACE_VAL_NO_AUTH_SUC)
                || status.equals(STATUS_FACE_VAL_SUC_AUTH_SUC) || status.equals(STATUS_JXL_AUTH_ING)) {//立即认证
            return PAGE_STATUS_AUTH;
        } else if (status.equals(STATUS_OFFLINE_AUDIT_IN) || status.equals(STATUS_AUTH_SUCCESS)
                ) {//审核中
            return PAGE_STATUS_PROGRESS;
        } else if (status.equals(STATUS_END_AUDIT_PASS)) {//确认领取金额
            return PAGE_STATUS_CONFIRM_MONEY;
        } else if (status.equals(STATUS_REPAYMENT_IN) || status.equals(STATUS_EC_SIGN) || status.equals(STATUS_STORE_SIGN) ||
                status.equals(STATUS_PAY_FAIL)) {//查看订单状态
            return PAGE_STATUS_VIEW_ORDER;
        } else if (status.equals(STATUS_EC_SIGN_ING)) {//电子签约
            return PAGE_STATUS_ELECTRONIC_AGREEMENT;
        }
        return PAGE_STATUS_LOAN;//查看订单状态
    }

    //业务员的顾客状态
    public static int getClerkCustomerStatus(String status) {
        if (StringUtil.isNullOrEmpty(status)) {
            status = STATUS_NULL;
        }
        if (status.equals(STATUS_NULL)) {//""
            return CLERK_STATUS_APPLY;
        } else if (status.equals(STATUS_FRIST_AUDIT_IN) || status.equals(STATUS_OFFLINE_AUDIT_IN) || status.equals(STATUS_AUTH_SUCCESS)) {//0,2
            return CLERK_STATUS_AUDIT;
        } else if (status.equals(STATUS_FRIST_AUDIT_REFUSE) || status.equals(STATUS_OFFLINE_AUDIT_REFUSE) || status.equals(STATUS_USER_REFUSE_TO_LEND) || status.equals(BORROW_TIME_OUT)) {//1，3，16，20
            return CLERK_STATUS_AUDIT_FAIL;
        } else if (status.equals(STATUS_END_AUDIT_PASS)) {//7
            return CLERK_STATUS_AUDIT_SUCESS;
        } else if (status.equals(STATUS_FRIST_AUDIT_PASS_NOT_JXL_AUTH) || status.equals(STATUS_OFFLINE_AUTH_SUCCESS) || status.equals(STATUS_OFFLINE_AUTH_SUCCESS) || status.equals(STATUS_FACE_VAL_NO_AUTH_SUC)||status.equals(STATUS_FACE_VAL_SUC_AUTH_SUC)||status.equals(STATUS_JXL_AUTH_ING)   ) {//6，8，14，15，17
            return CLERK_STATUS_CERTIFICATE;
        } else if (status.equals(STATUS_EC_SIGN_ING)) {//18
            return CLERK_STATUS_ELECTRONIC_SIGN;
        } else if (status.equals(SIGN_TIMEOUT) || status.equals(SIGN_FAIL) || status.equals(OUT_TIME_SIGN_FAIL)) {//21，22，24
            return CLERK_STATUS_SIGN_FAIL;
        } else if (status.equals(STATUS_STORE_SIGN) || status.equals(STATUS_EC_SIGN) || status.equals(OFF_LINE_PAY)) {//12，13，23
            return CLERK_STATUS_LOANING;
        } else if (status.equals(STATUS_PAY_FAIL)) {//19
            return CLERK_STATUS_LOANING_FAIL;
        } else if (status.equals(STATUS_REPAYMENT_IN)) {//4
            return CLERK_STATUS_LOANING_SUCCESS;
        } else if (status.equals(STATUS_ALREADY_SETTLE)) {//5
            return CLERK_STATUS_CLOSED_ACCOUNT;
        }
        return CLERK_STATUS_APPLY;
    }

    public static String showClerkCustomerStatus(String status) {
        if (StringUtil.isNullOrEmpty(status)) {
            status = STATUS_NULL;
        }
        if (status.equals(STATUS_NULL)) {//""
            return "可申请";
        } else if (status.equals(STATUS_FRIST_AUDIT_IN) || status.equals(STATUS_OFFLINE_AUDIT_IN) || status.equals(STATUS_AUTH_SUCCESS)) {//0,2,9
            return "审核中";
        } else if (status.equals(STATUS_FRIST_AUDIT_REFUSE) || status.equals(STATUS_OFFLINE_AUDIT_REFUSE) || status.equals(STATUS_USER_REFUSE_TO_LEND) || status.equals(BORROW_TIME_OUT)) {//1，3，16，20
            return "审核失败";
        } else if (status.equals(STATUS_END_AUDIT_PASS)) {//7
            return "审核通过";
        } else if (status.equals(STATUS_FRIST_AUDIT_PASS_NOT_JXL_AUTH)
                || status.equals(STATUS_OFFLINE_AUTH_SUCCESS)
                || status.equals(STATUS_JXL_AUTH_ING) || status.equals(STATUS_FACE_VAL_NO_AUTH_SUC)
                || status.equals(STATUS_FACE_VAL_SUC_AUTH_SUC)) {//6，8，17，14，15
            return "认证中";
        } else if (status.equals(STATUS_EC_SIGN_ING)) {//18
            return "电子签约";
        } else if (status.equals(SIGN_TIMEOUT) || status.equals(SIGN_FAIL) || status.equals(OUT_TIME_SIGN_FAIL)) {//21，22，24
            return "签约失败";
        } else if (status.equals(STATUS_STORE_SIGN) || status.equals(STATUS_EC_SIGN) || status.equals(OFF_LINE_PAY)) {//12，13，23
            return "放款中";
        } else if (status.equals(STATUS_PAY_FAIL)) {//19
            return "放款失败";
        } else if (status.equals(STATUS_REPAYMENT_IN)) {//4
            return "还款中";
        } else if (status.equals(STATUS_ALREADY_SETTLE)) {//5
            return "已结清";
        }
        return "";
    }

    public static String showClerkCustomerStatus(int status) {
        switch (status) {
            case CLERK_STATUS_ALL:
                return "全部状态";
            case CLERK_STATUS_APPLY:
                return "可申请";
            case CLERK_STATUS_AUDIT:
                return "审核中";
            case CLERK_STATUS_AUDIT_FAIL:
                return "审核失败";
            case CLERK_STATUS_AUDIT_SUCESS:
                return "审核通过";
            case CLERK_STATUS_CERTIFICATE:
                return "认证中";
            case CLERK_STATUS_ELECTRONIC_SIGN:
                return "电子签约";
            case CLERK_STATUS_SIGN_FAIL:
                return "签约失败";
            case CLERK_STATUS_LOANING:
                return "放款中";
            case CLERK_STATUS_LOANING_FAIL:
                return "放款失败";
            case CLERK_STATUS_LOANING_SUCCESS:
                return "还款中";
            case CLERK_STATUS_CLOSED_ACCOUNT:
                return "已结清";
        }
        return "可申请";
    }

    public static String getClerkCustomerFormStatus(int status) {
        switch (status) {
            case CLERK_STATUS_ALL:
                return "-1";
            case CLERK_STATUS_APPLY:
                return null;
            case CLERK_STATUS_AUDIT:
                return "0#2#9";
            case CLERK_STATUS_AUDIT_FAIL:
                return "1#3#16#20";
            case CLERK_STATUS_AUDIT_SUCESS:
                return "7";
            case CLERK_STATUS_CERTIFICATE:
                return "6#8#17#14#15";
            case CLERK_STATUS_ELECTRONIC_SIGN:
                return "18";
            case CLERK_STATUS_SIGN_FAIL:
                return "21#22#24";
            case CLERK_STATUS_LOANING:
                return "12#13#23";
            case CLERK_STATUS_LOANING_FAIL:
                return "19";
            case CLERK_STATUS_LOANING_SUCCESS:
                return "4";
            case CLERK_STATUS_CLOSED_ACCOUNT:
                return "5";
        }
        return "";
    }

}
