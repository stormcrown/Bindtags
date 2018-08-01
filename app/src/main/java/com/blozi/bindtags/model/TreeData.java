package com.blozi.bindtags.model;

import java.util.ArrayList;
import java.util.List;

public class TreeData  {

    private String hideId ;
    private String name ;
    private String code ;
    private int progress;
    private int state ;
    private List<TreeData>  children = new ArrayList<>() ;

    public TreeData() {
    }

    public TreeData(String hideId, String name, String code) {
        this.hideId = hideId;
        this.name = name;
        this.code = code;
    }


    @Override
    public String toString() {
        return "TreeData{" +
                "hideId='" + hideId + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", progress=" + progress +
                ", state=" + state +
                ", children=" + children +
                '}';
    }

    public String getHideId() {
        return hideId;
    }

    public void setHideId(String hideId) {
        this.hideId = hideId;
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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<TreeData> getChildren() {
        return children;
    }

    public void setChildren(List<TreeData> children) {
        this.children = children;
    }
    public void addTopChild(TreeData child) {
        if(this.children==null || this.children.size()==0   )this.children = new ArrayList<>(1);
        this.children.add(child);
        this.children.set(0,child);
    }

}
