<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>MW 美之味</title>

    <!-- Bootstrap core CSS  rel 属性指示被链接的文档是一个样式表 -->
    <link href="resources/bootstrap-3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link href="views/css/happyfood.css" rel="stylesheet">
    <script src="resources/jquery/jquery-2.1.1.min.js"></script>
    <script src="resources/jquery/md5.min.js"></script>
    <script src="views/js/index.js"></script>

</head>

<body style="line-height: normal">

<div style="background-image: url(views/img/login_back.jpg); width: auto; height:630px">

    <br class="form-signin">
    <div class="fon_30px wid_50b ele_mid fon_mid">请先登录</div>
    <div class="mar_b_30 mar_t_30">
        <label for="username" class="sr-only">Email address</label>
        <input type="email" id="username" class="form-control ele_mid" placeholder="Email address" required autofocus>
    </div>
    <div class="mar_b_30 mar_t_30">
        <label for="password" class="sr-only">Password</label>
        <input type="password" id="password" class="form-control ele_mid" placeholder="Password" required>
    </div>
    <div class="checkbox ele_mid wid_50b fon_mid">
        <label>
            <input type="checkbox" value="remember-me"> 记住密码
        </label>
    </div>
    </br>
    <div>
        <button class="btn btn-lg btn-primary btn-block ele_mid but_med" id="logon">登录</button>
    </div>
    <a href="views/jsp/regist.jsp">没有帐号? 点我注册</a>
</div> <!-- /container -->


</body>
<!--<script src="views/js/index.js"></script>-->
</html>
