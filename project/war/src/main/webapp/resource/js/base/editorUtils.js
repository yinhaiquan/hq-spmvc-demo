/**
 * 富文本编辑器插件，基于wangeditor.js插件
 * 参考：https://www.kancloud.cn/wangfupeng/wangeditor3/335776
 * Created by yinhaiquan on 2017/8/11.
 */
define('editorUtils',['jquery','wangeditor','stringUtils'],function($,Editor,StringUtils){
    var editorFunction = {
        /**
         * 创建编辑器
         * 注意：填写两个参数时，菜单和编辑区分开展示
         * @param item1 [必填] class='toolbar' 菜单 可为id、class、item
         * @param item2 [可选]class='text' 文本编辑区 可为id、class、item
         */
        create:function(item1,item2){
            if(StringUtils.isEmpty_(item1)){
                return null;
            }
            var editor;
            if(StringUtils.isEmpty_(item2)){
                editor = new Editor(item1);
            } else {
                editor = new Editor(item1,item2);
            }
            editor.create();
            return editor;
        },
        /**
         * 编辑器开关
         * @param editor 编辑器对象
         * @param tag true 可编辑 false 禁用
         */
        switchOpenOrClose:function(editor,tag){
            editor.$textElem.attr('contenteditable', tag);
        }
    }
    return editorFunction;
})