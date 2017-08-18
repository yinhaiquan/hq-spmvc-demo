/**
 * 下拉框插件
 * Created by yinhaiquan on 2017/8/17.
 */
define('selectGroup',['jquery','ajax'],function($,AjaxUtils){
    var default_params = {
        url:'',               //[可选]请求地址
        data:[],              //[可选]下拉数据
        params:{},            //[可选]请求参数
        selector:'',          //[必选]下拉标签
        valueField:'value',   //[可选]值
        textField:'name',     //[可选]text
        tagField:'tag'        //[可选]是否selected
    }
    var selectGroupFunction = {
        selectGroup : function(options){
            var data = options.data;
            var valueField = options.valueField || 'value';
            var tagField = options.tagField || 'tag';
            var textField = options.textField || 'name';
            if(options.url){
                AjaxUtils.ajaxSimple(
                    'post',
                    options.url,
                    'json',
                    options.params,
                    AjaxUtils.Constant.Ajax.TIME_OUT,
                    function(obj){
                        data = obj.content;
                    }
                );
            }
            var str = '<span class="select-box mt-10"><select class="select" size="1">';
            for(var field in data){
                var obj = data[field];
                var selected = obj[tagField]?'selected':'';
                str+="<option value='"+obj[valueField]+"' "+selected+">"+obj[textField]+"</option>";
            }
            str+='</select></span>';
            $(options.selector).append(str);
        },
        formartSelectList : function(values,tag_val){
            var data = [];
            var t_v = tag_val || null;
            if(values){
                for(var value in values){
                    data.push({
                        value:values[value],
                        tag:values[value]==t_v?true:false,
                        name:values[value]
                    })
                }
            }
            return data;
        },
        getValue : function(selector){
           return $(selector).val();
        },
        setSelected : function(selector,value){
            $(selector).find("option[value='"+value+"']").attr("selected",true);
        }
    }
    return selectGroupFunction;
})