/**
 * checkbox插件基于icheck
 * 参考：https://github.com/fronteed/iCheck
 * Created by yinhaiquan on 2017/8/23.
 */
define('icheckUtils',['jquery'],function($){
    var icheckFunctions = {
        CheckBox:{},
        Radio:{}
    }
    /*复选框*/
    icheckFunctions.CheckBox = {
        /**
         * 获取checkbox所有选中的值
         * @param checkboxName 复选框name
         * @returns {string}
         */
        getCheckBoxChecked : function(checkboxName){
            var str="";
            $("input[name="+checkboxName+"]:checkbox").each(function(){
                if(true == $(this).is(':checked')){
                    str+=$(this).val()+",";
                }
            });
            return str?str.substr(0,str.length-1):'';
        },
        /**
         * 全选
         * @param checkboxName 复选框name
         */
        allSelected : function(checkboxName){
            var tag = true;
            $("input[name="+checkboxName+"]:checkbox").each(function(){
                if(true == $(this).is(':checked')){
                    tag = false;
                }
            })
            $("input[name="+checkboxName+"]:checkbox").iCheck(tag?'check':'uncheck');
        },
        /**
         * 反选
         * @param checkboxName 复选框name
         */
        reverse : function(checkboxName){
            $("input[name="+checkboxName+"]:checkbox").iCheck('toggle');
        }
    }
    /*单选框*/
    icheckFunctions.Radio = {

    }
    icheckFunctions = {
        /**
         * 设置复选框/单选框属性状态
         * @param item 复选框/单选框实体
         * @param state 状态值 check        选中
         *                    uncheck      未选中
         *                    toggle       切换
         *                    disable      不可用
         *                    enable       可用
         */
        setICheck : function(item,state){
            $(item).iCheck(state);
        }
    }
    return icheckFunctions;
})