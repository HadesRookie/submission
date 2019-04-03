/**
 * 菜单
 * */
//获取路径uri
var pathUri=window.location.href;

// console.log("pathUrl:"+pathUri);
$(function(){
    layui.use('element', function(){
        var element = layui.element;
        // 左侧导航区域（可配合layui已有的垂直导航）
        $.get("/auth/getUserPerms",function(data){
            if(data!=null){
                getMenus(data);
                element.render('nav');
            }else{
                layer.alert("权限不足，请联系管理员",function () {
                    //退出
                    window.location.href="/logout";
                });
            }
        });
    });
})
var getMenus=function(data){
    //回显选中
    var ul=$("<ul class='layui-nav layui-nav-tree' lay-filter='test'></ul>");
    for(var i=0;i < data.length;i++){
        var node=data[i];
        if( node.permitType==0){
            if(node.pid==0){
                var li=$("<li class='layui-nav-item' flag='"+node.id+"'></li>");
                //父级无page
                var a=$("<a class='' href='javascript:;'>"+node.permitName+"</a>");
                li.append(a);
                //获取子节点
                var childArry = getParentArry(node.id, data);
                if(childArry.length>0){
                    a.append("<span class='layui-nav-more'></span>");
                    var dl=$("<dl class='layui-nav-child'></dl>");
                    for (var y in childArry) {
                        var dd=$("<dd><a href='"+childArry[y].url+"'>"+childArry[y].permitName+"</a></dd>");
                        //判断选中状态
                        if(pathUri.indexOf(childArry[y].url)>0){
                            li.addClass("layui-nav-itemed");
                            dd.addClass("layui-this")
                        }
                        var childHasArray = getParentArry(childArry[y].id,data);
                        if (childHasArray.length > 0){
                            var dll = $("<dl class='layui-nav-child'></dl>");
                            for (var yy in childHasArray){
                                var ddd = $("<dd><a href='"+childHasArray[yy].url+"'>"+childHasArray[yy].permitName+"</a></dd>");
                                //判断选中状态
                                if(pathUri.indexOf(childHasArray[yy].url)>0){
                                    li.addClass("layui-nav-itemed");
                                    dd.addClass("layui-this")
                                }
                                dll.append(ddd);
                            }
                            dd.append(dll);
                        }
                        //TODO 由于layui菜单不是规范统一的，多级菜单需要手动更改样式实现；
                        dl.append(dd);
                    }
                    li.append(dl);
                }
                ul.append(li);
            }
        }
    }
    $(".layui-side-scroll").append(ul);
}
//根据菜单主键id获取下级菜单
//id：菜单主键id
//arry：菜单数组信息
function getParentArry(id, arry) {
    var newArry = new Array();
    for (var x in arry) {
        if (arry[x].pid == id)
            newArry.push(arry[x]);
    }
    return newArry;
}
function updateUsePwd(){
    layer.open({
        type:1,
        title: "修改密码",
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['450px'],
        content:$('#useDetail')
    });
}

function userInfo() {
    layer.open({
        type:1,
        title: "修改个人信息",
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px'],
        content:$('#updateUser')
    });
}

function updateUserInfo(){

   //回显数据
    $.get("/user/getUserById",function(data){
        if(data.msg=="ok" && data.user!=null){

            $("#fid").val(data.user.id==null?'':data.user.id);
            $("#fusername").val(data.user.username==null?'':data.user.username);
            $("#fmobile").val(data.user.mobile==null?'':data.user.mobile);
            $("#femail").val(data.user.email==null?'':data.user.email);
            $("#fpayNum").val(data.user.payNum == null?'':data.user.payNum);
            $("#frealName").val(data.user.realName == null?'':data.user.realName);
            userInfo();
        }else{
            //弹出错误提示
            layer.alert(data.msg,function () {
                layer.closeAll();
            });
        }

    });


}