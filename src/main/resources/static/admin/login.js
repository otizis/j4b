Zepto(function ($) {
    $("#access").on("keydown",function(ev){
        if(ev.keyCode == "13") {
            login()
        }
    })
    $("#submit").on("click", function () {
        login()
    })
    function login(){
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
                        if(location.search.length>10){
                            location.href = decodeURIComponent(location.search.substring(1));
                            return;
                        }
                        location.href = "/config.html";
                    }
                    else{
                        window.alert(resp.msg||'error')
                    }
                }
            }
        )
    }
})