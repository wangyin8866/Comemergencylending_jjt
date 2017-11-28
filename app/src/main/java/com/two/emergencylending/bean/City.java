package com.two.emergencylending.bean;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by szx on 16/7/26.
 */
public class City {
    private static City instance;

    public static City getInstance() {
        if (instance == null) {
            instance = new City();
        }
        return instance;
    }


    private List<CityBean> citys = new ArrayList<CityBean>();

    public void provinceSpecial() {
        initCity("北京", "B","beijing");
        initCity("上海", "S","shanghai");
        initCity("天津", "T","tianjin");
        initCity("重庆", "C","chongqing");
        initCity("香港", "X","xianggang");
        initCity("澳门", "A","aomen");
        initCity("台湾", "T","taiwan");
//        initCity("广州", "G");
//        initCity("杭州", "H");
//        initCity("深圳", "S");
//        initCity("武汉", "W");

//        initCity("成都", "C");
//        initCity("南京", "N");
//        initCity("西安", "X");

    }


    public void provinceAnhui() {
        initCity("合肥", "H","hefei");
        initCity("安庆", "A","anqing");
        initCity("蚌埠", "B","bangbu");
        initCity("池州", "C","chizhou");
        initCity("滁州", "C","chuzhou");
        initCity("阜阳", "F","fuyang");
        initCity("淮北", "H","huaibei");
        initCity("淮南", "H","huainan");
        initCity("黄山", "H","huangshan");
        initCity("六安", "L","liuan");
        initCity("马鞍山", "M","maanshan");
        initCity("宿州", "S","suzhou");
        initCity("铜陵", "T","tongling");
        initCity("芜湖", "W","wuhu");
        initCity("宣城", "X","xuancheng");
        initCity("亳州", "B","bozhou");
    }


    public void provinceFujian() {
        initCity("福州", "F","fuzhou");
        initCity("龙岩", "L","longyan");
        initCity("南平", "N","nanping");
        initCity("宁德", "N","ningde");
        initCity("莆田", "P","putian");
        initCity("泉州", "Q","quanzhou");
        initCity("三明", "S","sanming");
        initCity("厦门", "X","xhamen");
        initCity("漳州", "Z","zhangzhou");
    }


    public void provinceGansu() {
        initCity("兰州", "L","lanzhou");
        initCity("白银", "B","baiyin");
        initCity("定西", "D","dingxi");
        initCity("甘南", "G","gannan");
        initCity("嘉峪关", "J","jiayuguan");
        initCity("金昌", "J","jinchang");
        initCity("酒泉", "J","jiuquan");
        initCity("临夏", "L","linxia");
        initCity("陇南", "L","longnan");
        initCity("平凉", "P","pingliang");
        initCity("庆阳", "Q","qingyang");
        initCity("天水", "T","tianshui");
        initCity("武威", "W","wuwei");
        initCity("张掖", "Z","zhangye");
    }


    public void provinceGuangdong() {
        initCity("广州", "G","guangzhou");
        initCity("深圳", "S","shenzhen");
        initCity("潮州", "C","chaozhou");
        initCity("东莞", "D","dongguan");
        initCity("佛山", "F","foshan");
        initCity("河源", "H","heyuan");
        initCity("惠州", "H","huizhou");
        initCity("江门", "J","jiangmen");
        initCity("揭阳", "J","jieyang");
        initCity("茂名", "M","maoming");
        initCity("梅州", "M","meizhou");
        initCity("清远", "Q","qingyuan");
        initCity("汕头", "S","shantou");
        initCity("汕尾", "S","shanwei");
        initCity("韶关", "S","shaoguan");
        initCity("阳江", "Y","yangjiang");
        initCity("云浮", "Y","yunfu");
        initCity("湛江", "Z","zhanjiang");
        initCity("肇庆", "Z","zhaoqing");
        initCity("中山", "Z","zhongshan");
        initCity("珠海", "Z","zhuhai");
    }


    public void provinceGuangxi() {
        initCity("南宁", "N","nanning");
        initCity("桂林", "G","guilin");
        initCity("百色", "B","baise");
        initCity("北海", "B","beihai");
        initCity("崇左", "C","chongzuo");
        initCity("防城港", "F","fangchenggang");
        initCity("贵港", "G","guigang");
        initCity("河池", "H","hechi");
        initCity("贺州", "H","hezhou");
        initCity("来宾", "L","laibin");
        initCity("柳州", "L","liuzhou");
        initCity("钦州", "Q","qinzhou");
        initCity("梧州", "W","wuzhou");
        initCity("玉林", "Y","yulin");
    }


    public void provinceGuizhou() {
        initCity("贵阳", "G","guiyang");
        initCity("安顺", "A","anshun");
        initCity("毕节", "B","bijie");
        initCity("六盘水", "L","liupanshui");
        initCity("黔东南", "Q","qiandongnan");
        initCity("黔南", "Q","qiannan");
        initCity("黔西南", "Q","qianxinan");
        initCity("铜仁", "T","tongren");
        initCity("遵义", "Z","zunyi");
    }


    public void provinceHainan() {
        initCity("海口", "H","haikou");
        initCity("三亚", "S","sanya");
        initCity("白沙", "B","baisha");
        initCity("保亭", "B","baoting");
        initCity("昌江", "C","changjiang");
        initCity("澄迈县", "C","chengmaixian");
        initCity("定安县", "D","dinganxian");
        initCity("东方", "D","dongfang");
        initCity("乐东", "L","ledong");
        initCity("临高县", "L","lingaoxian");
        initCity("陵水", "L","lingshui");
        initCity("琼海", "Q","qionghai");
        initCity("琼中", "Q","qiongzhong");
        initCity("屯昌县", "T","tunchangxian");
        initCity("万宁", "W","wanning");
        initCity("文昌", "W","wenchang");
        initCity("五指山", "W","wuzhishan");
        initCity("儋州", "D","danzhou");
    }


    public void provinceHebei() {
        initCity("石家庄", "S","shijiazhuang");
        initCity("保定", "B","baoding");
        initCity("沧州", "C","cangzhou");
        initCity("承德", "C","chengde");
        initCity("邯郸", "H","handan");
        initCity("衡水", "H","hengshui");
        initCity("廊坊", "L","langfang");
        initCity("秦皇岛", "Q","qinhuangdao");
        initCity("唐山", "T","tangshan");
        initCity("邢台", "X","xingtai");
        initCity("张家口", "Z","zhangjiakou");
    }


    public void provinceHenan() {
        initCity("郑州", "Z","zhengzhou");
        initCity("洛阳", "L","luoyang");
        initCity("开封", "K","kaifeng");
        initCity("安阳", "A","anyang");
        initCity("济源", "J","jiyuan");
        initCity("焦作", "J","jiaozuo");
        initCity("南阳", "N","nanyang");
        initCity("平顶山", "P","pingdingshan");
        initCity("三门峡", "S","sanmenxia");
        initCity("商丘", "S","shangqiu");
        initCity("新乡", "X","xinxiang");
        initCity("信阳", "X","xinyang");
        initCity("许昌", "X","xuchang");
        initCity("周口", "Z","zhoukou");
        initCity("驻马店", "Z","zhumadian");
        initCity("漯河", "L","luohe");
        initCity("濮阳", "P","puyang");
    }


    public void provinceHeilongjiang() {
        initCity("哈尔滨", "H","haerbin");
        initCity("大庆", "D","daqing");
        initCity("大兴安岭", "D","daxinganling");
        initCity("鹤岗", "H","hegang");
        initCity("黑河", "H","heihe");
        initCity("鸡西", "J","jixi");
        initCity("佳木斯", "J","jiamusi");
        initCity("牡丹江", "M","mudanjiang");
        initCity("七台河", "Q","qitaihe");
        initCity("齐齐哈尔", "Q","qiqihaer");
        initCity("双鸭山", "S","shuangyashan");
        initCity("绥化", "S","suihua");
        initCity("伊春", "Y","yichun");
      
        
    }


    public void provinceHubei() {
        initCity("武汉", "W","wuhan");
        initCity("仙桃", "X","xiantao");
        initCity("鄂州", "E","ezhou");
        initCity("黄冈", "H","huanggang");
        initCity("黄石", "H","huangshi");
        initCity("荆门", "J","jingmen");
        initCity("荆州", "J","jingzhou");
        initCity("潜江", "Q","qianjiang");
        initCity("神农架林区", "S","shennongjialinqu");
        initCity("十堰", "S","shiyan");
        initCity("随州", "S","suizhou");
        initCity("天门", "T","tianmen");
        initCity("咸宁", "X","xianning");
        initCity("襄阳", "X","xiangyang");
        initCity("孝感", "X","xiaogan");
        initCity("宜昌", "Y","yichang");
        initCity("恩施", "E","enshi");
    }


    public void provinceHunan() {
        initCity("长沙", "C","changsha");
        initCity("张家界", "Z","zhangjiajie");
        initCity("常德", "C","changde");
        initCity("郴州", "C","chenzhou");
        initCity("衡阳", "H","hengyang");
        initCity("怀化", "H","huaihua");
        initCity("娄底", "L","loudi");
        initCity("邵阳", "S","shaoyang");
        initCity("湘潭", "X","xiangtan");
        initCity("湘西", "X","xiangxi");
        initCity("益阳", "Y","yiyang");
        initCity("永州", "Y","yongzhou");
        initCity("岳阳", "Y","yueyang");
        initCity("株洲", "Z","zhuzhou");
    }


    public void provinceJilin() {
        initCity("长春", "C","changchun");
        initCity("吉林", "J","jilin");
        initCity("白城", "B","baicheng");
        initCity("白山", "B","baishan");
        initCity("辽源", "L","liaoyuan");
        initCity("四平", "S","siping");
        initCity("松原", "S","songyuan");
        initCity("通化", "T","tonghua");
        initCity("延边", "Y","yanbian");
    }


    public void provinceJiangsu() {
        initCity("南京", "N","nanjing");
        initCity("苏州", "S","suzhou");
        initCity("无锡", "W","wuxi");
        initCity("常州", "C","changzhou");
        initCity("淮安", "H","huaian");
        initCity("连云港", "L","lianyungang");
        initCity("南通", "N","nantong");
        initCity("宿迁", "S","suqian");
        initCity("泰州", "T","taizhou");
        initCity("徐州", "X","xuzhou");
        initCity("盐城", "Y","yancheng");
        initCity("扬州", "Y","yangzhou");
        initCity("镇江", "Z","zhenjiang");
    }


    public void provinceJiangxi() {
        initCity("南昌", "N","nanchang");
        initCity("抚州", "F","fuzhou");
        initCity("赣州", "G","ganzhou");
        initCity("吉安", "J","jian");
        initCity("景德镇", "J","jingdezhen");
        initCity("九江", "J","jiujiang");
        initCity("萍乡", "P","pingxiang");
        initCity("上饶", "S","shangrao");
        initCity("新余", "X","xinyu");
        initCity("宜春", "Y","yichun");
        initCity("鹰潭", "Y","yingtan");
    }


    public void provinceLiaoning() {
        initCity("沈阳", "S","shenyang");
        initCity("大连", "D","dalian");
        initCity("鞍山", "A","anshan");
        initCity("本溪", "B","benxi");
        initCity("朝阳", "C","chaoyang");
        initCity("丹东", "D","dandong");
        initCity("抚顺", "F","fushun");
        initCity("阜新", "F","fuxin");
        initCity("葫芦岛", "H","huludao");
        initCity("锦州", "J","jinzhou");
        initCity("辽阳", "L","liaoyang");
        initCity("盘锦", "P","panjin");
        initCity("铁岭", "T","tieling");
        initCity("营口", "Y","yingkou");
    }


    public void provinceNeimenggu() {
        initCity("呼和浩特", "H","huhehaote");
        initCity("阿拉善盟", "A","alashanmeng");
        initCity("巴彦淖尔盟", "B","bayannaoermeng");
        initCity("包头", "B","baotou");
        initCity("赤峰", "C","chifeng");
        initCity("鄂尔多斯", "E","eerduosi");
        initCity("呼伦贝尔", "H","hulunbeier");
        initCity("通辽", "T","tongliao");
        initCity("乌海", "W","wuhai");
        initCity("乌兰察布盟", "W","wulanchabushi");
        initCity("锡林郭勒盟", "X","xilinguolemeng");
        initCity("兴安盟", "X","xinganmeng");
    }


    public void provinceNingxia() {
        initCity("银川", "Y","yinchuan");
        initCity("固原", "G","guyuan");
        initCity("石嘴山", "S","shizuishan");
        initCity("吴忠", "W","wuzhong");
        initCity("中卫", "Z","zhongwei");
    }


    public void provinceQinghai() {
        initCity("西宁", "X","xining");
        initCity("果洛", "G","guoluo");
        initCity("海北", "H","haibei");
        initCity("海东", "H","haidong");
        initCity("海南", "H","hainan");
        initCity("海西", "H","haixi");
        initCity("黄南", "H","huangnan");
        initCity("玉树", "Y","yushu");
    }

    public void provinceShandong() {
        initCity("济南", "J","jinan");
        initCity("青岛", "Q","qingdao");
        initCity("滨州", "B","binzhou");
        initCity("德州", "D","dezhou");
        initCity("东营", "D","dongying");
        initCity("菏泽", "H","heze");
        initCity("济宁", "J","jining");
        initCity("莱芜", "L","laiwu");
        initCity("聊城", "L","liaocheng");
        initCity("临沂", "L","linyi");
        initCity("日照", "R","rizhao");
        initCity("泰安", "T","taian");
        initCity("威海", "W","weihai");
        initCity("潍坊", "W","weifang");
        initCity("烟台", "Y","yantai");
        initCity("枣庄", "Z","zaozhuang");
        initCity("淄博", "Z","zibo");
    }

    ;

    public void provinceShanxi() {
        initCity("太原", "T","taiyuan");
        initCity("长治", "C","changzhi");
        initCity("大同", "D","datong");
        initCity("晋城", "J","jincheng");
        initCity("晋中", "J","jinzhong");
        initCity("临汾", "L","linfen");
        initCity("吕梁", "L","liang");
        initCity("朔州", "S","shuozhou");
        initCity("忻州", "X","xinzhou");
        initCity("阳泉", "Y","yangquan");
        initCity("运城", "Y","yuncheng");
    }

    ;

    public void provinceShanxi2() {
        initCity("西安", "X","xian");
        initCity("安康", "A","ankang");
        initCity("宝鸡", "B","baoji");
        initCity("汉中", "H","hanzhong");
        initCity("商洛", "S","shangluo");
        initCity("铜川", "T","tongchuan");
        initCity("渭南", "W","weinan");
        initCity("咸阳", "X","xianyang");
        initCity("延安", "Y","yanan");
        initCity("榆林", "Y","yulin");
    }

    ;

    public void provinceSichuan() {
        initCity("成都", "C","chengdou");
        initCity("绵阳", "M","mianyang");
        initCity("阿坝", "A","aba");
        initCity("巴中", "B","bazhong");
        initCity("达州", "D","dazhou");
        initCity("德阳", "D","deyang");
        initCity("甘孜", "G","ganzi");
        initCity("广安", "G","guangan");
        initCity("广元", "G","guangyuan");
        initCity("乐山", "L","leshan");
        initCity("凉山", "L","liangshan");
        initCity("眉山", "M","meishan");
        initCity("南充", "N","nanchong");
        initCity("内江", "N","neijiang");
        initCity("攀枝花", "P","panzhihua");
        initCity("遂宁", "S","suining");
        initCity("雅安", "Y","yaan");
        initCity("宜宾", "Y","yibin");
        initCity("资阳", "Z","ziyang");
        initCity("自贡", "Z","zigong");
        initCity("泸州", "L","luzhou");
    }

    ;

    public void provinceXizang() {
        initCity("拉萨", "L","lasa");
        initCity("阿里", "A","ali");
        initCity("昌都", "C","changdou");
        initCity("林芝", "L","linzhi");
        initCity("那曲", "N","neiqu");
        initCity("日喀则", "R","rikaze");
        initCity("山南", "S","shannan");
    }

    ;

    public void provinceXinjiang() {
        initCity("乌鲁木齐", "W","wulumuqi");
        initCity("阿克苏", "A","akesu");
        initCity("阿拉尔", "A","alaer");
        initCity("巴音郭楞", "B","bayinguoleng");
        initCity("博尔塔拉", "B","boertala");
        initCity("昌吉", "C","changji");
        initCity("哈密", "H","hami");
        initCity("和田", "H","hetian");
        initCity("喀什", "K","kashen");
        initCity("克拉玛依", "K","kelamayi");
        initCity("克孜勒苏", "K","kezilesu");
        initCity("石河子", "S","shihezi");
        initCity("图木舒克", "T","tumushuke");
        initCity("吐鲁番", "T","tulufan");
        initCity("五家渠", "W","wujiaqu");
        initCity("伊犁", "Y","yili");
    }

    ;

    public void provinceYunnan() {
        initCity("昆明", "K","kunming");
        initCity("怒江", "N","nujiang");
        initCity("普洱", "P","puer");
        initCity("丽江", "L","lijiang");
        initCity("保山", "B","baoshan");
        initCity("楚雄", "C","chuxiong");
        initCity("大理", "D","dali");
        initCity("德宏", "D","dehong");
        initCity("迪庆", "D","diqing");
        initCity("红河", "H","honghe");
        initCity("临沧", "L","lincang");
        initCity("曲靖", "Q","qujing");
        initCity("文山", "W","wenshan");
        initCity("西双版纳", "X","xishuangbanna");
        initCity("玉溪", "Y","yuxi");
        initCity("昭通", "Z","zhaotong");
    }

    ;

    public void provinceZejiang() {
        initCity("杭州", "H","hangzhou");
        initCity("湖州", "H","huzhou");
        initCity("嘉兴", "J","jiaxing");
        initCity("金华", "J","jinhua");
        initCity("丽水", "L","lishui");
        initCity("宁波", "N","ningbo");
        initCity("绍兴", "S","shaoxing");
        initCity("台州", "T","taizhou");
        initCity("温州", "W","wenzhou");
        initCity("舟山", "Z","zhoushan");
        initCity("衢州", "Q","quzhou");
    }


    public void initCitys() {
        provinceSpecial();
        provinceAnhui();
        provinceFujian();
        provinceGansu();
        provinceGuangdong();
        provinceGuangxi();
        provinceGuizhou();
        provinceHainan();
        provinceHebei();
        provinceHenan();
        provinceHeilongjiang();
        provinceHubei();
        provinceHunan();
        provinceJilin();
        provinceJiangsu();
        provinceJiangxi();
        provinceLiaoning();
        provinceNeimenggu();
        provinceNingxia();
        provinceQinghai();
        provinceShandong();
        provinceShanxi();
        provinceShanxi2();
        provinceSichuan();
        provinceXizang();
        provinceXinjiang();
        provinceYunnan();
        provinceZejiang();
    }


    public List<CityBean> getCitys() {
        return citys;
    }

    public void setCitys(List<CityBean> citys) {
        this.citys = citys;
    }

    public void initCity(String city, String firstName) {
        CityBean bean = new CityBean();
        bean.setName(city);
        bean.setFirstName(firstName);
        citys.add(bean);
    }

    public void initCity(String city, String firstName, String pinyin) {
        CityBean bean = new CityBean();
        bean.setName(city);
        bean.setFirstName(firstName);
        bean.setPinyin(pinyin);
        citys.add(bean);
    }
}
