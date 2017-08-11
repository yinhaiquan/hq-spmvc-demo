/**
 * 字符串工具插件
 * Created by yinhaiquan on 2017/7/21.
 */
define('stringUtils',function(){
    return{
        /**
         * 判断字符串str是否以prefix前缀开始,true 是 false 否
         *
         * @param str          字符串
         * @param prefix       前缀
         * @returns {boolean}
         */
        startWith:function(str,prefix){
            if(null!=str&&null!=prefix){
               var obj_ = str.substring(0,prefix.length);
               if(obj_ == prefix){
                   return true;
               }
            }
            return false;
        },
        /**
         * 判断字符串str是否以suffix后缀结尾,true 是 false 否
         *
         * @param str          字符串
         * @param suffix       后缀
         * @returns {boolean}
         */
        endWith:function(str,suffix){
            if(null!=str&&null!=suffix){
                var obj_ = str.substring(str.length-suffix.length,str.length);
                if(obj_ == suffix){
                    return true;
                }
            }
            return false;
        },
        /**
         * 判断obj是否为空
         *
         * @param obj
         * @returns {boolean}
         */
        isEmpty_:function(obj){
            if(typeof obj === undefined){
                return true;
            }
            if(null==obj||''==obj){
                return true;
            }
            return false;
        },
        /**
         * 获取对象中key值列表
         * @param obj
         * @returns {Array}
         */
        getKeys:function(obj){
            var arr = new Array();
            if(obj&&typeof obj == "object"){
                for(var key in obj){
                    arr.push(key);
                }
            }
            return arr;
        }
    }
})