package com.two.emergencylending.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016/8/10.
 */
public class UserInfoManager {
    private static UserInfoManager instance = null;
    private String renew_loans;
    private String jump = "login";
    private String token = null;
    private String juid;
    private String login_token;
    private boolean isLogin;
    //上传头像
    private String advater;
    private String IDcardZ;
    private String IDcardF;
    private String bankCardZ;
    private String bankCardF;
    private String selfHand;
    private String idCardHandId;
    private String bankCardUrlZ;
    private String bankCardUrlF;
    private String bankcard_must, idcard_must;
    private String IDcardDownZ;
    private String IDcardDownF;
    private String bankCardFId;
    private String bankCardZId;

    private String userName;
    private String userHuijiDelAddress;
    private String user_IdCard;
    private int store_count;
    private String phone_pwd;

    public void setPhone_pwd(String phone_pwd) {
        this.phone_pwd = phone_pwd;
    }

    public String getPhone_pwd() {
        return phone_pwd;
    }

    public int getStore_count() {
        return store_count;
    }

    public void setStore_count(int store_count) {
        this.store_count = store_count;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    private String count;

    public int getLoan_type() {
        return loan_type;
    }

    public void setLoan_type(int loan_type) {
        this.loan_type = loan_type;
    }

    private int loan_type;
    List<ContactListModel> contactListModels = new ArrayList<ContactListModel>();
    UserStatuModel userStatuModels = new UserStatuModel();
    private BorrowInfoBean borrowInfo = new BorrowInfoBean();
    private RepayBorrowInfoBean RepayBorrowInfo;
    private int isUrgency;
    private String junxinlin, junxinlinPhone;
    private String idCardZId, idCardFId, self_hand;
    private String qr_code_url, qr_code;
    private JobInforBean jobBean;
    private ContactBean contactBean;
    private PerSonalBean perSonalBean = new PerSonalBean();
    private List<CodeBean> openBanks;
    private List<PresentStore> stores;
    private String product_id;

    private LocationBean location = new LocationBean();


    public List<CodeBean> getOpenBanks() {
        return openBanks;
    }

    public void setOpenBanks(List<CodeBean> openBanks) {
        this.openBanks = openBanks;
    }

    private UserInfoManager() {
    }

    public static UserInfoManager getInstance() {
        if (instance == null) {
            instance = new UserInfoManager();
        }
        return instance;
    }

    public void clear() {
        instance = null;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setJump(String jump) {
        this.jump = jump;
    }

    public String getJump() {
        return jump;
    }


    public String getLogin_token() {
        return login_token;
    }

    public void setLogin_token(String login_token) {
        this.login_token = login_token;
    }

    public String getJuid() {
        return juid;
    }

    public void setJuid(String juid) {
        this.juid = juid;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }


    public String getAdvater() {
        return advater;
    }

    public void setAdvater(String advater) {
        this.advater = advater;
    }

    public String getIDcardZ() {
        return IDcardZ;
    }

    public void setIDcardZ(String IDcardZ) {
        this.IDcardZ = IDcardZ;
    }

    public String getIDcardF() {
        return IDcardF;
    }

    public void setIDcardF(String IDcardF) {
        this.IDcardF = IDcardF;
    }

    public BorrowInfoBean getBorrowInfo() {
        return borrowInfo;
    }

    public void setBorrowInfo(BorrowInfoBean borrowInfo) {
        this.borrowInfo = borrowInfo;
    }

    public List<ContactListModel> getContactListModels() {
        return contactListModels;
    }

    public void setContactListModels(List<ContactListModel> contactListModels) {
        this.contactListModels = contactListModels;
    }

    public UserStatuModel getUserStatuModels() {
        return userStatuModels;
    }

    public void setUserStatuModels(UserStatuModel userStatuModels) {
        this.userStatuModels = userStatuModels;
    }


    public String getJunxinlin() {
        return junxinlin;
    }

    public void setJunxinlin(String junxinlin) {
        this.junxinlin = junxinlin;
    }

    public String getJunxinlinPhone() {
        return junxinlinPhone;
    }

    public void setJunxinlinPhone(String junxinlinPhone) {
        this.junxinlinPhone = junxinlinPhone;
    }


    public String getIdCardZId() {
        return idCardZId;
    }

    public void setIdCardZId(String idCardZId) {
        this.idCardZId = idCardZId;
    }

    public String getIdCardFId() {
        return idCardFId;
    }

    public void setIdCardFId(String idCardFId) {
        this.idCardFId = idCardFId;
    }

    public String getIDcardDownZ() {
        return IDcardDownZ;
    }

    public void setIDcardDownZ(String IDcardDownZ) {
        this.IDcardDownZ = IDcardDownZ;
    }

    public String getIDcardDownF() {
        return IDcardDownF;
    }

    public void setIDcardDownF(String IDcardDownF) {
        this.IDcardDownF = IDcardDownF;
    }

    public String getBankCardZ() {
        return bankCardZ;
    }

    public void setBankCardZ(String bankCardZ) {
        this.bankCardZ = bankCardZ;
    }

    public String getBankCardF() {
        return bankCardF;
    }

    public void setBankCardF(String bankCardF) {
        this.bankCardF = bankCardF;
    }

    public String getSelfHand() {
        return selfHand;
    }

    public void setSelfHand(String selfHand) {
        this.selfHand = selfHand;
    }

    public String getBankCardUrlZ() {
        return bankCardUrlZ;
    }

    public void setBankCardUrlZ(String bankCardUrlZ) {
        this.bankCardUrlZ = bankCardUrlZ;
    }

    public String getBankCardUrlF() {
        return bankCardUrlF;
    }

    public void setBankCardUrlF(String bankCardUrlF) {
        this.bankCardUrlF = bankCardUrlF;
    }

    public String getQr_code_url() {
        return qr_code_url;
    }

    public void setQr_code_url(String qr_code_url) {
        this.qr_code_url = qr_code_url;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public String getBankCardFId() {
        return bankCardFId;
    }

    public void setBankCardFId(String bankCardFId) {
        this.bankCardFId = bankCardFId;
    }

    public String getBankCardZId() {
        return bankCardZId;
    }

    public void setBankCardZId(String bankCardZId) {
        this.bankCardZId = bankCardZId;
    }

    public String getBankcard_must() {
        return bankcard_must;
    }

    public void setBankcard_must(String bankcard_must) {
        this.bankcard_must = bankcard_must;
    }

    public String getIdcard_must() {
        return idcard_must;
    }

    public void setIdcard_must(String idcard_must) {
        this.idcard_must = idcard_must;
    }


    public JobInforBean getJobBean() {
        return jobBean;
    }

    public void setJobBean(JobInforBean jobBean) {
        this.jobBean = jobBean;
    }


    public ContactBean getContactBean() {
        return contactBean;
    }

    public void setContactBean(ContactBean contactBean) {
        this.contactBean = contactBean;
    }

    public PerSonalBean getPerSonalBean() {
        return perSonalBean;
    }

    public void setPerSonalBean(PerSonalBean perSonalBean) {
        this.perSonalBean = perSonalBean;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHuijiDelAddress() {
        return userHuijiDelAddress;
    }

    public void setUserHuijiDelAddress(String userHuijiDelAddress) {
        this.userHuijiDelAddress = userHuijiDelAddress;
    }

    public String getUser_IdCard() {
        return user_IdCard;
    }

    public void setUser_IdCard(String user_IdCard) {
        this.user_IdCard = user_IdCard;
    }

    public String getIdCardHandId() {
        return idCardHandId;
    }

    public void setIdCardHandId(String idCardHandId) {
        this.idCardHandId = idCardHandId;
    }

    public List<PresentStore> getStores() {
        return stores;
    }

    public void setStores(List<PresentStore> stores) {
        this.stores = stores;
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public void clearLocation() {
        location = null;
    }

    public RepayBorrowInfoBean getRepayBorrowInfo() {
        return RepayBorrowInfo;
    }

    public void setRepayBorrowInfo(RepayBorrowInfoBean repayBorrowInfo) {
        RepayBorrowInfo = repayBorrowInfo;
    }
}
