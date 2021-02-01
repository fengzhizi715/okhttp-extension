# okhttp-extension

okhttp-extension 是针对 okhttp 3 增强的网络框架。使用 Kotlin 特性编写，提供便捷的 DSL 方式创建网络请求，支持协程、响应式编程等等。
core 模块只依赖 okhttp，不会引入第三方库。

okhttp-extension 可以兼容 Retrofit、Feign。 另外，okhttp-extension 也给开发者提供一种新的选择。 

## Features:

* 支持 DSL 创建网络请求
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

## TODO List:

* 支持 websocket 的实现
* 支持文件上传、下载
* 使用 RxCache 存储 cookie
* 使用 RxCache 作为 OkHttp 的缓存