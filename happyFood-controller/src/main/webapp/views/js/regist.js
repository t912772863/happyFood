$(function () {
    /**
     * 获取验证码
     */
    $("#get_code").click(function () {
        var mail= $("#mail").val();
        $.ajax({
            url:"/happyFood/getRegisterCode?mail="+mail,
            success:function (data) {
                if(data.code == 200){
                    alert("验证码已成功发送到您的邮箱, 请查收");
                }else{
                    alert(data.data);
                }
            }

        })
    })

    /**
     * 注册
     */
    $('#register').click(function () {
        var mail= $("#mail").val();
        var mobile = $("#phone").val();
        var nickName = $("#nick_name").val();
        var remark = $("#remark").val();
        var password = md5($("#password").val());
        var code = $("#code").val();
        $.ajax({
            url: "/happyFood/insert",
            type: 'post',
            data:{
                mobile:mobile,
                nickName:nickName,
                remark:remark,
                passWord:password,
                code:code,
                mail:mail
            },
            success: function (data) {
                if(data.code == 200){
                    window.location.href = '/views/jsp/buttonList.jsp';
                }else{
                    alert(data.data);
                }
            }
        });


    })


});