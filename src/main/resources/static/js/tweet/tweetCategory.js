/**
 * 分类列表
 */
$(function() {
    //初始化treegrid 页面表格
    layui.config({
        base: '../treegrid/'
    }).use(['laytpl', 'treegrid'], function () {
        var laytpl = layui.laytpl,
            treegrid = layui.treegrid;
        treegrid.config.render = function (viewid, data) {
            var view = document.getElementById(viewid).innerHTML;
            return laytpl(view).render(data) || '';
        };

        var treeForm=treegrid.createNew({
            elem: 'tweetTable',
            view: 'view',
            data: { rows: categoryList },
            parentid: 'parentId',
            singleSelect: false
        });
        treeForm.build();

    });
    //操作
    layui.use('form', function(){
        var form = layui.form;
        //监听提交
        form.on('submit(categorySubmit)', function(data){
            //校验 TODO
            $.ajax({
                type: "POST",
                data: $("#categoryForm").serialize(),
                url: "/tweet/setCategory",
                success: function (data) {
                    if (data == "ok") {
                        layer.alert("操作成功",function(){
                            layer.closeAll();
                        });
                    } else {
                        layer.alert(data);
                    }
                },
                error: function (data) {
                    layer.alert("操作请求错误，请您稍后再试");
                }
            });
            return false;
        });
        form.render();
    });

});

function edit(id,type){
    if(null!=id){
        $("#type").val(type);
        $("#id").val(id);
        $.get("/tweet/getCategory",{"id":id},function(data) {
            // console.log(data);
            if(null!=data){
                $("input[name='category']").val(data.category);
                $("textarea[name='description']").text(data.description);
                $("#parentId").val(data.parentId);
                layer.open({
                    type:1,
                    title: "更新分类",
                    fixed:false,
                    resize :false,
                    shadeClose: true,
                    area: ['500px', '380px'],
                    content:$('#updateCategory'),
                    end:function(){
                        location.reload();
                    }
                });
            }else{
                layer.alert("获取类别数据出错，请您稍后再试");
            }
        });
    }
}
//开通权限
function addCategory(pid,flag){
    if(null!=pid){
        //flag[0:新增分类；1：新增子节点分类]
        //type[0:编辑；1：新增]
        if(flag==0){
            $("#type").val(1);
            $("#parentId").val(0);
        }else{
            //设置父id
            $("#type").val(1);
            $("#parentId").val(pid);
        }
        layer.open({
            type:1,
            title: "添加分类",
            fixed:false,
            resize :false,
            shadeClose: true,
            area: ['500px', '380px'],
            content:$('#updateCategory'),  //页面自定义的div，样式自定义
            end:function(){
                location.reload();
            }
        });
    }
}

function del(id,name){
    // console.log("===删除id："+id);
    if(null!=id){
        layer.confirm('您确定要删除'+name+'类别吗？', {
            btn: ['确认','返回'] //按钮
        }, function(){
            $.post("/tweet/del",{"id":id},function(data){
                if(data=="ok"){
                    //回调弹框
                    layer.alert("删除成功！",function(){
                        layer.closeAll();
                        //加载load方法
                        location.reload();;//自定义
                    });
                }else{
                    layer.alert(data);//弹出错误提示
                }
            });
        }, function(){
            layer.closeAll();
        });
    }

}

//关闭弹框
function close(){
    layer.closeAll();
}