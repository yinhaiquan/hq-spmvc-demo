/*
配置例子：
requirejs.config({
    paths: {
        tmpl:'../../resource/lib/requirejs/jquery.tmpl.min',
        jquery: '../../resource/lib/jquery/1.9.1/jquery',
        tpl:'test.tpl',
        htm:"test.html",
        text:'../../resource/lib/requirejs/text'
    },
    shim:{
        //使用tmpl模板时需要依赖被引用tpl，隐藏需要添加依赖关系，待tpl加载完时方可使用tmpl模板
        'tmpl':{
            deps:['jquery','text!tpl'],
            exports:'tmpl'
        }
    }
});

*/
requirejs.config({
    baseUrl: 'http://localhost:63342/project/war/src/main/webapp/',
    // baseUrl: 'http://localhost:8080/',
    paths: {
        /*js配置路径*/
        tmpl:'resource/lib/requirejs/jquery.tmpl.min',
        jquery: 'resource/lib/jquery/1.9.1/jquery.min',
        jqueryi18n:'resource/lib/jquery.i18n/jquery.i18n.properties-1.0.9',
        jqueryztreecore:'resource/lib/zTree/v3/js/jquery.ztree.core-3.5',
        jqueryztreexcheck:'resource/lib/zTree/v3/js/jquery.ztree.excheck-3.5',
        jqueryztreexedit:'resource/lib/zTree/v3/js/jquery.ztree.exedit-3.5',
        jqueryztreexhide:'resource/lib/zTree/v3/js/jquery.ztree.exhide-3.5',
        layer:'resource/lib/layer/2.4/layer',
        laypager:'resource/lib/laypage/1.2/laypage',
        layui:'resource/lib/layui/layui',
        validat:'resource/lib/jquery.validation/1.14.0/jquery.validate',
        validate_methods:'resource/lib/jquery.validation/1.14.0/validate-methods',
        messages_:'resource/lib/jquery.validation/1.14.0/messages_zh',
        hui:'resource/static/h-ui/js/H-ui',
        hui_admin_page:'resource/static/h-ui.admin/js/H-ui.admin.page',
        text:'resource/lib/requirejs/text',
        css:'resource/lib/requirejs/css',
        webuploader:'resource/lib/webuploader/0.1.5/webuploader.min',
        lightbox2:'resource/lib/lightbox2/2.8.1/js/lightbox',
        prettify:'resource/lib/prettify/prettify',
        wangeditor:'resource/lib/wangeditor/3.0.7/wangEditor',
        bootstrapswitch:'resource/lib/switch/bootstrapSwitch',
        xss:'resource/js/xss',
        wdatepicker:'resource/lib/My97DatePicker/4.8/WdatePicker',
        md5:'resource/lib/md5/md5',
        jsrsasign:'resource/lib/rsa/jsrsasign-all-min',

        /*自定义插件扩展*/
        constant:'resource/js/base/constant',
        ajax:'resource/js/base/ajaxUtils',
        url:'resource/js/base/url',
        stringUtils:'resource/js/base/stringUtils',
        i18nUtils:'resource/js/base/i18nUtils',
        fileUploadUtils:'resource/js/base/fileUploadUtils',
        ztreeUtils:'resource/js/base/ztreeUtils',
        layPageUtils:'resource/js/base/layPageUtils',
        editorUtils:'resource/js/base/editorUtils',
        webSecurity:'resource/js/base/webSecurity',
        selectGroup:'resource/js/base/selectGroup',
        icheckUtils:'resource/js/base/icheckUtils',

        /*html、tpl路径配置*/
        testfk:'html/fk.html',
        header:'html/base/header.html',
        menu:'html/base/menu.tpl',
        uploader:'html/uploader.html',
        quartz:"html/quartz/quartz.html",
        add_quartz:"html/quartz/add_quartz.html",

        /*css路径配置*/
        jqueryztreestyle:'resource/lib/zTree/v3/css/metroStyle/metroStyle',
        laypagestyle:'resource/lib/laypage/1.2/skin/laypage',
        webuploaderstyle:'resource/lib/webuploader/0.1.5/webuploader',
        lightbox2style:'resource/lib/lightbox2/2.8.1/css/lightbox',
        prettifystyle:'resource/lib/prettify/prettify',
        bootstrapswitchstyle:'resource/lib/switch/bootstrapSwitch',
        layerstyle:'resource/lib/layer/2.4/skin/layer'
    },
    shim:{
        'tmpl':{
            deps:[
                'jquery',
                'text!header',
                'text!menu',
                'ajax',
                'text!testfk',
                'text!uploader',
                'text!quartz',
                'text!add_quartz'
            ],
            exports:'tmpl'
        },
        'hui':{
            deps:['jquery','layer','validate_methods','messages_'],
            exports:'hui'
        },
        'hui_admin_page':{
            deps:['jquery','hui','layer','validate_methods','messages_'],
            exports:'hui_admin_page'
        },
        'layer':{
            deps:['jquery','css!layerstyle'],
            exports:'layer'
        },
        'ajax':{
            deps:['jquery'],
            exports:'ajax'
        },
        'validate_methods':{
            deps:['jquery','validat'],
            exports:'validate_methods'
        },
        'messages_':{
            deps:['jquery','validate_methods','validat'],
            exports:'messages_'
        },
        'validat':{
            deps:['jquery','layer'],
            exports:'validat'
        },
        'jqueryi18n':{
            deps:['jquery'],
            exports:'jqueryi18n'
        },
        'i18nUtils':{
            deps:['jquery','jqueryi18n'],
            exports:'i18nUtils'
        },
        'laypager':{
            deps:['jquery','css!laypagestyle'],
            exports:'laypager'
        },
        'jqueryztreecore':{
            deps:['jquery','css!jqueryztreestyle'],
            exports:'jqueryztreecore'
        },
        'jqueryztreexcheck':{
            deps:['jquery','jqueryztreecore','css!jqueryztreestyle'],
            exports:'jqueryztreexcheck'
        },
        'jqueryztreexedit':{
            deps:['jquery','jqueryztreecore','css!jqueryztreestyle'],
            exports:'jqueryztreexedit'
        },
        'jqueryztreexhide':{
            deps:['jquery','jqueryztreecore','css!jqueryztreestyle'],
            exports:'jqueryztreexhide'
        },
        'ztreeUtils':{
            deps:['jquery','jqueryztreecore','css!jqueryztreestyle']
        },
        'webuploader':{
            deps:['jquery','css!webuploaderstyle']
        },
        'lightbox2':{
            deps:['jquery','css!lightbox2style']
        },
        'prettify':{
            deps:['jquery','css!prettifystyle']
        },
        'wangeditor':{
            deps:['jquery']
        },
        'editorUtils':{
            deps:['jquery','wangeditor']
        },
        'bootstrapswitch':{
            deps:['jquery','css!bootstrapswitchstyle']
        },
        'selectGroup':{
            deps:['jquery','ajax']
        },
        'icheckUtils':{
            deps:['jquery','hui']
        }
    }
});