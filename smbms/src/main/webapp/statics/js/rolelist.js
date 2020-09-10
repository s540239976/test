var roleObj;


function deleteRole(obj){
    $.ajax({
        type:"GET",
        url:path+"/role/delete",
        data:{method:"deluser",rid:obj.attr("roleid")},
        dataType:"json",
        success:function(data){
            if(data.delResult == "true"){//删除成功：移除删除行
                cancleBtn();
                obj.parents("tr").remove();
            }else if(data.delResult == "false"){//删除失败
                //alert("对不起，删除用户【"+obj.attr("username")+"】失败");
                changeDLGContent("对不起，删除用户【"+obj.attr("roleName")+"】失败");
            }else if(data.delResult == "notexist"){
                //alert("对不起，用户【"+obj.attr("username")+"】不存在");
                changeDLGContent("对不起，用户【"+obj.attr("roleName")+"】不存在");
            }
        },
        error:function(data){
            //alert("对不起，删除失败");
            changeDLGContent("对不起，删除失败");
        }
    });
}

function openYesOrNoDLG(){
    $('.zhezhao').css('display', 'block');
    $('#removeUse').fadeIn();
}

function cancleBtn(){
    $('.zhezhao').css('display', 'none');
    $('#removeUse').fadeOut();
}
function changeDLGContent(contentStr){
    var p = $(".removeMain").find("p");
    p.html(contentStr);
}
$(function() {

    $(".modifyRole").on("click", function () {
        var obj = $(this);
        window.location.href = path + "/role/modifyRole?rid=" + obj.attr("roleid");
    });

    $('#no').click(function () {
        cancleBtn();
    });

    $('#yes').click(function () {
        deleteRole(roleObj);
    });

    $(".deleteRole").on("click",function(){
        roleObj = $(this);
        changeDLGContent("你确定要删除角色【"+roleObj.attr("roleName")+"】吗？");
        openYesOrNoDLG();
    });
});