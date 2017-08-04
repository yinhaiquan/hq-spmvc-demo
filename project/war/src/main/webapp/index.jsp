<html>
<head>
    <link rel="icon" href="/favicon.ico" type="image/x-icon"/>
    <script typet="text/javascript" src="./resource/jquery/jquery-latest.js"></script>
    <script>
        function test() {
            var url = "../login/login.do"
            $.ajax({
                type: 'post',
                url: url,
                cache: false,
                dataType: "json",
                data: {
                    name: "123",
                    password: "123456"
                },
                timeout: 10000, //超时时间，毫秒
                complete: function (data) {
                    alert(data);
                }
            });
        }

        $(function () {
            $('#kaptchaImage').click(function () {//生成验证码
                $(this).hide().attr('src', './login/codec.do?sid=' + Math.floor(Math.random() * 100)).fadeIn();
                event.cancelBubble = true;
            });
        });


        window.onbeforeunload = function () {
            //关闭窗口时自动退出
            if (event.clientX > 360 && event.clientY < 0 || event.altKey) {
                alert(parent.document.location);
            }
        };


        function changeCode() {
            $('#kaptchaImage').hide().attr('src', './login/codec.do?' + Math.floor(Math.random() * 100)).fadeIn();
            event.cancelBubble = true;
        }
    </script>
</head>
<body>
<h2>Login</h2>
<button value="login" onclick="test()">login</button>
<img src="./login/codec.do?sid=123" id="kaptchaImage" style="margin-bottom: -3px"/>
<a href="#" onclick="changeCode()">看不清?换一张</a>
</body>
</html>
