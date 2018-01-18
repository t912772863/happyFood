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
    <link href="<%=request.getContextPath()%>/resources/bootstrap-3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/views/css/happyfood.css" rel="stylesheet">
    <script src="<%=request.getContextPath()%>/resources/jquery/jquery-2.1.1.min.js"></script>
    <script src="<%=request.getContextPath()%>/resources/jquery/md5.min.js"></script>
</head>

<body style="line-height: normal">
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
			<h3 id="dish_name">
			</h3>
			<img id="over_img" alt="140x140" src="#" />
			<p id="dish_remark">
			</p>

			<div id="dish_world">
			</div>
			<h2>用料</h2>
			<div id="dish_materials">
			</div>


			<h2>
				制作过程
			</h2>
			<div id="dish_step">
			</div>

		</div>
	</div>
</div>
</body>
<script>
	$(function () {
		//把后台返回的菜品详情, 展示到页面上
		var id= ${id};
		$.ajax({
			url:"/happyFood/f/queryDishDetail?id="+id,
			success:function (data) {
				if(data.code == 200){
					var dishDetail = data.data;
					$('#dish_name').html(dishDetail.name);
					$('#over_img').attr('src',dishDetail.pic);
					$('#dish_remark').html(dishDetail.content);

					// 菜品标签
					var dishWords = dishDetail.tag.split(",");
					var tagEle = "";
					for(var i=0;i<dishWords.length;i++){
						tagEle = tagEle + '<span class="label">'+dishWords[i]+'</span>';
					}
					$('#dish_world').html(tagEle);

					// 菜品用料
					var dishMater = dishDetail.materials;
					var materEle = "";
					for(var i=0;i<dishMater.length;i++){
						materEle = materEle + dishMater[i].name+': '+dishMater[i].amount+'<br/>';
					}

					$("#dish_materials").append(materEle);

					// 菜品步骤
					var dishStep = dishDetail.procedures;
					var stepEle = "";
					for(var i=0;i<dishStep.length;i++){
						stepEle = stepEle + '<p>'+(i+1)+'. '+dishStep[i].content+'</p><img alt="140x140" src="'+dishStep[i].pic+'" />';
					}
					$('#dish_step').html(stepEle);
				}else{
					alert(data.data);
				}
			}

		})


	});
</script>
</html>