/**
 * 已投稿管理
 */
var pageCurr;
var editIndex;
$(function() {

    layui.use('layedit',function () {
        layedit = layui.layedit;
        layedit.set({
            uploadImage : {
                url : "/submit/upload",
                type: 'post'
            }
        })
        //创建一个编辑器
        editIndex = layedit.build('content');
        var lookIndex = layedit.build('lcontent');
        //用于同步编辑器内容到textarea
        layedit.sync(editIndex);
        layedit.sync(lookIndex);
    });

    layui.use('table', function(){
        var table = layui.table
            ,form = layui.form;


        tableIns=table.render({
            elem: '#submissionList'
            ,url:'/submit/newsList'
            ,method: 'post' //默认：get请求
            ,cellMinWidth: 80
            ,page: true,
            request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'limit' //每页数据量的参数名，默认：limit
            },response:{
                statusName: 'code' //数据状态的字段名称，默认：code
                ,statusCode: 200 //成功的状态码，默认：0
                ,countName: 'totals' //数据总数的字段名称，默认：count
                ,dataName: 'list' //数据列表的字段名称，默认：data
            }
            ,cols: [[
                {type:'numbers'}
                ,{field:'id', title:'ID',width:80, unresize: true, sort: true}
                ,{field:'topic', title:'标题'}
                ,{field:'content',title:'简要内容',align:'center', minWidth:110}
                ,{field:'mstatus', title:'审核状态',width:95,align:'center',templet:'#submissionStatus'}
                ,{field:'insertTime', title: '发布时间'}
                ,{field:'updateTime', title: '更改时间'}
                ,{fixed:'right', title:'操作',width:150,align:'center', toolbar:'#submissionListBar'}
            ]]
            ,  done: function(res, curr, count){
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                //console.log(res);
                //得到当前页码
                //console.log(curr);
                //得到数据总量
                //console.log(count);
                pageCurr=curr;
            }
        });

        //监听工具条
        table.on('tool(submissionTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){  //删除
                delManuscript(data,data.id);
            } else if(obj.event === 'edit'){
                //编辑
                getManuscript(data,data.id);
            }else if(obj.event === 'look'){ //预览
                look(data,data.id);
            }
        });
        //监听提交
        form.on('submit(editSubmit)', function(data){
            // TODO 校验
            formSubmit(data);
            return false;
        });
    });

});

//提交表单
function formSubmit(obj){
    $.post("/submit/newsEdit",{
        "id":$("#id").val(),
        "topic" : $("#topic").val(),  //文章标题
        "content" : layedit.getContent(editIndex) //文章内容
    },function(res){
        if(res.code=="1000"){
            layer.alert("文章更新成功！",function () {
                $("#id").val("");
                $("#topic").val("");
                $("#content").val("");
                layer.closeAll();
                load(obj)
            });
        }else{
            layer.alert(res.message,function(){
                layer.closeAll();//关闭所有弹框
                load(obj)
            });
        }
    });
}

function openEdit(title){

    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area:['900px'],
        content:$('#editSubmission')
    });
}

function openLook(title){

    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area:['900px'],
        content:$('#lookSubmission')
    });
}

function getManuscript(obj,id) {
    //如果已经审核通过，提醒不可编辑和删除
    if(obj.mstatus === '审核通过'){
        layer.alert("该文章已审核通过，不可进行编辑。");
    }else{
        //回显数据
        $.get("/submit/getNewsDetail",{"id":id},function(data){
                if(data.msg=="ok" && data.manuscript!=null){

                    $("#id").val(id);
                    $("#topic").val(data.manuscript.topic==null?'':data.manuscript.topic);
                    $("#content").val(data.manuscript.content==null?'':data.manuscript.content);
                    $("#opinionRead").val(data.manuscript.opinion==null?'':data.manuscript.opinion);
                    openEdit("编辑文章");
                }else{
                    //弹出错误提示
                    layer.alert(data.msg,function () {
                        layer.closeAll();
                    });
                }

        });
    }
}

function look(obj,id) {
    $.get("/submit/getNewsDetail",{"id":id},function(data){
        if(data.msg=="ok" && data.manuscript!=null){


            $("#ltopic").val(data.manuscript.topic==null?'':data.manuscript.topic);
            $("#lcontent").val(data.manuscript.content==null?'':data.manuscript.content)

            openLook("预览文章");
        }else{
            //弹出错误提示
            layer.alert(data.msg,function () {
                layer.closeAll();
            });
        }

    });
}

function delManuscript(obj,id) {

    if(null!=id){
            layer.confirm('您确定要删除该文章吗？', {
                btn: ['确认','返回'] //按钮
            }, function(){
                $.post("/submit/delNews",{"id":id},function(data){
                    if(isLogin(data)){
                        if(data=="ok"){
                            //回调弹框
                            layer.alert("删除成功！",function(){
                                layer.closeAll();
                                //加载load方法
                                load(obj);//自定义
                            });
                        }else{
                            layer.alert(data,function(){
                                layer.closeAll();
                                //加载load方法
                                load(obj);//自定义
                            });
                        }
                    }
                });
            }, function(){
                layer.closeAll();
            });
        }

}


function load(obj){
    //重新加载table
    tableIns.reload({
        where: obj.field
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
}

