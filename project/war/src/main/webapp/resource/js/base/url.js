/**
 * 请求地址管理,可分模块管理
 * Created by yinhaiquan on 2017/7/21.
 */
define({
    baseUrl:'http://localhost:8080/',
    // baseUrl:'http://localhost:63342/project/war/src/main/webapp/',

    findUserInfo:'/handler/shiroUserSvc/findUserInfo.do',
    fileUpload:'/file/upload.do',
    fileDownload:'/file/download.do',
    findUsers:'/handler/adminSvc/findUsers.do',
    quartzURLS:{
        getSchedulerList:'/handler/quartzSvc/getSchedulerList.do',
        switchJob:'/handler/quartzSvc/switchJob.do',
        deleteJob:'/handler/quartzSvc/deleteJob.do',
        updateJobTime:'/handler/quartzSvc/updateJobTime.do',
        addJob:'/handler/quartzSvc/addJob.do',
        monitorContainerScheduler:'/handler/quartzSvc/monitorContainerScheduler.do'
    }
})