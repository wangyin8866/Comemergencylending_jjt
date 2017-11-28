package com.two.emergencylending.bean;

/**
 * Created by 36420 on 2016/08/17.
 */
public class Icon {
  public   String txt_name;
    public  String use;
    public int icon;
    public  int num;
    public  String useColor;

    public Icon(String txt_name, String use, int icon, int num,String useColor) {
        this.txt_name = txt_name;
        this.use = use;
        this.icon = icon;
        this.num = num;
        this.useColor = useColor;
    }
}
