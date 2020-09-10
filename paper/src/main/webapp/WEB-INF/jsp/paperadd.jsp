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

<div style="border: deepskyblue 1px solid;margin-left: 200px;height: 500px">
    <form enctype="multipart/form-data" method="post" action="paper/addPaperSave" id="addForm">
        <p>
            论文题目:<input type="text" name="title" id="title"><span id="msg"></span>
        </p>
        <p>
            论文类型:
            <select name="type">
                <option value="0">请选择</option>
                <option value="1">应用性</option>
                <option value="2">理论性</option>
            </select>
        </p>
        <p>
            论文摘要：<textarea name="paperSummary"></textarea>
        </p>
        <p>
            论文内容：<input type="file" name="paperFile">
        </p>
        <p>
            <input type="button" value="保存" id="save">
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
            $("#addForm").submit();
        }
    })

    $("#title").bind("blur",function () {
        $.ajax({
            type:"get",
            url:"paper/check",
            data:{title:$("#title").val()},
            dataType:"json",
            success:function (data) {
                if(data.result == "true"){
                    $("#msg").html("可以使用");
                }else{
                    $("#msg").html("该标题已经存在");
                }
            }
        })
    })

</script>
</body>
</html>
