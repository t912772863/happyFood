<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <title>Index</title>
    <link rel="stylesheet" href="../../resources/bootstrap-3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="../../resources/bootstrap-table/bootstrap-table.css">
    <link href="../css/happyfood.css" rel="stylesheet">
    <script src="../../resources/jquery/jquery-2.1.1.min.js"></script>
    <script src="../../resources/bootstrap-3.3.7/js/bootstrap.min.js"></script>
    <script src="../../resources/bootstrap-table/bootstrap-table.js"></script>
    <script src="../../resources/bootstrap-table/locale/bootstrap-table-zh-CN.js"></script>
</head>
<body>
<script src="../js/head.js"></script>
<h2>Details</h2>
<button class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
    开始演示模态框
</button>
<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    新增/修改按钮
                </h4>
            </div>
            <div class="modal-body">
                在这里添加一些文本
            </div>
            <div class="input-group">
                <span class="input-group-addon" >@</span>
                <input  type="text" class="form-control ele_mid" placeholder="Username" aria-describedby="basic-addon1">
            </div>
            <div class="input-group">
                <span class="input-group-addon" >@</span>
                <input type="text" class="form-control" placeholder="Username" aria-describedby="basic-addon1">
            </div>
            <div class="input-group">
                <span class="input-group-addon" >@</span>
                <input type="text" class="form-control" placeholder="Username" aria-describedby="basic-addon1">
            </div>


            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                </button>
                <button type="button" class="btn btn-primary">
                    提交更改
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<table id="table"></table>
<script src="../js/foot.js"></script>
</body>
<script>
    $('#table').bootstrapTable({
        // 请求地址
        url: '/happyFood/queryButtonByPage',
        // 定义显示列名
        columns: [{
            field: 'id',
            title: 'ID',
        }, {
            field: 'name',
            title: '名称'
        }, {
            field: 'type',
            title: '类型'
        }, {
            field:'level',
            title: '等级'
        },{
            field:'orders',
            title: '排序'
        },{
            field:'useStatus',
            title: '使用状态',
            formatter:function (value, row, index) {
                // value表示该值,row是该条记录对象, index表示第几条记录
                if(0==value){
                    return "未使用";
                }
                if(1==value){
                    return "使用中";
                }
                if(-1 == value){
                    return "已删除";
                }
                return "未知";
            }
        },{
            field:'createTime',
            title: '创建时间'
        }
        ],
        // 请求成功后数据处理
        responseHandler:function (res) {
            var o = {
                // 如果用服务端分页逻辑, 则把记录和总记录数拼成这样一个对象
                total:res.data.totalNumber,
                rows:res.data.result
            }

            return o;
        },
        // 是否显示分页器
        pagination:true,
        // 设置页面分页或者服务端分页 server, client
        sidePagination:'server',
        // 默认显示页码
        pageNumber:1,
        // 每页记录数
        pageSize:10,
        // 可修改的每页记录数
        pageList:[5, 10, 15, 20],
        // 分页请求参数添加
        queryParams: 	function(params) {
            // 插件会基于分页逻辑传一些参数, 这里把这参参数转换成需要的格式
            params.pageSize = params.limit;
            params.pageNumber = (params.offset)/10+1;
            return params;
        },
        queryParamsType:'limit',
        // 是否显示行间色
        striped: true,
        // 当数据为 undefined 时显示的字符
        undefinedText:'--',
        //
        selectItemName:'id',
        // 是否启动搜索框
        search:true,
        // 设置为 true时，按回车触发搜索方法，否则自动触发搜索方法
        searchOnEnterKey:true

    });

</script>
</html>