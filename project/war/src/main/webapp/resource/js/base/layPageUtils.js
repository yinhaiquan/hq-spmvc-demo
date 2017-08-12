/**
 * 分页控件，基于laypage.js插件扩展
 * Created by yinhaiquan on 2017/8/1.
 * 本插件使用方法：
 *         方式一：传参data_数据源，可以根据fn函数自定义数据展示，亦可定义data数据列表按照内置函数展示【注意：使用内置函数时不可定义fn】;
 *         方式二: 传参url服务端请求地址，可以根据fn函数自定义数据展示，亦可定义data数据列表按照内置函数展示【注意：使用内置函数时不可定义fn】;
 */
define('layPageUtils',['jquery','laypager','css!laypagestyle','stringUtils','ajax'],function($,laypager,laypagestyle,StringUtils,AjaxUtils){
    /**
     * 控件参数配置讲解例子
     * @type {{data_: {}, url: string, method: string, tableId: string, cont: Element, pages: number, skip: boolean, groups: number, skin: string, first_: number, last_: string, prev_: string, next_: string, fn: fn, data: [*]}}
     */
    var default_setting = {
        data_ : {},                                  //[可选]数据源  用户测试
        url : '',                                    //[可选]请求地址
        params :{},                                  //[可选]请求参数
        type:'json',                                 //[可选]请求参数数据类型
        method : '',                                 //[可选]请求类型 post get
        tableId : 'biuuu_city_list',                 //[可选]数据容器 Id，若使用默认table，则须传入table的div ID
        cont : document.getElementById('biuuu_city'),//[必选]分页标签容器。值支持id名、原生dom对象，jquery对象
        skip : true,                                 //[可选]是否显示跳转
        groups :7,                                   //[可选]连续分页数
        skin:'#00AA91',                              //[可选]控制分页皮肤。一般传16进制色值即可
        first:1,                                    //[可选]首页数值[Number/String/Boolean]用于控制首页。值支持三种类型。如：first: '首页' 如：first: false，则表示不显示首页项
        last:'尾页',                                 //[可选]总页数值[Number/String/Boolean]用于控制尾页。值支持三种类型。如：last: '尾页' 如：last: false，则表示不显示尾页项
        prev:'上一页',                               //[可选]上一页[String/Boolean]用于控制上一页。若不显示，设置false即可
        next:'下一页',                               //[可选]下一页[String/Boolean]用于控制下一页。若不显示，设置false即可
        fn : function(obj){
            var str = '';
            str +='<table class="table table-border table-bordered table-bg radius"><thead><tr><th>表头</th></tr></thead><tbody>';
            str += '<tr><td>'+ '目前正在第'+ obj.curr +'页，一共有：'+ obj.pages +'页' +'</td></tr>';
            str+='</tbody></table>';
            document.getElementById('biuuu_city_list').innerHTML = str;
        },                                           //[可选]表单展示函数
        data:[
            {
                name:'姓名',
                value:'name',
                align:'text-c', //text-c 居中  text-r 居右  默认 居左
                width:'20%',
                iShow:true,
                formartVal:function(value){
                    return value;
                }
            },{
                name:'性别',
                value:'sex',
                align:'text-c',
                width:'50%',
                iShow:true,
                formartVal:function(value){
                    return value;
                }
            },{
                name:'操作',
                value:'option',
                align:'text-c',
                iShow:true,
                formartVal:function(value){
                    return value;
                }
            }
        ]                                            //[可选]数据源对应列名
    }
    var layPageFunction = {
        AjaxUtils : AjaxUtils,
        dataGride : function(setting){
            if(StringUtils.isEmpty_(setting)){
                console.info("空配置信息");
                return;
            }
            var data_ = setting.data_;
            var pager = layPageFunction.dataOption(setting,1);
            var pages = 0;
            //首页加载
            if(!StringUtils.isEmpty_(pager)){
                data_ = pager.rows;
                pages = pager.pages;
            }
            layPageFunction.dataTable(setting.data,data_,null,setting.tableId);
            console.info(StringUtils.isEmpty_(setting.first)?false:setting.first);
            console.info(StringUtils.isEmpty_(setting.last)?false:setting.last);
            console.info(StringUtils.isEmpty_(setting.prev)?false:setting.prev);
            /**
             * 分页信息展示
             */
            laypager({
                cont: setting.cont,
                pages: pages || 0,
                skip: StringUtils.isEmpty_(setting.skip)?false:setting.skip,
                skin: StringUtils.isEmpty_(setting.skin)?'#00AA91':setting.skin,
                groups: StringUtils.isEmpty_(setting.groups)?5:setting.groups,
                first: StringUtils.isEmpty_(setting.first)?false:setting.first,
                last: StringUtils.isEmpty_(setting.last)?false:setting.last,
                prev: StringUtils.isEmpty_(setting.prev)?false:setting.prev,
                next: StringUtils.isEmpty_(setting.next)?false:setting.next,
                jump: function(obj,first){
                    if(!first){
                        if(setting.fn){
                            setting.fn(data_);
                        }else{
                            //分页点击加载
                            var pager = layPageFunction.dataOption(setting,obj.curr);
                            if(!StringUtils.isEmpty_(pager)){
                                data_ = pager.rows;
                            }
                            layPageFunction.dataTable(setting.data,data_,obj,setting.tableId);
                        }
                    }
                }
            })
        },
        dataOption:function(setting,curr){
            var pager =null;
            if(setting.url){
                /**
                 * 分页请求参数格式
                 *   root:{
                             *      route: {
                             *          lang: "cn_ZH"
                             *       },
                             *       msg:{
                             *          xxxx:{
                             *             page:1,      //页码
                             *             pageSize:10, //页面大小
                             *             .
                             *             .
                             *             .
                             *          }
                             *       }
                             *      }
                 * @type {{}}
                 */
                setting.params.root.msg[StringUtils.getKeys(setting.params.root.msg)[0]].page = curr || 1;
                setting.params.root.msg[StringUtils.getKeys(setting.params.root.msg)[0]].pageSize = 3;
                AjaxUtils.ajaxSimple(
                    setting.method,
                    setting.url,
                    setting.type,
                    setting.params,
                    AjaxUtils.Constant.Ajax.TIME_OUT,
                    function(data){
                        /**
                         * 分页返回结果格式，其中content内为分页对象Pager
                         * {
                                     *   code : 0,
                                     *   desc : 'response ok',
                                     *   content : {
                                     *       pages : 5,       --总页数
                                     *       rows : [{},{}],  --数据列表
                                     *       total : 100,     --总数
                                     *       pageSize : 10    --页面大小
                                     *   }
                                     * }
                         */
                        if(data&&data.code==0){
                            if(!StringUtils.isEmpty_(data.content)){
                                pager ={};
                                pager.pages = data.content.pages;
                                pager.rows = data.content.rows;
                                pager.total = data.content.total;
                                pager.pageSize = data.content.pageSize;
                            }
                        }
                    }
                )
            }
            return pager;
        },
        /**
         * 表单遍历函数
         * @param data  表单列表
         * @param data_ 源数据
         * @param obj   laypager对象
         */
        dataTable : function(data,data_,obj,id){
            var str = '';
            str +='<table class="table table-border table-bordered table-bg radius"><thead><tr>';
            if(data&&data.length>0){
               for(var i =0;i<data.length;i++){
                   var obj_ = data[i];
                   if(obj_.iShow){
                       if(obj_.align){
                           if(obj_.width){
                               str += '<th class="'+obj_.align+'" width="'+obj_.width+'">'+obj_.name+'</th>';
                           }else{
                               str += '<th class="'+obj_.align+'">'+obj_.name+'</th>';
                           }
                       }else{
                           if(obj_.width){
                               str += '<th width="'+obj_.width+'">'+obj_.name+'</th>';
                           }else{
                               str += '<th>'+obj_.name+'</th>';
                           }
                       }
                   }
               }
            }else{
                return;
            }
            str +='</tr></thead><tbody>';
            if(data_&&data_.length>0){
                for(var i =0;i<data_.length;i++){
                   var val_obj = data_[i];
                   str+='<tr class="active">';
                   for(var j =0;j<data.length;j++){
                        var obj_ = data[j];
                        if(obj_.iShow){
                            if(obj_.align){
                                str+='<td class="'+obj_.align+'">';
                            }else{
                                str+='<td>';
                            }
                            if(val_obj[obj_.value]!= undefined){
                                if(obj_.formartVal){
                                    str += obj_.formartVal(val_obj[obj_.value],val_obj);
                                }else{
                                    str += val_obj[obj_.value];
                                }
                            }else if(val_obj[obj_.value] === undefined){
                                if(obj_.formartVal){
                                    str += obj_.formartVal(val_obj);
                                }
                            }
                            str+='</td>';
                        }
                   }
                   str+='</tr>';
                }
            }
            str+='</tbody></table>';
            document.getElementById(id).innerHTML = str;
        }
    }
    return layPageFunction;
})
