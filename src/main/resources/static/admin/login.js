Zepto(function ($) {
    $("#submit").on("click", function () {
        $.ajax(
            {
                type: 'POST',
                url: "/sys/login",
                contentType: 'application/json',
                data: JSON.stringify({access: $("#access")[0].value}),
                success: function (resp) {
                    console.log(resp);
                    if(resp.code === 0){
                        localStorage.setItem("token", resp.token);
                        location.href = "/config.html";
                    }
                    else{
                        window.alert(resp.msg||'error')
                    }
                }
            }
        )
    })

})