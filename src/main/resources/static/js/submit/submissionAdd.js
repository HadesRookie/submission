layui.use(['form','layer','layedit','laydate','upload'],function(){
    var form = layui.form
    layer = parent.layer === undefined ? layui.layer : top.layer,
        laypage = layui.laypage,
        upload = layui.upload,
        layedit = layui.layedit,
        laydate = layui.laydate,
        $ = layui.jquery;



    layedit.set({
        uploadImage : {
            url : "/submit/upload",
            type: 'post'
        }
    });
    //创建一个编辑器
    var editIndex = layedit.build('news_content');
    //格式化时间

    form.on("submit(addNews)",function(data){
        // 实际使用时的提交信息
        $.post("/submit/newsAdd",{
            "topic" : $(".newsName").val(),  //文章标题
            "content" : layedit.getContent(editIndex) //文章内容
        },function(res){
            if(res.code=="1000"){
                layer.alert("文章添加成功！",function () {
                    $(".newsName").val("");
                    $("#news_content").val("");
                    layer.closeAll();
                });
            }else{
                layer.alert(res.message,function(){
                    layer.closeAll();//关闭所有弹框
                });
            }
        });
    });

    //预览
    form.on("submit(look)",function(){
        layer.alert("此功能需要前台展示，实际开发中传入对应的必要参数进行文章内容页面访问");
        return false;
    })


})