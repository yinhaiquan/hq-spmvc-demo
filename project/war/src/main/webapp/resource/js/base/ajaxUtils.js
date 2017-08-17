/**
 * 自定义ajax通用插件，基于requirejs插件
 * Created by yinhaiquan on 2017/7/21.
 */
define('ajax',['url','stringUtils','constant'],function(url,stringUtils,constant){
        var ajaxContent = {
            /**
             * 请求地址集合
             */
            URLS : url,
            /**
             * string工具函数
             */
            StringUtils : stringUtils,
            /**
             * 常量集合
             */
            Constant : constant,
            /**
             * 请求参数定义
             *      route : 路由信息
             *      msg ：ajax请求参数信息
             *
             */
            ajaxParam:{
                root:{
                    route: {
                        lang: "cn_ZH"
                    },
                    msg:{}
                }
            },
            /**
             * ajax 请求函数
             * @param async      同步标识 false|true
             * @param type       请求类型 post|get
             * @param url        请求地址
             * @param iscache    缓存标识 false|true
             * @param dataType   数据类型 json|text|jsonp...
             * @param data       参数
             * @param timeout    ajax请求超时时间(单位ms) 默认30s
             * @param completefn 完成/失败处理函数 获取返回数据方式：var obj = eval('(' + json.responseText + ')');
             * @param sucessfn   请求成功处理函数
             * @param errorfn    失败处理函数
             */
            HQAJAX : function(async,type,url,iscache,dataType,data,timeout,completefn,sucessfn,errorfn){
                $.ajax({
                    async:async,
                    type: type,
                    url: url,
                    cache: iscache,
                    dataType: dataType,
                    data: data,
                    timeout: timeout,
                    complete: function (XMLHttpRequest, textStatus) {
                        if(completefn){
                            completefn(XMLHttpRequest);
                        }
                    },
                    success:function(data, textStatus){
                        if(sucessfn){
                            sucessfn(data);
                        }
                    },
                    error:function (XMLHttpRequest, textStatus, errorThrown) {
                        if(errorfn){
                            errorfn(XMLHttpRequest);
                        }
                    }
                })
            },
            /**
             * ajax 请求函数(简版)
             *
             * @note: 默认异步请求async:false
             *        默认不使用缓存cache: false
             *
             * @param type      请求类型 post|get
             * @param url       请求地址
             * @param dataType  数据类型 json|text|jsonp...
             * @param data      参数
             * @param timeout   ajax请求超时时间(单位ms)
             * @param sucessfn  请求成功处理函数
             */
            ajaxSimple : function(type,url,dataType,data,timeout,sucessfn){
                $.ajax({
                    async:false,
                    type: type,
                    url: ajaxContent.formartURL(url),
                    cache: false,
                    dataType: dataType,
                    data: data,
                    timeout: timeout,
                    success:function(data, textStatus){
                        if(sucessfn){
                            sucessfn(data);
                        }
                    },
                    error:function (XMLHttpRequest, textStatus, errorThrown) {
                       console.info("ajax 异常日志：");
                       if(textStatus == 'timeout'){
                           layer.alert("网络请求超时！",{icon: 1});
                           // $.Huimodalalert('网络请求超时！',2000);
                           console.info('网络请求超时!');
                           return;
                       }
                       if (textStatus == 'error' && XMLHttpRequest.status == 404){
                           layer.alert('请求地址不存在,或者服务未启动!',{icon: 1});
                           // $.Huimodalalert('请求地址不存在,或者服务未启动!',2000);
                           console.info('请求地址不存在,或者服务未启动!');
                           return;
                       }
                       $.Huimodalalert(XMLHttpRequest.responseText);
                    }
                })
            },
            /**
             * 格式化请求地址
             *
             * @param url_ 相对地址
             * @returns {*}
             */
            formartURL : function(url_){
                var base = url.baseUrl;
                var split_;
                if(stringUtils.startWith(url_,constant.Characters.C_HTTP)||stringUtils.startWith(url_,constant.Characters.C_HTTPS)){
                    return url_;
                }
                if(stringUtils.endWith(base,constant.Characters.C_SLASH)||
                    stringUtils.startWith(url_,constant.Characters.C_SLASH)){
                    split_ = '';
                }else{
                    split_ = constant.Characters.C_SLASH;
                }
                return base+split_+url_;
            }
        }
    return ajaxContent;
})