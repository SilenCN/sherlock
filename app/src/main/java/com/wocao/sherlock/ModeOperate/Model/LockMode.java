package com.wocao.sherlock.ModeOperate.Model;

import java.io.Serializable;

/**
 * Created by silen on 16-9-9.
 */

public class LockMode implements Serializable{
    private int id=0;
    private String name="标准模式";
    private long createTime=0l;
    private boolean isNew=false;
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
