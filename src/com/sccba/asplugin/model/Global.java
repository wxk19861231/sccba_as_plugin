package com.sccba.asplugin.model;

import com.intellij.openapi.diagnostic.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Global {

    // 获取日志实例
    public static final Logger logger = Logger.getInstance(Global.class);

    // XML存储对象
    public static List<ListTreeInfo> listTreeInfos = new CopyOnWriteArrayList<>();

    // 项目路径
    public static String PROJECT_PATH = null;

    // 配置文件名称
    public static final String DIRECTORY = "directory.xml";

    // 节点路径
    public static final String PATH = "path";

    // 节点属性
    public static final String PROPERTIES = "properties";

    // 节点注解
    public static final String TITLE = "title";

    // 节点提示
    public static final String TOOLTIP = "tooltip";

    // 名称约束
    public static final String CODE = "code";

    // 数值约束
    public static final String CODEVALUE = "codeValue";

    // 消息约束
    public static final String MSG = "msg";

    // 分支路径
    public static final String SUBPATH = "subpath";

    // 根节点
    public static final String TREES = "trees";

    // 子节点
    public static final String TREE = "tree";

    // 子模块
    public static final String MODEL = "model";
}
