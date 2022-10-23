## 记录项目中遇到的一些错误和解决方法

* 项目启动的时候控制台报这个错误

    **解决：这个问题是mybatis xml中传入类型不对导致的。将parameterType 改为传入参数类型即可，比如我传入的类型是String，就改为String。**
    **因为这个错误跟报错信息没有直接关系，所以找了好久好久。后来发现这个跟数据库设计的属性有关系。**
```
java.lang.ClassCastException: class java.io.ObjectStreamClass cannot be cast to class java.lang.String (java.io.ObjectStreamClass and java.lang.String are in module java.base of loader 'bootstrap')
        at java.base/java.io.ObjectInputStream.readTypeString(ObjectInputStream.java:1772) ~[na:na]
        at java.base/java.io.ObjectStreamClass.readNonProxy(ObjectStreamClass.java:793) ~[na:na]
        at java.base/java.io.ObjectInputStream.readClassDescriptor(ObjectInputStream.java:971) ~[na:na]
        at java.base/java.io.ObjectInputStream.readNonProxyDesc(ObjectInputStream.java:1997) ~[na:na]
        at java.base/java.io.ObjectInputStream.readClassDesc(ObjectInputStream.java:1875) ~[na:na]
        at java.base/java.io.ObjectInputStream.readOrdinaryObject(ObjectInputStream.java:2206) ~[na:na]
        at java.base/java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1692) ~[na:na]
        at java.base/java.io.ObjectInputStream.readObject(ObjectInputStream.java:499) ~[na:na]
        at java.base/java.io.ObjectInputStream.readObject(ObjectInputStream.java:457) ~[na:na]
        at org.apache.catalina.session.StandardSession.doReadObject(StandardSession.java:1587) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.session.StandardSession.readObjectData(StandardSession.java:1040) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.session.StandardManager.doLoad(StandardManager.java:218) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.session.StandardManager.load(StandardManager.java:162) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.session.StandardManager.startInternal(StandardManager.java:354) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.core.StandardContext.startInternal(StandardContext.java:5189) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1384) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1374) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at java.base/java.util.concurrent.FutureTask.run$$$capture(FutureTask.java:264) ~[na:na]
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java) ~[na:na]
        at org.apache.tomcat.util.threads.InlineExecutorService.execute(InlineExecutorService.java:75) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at java.base/java.util.concurrent.AbstractExecutorService.submit(AbstractExecutorService.java:140) ~[na:na]
        at org.apache.catalina.core.ContainerBase.startInternal(ContainerBase.java:909) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.core.StandardHost.startInternal(StandardHost.java:829) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1384) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1374) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at java.base/java.util.concurrent.FutureTask.run$$$capture(FutureTask.java:264) ~[na:na]
        at java.base/java.util.concurrent.FutureTask.run(FutureTask.java) ~[na:na]
        at org.apache.tomcat.util.threads.InlineExecutorService.execute(InlineExecutorService.java:75) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at java.base/java.util.concurrent.AbstractExecutorService.submit(AbstractExecutorService.java:140) ~[na:na]
        at org.apache.catalina.core.ContainerBase.startInternal(ContainerBase.java:909) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.core.StandardEngine.startInternal(StandardEngine.java:262) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.core.StandardService.startInternal(StandardService.java:433) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.core.StandardServer.startInternal(StandardServer.java:930) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.apache.catalina.startup.Tomcat.start(Tomcat.java:486) ~[tomcat-embed-core-9.0.46.jar:9.0.46]
        at org.springframework.boot.web.embedded.tomcat.TomcatWebServer.initialize(TomcatWebServer.java:123) ~[spring-boot-2.5.0.jar:2.5.0]
        at org.springframework.boot.web.embedded.tomcat.TomcatWebServer.<init>(TomcatWebServer.java:104) ~[spring-boot-2.5.0.jar:2.5.0]
        at org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory.getTomcatWebServer(TomcatServletWebServerFactory.java:450) ~[spring-boot-2.5.0.jar:2.5.0]
        at org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory.getWebServer(TomcatServletWebServerFactory.java:199) ~[spring-boot-2.5.0.jar:2.5.0]
        at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.createWebServer(ServletWebServerApplicationContext.java:182) ~[spring-boot-2.5.0.jar:2.5.0]
        at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.onRefresh(ServletWebServerApplicationContext.java:160) ~[spring-boot-2.5.0.jar:2.5.0]
        at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:577) ~[spring-context-5.3.7.jar:5.3.7]
        at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:145) ~[spring-boot-2.5.0.jar:2.5.0]
        at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:758) ~[spring-boot-2.5.0.jar:2.5.0]
        at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:438) ~[spring-boot-2.5.0.jar:2.5.0]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:337) ~[spring-boot-2.5.0.jar:2.5.0]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1336) ~[spring-boot-2.5.0.jar:2.5.0]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1325) ~[spring-boot-2.5.0.jar:2.5.0]
        at com.soft.mydemo.MyDemoApplication.main(MyDemoApplication.java:15) ~[classes/:na]
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:na]
        at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
        at java.base/java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
        at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:49) ~[spring-boot-devtools-2.5.0.jar:2.5.0]

```