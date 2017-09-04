package com.wocao.sherlock.Shortcut.Model;

/**
 * Created by silen on 17-3-8.
 */

public class ShortCut {
    private int id;
    private String label;
    private long time;//秒为单位
    private int modeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getModeId() {
        return modeId;
    }

    public void setModeId(int modeId) {
        this.modeId = modeId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
