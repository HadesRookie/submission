/**
 * 注册
 */
var picCode;
$(function(){
    picCode=drawPic();
    layui.use(['form' ,'layer'], function() {
        var form = layui.form;
        var layer = layui.layer;
        //监控提交
        form.on("submit(sendMsg)",function (data) {
            //sendMsg();
                send(this,true);
            var flag=checkParams();
            if(flag!=false){
                send(this,true);
            }
            return false;

        });
        form.on("submit(register)",function () {
            register();
            return false;
        });

    })
})
//定时发送验证码
var wait = 60;
var startJob;
//o 对象
function send(o, flag) {
    if (!flag) {
        return false;
    }

    //第一次秒数
    if (wait == 60) {
        o.setAttribute("disabled", true);
        //自定义验证规则
        $.post("/user/sendMsg", {"mobile":$("#mobile").val()}, function (data) {
            console.log("data:" + data)
            if (data.code == "1000") {
                layer.msg("发送短信成功");
            } else {
                $("#password").val("");
                picCode = drawPic();
                $("#code").val("");
                //禁用发送短信验证码按钮
                o.removeAttribute("disabled");
                //o.value = "获取验证码";
                wait = 60;
                flag = false;
                layer.alert(data.message);
            }
            return false;
        });
    }
    if (wait == 0) {
        o.removeAttribute("disabled");
        $("#msgBtn").html("获取验证码");
        wait = 60;
    } else {
        o.setAttribute("disabled", true);
        if (wait <60) {
            $("#msgBtn").html("<span style='margin-left: -12px;'>"+wait + "s后可重新发送</span>");
        }
        wait--;
        startJob=setTimeout(function () {
            if (wait == 0) {
                flag = true
            }
            ;
            send(o, flag)
        }, 1000)
    }
}
function closeSend(){
    $("#msgBtn").removeAttr("disabled");
    $("#msgBtn").html("获取验证码");
    clearTimeout(startJob);
    wait = 60;
}


function register(){

    var flag=checkParams();
    var pwd=$("#password").val();
    var isPwd=$("#repass").val();
    if(pwd!=isPwd){
        //tips层-右
        $("#password").val("");
        $("#repass").val("");
        layer.tips("两次输入的密码不一致", '#repass', {
            tips: [2, '#78BA32'], //还可配置颜色
            tipsMore: true
        });
        return false;
    }
    if(flag!=false){
        //校验短信验证码
        var smsCode=$("#smsCode").val();
        if("ok"!=ValidateUtils.checkCode(smsCode)){
            //tips层-右
            layer.tips(ValidateUtils.checkCode(smsCode), '#smsCode', {
                tips: [3, '#78BA32'], //还可配置颜色
                tipsMore: true
            });
            return false;
        }
        $.post("/user/register",$("#useRegister").serialize(),function(data){
            console.log("data:"+data)
            if(data.code=="1000"){
                layer.alert("注册成功",function () {
                    window.location.href="/toLogin";
                });
            }else{
                //$("#password").val("");
                picCode=drawPic();
                $("#code").val("");
                $("#smsCode").val("");
                layer.alert(data.message,function(){
                    layer.closeAll();//关闭所有弹框
                    //关闭发送验证码按钮倒计时
                    closeSend();
                });
            }
        });
    }
}

function checkParams(){
    //  校验
    var username=$("#username").val();
    var password=$("#password").val();
    var mobile=$("#mobile").val();
    var code=$("#code").val();
    if("ok"!=ValidateUtils.checkUserName(username) || "ok"!=ValidateUtils.checkSimplePassword(password)){
        layer.alert("请您输入正确的用户名和密码");
        return false;
    }
    if("ok"!=ValidateUtils.checkMobile(mobile)){
        //tips层-右
        layer.tips(ValidateUtils.checkMobile(mobile), '#mobile', {
            tips: [2, '#78BA32'], //还可配置颜色
            tipsMore: true
        });
        return false;
    }
    if("ok"!=ValidateUtils.checkPicCode(code)){
        //tips层-右
        layer.tips(ValidateUtils.checkPicCode(code), '#canvas', {
            tips: [2, '#78BA32'], //还可配置颜色
            tipsMore: true
        });
        return false;
    }
    if(picCode.toLowerCase()!=code.toLowerCase()){
        //tips层-右
        layer.tips("请您输入正确的验证码", '#canvas', {
            tips: [2, '#78BA32'], //还可配置颜色
            tipsMore: true
        });
        return false;
    }
}