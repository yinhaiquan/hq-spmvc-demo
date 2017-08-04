/**
 * js国际化工具函数插件[自己扩展]
 *
 * 使用自定义国际化标签定义字段<i18n key='test'></i18n>
 * Created by yinhaiquan on 2017/7/25.
 */
define('i18nUtils',['jquery','jqueryi18n'],function($,jqueryi18n){
    return{
        /**
         * 获取当前浏览器语言类型
         * @returns {string}
         */
        language:function(){
            var language_;
            if(navigator.language){
                language_ = navigator.language;
            }else{
                language_ = navigator.browserLanguage;
            }
            return language_.toLowerCase().replace('-','_');
        },
        /**
         * 初始化国际化js核心库
         * @param language
         */
        initI18n:function(language){
            console.info("当前所在语言环境:")
            console.info(language);
            $.i18n.properties({//加载资浏览器语言对应的资源文件
                name:'hqmessages', //资源文件名称
                path:'../../resource/js/i18n/', //资源文件路径
                language:language,
                mode:'map', //用Map的方式使用资源文件中的值
                cache:false,
                encoding: 'UTF-8',
                callback: function() {//加载成功后设置显示内容
                    console.info("js国际化初始化完成!")
                }
            });
        },
        /**
         * 执行国际化脚本
         * i18n乃自定义国际化标签字段标记
         */
        excutei18n:function(){
            $('body','html').find('i18n').each(function(){
                $(this).text($.i18n.prop($(this).attr('key')));
            })
        },
        /**
         * 获取国际化内容
         * @param key
         */
        getMessages:function(key,str){
            if(typeof str == undefined){
                return $.i18n.prop(key);
            }
            return $.i18n.prop(key,str);
        },
        getMessages:function(key,var_1,var_2){
            if(typeof var_1 == undefined || typeof var_2 == undefined){
                return $.i18n.prop(key);
            }
            return $.i18n.prop(key,var_1,var_2);
        }
    }
})