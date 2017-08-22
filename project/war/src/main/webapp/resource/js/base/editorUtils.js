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
         * @param config [可选]自定义菜单 默认菜单：
         *                                      [
         *                                       'head',  // 标题
         *                                       'bold',  // 粗体
         *                                       'italic',  // 斜体
         *                                       'underline',  // 下划线
         *                                       'strikeThrough',  // 删除线
         *                                       'foreColor',  // 文字颜色
         *                                       'backColor',  // 背景颜色
         *                                       'link',  // 插入链接
         *                                       'list',  // 列表
         *                                       'justify',  // 对齐方式
         *                                       'quote',  // 引用
         *                                       'emoticon',  // 表情
         *                                       'image',  // 插入图片
         *                                       'table',  // 表格
         *                                       'video',  // 插入视频
         *                                       'code',  // 插入代码
         *                                       'undo',  // 撤销
         *                                       'redo'  // 重复
         *                                       ]
         */
        create:function(item1,item2,config){
            if(StringUtils.isEmpty_(item1)){
                return null;
            }
            var editor;
            if(StringUtils.isEmpty_(item2)){
                editor = new Editor(item1);
            } else {
                editor = new Editor(item1,item2);
            }
            if(!StringUtils.isEmpty_(config)){
                // 自定义菜单配置
                editor.customConfig.menus = config;
            }
            // 使用 base64 保存图片
            editor.customConfig.uploadImgShowBase64 = true;
            // 隐藏“网络图片”tab
            editor.customConfig.showLinkImg = false;
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
        },
        /**
         * 设置编辑器内容
         */
        setHtml:function(editor,content){
            editor.txt.html(content);
        },
        /**
         * 追加内容
         */
        appendHtml:function(editor,content){
            editor.txt.append(content);
        },
        /**
         * 清空内容
         */
        clearHtml:function(editor){
            editor.txt.clear();
        },
        /**
         * 获取编辑器内容
         * @param editor 编辑器对象
         * @param tag true 获取编辑器html内容 false 获取编辑器文本内容
         */
        getHtml:function(editor,tag){
            if(tag){
               return editor.txt.html();
            }else{
               return editor.txt.text();
            }
        }
    }
    return editorFunction;
})