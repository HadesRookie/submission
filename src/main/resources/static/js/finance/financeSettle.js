/**
 * 财务结算
 */
var pageCurr;

$(function() {


    layui.use('table', function(){
        var table = layui.table
            ,form = layui.form;

        tableIns=table.render({
            elem: '#financeList'
            ,url:'/finance/getFinanceList'
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
                ,{field:'feeStatus', title:'稿费状态',width:95,align:'center',templet:'#financeStatus'}
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
        table.on('tool(financeTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'settle'){
                //编辑
                settle(data,data.id);
            }else if(obj.event == 'look'){
                getLook(data,data.id)
            }
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

//结算
function settle(obj,id) {
    if (obj.feeStatus === '已结算'){
        layer.alert("该推文已结算，不可再次结算。");
    } else {
        $.post("/finance/settle",{
            "id":id
        },function(res){
            if(res.code=="1000"){
                layer.alert("已结算！",function () {
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
}



function openLook(title){

    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area:['550px'],
        content:$('#lookUserInfo')
    });
}

function getLook(obj,id) {

        //回显数据
        $.get("/user/getUserByManuscriptId",{"id":id},function(data){
            if(data.msg=="ok" && data.user!=null){

                $("#username").val(data.user.username==null?'':data.user.username);
                $("#mobile").val(data.user.mobile==null?'':data.user.mobile);
                $("#email").val(data.user.email==null?'':data.user.email);
                $("#payNum").val(data.user.payNum == null?'':data.user.payNum);
                $("#realName").val(data.user.realName == null?'':data.user.realName);

                openLook("查看稿主个人信息");
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

