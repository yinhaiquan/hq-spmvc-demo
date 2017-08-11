/**
 * 文件上传插件，基于webuploader.js
 * http://fex.baidu.com/webuploader/doc/
 * Created by yinhaiquan on 2017/7/27.
 */
define('fileUploadUtils',
    [
        'webuploader',
        'css!webuploaderstyle',
        'ajax',
        'stringUtils'
    ],function(WebUploader,webuploaderstyle,AjaxUtils,StringUtils){
        /**
         * 普通创建文件上传组件默认配置
         * @type {{fileList: string, btn: string}}
         */
        var default_target = {
            fileList : "#fileList",         //[必选]文件列表
            btn:"#btn-star",                //[可选]上传按钮 若auto=true则可不用此参数
            filepick:"#filePicker",         //[必选]文件选择按钮。内部根据当前运行是创建，可能是input元素，也可能是flash.
            thumbnailWidth:110,             //[可选]缩略图宽度
            thumbnailHeight:110,            //[可选]缩略图高度
            isAuto:false,                   //[必选]是否自动上传，[默认值：false] 设置为 true 后，不需要手动调用上传，有文件选择即开始上传。
            serverUrl:AjaxUtils.URLS.baseUrl+'/resource/lib/webuploader/0.1.5/server/fileupload.php',//[必选]文件接收服务端地址。
            fileTotal:1,                    //[必选]文件个数限制
            fileSizeTotal:1024*1024*50,     //[可选][默认值：undefined] 验证文件总大小是否超出限制, 超出则不允许加入队列。
            fileSingleSizeTotal:1024*1024*5,//[可选][默认值：undefined] 验证单个文件大小是否超出限制, 超出则不允许加入队列。
            duplicate_:true,                //[可选][默认值：undefined] 去重， 根据文件名字、文件大小和最后修改时间来生成hash Key。true去重，false不去重
            accept_:{
                acceptTitle:'Images',
                acceptExtensions:'gif,jpg,jpeg,bmp,png',
                acceptMimeTypes:'image/*'
            },                              //[必选]文件上传类型限制，默认图片格式。若是图片格式，则thumbnailWidth、thumbnailHeight、btn必选
            isHowThumbnail:false,           //[必选]是否显示缩略图，true 展示缩略图 false 不展示
            fileValue:'fileData'            //[可选][默认值：'file'] 设置文件上传域的name
        }
        var default_senior_target = {
            uploaderListContainer : '#uploader_list_container', //图片容器
            queueList : '#queueList',               // 图片列表
            statusBar : '#statusBar',               // 状态栏，包括进度和控制按钮
            infoMsg : '#info',                      // 文件总体选择信息
            uploadBtn : '#uploadBtn',               // 上传按钮
            placeHolder : '#dndArea',               // 没选择文件之前的内容
            progress_ : '#progress',                // 进度条
            thumbnailWidth:110,                     // 缩略图宽度
            thumbnailHeight:110,                    // 缩略图高度
            pick_ : {
                id : '#filePicker-2',
                label: '点击选择图片'
            },
            formData_ : {
                uid: 123
            },                                      // 自定义参数,文件上传请求的参数表，每次发送都会发送此对象中的参数
            dnd_ : "#dndArea",                      // [默认值：undefined] 指定Drag And Drop拖拽的容器，如果不指定，则不启动
            paste_ : $("#uploader_list_container"),    // [默认值：undefined] 指定监听paste事件的容器,即标签对象，如果不指定，不启用此功能。此功能为通过粘贴来添加截屏的图片。建议设置为document.body
            fileValue : 'file',                     // [默认值：'file'] 设置文件上传域的name
            serverUrl : AjaxUtils.URLS.baseUrl+'/resource/lib/webuploader/0.1.5/server/fileupload.php',// 文件接收服务端地址。
            accept_ : {
                title: 'Images',
                extensions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/*'
            },
            fileTotal:5,                            // 文件个数限制
            fileSizeTotal:1024*1024*50,             // 50M[默认值：undefined] 验证文件总大小是否超出限制, 超出则不允许加入队列。
            fileSingleSizeTotal:1024*1024*5,        // 5M[默认值：undefined] 验证单个文件大小是否超出限制, 超出则不允许加入队列。
            duplicate_:true,                        // [默认值：undefined] 去重， 根据文件名字、文件大小和最后修改时间来生成hash Key。true去重，false不去重
            addBtnId : '#filePicker2',              // 添加按钮
            addBtnLable : '继续添加'                 // 按钮名称
        }
        var uploaderFunction = {
            /**
             * 普通上传组件
             * @description 上传文件
             * @param settting 上传组件配置信息，参考默认配置修改
             */
            upload_File : function(settting){
                if(StringUtils.isEmpty_(settting)){
                    console.info("空配置信息,赋值默认配置信息");
                    settting = default_target;
                }
                console.info("上传配置信息:");
                console.info(settting);
                // 优化retina, 在retina下这个值是2
                ratio = window.devicePixelRatio || 1,
                thumbnailWidth = StringUtils.isEmpty_(settting.thumbnailWidth)?0:settting.thumbnailWidth * ratio,
                thumbnailHeight = StringUtils.isEmpty_(settting.thumbnailHeight)?0:settting.thumbnailHeight * ratio;
                var $list = $(settting.fileList),$btn = $(settting.btn),state = "pending",uploader;
                var uploader = WebUploader.create({
                    auto: settting.isAuto,
                    swf: AjaxUtils.URLS.baseUrl+'/resource/lib/webuploader/0.1.5/Uploader.swf',
                    server: AjaxUtils.URLS.baseUrl+AjaxUtils.URLS.fileUpload,
                    pick: settting.filepick,
                    resize: false, // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
                    fileNumLimit:settting.fileTotal,
                    fileSizeLimit:settting.fileSizeTotal,
                    fileSingleSizeLimit:settting.fileSingleSizeTotal,
                    duplicate:settting.duplicate_,
                    accept: {
                        title: settting.accept_.acceptTitle,
                        extensions: settting.accept_.acceptExtensions,
                        mimeTypes: settting.accept_.acceptMimeTypes
                    },
                    fileVal : settting.fileValue
                });
                /**
                 * 错误信息监听
                 */
                uploaderFunction.error_show(uploader,settting);

                /**
                 * 文件队列处理
                 */
                uploader.on( 'fileQueued', function( file ) {
                    var $li = $(
                            '<div id="' + file.id + '" class="item">' +
                            '<div class="pic-box"><img><i class="icon Hui-iconfont" val="'+file.id+'"></i></div>'+
                            '<div class="info">' + file.name + '</div>' +
                            '<p class="state">等待上传...</p>'+
                            '</div>'
                        ),
                        $img = $li.find('img');
                        $list.append( $li );
                        // 创建缩略图
                        // 如果为非图片文件，可以不用调用此方法。
                        // thumbnailWidth x thumbnailHeight 为 100 x 100
                        if(settting.isHowThumbnail){
                            uploader.makeThumb( file, function( error, src ) {
                                if ( error ) {
                                    $img.replaceWith('<span>不能预览</span>');
                                    return;
                                }
                                $img.attr( 'src', src );
                            }, thumbnailWidth, thumbnailHeight );
                        }
                    /**
                     * 删除文件
                     */
                    $("div .item").find('i').each(function(i,obj){
                        console.info(obj);
                        console.info($(obj).attr('val'));
                        var id = $(obj).attr('val');
                        var total = $("div .item").find('i').length;
                        console.info(total+"-"+i);
                        if(total>=1&&(total-1)==i){
                            $(obj).bind('click',function(){
                                console.info(id);
                                console.info($("#"+id));
                                $("#"+id).remove();
                                uploader.removeFile(id,true);
                            })
                        }
                    })
                });

                // 文件上传过程中创建进度条实时显示。
                uploader.on( 'uploadProgress', function( file, percentage ) {
                    var $li = $( '#'+file.id ),$percent = $li.find('.progress-box .sr-only');
                    // 避免重复创建
                    if ( !$percent.length ) {
                        $percent = $('<div class="progress-box"><span class="progress-bar radius"><span class="sr-only" style="width:0%"></span></span></div>').appendTo( $li ).find('.sr-only');
                    }
                    $li.find(".state").text("上传中");
                    $percent.css( 'width', percentage * 100 + '%' );
                });

                // 文件上传成功，给item添加成功class, 用样式标记上传成功。
                uploader.on( 'uploadSuccess', function( file,response) {
                    $( '#'+file.id ).addClass('upload-state-success').find(".state").text("已上传");
                });

                // 文件上传失败，显示上传出错。
                uploader.on( 'uploadError', function( file ) {
                    $( '#'+file.id ).addClass('upload-state-error').find(".state").text("上传出错");
                });

                // 完成上传完了，成功或者失败，先删除进度条。
                uploader.on( 'uploadComplete', function( file ) {
                    $( '#'+file.id ).find('.progress-box').fadeOut();
                });
                uploader.on('all', function (type) {
                    if (type === 'startUpload') {
                        state = 'uploading';
                    } else if (type === 'stopUpload') {
                        state = 'paused';
                    } else if (type === 'uploadFinished') {
                        state = 'done';
                    }

                    if (state === 'uploading') {
                        $btn.text('暂停上传');
                    } else {
                        $btn.text('开始上传');
                    }
                });

                $btn.on('click', function () {
                    if (state === 'uploading') {
                        uploader.stop();
                    } else {
                        uploader.upload();
                    }
                });
            },
            /**
             * 高级上传组件
             * @description 支持多图片上传，支持复制粘贴图片
             * @param settting 上传组件配置信息，参考默认配置修改
             */
            upload_Senior_File : function(settting){
                if(StringUtils.isEmpty_(settting)){
                    console.info("空配置信息,赋值默认配置信息");
                    settting = default_senior_target;
                }
                console.info("上传配置信息:");
                console.info(settting);
                //定义变量
                var $wrap = $(settting.uploaderListContainer),
                    $queue = $('<ul class="filelist"></ul>').appendTo($wrap.find(settting.queueList)),
                    $statusBar = $wrap.find(settting.statusBar),
                    $info = $statusBar.find(settting.infoMsg),
                    $upload = $wrap.find(settting.uploadBtn),
                    $placeHolder = $wrap.find(settting.placeHolder),
                    $progress = $statusBar.find(settting.progress_).hide(),
                    // 添加的文件数量
                    fileCount = 0,
                    // 添加的文件总大小
                    fileSize = 0,
                    // 优化retina, 在retina下这个值是2
                    ratio = window.devicePixelRatio || 1,
                    thumbnailWidth = settting.thumbnailWidth * ratio,
                    thumbnailHeight = settting.thumbnailHeight * ratio,
                    // 可能有pedding, ready, uploading, confirm, done.
                    state = 'pedding',
                    // 所有文件的进度信息，key为file id
                    percentages = {},
                    // 判断浏览器是否支持图片的base64
                    isSupportBase64 = ( function() {
                        var data = new Image();
                        var support = true;
                        data.onload = data.onerror = function() {
                            if( this.width != 1 || this.height != 1 ) {
                                support = false;
                            }
                        }
                        data.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
                        return support;
                    } )(),
                    supportTransition = (function(){
                        var s = document.createElement('p').style,
                            r = 'transition' in s ||
                                'WebkitTransition' in s ||
                                'MozTransition' in s ||
                                'msTransition' in s ||
                                'OTransition' in s;
                        s = null;
                        return r;
                    })(),uploader;
                //检查flash
                uploaderFunction.check_Flash($wrap);

                // 实例化
                uploader = WebUploader.create({
                    pick: {
                        id: settting.pick_.id,
                        label: settting.pick_.label
                    },
                    formData: settting.formData_,
                    dnd: settting.dnd_,
                    paste: settting.paste_,
                    swf: AjaxUtils.URLS.baseUrl+'/resource/lib/webuploader/0.1.5/Uploader.swf',
                    chunked: false,
                    fileVal: settting.fileValue,
                    chunkSize: 512 * 1024,
                    server: AjaxUtils.URLS.baseUrl+AjaxUtils.URLS.fileUpload,
                    // runtimeOrder: 'flash', //[默认值：html5,flash] 指定运行时启动顺序。默认会想尝试 html5 是否支持，如果支持则使用 html5, 否则则使用 flash.可以将此值设置成 flash，来强制使用 flash 运行时。
                    accept: {
                        title: settting.accept_.title,
                        extensions: settting.accept_.extensions,
                        mimeTypes: settting.accept_.mimeTypes
                    },
                    disableGlobalDnd: true,// 禁掉全局的拖拽功能。这样不会出现图片拖进页面的时候，把图片打开。
                    fileNumLimit: settting.fileTotal,
                    fileSizeLimit: settting.fileSizeTotal,
                    fileSingleSizeLimit: settting.fileSingleSizeTotal,
                    duplicate: settting.duplicate_
                });

                uploader.on( 'uploadSuccess', function( file,response  ) {
                    console.info(response);
                });
                // 拖拽时不接受 js, txt 文件。
                uploader.on( 'dndAccept', function( items ) {
                    var denied = false,
                        len = items.length,
                        i = 0,
                        // 修改js类型
                        unAllowed = 'text/plain;application/javascript';
                    for ( ; i < len; i++ ) {
                        // 如果在列表里面
                        if ( ~unAllowed.indexOf( items[ i ].type ) ) {
                            denied = true;
                            break;
                        }
                    }
                    return !denied;
                });
                uploader.on('dialogOpen', function() {
                    alert("dialogOpen");
                    console.log('here');
                });
                // 添加“添加文件”的按钮，
                uploader.addButton({
                    id: settting.addBtnId,
                    label: settting.addBtnLable
                });
                uploader.on('ready', function() {
                    window.uploader = uploader;
                });

                // 当有文件添加进来时执行，负责view的创建
                function addFile( file ) {
                    var $li = $( '<li id="' + file.id + '">' +
                            '<p class="title">' + file.name + '</p>' +
                            '<p class="imgWrap"></p>'+
                            '<p class="progress"><span></span></p>' +
                            '</li>' ),
                        $btns = $('<div class="file-panel">' +
                            '<span class="cancel">删除</span>' +
                            '<span class="rotateRight">向右旋转</span>' +
                            '<span class="rotateLeft">向左旋转</span></div>').appendTo( $li ),
                        $prgress = $li.find('p.progress span'),
                        $wrap = $li.find('p.imgWrap'),
                        $info = $('<p class="error"></p>'),
                        showError = function(code) {
                            switch(code) {
                                case 'exceed_size':text = '文件大小超出';break;
                                case 'interrupt':text = '上传暂停';break;
                                default:text = '上传失败，请重试';break;
                            }
                            $info.text(text).appendTo($li);
                        };
                    if ( file.getStatus() === 'invalid' ) {
                        showError( file.statusText );
                    } else {
                        // @todo lazyload
                        $wrap.text( '预览中' );
                        uploader.makeThumb( file, function( error, src ) {
                            var img;
                            if ( error ) {
                                $wrap.text( '不能预览' );
                                return;
                            }
                            if( isSupportBase64 ) {
                                img = $('<img src="'+src+'">');
                                $wrap.empty().append( img );
                            } else {
                                $.ajax(AjaxUtils.URLS.baseUrl+'/resource/lib/webuploader/0.1.5/server/preview.php', {
                                    method: 'POST',
                                    data: src,
                                    dataType:'json'
                                }).done(function( response ) {
                                    if (response.result) {
                                        img = $('<img src="'+response.result+'">');
                                        $wrap.empty().append( img );
                                    } else {
                                        $wrap.text("预览出错");
                                    }
                                });
                            }
                        }, settting.thumbnailWidth, settting.thumbnailHeight);
                        percentages[ file.id ] = [ file.size, 0 ];
                        file.rotation = 0;
                    }
                    file.on('statuschange', function( cur, prev ) {
                        if ( prev === 'progress' ) {
                            $prgress.hide().width(0);
                        } else if ( prev === 'queued' ) {
                            // $li.off( 'mouseenter mouseleave' );
                            // $btns.remove();
                        }
                        // 成功
                        if ( cur === 'error' || cur === 'invalid' ) {
                            console.log( file.statusText );
                            showError( file.statusText );
                            percentages[ file.id ][ 1 ] = 1;
                        } else if ( cur === 'interrupt' ) {
                            showError( 'interrupt' );
                        } else if ( cur === 'queued' ) {
                            percentages[ file.id ][ 1 ] = 0;
                        } else if (cur === 'progress') {
                            $info.remove();
                            $prgress.css('display', 'block');
                        } else if (cur === 'complete') {
                            $li.append('<span class="success"></span>');
                        }
                        $li.removeClass( 'state-' + prev ).addClass( 'state-' + cur );
                    });
                    $li.on( 'mouseenter', function() {
                        $btns.stop().animate({height: 30});
                    });
                    $li.on( 'mouseleave', function() {
                        $btns.stop().animate({height: 0});
                    });
                    $btns.on( 'click', 'span', function() {
                        var index = $(this).index(),
                            deg;
                        switch ( index ) {
                            case 0:
                                uploader.removeFile( file );
                                return;
                            case 1:
                                file.rotation += 90;
                                break;
                            case 2:
                                file.rotation -= 90;
                                break;
                        }
                        if ( supportTransition ) {
                            deg = 'rotate(' + file.rotation + 'deg)';
                            $wrap.css({
                                '-webkit-transform': deg,
                                '-mos-transform': deg,
                                '-o-transform': deg,
                                'transform': deg
                            });
                        } else {
                            $wrap.css( 'filter', 'progid:DXImageTransform.Microsoft.BasicImage(rotation='+ (~~((file.rotation/90)%4 + 4)%4) +')');
                        }
                    });
                    console.info($wrap)
                    $li.appendTo($queue);
                }

                // 负责view的销毁
                function removeFile( file ) {
                    var $li = $('#'+file.id);
                    delete percentages[ file.id ];
                    updateTotalProgress();
                    $li.off().find('.file-panel').off().end().remove();
                }
                function updateTotalProgress() {
                    var loaded = 0,
                        total = 0,
                        spans = $progress.children(),
                        percent;
                    $.each( percentages, function( k, v ) {
                        total += v[ 0 ];
                        loaded += v[ 0 ] * v[ 1 ];
                    } );
                    percent = total ? loaded / total : 0;
                    spans.eq( 0 ).text( Math.round( percent * 100 ) + '%' );
                    spans.eq( 1 ).css( 'width', Math.round( percent * 100 ) + '%' );
                    updateStatus();
                }
                function updateStatus() {
                    var text = '', stats;

                    if ( state === 'ready' ) {
                        text = '选中' + fileCount + '张图片，共' +
                            WebUploader.formatSize( fileSize ) + '。';
                    } else if ( state === 'confirm' ) {
                        stats = uploader.getStats();
                        if ( stats.uploadFailNum ) {
                            text = '已成功上传' + stats.successNum+ '张照片至XX相册'+
                                stats.uploadFailNum + '张照片上传失败' +
                                '<a class="retry" href="javascript:void(0);">重新上传</a>' +
                                '失败图片或<a class="ignore" href="javascript:void(0);">忽略</a>';
                        }
                        $.Huimodalalert(text,0);
                        text = '';
                    } else {
                        stats = uploader.getStats();
                        text = '共' + fileCount + '张（' +
                            WebUploader.formatSize( fileSize )  +
                            '），已上传' + stats.successNum + '张';

                        if ( stats.uploadFailNum ) {
                            text += '，失败' + stats.uploadFailNum + '张';
                        }
                    }
                    $info.html( text );
                }
                function setState( val ) {
                    var file, stats;
                    if ( val === state ) {
                        return;
                    }
                    $upload.removeClass( 'state-' + state );
                    $upload.addClass( 'state-' + val );
                    state = val;
                    switch ( state ) {
                        case 'pedding':
                            $placeHolder.removeClass( 'element-invisible' );
                            $queue.hide();
                            $statusBar.addClass( 'element-invisible' );
                            uploader.refresh();
                            break;

                        case 'ready':
                            $placeHolder.addClass( 'element-invisible' );
                            $(settting.addBtnId).removeClass( 'element-invisible');
                            $queue.show();
                            $statusBar.removeClass('element-invisible');
                            uploader.refresh();
                            break;

                        case 'uploading':
                            $(settting.addBtnId).addClass( 'element-invisible' );
                            $progress.show();
                            $upload.text( '暂停上传' );
                            break;

                        case 'paused':
                            $progress.show();
                            $upload.text( '继续上传' );
                            break;

                        case 'confirm':
                            $progress.hide();
                            $(settting.addBtnId).removeClass( 'element-invisible' );
                            $upload.text( '开始上传' );

                            stats = uploader.getStats();
                            if ( stats.successNum && !stats.uploadFailNum ) {
                                setState( 'finish' );
                                return;
                            }
                            break;
                        case 'finish':
                            stats = uploader.getStats();
                            if ( stats.successNum ) {
                                alert( '上传成功' );
                            } else {
                                // 没有成功的图片，重设
                                state = 'done';
                                location.reload();
                            }
                            break;
                    }
                    updateStatus();
                }
                uploader.onUploadProgress = function( file, percentage ) {
                    var $li = $('#'+file.id),
                        $percent = $li.find('.progress span');

                    $percent.css( 'width', percentage * 100 + '%' );
                    percentages[ file.id ][ 1 ] = percentage;
                    updateTotalProgress();
                };
                uploader.onFileQueued = function( file ) {
                    fileCount++;
                    fileSize += file.size;
                    if ( fileCount === 1 ) {
                        $placeHolder.addClass( 'element-invisible' );
                        $statusBar.show();
                    }
                    addFile( file );
                    setState( 'ready' );
                    updateTotalProgress();
                };
                uploader.onFileDequeued = function( file ) {
                    fileCount--;
                    fileSize -= file.size;
                    if ( !fileCount ) {
                        setState( 'pedding' );
                    }
                    removeFile( file );
                    updateTotalProgress();
                };

                uploader.on( 'all', function( type ) {
                    var stats;
                    switch( type ) {
                        case 'uploadFinished':
                            setState( 'confirm' );
                            break;
                        case 'startUpload':
                            setState( 'uploading' );
                            break;
                        case 'stopUpload':
                            setState( 'paused' );
                            break;

                    }
                });

                uploaderFunction.error_show(uploader,settting);

                $upload.on('click', function() {
                    if ( $(this).hasClass( 'disabled' ) ) {
                        return false;
                    }
                    if ( state === 'ready' ) {
                        uploader.upload();
                    } else if ( state === 'paused' ) {
                        uploader.upload();
                    } else if ( state === 'uploading' ) {
                        uploader.stop();
                    }
                });

                $('body').on( 'click', '.retry', function() {
                    uploader.retry();
                } );

                $('body').on( 'click', '.ignore', function() {
                    alert( 'todo' );
                } );

                $upload.addClass( 'state-' + state );
                updateTotalProgress();
            },
            /**
             * 检查是否安装flash，否则自动安装
             */
            check_Flash : function($wrap){
                // 检测是否已经安装flash，检测flash的版本
                var flashVersion = ( function() {
                    var version;
                    try {
                        version = navigator.plugins[ 'Shockwave Flash' ];
                        version = version.description;
                    } catch ( ex ) {
                        try {
                            version = new ActiveXObject('ShockwaveFlash.ShockwaveFlash')
                                .GetVariable('$version');
                        } catch ( ex2 ) {
                            version = '0.0';
                        }
                    }
                    version = version.match( /\d+/g );
                    return parseFloat( version[ 0 ] + '.' + version[ 1 ], 10 );
                } )();

                if ( !WebUploader.Uploader.support('flash') && WebUploader.browser.ie ) {
                    // flash 安装了但是版本过低。
                    if (flashVersion) {
                        (function(container) {
                            window['expressinstallcallback'] = function( state ) {
                                switch(state) {
                                    case 'Download.Cancelled':
                                        // alert('您取消了更新！')
                                        $.Huimodalalert('您取消了更新！',1500);
                                        break;
                                    case 'Download.Failed':
                                        // alert('安装失败')
                                        $.Huimodalalert('安装失败！',1500);
                                        break;
                                    default:
                                        // alert('安装已成功，请刷新！');
                                        $.Huimodalalert('安装已成功，请刷新！',1500);
                                        break;
                                }
                                delete window['expressinstallcallback'];
                            };
                            var swf = AjaxUtils.URLS.baseUrl+'/resource/lib/webuploader/0.1.5/expressInstall.swf';
                            // insert flash object
                            var html = '<object type="application/' +
                                'x-shockwave-flash" data="' +  swf + '" ';

                            if (WebUploader.browser.ie) {
                                html += 'classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" ';
                            }
                            html += 'width="100%" height="100%" style="outline:0">'  +
                                '<param name="movie" value="' + swf + '" />' +
                                '<param name="wmode" value="transparent" />' +
                                '<param name="allowscriptaccess" value="always" />' +
                                '</object>';
                            container.html(html);
                        })($wrap);
                        // 压根就没有安转。
                    } else {
                        $wrap.html('<a href="http://www.adobe.com/go/getflashplayer" target="_blank" border="0"><img alt="get flash player" src="http://www.adobe.com/macromedia/style_guide/images/160x41_Get_Flash_Player.jpg" /></a>');
                    }
                    return;
                } else if (!WebUploader.Uploader.support()) {
                    // alert( 'Web Uploader 不支持您的浏览器！');
                    $.Huimodalalert('Web Uploader 不支持您的浏览器！',1500);
                    return;
                }
            },
            /**
             * 错误处理
             *
             * type:F_DUPLICATE          重复上传错误类型
             *      Q_EXCEED_NUM_LIMIT   超出文件个数限制错误类型
             *      Q_EXCEED_SIZE_LIMIT  文件总大小超出错误类型
             *      Q_TYPE_DENIED        文件类型错误
             */
            error_show : function(uploader,settting){
                uploader.on('error', function(type){
                    console.info("错误信息类型:");
                    console.info(type);
                    var text;
                    switch (type){
                        case 'F_DUPLICATE':
                            text = '请不要重复上传文件!';
                            break;
                        case 'Q_EXCEED_NUM_LIMIT':
                            text = '最多上传'+settting.fileTotal+'个文件，已超出限制!';
                            break;
                        case 'Q_EXCEED_SIZE_LIMIT':
                            text = '文件总大小限制为'+settting.fileSizeTotal/1024/1024+"M,已超出限制!";
                            break;
                    }
                    $.Huimodalalert(text,1500);
                })
            }
        }
        return uploaderFunction;
})