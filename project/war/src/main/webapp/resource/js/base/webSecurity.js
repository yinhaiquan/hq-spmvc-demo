/**
 * web 安全控制插件
 * @description: 包括内容如下：
 *                    1. 防xss攻击
 *                    2. 防sql注入攻击
 *                    3. 基于MD5数据加密
 *                    4. 基于PKCS8格式RSA私钥签名
 * Created by yinhaiquan on 2017/8/14.
 */
define('webSecurity',['xss','stringUtils','md5','jsrsasign','constant'],
    function(XSS,StringUtils,MD5,Jsrsasign,Constant){
    var webSecurityFunctions = {
        /**
         * 防xss攻击
         * @param content 过滤内容
         * @param options 选项：whiteList, onTag, onTagAttr, onIgnoreTag,
         *                     onIgnoreTagAttr, safeAttrValue, escapeHtml
         *                     stripIgnoreTagBody, allowCommentTag, stripBlankChar
         *                     css{whiteList, onAttr, onIgnoreAttr} css=false表示禁用cssfilter可为空
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
        },
        /**
         * 基于MD5数据加密
         * @param data 待加密数据
         * @returns {*}
         */
        md5 : function(data){
            return MD5(data);
        },
        /**
         * 基于PKCS8格式RSA私钥签名
         * @description: 通过pkcs8格式私钥签名 注意：私钥中不存在空格
         * @param data
         */
        sign : function(data){
            var rsaPrivateKey = new RSAKey();
            /*由于java后台生成的key格式是pkcs8格式 而前端js插件是pkcs1格式解析，故使用KEYUTIL.getKey(pkcs8key)获取私钥*/
            rsaPrivateKey =KEYUTIL.getKey(Constant.RsaKey.PRIVATE_KEY_PKCS8_PEM);
            return rsaPrivateKey.sign(data,"sha1");
        }
    }
    return webSecurityFunctions;
})