$(function () {
    /**
     * 登录方法
     */
    $("#logon").click(function () {
        var username = $("#username").val();
        var password = $("#password").val();
        $.ajax({
            url: "login?username="+username+"&password="+md5(password),
            success: function (data) {
               if(data.code == 200){
                   window.location.href = 'views/jsp/buttonList.jsp';
               }else{
                   alert(data.data);
               }
            }
        });


    })



});



