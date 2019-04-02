/**
 * 推文管理
 */
var pageCurr;

$(function() {

    layui.use(['form' ,'tree','layer'],function () {
        var layer=layui.layer;
        //监听提交
        getTreeData()
    });

    layui.use('layedit',function () {
        layedit = layui.layedit;
        layedit.set({
            uploadImage : {
                url : "/submit/upload",
                type: 'post'
            }
        })
        //创建一个编辑器
        var editIndex = layedit.build('content');
        var lookIndex = layedit.build('lcontent');
        //用于同步编辑器内容到textarea
        layedit.sync(editIndex);
        layedit.sync(lookIndex);
    });

    layui.use('table', function(){
        var table = layui.table
            ,form = layui.form;

        tableIns=table.render({
            elem: '#tweetList'
            ,url:'/tweet/getTweetListByNodeId'
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
                ,{field:'tstatus', title:'推送状态',width:95,align:'center',templet:'#tweetStatus'}
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
        table.on('tool(tweetTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'push'){
                //打开推送审核框
                push(data,data.id);
            }else if(obj.event === 'look'){
                look(data,data.id)
            }
        });
        //监听确认推送
        form.on('submit(pushTweet)', function(data){
            // TODO 校验
            pushTweet(data);
            return false;
        });


    });

});

function getTreeData() {
    $.ajax({
        type: "get",
        url: "/tweet/findCategory",
        success: function (data) {
            if (data !=null) {
                initTree(data);
            } else {
                layer.alert(data);
            }
        },
        error: function () {
            layer.alert("获取数据错误，请您稍后再试");
        }
    });
}

function initTree(data){

    layui.tree({
        elem: '#category', //指定元素
        target: '_blank', //是否新选项卡打开（比如节点返回href才有效）
        check: 'arrow', //勾选风格
        checked: function(item) {//复选框
            //layer.msg('check当前节名称：'+ item.name + '<br>全部参数：'+ JSON.stringify(item));
            // console.log('item is Array：'+ item instanceof Array);
            // console.log('item is：'+ item);
            // console.log('check当前节名称：'+ JSON.stringify(item));
            //判断是选中还是移除选中 ,checkbox: ['&#xe626;', '&#xe627;'] //复选框
            /*var checkFlag = data.elem.checked;
            var cFlag = $(this).checked;
            console.log("checkFlag:"+checkFlag)
            console.log("cFlag:"+cFlag)*/
            //当前节点
            //nodeIds.push(item.id);
            /* if( item.children.length > 0 ){
                 nodeIds= getChildNode(item);
             }
             console.log('nodeIds：'+ JSON.stringify(nodeIds));
             // permArray.add(item);
             $.unique(nodeIds);
             console.log('check当前节名称：'+ JSON.stringify(nodeIds));*/
            //校验 TODO
            //$("#permIds").val(permIds);
        },
        click: function(node){ //点击节点回调
            $("#nodeId").val(node.id);
            load(node);
        },
        skin:'shihuang',//皮肤
        //checkboxName: 'permCheck',//复选框的name属性值
        //checkboxStyle: "color: #FD482C",//设置复选框的样式，必须为字符串，css样式
        /* change: function (item){//当当前input发生变化后所执行的回调//console.log(item);
             resourceIds=item;
         },
         data: {//为元素添加额外数据，即在元素上添加data-xxx="yyy"，可选
             hasChild: true
         }*/
        nodes:listToTreeJson(data)
    });
}

//已推送
function pushTweet(obj) {
    $.post("/tweet/push",{
        "id":$("#id").val()
    },function(res){
        var nodeId = $("#nodeId").val()
        if(res.code=="1000"){
            layer.alert("已推送！",function () {
                layer.closeAll();
                reload(nodeId)
            });
        }else{
            layer.alert(res.message,function(){
                layer.closeAll();//关闭所有弹框
                reload(nodeId)
            });
        }
    });
}


function openPush(title){

    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area:['900px'],
        content:$('#pushTweet')
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
        content:$('#lookTweet')
    });
}

function push(obj,id) {
    //如果已经审核通过，提醒不可编辑和删除
    if(obj.tstatus === '已推送'){
        layer.alert("该推文已推送。");
    }else{
        //回显数据
        $.get("/submit/getNewsDetail",{"id":id},function(data){
            if(data.msg=="ok" && data.manuscript!=null){

                $("#id").val(id);
                $("#topic").val(data.manuscript.topic==null?'':data.manuscript.topic);
                $("#content").val(data.manuscript.content==null?'':data.manuscript.content)

                openPush("推送文章");
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
        where: {nodeId:obj.id}
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
}


function reload(nodeId) {
    tableIns.reload({
        where: {nodeId:nodeId}
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
}

/**
 * list转化为tree结构的json数据
 */
function listToTreeJson(data){
    //data不能为null，且是数组
    if(data!=null && (data instanceof Array)){
        //递归转化
        var getJsonTree=function(data,parentId){
            var itemArr=[];
            for(var i=0;i < data.length;i++){
                var node=data[i];
                if(node.parentId==parentId && parentId!=null){
                    var newNode={name:node.category,spread:true,id:node.id,pid:node.parentId,children:getJsonTree(data,node.id)};
                    itemArr.push(newNode);
                }
            }
            return itemArr;
        }
        // return JSON.stringify(getJsonTree(data,''));
        return getJsonTree(data,0);
    }
    //console.log(JSON.stringify(getJsonTree(data,'')));
}

