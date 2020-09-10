<%--
  Created by IntelliJ IDEA.
  User: HiSoft-36
  Date: 2020/9/8
  Time: 17:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
</head>
<body>
<div style="width: 500px;margin-left: 550px">
    <h1>论文管理系统</h1>
    <form action="user/login" method="post">
        <p>
            用户名：<input type="text" name="userName">
        </p>
        <p>
            密码：  <input type="password" name="password">
        </p>
        <p>
            <input type="submit" value="登录">
        </p>
    </form>
</div>
</body>
</html>
