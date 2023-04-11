# 日志组件
## 1 简介
- 提供公共 logback-spring.xml
- 提供 controller 日志切面
- 提供自定义 Feign Logger
- 支持日志级别动态调整

## 2 快速开始
### step0：前提
- 若要开启自定义 Feign Logger，需要引入 openFeign
- 若要使用动态日志级别调整，需要引入 nacos

### step1：引入依赖
```xml
<dependency>
    <groupId>com.gjk/groupId>
    <artifactId>gjk-log-spring-boot-starter</artifactId>
</dependency>
```

### step2：配置
```properties
# 开启 controller 切面日志
gjk.log.controller.enable=true
gjk.log.controller.ignore-get-method=false
gjk.log.controller.record-response=true
# 开启自定义 feign 日志
gjk.log.feign.enable=true
# feign 原生配置
feign.client.config.default.logger-level=FULL
```

## 3 其他说明
- 开启自定义 feign 日志时，也支持原生 feign.client.config.default.logger-level 的配置
  - NONE: 不记录日志
  - BASIC: 仅记录请求方法、URL 以及响应状态码和执行时间
  - HEADERS: 除了记录 BASIC 级别的信息之外，还会记录请求和响应的头信息
  - FULL: 记录所有请求与响应的明细，包括头信息、请求体、元数据等
- 日志级别动态调整
  - 在对应 nacos 配置文件中直接改即可，例如：
```properties
logging.level.com.gjk=INFO
```
## 4 附录-自定义配置说明

| 属性                                     | 默认值   | 说明                   |
|----------------------------------------|-------|----------------------|
| `gjk.log.controller.enable`            | false | 是否开启 controller 日志切面 |
| `gjk.log.controller.ignore-get-method` | true  | 是否忽略 GET 请求的日志       |
| `gjk.log.controller.record-response`   | false | 日志是否记录返回参数           |
| `gjk.log.feign.enable`                 | false | 是否开启自定义 Feign 日志     |
| `gjk.log.feign.record-response`        | false | 日志是否记录返回参数           |
