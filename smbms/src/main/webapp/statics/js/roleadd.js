var roleCode = null;
var roleName = null;
var addBtn = null;
var backBtn = null;

$(function () {
    roleCode = $("#roleCode");
    roleName = $("#roleName");
    addBtn = $("#add");
    backBtn = $("#back");

    roleCode.next().html("*");
    roleName.next().html("*");


    roleCode.bind("blur",function() {
        $.ajax({
            type: "GET",//请求类型
            url: path + "/role/checkRoleCode",//请求的url
            data: {roleCode: roleCode.val()},//请求参数
            dataType: "json",//ajax接口（请求url）返回的数据类型
            success: function (data) {//data：返回数据（json对象）
                if (data.roleCode == "exist") {
                    validateTip(roleCode.next(), {"color": "red"}, imgNo + " 该用户角色已存在", false);
                } else {
                    validateTip(roleCode.next(), {"color": "green"}, imgYes + " 该角色可以使用", true);
                }
            },
            error: function (data) {//当访问时候，404，500 等非200的错误状态码
                validateTip(roleCode.next(), {"color": "red"}, imgNo + " 您访问的页面不存在", false);
            }
        });
    });


    addBtn.bind("click",function(){
        {
            if(confirm("是否确认提交数据")){
                $("#userForm").submit();
            }
        }
    });

    backBtn.on("click",function(){
        if(referer != undefined
            && null != referer
            && "" != referer
            && "null" != referer
            && referer.length > 4){
            window.location.href = referer;
        }else{
            history.back(-1);
        }
    });
})

