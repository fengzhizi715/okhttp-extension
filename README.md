[![@Tony沈哲 on weibo](https://img.shields.io/badge/weibo-%40Tony%E6%B2%88%E5%93%B2-blue.svg)](http://www.weibo.com/fengzhizi715)
[![License](https://img.shields.io/badge/license-Apache%202-lightgrey.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

# okhttp-extension

okhttp-extension 是针对 okhttp 3 增强的网络框架。使用 Kotlin 特性编写，提供便捷的 DSL 方式创建网络请求，支持协程、响应式编程等等。
core 模块只依赖 okhttp，不会引入第三方库。

okhttp-extension 可以兼容 Retrofit、Feign。 另外，okhttp-extension 也给开发者提供一种新的选择。

## Features:

* 支持 DSL 创建网络请求
* 支持 websocket 的实现、重连等
* 支持自定义线程池
* 支持 Kotlin 协程
* 支持响应式(RxJava、Spring Reactor)
* 支持函数式
* 支持熔断器(Resilience4j)
* 支持异步请求的取消
* 支持 Request、Response 的拦截器
* 支持 traceId 的传递(通过拦截器)
* 支持 Retrofit、Feign 框架
* core 模块只依赖 okhttp，不依赖其他第三方库

## 最新版本

模块|最新版本
---|:-------------:
http-core|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/http-core/images/download.svg) ](https://bintray.com/fengzhizi715/maven/http-core/_latestVersion)|
http-coroutines|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/http-coroutines/images/download.svg) ](https://bintray.com/fengzhizi715/maven/http-coroutines/_latestVersion)|
http-rxjava3|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/http-rxjava3/images/download.svg) ](https://bintray.com/fengzhizi715/maven/http-rxjava3/_latestVersion)|
http-rxjava2|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/http-rxjava2/images/download.svg) ](https://bintray.com/fengzhizi715/maven/http-rxjava2/_latestVersion)|
http-reactor|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/http-reactor/images/download.svg) ](https://bintray.com/fengzhizi715/maven/http-reactor/_latestVersion)|
http-result|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/http-result/images/download.svg) ](https://bintray.com/fengzhizi715/maven/http-result/_latestVersion)|
http-resilience4j|[ ![Download](https://api.bintray.com/packages/fengzhizi715/maven/http-resilience4j/images/download.svg) ](https://bintray.com/fengzhizi715/maven/http-resilience4j/_latestVersion)|

## 下载

```groovy
implementation 'cn.netdiscovery.http:http-core:<latest-version>'
```

```groovy
implementation 'cn.netdiscovery.http:http-coroutines:<latest-version>'
```

```groovy
implementation 'cn.netdiscovery.http:http-rxjava3:<latest-version>'
```

```groovy
implementation 'cn.netdiscovery.http:http-rxjava2:<latest-version>'
```

```groovy
implementation 'cn.netdiscovery.http:http-reactor:<latest-version>'
```

```groovy
implementation 'cn.netdiscovery.http:http-result:<latest-version>'
```

```groovy
implementation 'cn.netdiscovery.http:http-resilience4j:<latest-version>'
```

## TODO List:

* 支持文件上传、下载
* 支持OAuth2验证