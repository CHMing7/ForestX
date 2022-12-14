[**🌎English Documentation**](README-EN.md)

-------------------------------------------------------------------------------

## 📚 简介

<!-- Plugin description -->
<h3>ForestX 是一款专为 Forest 提供支持的 IDEA 插件</h3>
<h3>它能大幅提高您使用 <a href="https://forest.dtflyx.com/">Forest 框架</a>时的开发体验</h2>

## 🎁 ForestX 特性

- 支持 Forest 模板表达式语法 (语法高亮、智能提示)
- 提供 Forest 工具窗口，可快速浏览项目中的 Forest 接口
- 在工具窗中，不同类型的请求有不同的图标 (如 `GET`、`POST`)
- 在工具窗中， 每个 Forest 接口和请求方法后都会显示 URL 路径
- 支持模板表达式中变量到`properties`配置文件的跳转
- 支持模板表达式中变量到`yaml`配置文件的跳转
- 支持模板表达式中变量到`@BindingVar`方法定义代码的跳转
- 支持模板表达式中变量到方法中`@Var`参数定义的跳转
- 支持模板表达式中变量属性到`java`定义代码的跳转

<!-- Plugin description end -->
-------------------------------------------------------------------------------

## 🛍 安装

#### 1. 从 Marketplace 下载

点开 Intellij IDEA 菜单中的 `File`->`Settings`->`Plugins`

选择`Marketplace`选项卡，搜索`ForestX`，然后点击`install`进行安装

环境要求: Intellij IDEA >= `2021.3`

#### 2. 本地安装

到如下地址中，寻找最新版本的 jar包进行下载，并在 IDEA 中以 Jar 包方式安装插件

[https://gitee.com/CHMing7/ForestX/releases](https://gitee.com/CHMing7/ForestX/releases)

## 🎨 功能展示

### 侧边导航工具栏

![tools-windows](/img/tools-window.gif)

点击右边的`Forest`logo小鸟图标，可打开`ForestX`的导航工具栏，它会把项目中定义的 Forest 接口都罗列在一起，方便管理

### 代码补全

1. 根据配置文件中`forest.variables`下定义的全局变量来补全代码

![completion-global-variables](/img/completion-global-variables.gif)

2. 根据YAML配置文件中定义的YAML配置项来补全代码

![completion-yaml](/img/completion-yaml.gif)

3. 根据请求方法的`@Var`参数定义来补全代码

![completion-var-parameter](/img/completion-var-parameter.gif)

4. 根据`@BindingVar`注解定义的方法来补全代码

![completion-binding-var](/img/completion-BindingVar.gif)

5. 在编程式的代码中，也可出现代码补全的智能提示

不过目前仅对 `Forest.get`、`Forest.post` 等请求方法开放次功能

![completion-forest-api](/img/completion-forest-api.gif)

### 代码跳转

按住键盘`Ctrl`键，将鼠标移动到 Forest 模板表达式中的标识符上(比如变量名)，并悬停一小段时间，就会跳出该标识符所引用的配置变量或Java属性的简短信息

此时点击鼠标左键，即可跳转到该标识符所引用的变量/配置的定义代码

![jump](/img/jump-to-definition.gif)

项目许可证
--------------------------
The MIT License (MIT)

Copyright (c) 2022 CHMing
