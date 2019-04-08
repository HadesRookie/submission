/**
 * 用户管理
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
            elem: '#reviewList'
            ,url:'/review/getReviewList'
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
                ,{field:'username', title:'用户名'}
                ,{field:'topic', title:'标题'}
                ,{field:'content',title:'简要内容',align:'center', minWidth:110}
                ,{field:'mstatus', title:'审核状态',width:95,align:'center',templet:'#submissionStatus'}
                ,{field:'insertTime', title: '发布时间'}
                ,{field:'updateTime', title: '更改时间'}
                ,{fixed:'right', title:'操作',width:140,align:'center', toolbar:'#optBar'}
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
        table.on('tool(reviewTable)', function(obj){
            var data = obj.data;
             if(obj.event === 'review'){
                //编辑
                getReview(data,data.id);
             }else if(obj.event === 'look'){
                 look(data,data.id);
             }
        });
        //监听审核通过
        form.on('submit(reviewPass)', function(data){
            // TODO 校验
            reviewPass(data);
            return false;
        });

        //监听退回修改
        form.on('submit(returnEdit)', function(data){
            // TODO 校验
            returnEdit(data);
            return false;
        });

    });
    //搜索框
    layui.use(['form','laydate'], function(){
        var form = layui.form ,layer = layui.layer
            ,laydate = layui.laydate;
        //日期
        laydate.render({
            elem: '#insertTimeStart'
        });
        laydate.render({
            elem: '#insertTimeEnd'
        });
        //TODO 数据校验
        //监听搜索框
        form.on('submit(searchSubmit)', function(data){
            //重新加载table
            load(data);
            return false;
        });
    });


});

//审核通过
function reviewPass(obj) {
    $.post("/review/pass",{
        "id":$("#id").val()
    },function(res){
        if(res.code=="1000"){
            layer.alert("通过审核！",function () {
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

function returnEdit(obj) {
    $.post("/review/returnEdit",{
        "id":$("#id").val(),
        "opinion" : $("#opinion").val() //审核意见
    },function(res){
        if(res.code=="1000"){
            layer.alert("退回修改！",function () {
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
        content:$('#reviewSubmission')
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

function getReview(obj,id) {
    //如果已经审核通过，提醒不可编辑和删除
    if(obj.mstatus === '审核通过'){
        layer.alert("该文章已审核通过，不可进行再次审核。");
    }else{
        //回显数据
        $.get("/submit/getNewsDetail",{"id":id},function(data){
            if(data.msg=="ok" && data.manuscript!=null){

                $("#id").val(id);
                $("#topic").val(data.manuscript.topic==null?'':data.manuscript.topic);
                $("#content").val(data.manuscript.content==null?'':data.manuscript.content);
                $("#opinion").val(data.manuscript.opinion==null?'':data.manuscript.opinion);

                openEdit("审核文章");
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


function load(obj){
    //重新加载table
    tableIns.reload({
        where: obj.field
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
}

