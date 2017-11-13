<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>${title}</title>
    <script src="http://code.jquery.com/jquery-latest.js"></script>
</head>
<script>
    /*测试接口添加已登录授权的jsid是否有权限直接访问*/
    $.ajax({
        type: "GET",
        url: "http://localhost:8088/handler/error.jsp",
        success: function(data) {
            console.log(data);
        },
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Cookie", "JSID=cd5c2988-bc17-47fe-b66d-cbdb4b55f5cb");
        }
    });
</script>
<body>
<b>${theme}</b>
<p>
    note:<br/>
    <b>${errorname} ：</b>${name}<br/>
    <br/>
    <b>${desc} ：</b>${msg}
</p>
</body>
</html>