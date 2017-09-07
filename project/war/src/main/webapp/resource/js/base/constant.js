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
    /*ajax请求超时时间(单位/ms)*/
    Ajax:{
        TIME_OUT :30000
    },
    /*私钥 测试使用私钥*/
    RsaKey:{
        PRIVATE_KEY_PKCS8_PEM :"-----BEGIN PRIVATE KEY-----MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALdDPQ0DUqTC8Bbse7RwpWA5N2IB0UgRWRYEu/J9mawKiqTL+6tNLiO/eXRqu5m0nTLWlIjx+7xH81woT7YBrsq7pEcp9swUtrqs2pmPvzfwl0du1qwa9d6n5KgVgkYMj4TbCYYSKnm94Dg53rhqJ1uNVu20DwGwF2gDbZxL27jNAgMBAAECgYBdcGwFYYdWuIn/ti3Qr4qyiBnD39dcHnREtL87gWzD+k/8fIDiM6Tt1yfPAsiKMzvfeuca9/55Xlonx+n0i+EZYW7jr7ykDE4srqRgzbGPpmcSKNDOROpYwAhm4O3oqSh/87KxVR6gY72w7dwlp6A25EUBN2yIugdmziCLh9VZpQJBAOuwzkBLzkzh5PdwWmW2DN1bB3ViS2oq/URqEFEmEF/KCUcfdSV5pFo0CMRgESwqhy952efouTm9Dq4ROzzbR8cCQQDHDecTkoXj8kbpknzeAMLh99amEe4kW4EHx8u1J37yRdp7zKtz+RsOyRn1E51C5E1Pj5SMfooNryj4wcNHw8LLAkEAq2+BtrbtKnMtjuZEerM/DEvIUxPaczEt4/OaRsx/II3ezcMN/OJML+ilb+HI2+1bQ6dIXy2ifWMNKIQUKKH/xQJBAK/l6DHShACHowRaT396mrxGX2cHZzK3rZDrjGa7zSFYexZ1KXllQvTQ3uBEAe8PggXxciqo02P+Si1wRBmRsvsCQE3ghUZky2xUwC8hh2tVzkEJ5he17wbGn2VJPxZeemI2jLIxT2cJKunZHbY3Q76KWqTC2FXpG5mWfhvyUeDM+Ig=-----END PRIVATE KEY-----"
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