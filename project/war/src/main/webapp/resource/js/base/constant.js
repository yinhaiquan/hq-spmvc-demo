/**
 * 常量管理,可分模块管理,类似Java中的枚举
 * Created by yinhaiquan on 2017/7/24.
 */
define({
    /*特殊字符数据*/
    Characters: {
        C_SLASH: '/',
        C_BACK_SLASH: '\\',
        C_HTTP: 'http://',
        C_HTTPS: 'https://'
    },
    Ajax:{
        TIME_OUT :30000
    },
    /*菜单测试数据*/
    Menus: [
        {
            'theme': '资讯管理',
            'icon': '&#xe616;',
            'childrens': [{
                'name': '资讯管理',
                'url': 'text!testfk'
            }]},
        {
            'theme': '图片管理',
            'icon': '&#xe613;',
            'childrens': [{
                'name': '图片管理',
                'url': 'text!uploader'
            }]},
        {
            'theme': '产品管理',
            'icon': '&#xe620;',
            'childrens': [
                {
                    'name': '品牌管理',
                    'url': 'text!testfk'
                },
                {
                    'name': '分类管理',
                    'url': 'text!testfk'
                },
                {
                    'name': '产品管理',
                    'url': 'text!testfk'
                }
                ]
        },
        {
            'theme': '评论管理',
            'icon': '&#xe622;',
            'childrens': [
                {
                    'name': '评论列表',
                    'url': 'text!testfk'},
                {
                    'name': '意见反馈',
                    'url': 'text!testfk'},
                {
                    'name': '意见反馈123',
                    'url': 'http://www.baidu.com'}
                ]
        }
    ]
})