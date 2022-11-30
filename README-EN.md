<!--[**🌎中文文档**](README.md)-->

-------------------------------------------------------------------------------

## 📚 Introduction
<!-- Plugin description -->
<h3>ForestX is an IDEA plugin designed to support Forest</h3>
<h3>It greatly improves your development experience when using the <a href="https://forest.dtflyx.com/">Forest framework</a></h2>

## 🎁 ForestX Features
- Forest template expression syntax is supported (Grammar highlighting、Smart Tips)
- Provides the Forest tool window to quickly browse the Forest interface in your project
- In the tool window, different types of requests have different ICONS (e.g.: `GET`、`POST`)
- In the tool window, the URL path is displayed after each Forest interface and request method
- Supports jumping variables in template expressions to the 'properties' configuration file
- Supports jumping variables in template expressions to 'yaml' configuration file
- Supports jumping from variables in template expressions to the '@BindingVar' method definition code
- Supports jumping from variables in template expressions to the '@Var' parameter definition in method
- Supports jumping from variables attributes in template expressions to 'java' definition code

<!-- Plugin description end -->
-------------------------------------------------------------------------------

## 🛍 Install

Open the Intellij IDEA menu `File`->`Settings`->`Plugins`

Select the`Marketplace`Tab，search for`ForestX`，and then click`install`to install

## 🎨 Function Display

### Sidebar Navigation Toolbar

![tools-windows](/img/tools-window.gif)

Click the 'Forest' logo bird icon on the right to open the navigation toolbar of 'ForestX', which will list the Forest interfaces defined in the project together for easy management
### Completion Of Code

1. The code is completed according to the global variables defined under 'forest.variables' in the configuration file

![completion-global-variables](/img/completion-global-variables.gif)

2. Complete the code according to the YAML configuration items defined in the YAML configuration file

![completion-yaml](/img/completion-yaml.gif)

3. Complete the code according to the '@Var' parameter definition of the request method

![completion-var-parameter](/img/completion-var-parameter.gif)

4. Complete the code according to the method defined by the '@BindingVar' annotation

![compeltion-binding-var](/img/completion-BindingVar.gif)

5. In programmatic code, intelligent prompts for code completion may also appear

However, currently only 'Forest.get', 'Forest.post' and other request methods open secondary functions

![compeltion-forest-api](/img/completion-forest-api.gif)

### Jump Of Code

Hold down the keyboard 'Ctrl' key, move the mouse over an identifier (e.g. variable name) in the Forest template expression, and hover for a short time to pop up a short message about the configuration variable or Java property that the identifier refers to

Click the left mouse button to jump to the definition code of the variable/configuration referenced by the identifier

![jump](/img/jump-to-definition.gif)

Project License
--------------------------
The MIT License (MIT)

Copyright (c) 2022 CHMing