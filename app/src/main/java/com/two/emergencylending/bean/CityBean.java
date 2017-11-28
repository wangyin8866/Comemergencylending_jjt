package com.two.emergencylending.bean;

/**
 * 项目名称：急借通
 * 类描述：城市bean
 * 创建人：szx
 * 创建时间：2016/8/4 17:38
 * 修改人：szx
 * 修改时间：2016/8/4 17:38
 * 修改备注：
 */
public class CityBean {
    private String id;
    private String parent;
    private String firstName;
    private String type;
    private String name;
    private String pinyin;

    public CityBean() {
        super();
    }

    public CityBean(String id, String parent, String name, String type, String firstName) {
        super();
        this.id = id;
        this.parent = parent;
        this.name = name;
        this.type = type;
        this.firstName = firstName;
    }

    public CityBean(String id, String parent, String name, String type, String firstName, String pinyin) {
        super();
        this.id = id;
        this.parent = parent;
        this.name = name;
        this.type = type;
        this.firstName = firstName;
        this.pinyin = pinyin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public String toString() {
        return "CityBean{" +
                "id='" + id + '\'' +
                ", parent='" + parent + '\'' +
                ", firstName='" + firstName + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", pinyin='" + pinyin + '\'' +
                '}';
    }
}
