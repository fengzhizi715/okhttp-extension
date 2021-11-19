[![@Tony沈哲 on weibo](https://img.shields.io/badge/weibo-%40Tony%E6%B2%88%E5%93%B2-blue.svg)](http://www.weibo.com/fengzhizi715)
[![License](https://img.shields.io/badge/license-Apache%202-lightgrey.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![](https://jitpack.io/v/fengzhizi715/okhttp-extension.svg)](https://jitpack.io/#fengzhizi715/okhttp-extension)

# okhttp-extension

okhttp-extension 是针对 okhttp 3 增强的网络框架。使用 Kotlin 特性编写，提供便捷的 DSL 方式创建网络请求，支持协程、响应式编程等等。

core 模块只依赖 okhttp，不会引入第三方库。

okhttp-extension 可以兼容 Retrofit、Feign。 另外，okhttp-extension 也给开发者提供一种新的选择。

## Features:

* 支持 DSL 创建 HTTP `GET`/`POST`/`PUT`/`HEAD`/`DELETE`/`PATCH` requests.
* 支持自定义线程池
* 支持 Kotlin 协程
* 支持响应式(RxJava、Spring Reactor)
* 支持函数式
* 支持熔断器(Resilience4j)
* 支持异步请求的取消
* 支持 Request、Response 的拦截器
* 支持 traceId 的传递(通过拦截器)
* 支持整合 Retrofit、Feign 框架
* 支持 websocket 的实现、重连等
* 提供常用的拦截器
* core 模块只依赖 okhttp，不依赖其他第三方库

![okhttp-extension](images/okhttp-extension.png)


## 最新版本
模块|最新版本
---|:-------------:
core|[![](https://jitpack.io/v/fengzhizi715/okhttp-extension.svg)](https://jitpack.io/#fengzhizi715/okhttp-extension)|
coroutines|[![](https://jitpack.io/v/fengzhizi715/okhttp-extension.svg)](https://jitpack.io/#fengzhizi715/okhttp-extension)|
rxjava3|[![](https://jitpack.io/v/fengzhizi715/okhttp-extension.svg)](https://jitpack.io/#fengzhizi715/okhttp-extension)|
rxjava2|[![](https://jitpack.io/v/fengzhizi715/okhttp-extension.svg)](https://jitpack.io/#fengzhizi715/okhttp-extension)|
reactor|[![](https://jitpack.io/v/fengzhizi715/okhttp-extension.svg)](https://jitpack.io/#fengzhizi715/okhttp-extension)|
result|[![](https://jitpack.io/v/fengzhizi715/okhttp-extension.svg)](https://jitpack.io/#fengzhizi715/okhttp-extension)|
resilience4j|[![](https://jitpack.io/v/fengzhizi715/okhttp-extension.svg)](https://jitpack.io/#fengzhizi715/okhttp-extension)|

## 下载

将它添加到项目的 root build.gradle 中：

```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```groovy
implementation 'com.github.fengzhizi715.okhttp-extension:core:<latest-version>'
```

```groovy
implementation 'com.github.fengzhizi715.okhttp-extension:coroutines:<latest-version>'
```

```groovy
implementation 'com.github.fengzhizi715.okhttp-extension:rxjava3:<latest-version>'
```

```groovy
implementation 'com.github.fengzhizi715.okhttp-extension:rxjava2:<latest-version>'
```

```groovy
implementation 'com.github.fengzhizi715.okhttp-extension:reactor:<latest-version>'
```

```groovy
implementation 'com.github.fengzhizi715.okhttp-extension:result:<latest-version>'
```

```groovy
implementation 'com.github.fengzhizi715.okhttp-extension:resilience4j:<latest-version>'
```

## TODO List:

* 增加文档
* 支持 OAuth2 认证
* 支持 JWT 认证
* 完善 Spring 和 Springboot 集成