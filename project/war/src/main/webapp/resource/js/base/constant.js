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
    /*私钥 测试使用私钥*/
    RsaKey:{
        PRIVATE_KEY_PKCS8_PEM :"-----BEGIN PRIVATE KEY-----MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJaEIGSDCSkqT5UhU/wgwczoAGkJmbN9gn4X1r//7ub7lcqs956Kw6huBH8+NK9qHfXphjbrNlO5IOPM68MC5/5pzYA8VDJP5NTneBh22/5qKDWAuj4KBsBIJlDlffx3Lj9mO2HbQ8NnKDijzZFw9WM6gwVA6aTyFCrmSAND3mf5AgMBAAECgYAZXdkrt13C30ucQYqq8kZXJz5ydVi/BEcKwy/BGfwEV6AuESqGQLKq3yfI3g35BjRYbmvdM5TrVUbyvWV6bzHzz2zL0uefHuiiu0Me5ZjyLMBTS7ErjUt3Ky5EP1Mc0c8IiLkrXvKjwon2+FlDKJl18DmsTn4A7V/k6IMeqxNZtQJBAMRtR4OAQwCpnvRgIFE6VEqErb6T2kIpelx9nFR64R3cFBo3rv+0b0n42mQcI4hBzQsNxEhTxqrKSxAQaQ6UnJMCQQDEKk3SILj9yi4v9lWhSg6tJskkk2PLChC2VvhZHzHy/nFMw1TYaE3CVYHXwglAIy0C5VpexQaNi4H081DqQszDAkEAj7D5baM4YJW06EQhoAoxe0nP5+g0881v65Uf9VTmtXc3ZW5yoDAYcV6QNEe5XoX0Py/U9KwEWxAdFSVMFRR17QJAJ1uJg5hXJWxUOgFFivfN3AXFI5aC1jDty3fFmjP9FJDicJFcS5MZztzTEVP4AStNk6Aqsor7Vpjf+SJ8YJQIewJAQaI5skTYM1EuMdZvGw2VLyjhhEUUeupXhzE7J44OCl/mliZ3xdP1Ye/5xBvGyFFchdqhaFU2Qh2tbwOorH9BoQ==-----END PRIVATE KEY-----"
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
        },
        {
            'theme': '任务容器',
            'icon': '&#xe690;',
            'childrens': [
                {
                    'name': '任务列表',
                    'url': 'text!quartz'}
            ]
        }
    ]
})