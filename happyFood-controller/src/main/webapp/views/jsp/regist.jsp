<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <title>Index</title>
    <link rel="stylesheet" href="../../resources/bootstrap-3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="../../resources/bootstrap-table/bootstrap-table.css">
    <script src="../../resources/jquery/jquery-2.1.1.min.js"></script>
    <script src="../../resources/bootstrap-3.3.7/js/bootstrap.min.js"></script>
    <script src="../../resources/bootstrap-table/bootstrap-table.js"></script>
    <script src="../../resources/bootstrap-table/locale/bootstrap-table-zh-CN.js"></script>
    <script src="../js/regist.js"></script>
</head>
<body>
<script src="../js/head.js"></script>
<div>
<div class="input-group">
    <span class="input-group-addon">邮箱</span>
    <input id="mail" type="text" class="form-control" placeholder="请输入邮箱, 用于登录" aria-describedby="basic-addon1">
    <button type="button" id="get_code" class="btn btn-info">获取验证码</button>
</div>
<div class="input-group">
    <span class="input-group-addon">验证码</span>
    <input id="code" type="text" class="form-control" placeholder="请输入收到的验证码" aria-describedby="basic-addon1">
</div>
<div class="input-group">
    <span class="input-group-addon" >昵称</span>
    <input id="nick_name" type="text" class="form-control" placeholder="支持中英文和数字" aria-describedby="basic-addon1">
</div>

<div class="input-group">
    <span class="input-group-addon" >密码</span>
    <input id="password" type="password" class="form-control" placeholder="密码" aria-describedby="basic-addon1">
</div>

<div class="input-group">
    <span class="input-group-addon" >手机号</span>
    <input id="phone" type="text" class="form-control" placeholder="手机号" aria-describedby="basic-addon1">
</div>

<div class="input-group">
    <span class="input-group-addon" >备注</span>
    <input id="remark" type="text" class="form-control" placeholder="其它备注信息" aria-describedby="basic-addon1">
</div>

<button id="register" type="button" class="btn btn-info">立即注册</button>
</div>
<script src="../js/foot.js"></script>
</body>
</html>