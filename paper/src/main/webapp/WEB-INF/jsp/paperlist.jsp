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
    <form method="post" action="paper/query">
        论文主题:<input type="text" name="title">
        论文类型:
        <select name="type">
            <option value="0">请选择</option>
            <option value="1">应用性</option>
            <option value="2">理论性</option>
        </select>
        <input type="submit" value="查询">
        <a href="paper/addPaper">添加论文</a>
    </form>

    <table style="text-align: center">
        <tr>
            <th width="20%">论文主题</th>
            <th width="10%">作者</th>
            <th width="10%">论文类型</th>
            <th width="20%">发表时间</th>
            <th width="20%">修改时间</th>
            <th width="20%">操作</th>
        </tr>
        <c:forEach var="paper" items="${paperList }" varStatus="status">
            <tr>
                <td>
                    <span>${paper.title }</span>
                </td>
                <td>
                    <span>${paper.author }</span>
                </td>
                <td>
                    <span>${paper.typeName}</span>
                </td>
                <td>
                    <span><fmt:formatDate value="${paper.creationDate}" pattern="yyyy-MM-dd"/></span>
                </td>
                <td>
                    <span><fmt:formatDate value="${paper.modifyDate}" pattern="yyyy-MM-dd"/></span>
                </td>
                <td>
                    <a href="paper/modifyPaper?id=${paper.id}">修改</a>
                    <a href="javascript:;" class="del" name="${paper.id}">删除</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>


<script src="statics/js/jquery-1.8.3.min.js" type="text/javascript"></script>
<script>

    $(".del").click(function () {
        obj = $(this);
        if(confirm("你确定要删除这篇论文吗?")){
            $.ajax({
                type:"get",
                url:"paper/delete",
                data:{id:obj.attr("name")},
                dataType:"json",
                success:function (data) {
                    if(data.delResult == "true"){
                        alert("删除成功");
                        obj.parents("tr").remove();
                    }else if(data.delResult == "false"){
                        alert("对不起，删除失败");
                    }
                },error:function(data){
                    alert("对不起，删除失败");
                }
            })
        }
    })
</script>
</body>
</html>
