<%--
  Created by IntelliJ IDEA.
  User: HiSoft-36
  Date: 2020/9/8
  Time: 18:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <style type="text/css">
        td{
            border: 1px deepskyblue solid;
        }
    </style>
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
    <form method="post" action="paper/modifyPaperSave?id=${paper.id}" id="modifyForm">
        <p>
            论文主题：<input type="text" name="title" value="${paper.title}">
        </p>
        <p>
            论文摘要:<textarea name="paperSummary">${paper.paperSummary}</textarea>
        </p>
        <p>
            作者：<input type="text" name="author" value="${paper.author}">
        </p>
        <p>
            论文类型:
            <select name="type">

                <option value="0" >请选择</option>
                <option value="1" <c:if test="${paper.type==1}">selected</c:if>>应用性</option>
                <option value="2" <c:if test="${paper.type==2}">selected</c:if>>理论性</option>
            </select>
        </p>

        <p>
            <input type="button" value="提交" id="save">
            <input type="button" value="返回" id="back">
        </p>
    </form>
</div>

<script src="statics/js/jquery-1.8.3.min.js" type="text/javascript"></script>
<script>
    $("#back").click(function () {
            history.back(-1);
    })

    $("#save").click(function () {
        if(confirm("是否确认提交?")){
            $("#modifyForm").submit();
        }
    })
</script>
</body>
</html>
