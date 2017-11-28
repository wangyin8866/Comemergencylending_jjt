package com.two.emergencylending.bean;

/**
 * Created by User on 2016/10/28.
 */
public class CodeBean {
    private int id;
    private String code;
    private String name;

    public CodeBean() {
    }

    public CodeBean(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this.id == 0) {
            return this.code.equals(((CodeBean) o).code);
        } else if (this.id == 1) {
            return this.name.equals(((CodeBean) o).name);
        } else {
            return false;
        }

    }

    @Override
    public String toString() {
        return "CodeBean{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }


}
