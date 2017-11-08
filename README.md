# DuerOS java sdk

[![DuerOS](http://duer.bdstatic.com/saiya/dueros_static_79c1cb0828ef81e9863d87b7c5a154c2/statics/images//dumi/logo2.png)](http://duer.bdstatic.com)

java 版本sdk
##示例
在demo-test/src/main/resources/目录下创建duer.properties,输入，并配置好Oauth回调为
http://127.0.0.1:8080/auth
```
clientId=clientId
clientSecret=clientSecret
```
然后运行demo-test项目下的
```
java -jar xin.yysf.gui.DuerOSGui
```
![](https://raw.githubusercontent.com/microxdd/dueros/master/duer/STEP1.jpg)

播放音乐示例
![](https://raw.githubusercontent.com/microxdd/dueros/master/duer/STEP2.jpg)

java9 以下的jdk如要启用http2需要在添加vm参数
```
-Xbootclasspath/p:${settings.localRepository}/org/mortbay/jetty/alpn/alpn-boot/${alpn-boot.version}/alpn-boot-${alpn-boot.version}.jar.
```

