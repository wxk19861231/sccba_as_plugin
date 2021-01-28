package com.sccba.asplugin.model;

import java.io.File;

/**
 * 目录对象结构
 */
public class ListTreeInfo {

    // 节点路径
    private String path;

    // 节点属性
    private String properties;

    // 节点注解
    private String title;

    // 节点提示
    private String tooltip;

    // 名称约束
    private String code;

    // 数值约束
    private String codeValue;

    // 消息约束
    private String msg;

    public String getPath() {
        if (path != null) {
            if (!File.separator.equals("/")) {
                return path.replaceAll("/", "\\\\");
            }
        }
        return path;
    }

    public ListTreeInfo setPath(String path) {
        this.path = path;
        return this;
    }

    public String getProperties() {
        return properties;
    }

    public ListTreeInfo setProperties(String properties) {
        this.properties = properties;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ListTreeInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTooltip() {
        return tooltip;
    }

    public ListTreeInfo setTooltip(String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ListTreeInfo setCode(String code) {
        this.code = code;
        return this;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public ListTreeInfo setCodeValue(String codeValue) {
        this.codeValue = codeValue;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ListTreeInfo setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
