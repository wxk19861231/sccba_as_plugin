# sccba_as_plugin

### sccba_as_plugin能做什么？

> sccba_as_plugin是基于IntelliJ 开发的项目树自定义备注显示，主要通过自定义XML来生成项目树备注。

### 为什么要这个插件

> 1、便于项目管理。

> 2、便于源码阅读。

### 使用环境

`IntelliJ IDEA`

`Android Studio`

### 在线安装(搜索)

IntelliJ IDEA or Android Studio -> Plugins -> sccba_as_plugin

### 手动安装

[sccba_as_plugin-1.0.0.zip](https://raw.githubusercontent.com/wxk19861231/sccba_as_plugin/master/builds/sccba_as_plugin-1.0.0.zip)

### 一、示列

> ##### 图片示列教程：


![样列](https://raw.githubusercontent.com/wxk19861231/sccba_as_plugin/master/image/show_1.png "样列")
![样列](https://raw.githubusercontent.com/wxk19861231/sccba_as_plugin/master/image/show_2.png "样列")
![样列](https://raw.githubusercontent.com/wxk19861231/sccba_as_plugin/master/image/show_3.png "样列")
![样列](https://raw.githubusercontent.com/wxk19861231/sccba_as_plugin/master/image/show_4.png "样列")

> ##### 说明文档：

1. 在项目根目录下手动创建directory.xml文件

```xml：
  <?xml version="1.0" encoding="UTF-8"?>
  <trees></trees>
```

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

  // 目录或文件
  <tree/>：目录或文件
```

4. 属性说明

```xml：
  // 相对路径
  <path/> 

  // 显示备注
  <title/> 
```