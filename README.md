# hq-spmvc-demo
基于springmvc spring aop分布式服务架构

<p>本框架使用注意事项</p>

@desc 有问题和建议可联系本人，一同改进框架
@author yinhaiquan
@date 2017/05/22 11:24:26.
@email 1083775683@qq.com
<p>**************************************************************</p>
<b>项目结构介绍</b>
project………………………………………………………………………………root
   |
   -----api………………………………………………………………………业务实现子项目，可实现对接分布式服务
   |
   -----common…………………………………………………………………集成工具子项目，redis、zookeeper、mq等
   |
   -----core……………………………………………………………………核心子项目，包括自定义注解定义，相关工具类等
   |
   -----datacenter…………………………………………………………数据中心,基于springmvc/aop/反射实现对方法请求处理
   |
   -----shiro……………………………………………………………………安全中心，基于shiro实现权限实现
   |
   -----war…………………………………………………………………………控制中心，处理请求响应统一入口，以及国际化处理

<p>**************************************************************</p>
<1>:请求参数定义

root:{
        route:{
            //自定义路由信息，可用于分布式项目分发使用，或者区域部署路由信息定义
        },
        msg:{
            //方法接收参数定义，msg内部参数可为String类型、自定义对象
            name:"sdf",
            userInfo:{
                name:"hehe",
                age:90
            }
        }
}

@note:
        1. 方法接收参数为[String类型]或者[String类型和自定义对象]时必须按照接收方法参数顺序定义
        2. 方法接收参数为[自定义对象]必须按照本框架自带参数注解配合使用,还需借助spring companent注解

        注解用法：
        eg:
        @Bean和@Component("userInfo")标记入参对象名称
        @Parameter(value = "name",type = String.class,desc = "name")定义入参字段及类型等

        @Bean
        @Component("userInfo")
        public class UserInDto extends InDto {
            @Parameter(value = "name",type = String.class,desc = "name")
            private String name;
            @Parameter(value = "age",type = Integer.class)
            private int age;
            @Parameter("desc")
            private String desc;
        }

<2>:请求响应
        返回json格式数据,并且必须重写输出实体的tostring方法json格式

<3>:请求路径定义

        http://ip:port/handler/className/methodName.do
        1. className:类全名包括包名
        2. methodName：方法名+.do(springMVC后缀，可自定义)
        3. 支持get/post请求

<4>:xml配置
        必须在资源文件夹中建立spring文件，并将xml文件存放至此
        eg:
        resources
            |
             ----spring
                     |
                     ------*.xml
<5>:思路
        基于springMVC、AOP开发，有助于静态分离，劈开前后端开发，支持基于dubbo、ICE等SOA框架开发

<6>:支持分布式部署

<7>:入参出参统一入口等，减小耦合度，开发难度及时间成本。

<8>:支持国际化，通过ctx中getMessage获取资源信息





===============================》 前端采用h-ui+requirejs框架，静态分离开发

注意事项：
生产环境或者测试环境或者开发环境需要修改config.js和url.js中的baseUrl相对路径，同时前端页面也可独立分支出去分布式部署，涉及权限代码根据实际项目情况修改即可































