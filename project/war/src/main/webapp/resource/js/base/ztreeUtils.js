/**
 * jquery ztree 树形菜单工具函数插件
 * 参考地址:http://www.treejs.cn/v3/api.php
 * Created by yinhaiquan on 2017/7/26.
 */
define('ztreeUtils',[
    'jquery',
    'jqueryztreecore',
    'jqueryztreexcheck',
    'css!jqueryztreestyle',
    'jqueryztreexedit',
    'jqueryztreexhide'],function($,jqztree,jqueryztreexcheck,jqztreecss,jqueryztreexedit,jqueryztreexhide){
    var ztreeFunctions = {
        /**
         * 默认ztree配置信息【参考配置使用】
         * @returns {{callback: {onClick: click}, data: {simpleData: {enable: boolean, idKey: string, pIdKey: string, rootPId: number}}, view: {fontCss: getFont, nameIsHTML: boolean, showIcon: boolean, showLine: boolean, showTitle: boolean}}}
         */
        setting : function () {
            var getFont = function(treeId, node){
                return node.font ? node.font : {};
            }
            var click = function(event, treeId, node){
                alert(node.name);
            }
            var check = function(event, treeId, node){
                console.info(node.checked);
                console.info(treeId);
                var checkCount = ztreeFunctions.getCount(treeId,true);
                var nocheckCount = ztreeFunctions.getCount(treeId,false);
                console.info(nocheckCount)
                console.info(checkCount);

            }
            var rename = function(event, treeId, node){
                /**添加自己修改业务*/
                console.info("修改节点"+node.name);
            }
            var remove = function(event, treeId, node){
                /**添加自己删除业务*/
                console.info("删除节点"+node.name);
            }
            /**
             * 拖拽之前操作
             * @param treeId
             * @param treeNodes
             */
            var beforeDrag = function(treeId, treeNodes){
                var tag = treeNodes[0].fk;
                console.info(tag);
                if(tag == 'fuck'){
                    $.Huimodalalert('fk',0);
                    return false;
                }
                console.info("========beforeDrag===========")
                // console.info("被拖拽的节点"+treeNodes.length);
                // console.info(treeNodes);
                for (var i=0,l=treeNodes.length; i<l; i++) {
                    if (treeNodes[i].drag === false) {
                        return false;
                    }
                }
                return true;

            }
            /**
             * 拖拽之后删除节点之前操作(可用onDrop)
             * @param treeId     目标节点 targetNode 所在 zTree 的 treeId，便于用户操控
             * @param treeNodes  被拖拽的节点 JSON 数据集合,无论拖拽操作为 复制 还是 移动，treeNodes 都是当前被拖拽节点的数据集合。
             * @param targetNode treeNodes 被拖拽放开的目标节点 JSON 数据对象,如果拖拽成为根节点，则 targetNode = null
             * @param moveType   指定移动到目标节点的相对位置,"inner"：成为子节点，"prev"：成为同级前一个节点，"next"：成为同级后一个节点
             */
            var beforeDrop = function(treeId, treeNodes,targetNode,moveType,isCopy){
                console.info("========beforeDrop===========")
                // console.info(treeNodes);
                // console.info(targetNode);
                // console.info(moveType);
                return targetNode ? targetNode.drop !== false : true;
            }
            var newCount = 1;
            /**
             * 新增添加节点按钮
             * @param treeId
             * @param treeNode
             */
            var addHoverDom = function (treeId, treeNode) {
                // console.info(treeNode)
                var sObj = $("#" + treeNode.tId + "_span");
                if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
                var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
                    + "' title='add node' onfocus='this.blur();'></span>";
                sObj.after(addStr);
                var btn = $("#addBtn_"+treeNode.tId);
                if (btn) btn.bind("click", function(){
                    /**可实现一个弹窗，填写信息后，新增节点，再处理自己业务*/
                    var zTree = $.fn.zTree.getZTreeObj("treeDemo");
                    //添加节点信息
                    zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:"new node" + (newCount++)});
                    //处理自己业务
                    //添加节点信息至数据库
                    return false;
                });
            };

            /**
             * 移除节点按钮
             * @param treeId
             * @param treeNode
             */
            var removeHoverDom = function(treeId, treeNode) {
                $("#addBtn_"+treeNode.tId).unbind().remove();
            };

            var setting = {
                edit: {
                    enable : true,
                    drag:{
                        isCopy : true,
                        isMove : false
                    }
                },
                check: {
                    enable : true,
                    chkboxType : {'Y':'ps','N':'ps'},
                    chkStyle : "checkbox"
                },
                callback: {
                    onClick : click,
                    onCheck : check,
                    onRemove : remove,
                    onRename : rename,
                    beforeDrag : beforeDrag,
                    beforeDrop : beforeDrop
                },
                data: {
                    simpleData: {
                        enable: true,
                        idKey : "id",
                        pIdKey : "pId",
                        rootPId : -1
                    }
                },
                view: {
                    fontCss : getFont,
                    nameIsHTML : true,
                    showIcon : true,
                    showLine : true,
                    showTitle : true,
                    addHoverDom : addHoverDom,
                    removeHoverDom: removeHoverDom
                }
            };
            return setting;
        },
        /**
         * 同步创建ztree结构函数
         * @param options 树形标签对象
         * @param data ztree节点数据
         * @param setting 自定义ztree配置信息
         */
        createTreeAMD : function(options,data,setting){
            var st = ztreeFunctions.setting();
            if(typeof setting != 'undefined' && null != setting){
                st = setting;
            }
            console.info("ztree配置信息");
            console.info(st);
            $.fn.zTree.init(options, st, data);
        },
        /**
         * 异步创建ztree结构函数
         * @param options 树形标签对象
         * @param url     请求地址
         * @param params  请求参数 json格式
         * @param setting 自定义ztree配置信息
         */
        createTreeCMD :　function(options,url,params,setting){
            var defauting = {
                async: {
                    enable: true,
                    url:url,
                    autoParam:["id", "name=n", "level=lv"],
                    otherParam:params,
                    dataFilter: filter
                }
            };
            function filter(treeId, parentNode, childNodes) {
                if (!childNodes) return null;
                for (var i=0, l=childNodes.length; i<l; i++) {
                    childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
                }
                return childNodes;
            }
            $.fn.zTree.init(options, (typeof setting != 'undefined'&&null!=setting)?setting:defauting);
        },
        /**
         * 获取ztree复选框已选/未选总数
         * @param id treeId
         * @param isChecked true 已选 false 未选
         */
        getCount : function(treeId,isChecked){
            return $.fn.zTree.getZTreeObj(treeId).getCheckedNodes(isChecked).length;
        },
        /**
         * 获取ztree复选框已选/未选节点列表
         * @param id treeId
         * @param isChecked true 已选 false 未选
         */
        getCheckeds : function(treeId,isChecked){
            return $.fn.zTree.getZTreeObj(treeId).getCheckedNodes(isChecked);
        },
        /**
         * 获取所有节点列表
         *
         * 注意：若是异步创建ztree结构树形，是不能够通过此函数获取所有节点包括子节点数据列表!!!
         * @param treeId
         * @returns {*}
         */
        getAllNodes : function(treeId){
            // 获取 id 为 tree 的 zTree 对象
            var treeObj = $.fn.zTree.getZTreeObj(treeId);
            return treeObj.transformToArray(treeObj.getNodes());
        }

    }
    return ztreeFunctions;
})