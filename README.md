# TreeInfoTip

### TreeInfoTip能做什么？

> TreeInfoTip是基于IntelliJ 开发的项目目录自定义备注显示，主要通过自定义XML来生成项目目录树备注。

### 为什么要这个插件

> 1、便于项目管理。

> 2、便于源码阅读。

### 使用环境

`IntelliJ IDEA`

`Android Studio`

### 在线安装(搜索)

IntelliJ IDEA or Android Studio -> Preferences -> Plugins -> TreeInfoTip

### 手动安装

[TreeInfoTip-1.0.0.zip](https://raw.githubusercontent.com/wxk19861231/TreeInfoTip/master/builds/TreeInfoTip-1.0.0.zip)

### 一、示列

> ##### 图片示列教程：


![样列](https://raw.githubusercontent.com/wxk19861231/TreeInfoTip/master/image/show_1.gif "样列")


> ##### 说明文档：

1. 在项目根目录下自动创建directory.xml文件

2. 文件内容示列

```xml：
  <?xml version="1.0" encoding="UTF-8"?>
  <trees>
      <tree path="/src" title="xxx"/>
  </trees>
```

3. 标签说明

```xml：
  // 根节点
  <trees></tress>：只能有一个，所有子标签都在此标签里面

  // 子模块 （待开发）
  <model/>：Maven多模块可以采用此标签作为标识

  // 子目录
  <tree/>：普通文件夹
```

4. 属性说明

```xml：
  // 相对路径
  <path/> 

  // 显示备注
  <title/> 
```