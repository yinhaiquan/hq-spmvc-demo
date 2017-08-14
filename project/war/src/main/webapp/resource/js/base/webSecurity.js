/**
 * web 安全控制插件
 * Created by yinhaiquan on 2017/8/14.
 */
define('webSecurity',['xss','stringUtils'],function(XSS,StringUtils){
    var webSecurityFunctions = {
        /**
         * 防xss攻击
         * @param content 过滤内容
         * @param options 选项：whiteList, onTag, onTagAttr, onIgnoreTag,
         *                     onIgnoreTagAttr, safeAttrValue, escapeHtml
         *                     stripIgnoreTagBody, allowCommentTag, stripBlankChar
         *                     css{whiteList, onAttr, onIgnoreAttr} css=false表示禁用cssfilter
         * @returns {*}
         */
        filterXSS : function(content,options){
            return filterXSS(content,options);
        },
        /**
         * 防sql注入攻击
         * @param content 过滤内容
         * @returns {boolean} true存在sql注入 false 不存在
         */
        filterSQL : function(content){
           var re =/select|call|update|delete|truncate|join|union|exec|insert|drop|count|'|"|;|>|<|%/i;
           if (!StringUtils.isEmpty_(content)&&re.test(content.toLowerCase())){
               console.info(content.toLowerCase());
               return true;
           }
           return false;
        }

    }
    return webSecurityFunctions;
})