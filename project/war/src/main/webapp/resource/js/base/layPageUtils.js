/**
 * 分页控件，基于laypage.js插件扩展
 * 参考：http://www.layui.com/laypage/?page=5
 * Created by yinhaiquan on 2017/8/1.
 * 本插件使用方法：
 *         方式一：传参data_数据源，可以根据fn函数自定义数据展示，亦可定义data数据列表按照内置函数展示【注意：使用内置函数时不可定义fn】;
 *         方式二: 传参url服务端请求地址，可以根据fn函数自定义数据展示，亦可定义data数据列表按照内置函数展示【注意：使用内置函数时不可定义fn】;
 */
define('layPageUtils',['jquery','laypager','css!laypagestyle','stringUtils','ajax','selectGroup'],function($,laypager,laypagestyle,StringUtils,AjaxUtils,SelectGroup){
    /**
     * 控件参数配置讲解例子
     * @type {{data_: {}, url: string, method: string, tableId: string, cont: Element, pages: number, skip: boolean, groups: number, skin: string, first_: number, last_: string, prev_: string, next_: string, fn: fn, data: [*]}}
     */
    var default_setting = {
        data_ : [],                                  //[可选]数据源  用户测试
        url : '',                                    //[可选]请求地址
        params :{},                                  //[可选]请求参数
        type:'json',                                 //[可选]请求参数数据类型
        method : '',                                 //[可选]请求类型 post get
        tableId : 'biuuu_city_list',                 //[可选]数据容器 Id，若使用默认table，则须传入table的div ID
        table_class:'table table-border table-bordered table-hover table-bg radius',//[可选]数据容器table class 默认table table-border table-bordered table-hover table-bg radius
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
                order:false,
                iShow:true,
                formartVal:function(value){
                    return value;
                }
            },{
                name:'性别',
                value:'sex',
                align:'text-c',
                width:'50%',
                order:true,
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
        ],                                           //[可选]数据源对应列名
        pageSize:[10,20,30,40,50],                   //[可选]页面大小列表
        currentSize:10                               //[可选]默认当前页面大小
    }
    //全局表ID后缀
    var tableIdSuffix = "_table_id";
    var layPageFunction = {
        AjaxUtils : AjaxUtils,
        dataGride : function(setting){
            if(StringUtils.isEmpty_(setting)){
                console.info("空配置信息");
                return;
            }
            //table 首行加载
            layPageFunction.titleTable(setting,null);
            layPageFunction.dataLoading(setting,null,false);
            $("table").find('.check-box input').iCheck({
                checkboxClass: 'icheckbox-grey',
                radioClass: 'iradio-blue',
                increaseArea: '20%'
            });
            return setting;
        },
        /**
         * 页面大小选中触发数据刷新
         * @param setting
         */
        pageSizeSelectChange : function(setting){
            $(setting.cont).find('div select').bind('change',function(){
                layPageFunction.dataLoading(setting,null,SelectGroup.getValue(this));
            })
        },
        /**
         * 分页请求
         * @param setting
         * @param obj
         */
        dataPage : function(setting,obj,currentPageSize){
            layPageFunction.dataOption(setting,obj.curr,currentPageSize,false,true);
        },
        /**
         * 页数大小下拉列表加载
         * 注意：若pageSize不传则默认不显示页面大小列表展示
         * @param setting
         * @param currenPageSize
         */
        selectGroupShow:function(setting,currenPageSize){
            if(!setting.pageSize){
                return;
            }
            layPageFunction.pageListShow(setting);
            SelectGroup.setSelected($(setting.cont).find('div select'),currenPageSize);
            layPageFunction.pageSizeSelectChange(setting);
        },
        /**
         * 重新加载
         * 注意：currenPageSize当前页输入查询条件调用时可不传
         * @param setting
         * @param msg
         */
        dataLoading : function(setting,msg,currenPageSize){
            currenPageSize = currenPageSize?SelectGroup.getValue($(setting.cont).find('div select')):setting.currentSize;
            setting.params.root.msg = msg || setting.params.root.msg;
            setting.params=setting.params;
            //请求数据
            layPageFunction.dataOption(setting,1,currenPageSize,true,true);
        },
        /**
         * 页数下拉表展示
         * @param setting
         */
        pageListShow : function(setting){
            if(!setting.pageSize){
                return;
            }
            var page_list = setting.pageSize;
            var current_pageSize = setting.currentSize;
            SelectGroup.selectGroup({
                data:SelectGroup.formartSelectList(page_list,current_pageSize),
                selector:$(setting.cont).find('div')
            });
            $(setting.cont).find('div span:last').css({'width':'50px','height':'28px','padding':'1px'});
        },
        /**
         * 请求数据
         * @param setting
         * @param curr
         * @param currenPageSize 当前页面大小
         * @param isPage 是否需要刷新分页控件
         * @param iSelect 是否重新加载页面大小列表
         * @returns {*}
         */
        dataOption : function(setting,curr,currenPageSize,isPage,iSelect){
            /*table id*/
            var id = setting.tableId;
            //添加加载loading
            $("#"+id+tableIdSuffix).find("tbody").remove('div');
            var list = $("#"+id+tableIdSuffix).find("thead tr th");
            $("#"+id+tableIdSuffix).find("tbody").append("<tr><td colspan='"+list.length+"'><div class='load'></div></td></tr>");
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
                             *             order:'',    //排序字段
                             *             orderType:'asc' //排序类型
                             *             .
                             *             .
                             *             .
                             *          }
                             *       }
                             *      }
                 * @type {{}}
                 */

                setting.params.root.msg[StringUtils.getKeys(setting.params.root.msg)[0]].page = curr || 1;
                setting.params.root.msg[StringUtils.getKeys(setting.params.root.msg)[0]].pageSize = currenPageSize;
                var params_ = setting.params;
                AjaxUtils.ajaxSimple(
                    setting.method,
                    setting.url,
                    setting.type,
                    params_,
                    AjaxUtils.Constant.Ajax.TIME_OUT,
                    function(data){
                        $("#"+id+tableIdSuffix).find("tbody").remove('div');
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
                                var data_;
                                var pages = 0;
                                data_ = setting.data_;
                                //首页加载
                                if(!StringUtils.isEmpty_(pager)){
                                    data_ = pager.rows;
                                    pages = pager.pages;
                                }
                                if(layPageFunction.noDateShow(setting.tableId,data_)){
                                    return;
                                }
                                //数据加载
                                layPageFunction.dataTable(setting,data_,null);
                                if(isPage){
                                    laypager({
                                        cont: setting.cont,
                                        pages: pages || 1,
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
                                                    layPageFunction.dataPage(setting,obj,currenPageSize);
                                                }
                                            }
                                        }
                                    })
                                }
                                if (iSelect){
                                    layPageFunction.selectGroupShow(setting,currenPageSize);
                                }
                            }
                        }
                    },
                    id+tableIdSuffix,
                    "tbody"
                )
            }
            return pager;
        },
        /**
         * table首行加载
         * @param setting
         * @param obj
         */
        titleTable : function(setting,obj){
            console.info("table首行加载");
            /*表单列表*/
            var data = setting.data;
            /*table id*/
            var id = setting.tableId,table_class=setting.table_class||'table table-border table-bordered table-hover table-bg radius';
            $(document.getElementById(id)).append('<table id="'+
                id+tableIdSuffix+'" class="'+table_class+'">' +
                '<thead><tr></tr></thead><tbody></tbody></table>');
            if(data&&data.length>0){
                var thead_tr = $("#"+id+tableIdSuffix).find("thead tr");
                for(var i =0;i<data.length;i++){
                    var obj_ = data[i];
                    if(obj_.iShow){
                        var align = obj_.align||'';
                        var width = obj_.width||'';
                        var order = align+(obj_.order?' sorting':'');
                        thead_tr.append('<th class="'+order+'" width="'+width+'" id="'+obj_.value+'">'+obj_.name+'</th>');
                    }
                }
                $("table thead th").find("input:checkbox").on("ifToggled",function() {
                    $("table td").find("input:checkbox").iCheck('toggle');
                });
            }
            $(document.getElementById(id)).find('th').bind('click',function(){
                var current_ = this;
                var currentcls = $(current_).attr('class');
                if(currentcls.indexOf('sorting')<0){
                    return;
                }
                $(this).toggleClass(function(){
                    var cls = $(this).attr('class');
                    $(this).removeClass('sorting_asc sorting sorting_desc');
                    return cls.indexOf('sorting_desc')>=0?'sorting_asc':'sorting_desc';
                })
                $(document.getElementById(id)).find('th').each(function(){
                    if(this!=current_){
                        var childcls = $(this).attr('class');
                        if (childcls.indexOf('sorting_asc')>=0||childcls.indexOf('sorting_desc')>=0){
                            $(this).removeClass('sorting_asc sorting sorting_desc');
                            $(this).addClass('sorting');
                        }
                    }
                })
                var clickcls = $(this).attr('class');
                var order_name = $(this).attr('id');
                var order_type = clickcls.indexOf('sorting_asc')>=0?'asc':'desc';
                //请求数据
                setting.params.root.msg[StringUtils.getKeys(setting.params.root.msg)[0]].order = order_name;
                setting.params.root.msg[StringUtils.getKeys(setting.params.root.msg)[0]].orderType = order_type || 'desc';
                layPageFunction.dataOption(setting,1,SelectGroup.getValue($(setting.cont).find('div select'))||setting.currentSize,false,false);
            })
        },
        /**
         * 表单遍历函数
         * @param setting 配置信息
         * @param data_ 源数据
         * @param obj   laypager对象
         */
        dataTable : function(setting,data_,obj){
            /*表单列表*/
            var data = setting.data;
            /*table id*/
            var id = setting.tableId;
            $("#"+id+tableIdSuffix).find("tbody").html('');
            if(data_&&data_.length>0){
                for(var i =0;i<data_.length;i++){
                   var val_obj = data_[i];
                   var str ='<tr class="">';
                   for(var j =0;j<data.length;j++){
                        var obj_ = data[j];
                        if(obj_.iShow){
                            var align = obj_.align||'';
                            str+='<td class="'+align+'">';
                            if(val_obj[obj_.value]!= undefined){
                                //已知参数自定义函数处理
                                str += obj_.formartVal?obj_.formartVal(val_obj[obj_.value],val_obj):val_obj[obj_.value];
                            }else if(val_obj[obj_.value] === undefined){
                                //未知参数自定义函数处理，例如：操作等行
                                if(obj_.formartVal){
                                    str += obj_.formartVal(val_obj);
                                }
                            }
                            str+='</td>';
                        }
                   }
                   str+='</tr>';
                   $("#"+id+tableIdSuffix).find("tbody").append(str);
                }
            }
        },
        noDateShow : function(id,data_){
            if(StringUtils.isEmpty_(data_)){
                var list = $("#"+id+tableIdSuffix).find("thead tr th");
                $("#"+id+tableIdSuffix).find("tbody").append("<tr><td colspan='"+list.length+"'><div align='center'>暂无数据</div></td></tr>");
                return true;
            }
            return false;
        }
    }
    return layPageFunction;
})
