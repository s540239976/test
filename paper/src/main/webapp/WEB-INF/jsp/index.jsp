<%--
  Created by IntelliJ IDEA.
  User: HiSoft-36
  Date: 2020/9/8
  Time: 18:03
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
<p>欢迎你，${user.userName} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="user/logout">注销</a> </p>
<ul style="float: left">
    <p>功能列表</p>
    <li>用户管理</li>
    <li><a href="paper/query">论文管理</a></li>
    <li>公共代码</li>
    <li>退出系统</li>
</ul>

<div style="border: deepskyblue 1px solid;margin-left: 200px;height: 500px;text-align: center">
<h1>欢迎使用论文管理系统</h1>
</div>
</body>
</html>
