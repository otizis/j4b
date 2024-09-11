Zepto(function($){
    var item = localStorage.getItem("token");
    if(item){
        $.ajaxSettings.headers={
            "token" : item
        }
    }

    $(".updateConfig").on("click",function(){
        console.log("click .updateConfig",$(this).closest('tr').find(".id-code"))
        $("#addConfig")[0].code.value = $(this).closest('tr').find(".id-code")[0].innerText
        $("#addConfig")[0].v.value =$(this).closest('tr').find(".id-value")[0].innerHTML
    })

    $(".deleteConfig").on("click",function(){
        $.ajax(
            {
                type: 'POST',
                url: "/sys/config/delete",
                contentType: 'application/json',
                data: JSON.stringify({code:$(this).data("c")}),
                success: function (resp) {
                    console.log(resp);
                    if(resp.code === 0){
                        location.reload()
                    }
                    else{
                        window.alert(resp.msg||'error')
                    }
                }
            }
        )
    })

    $("#addConfigBtn").one("click",function(){
        var data = $("#addConfig").serializeArray();
        console.log(data)
        var jsonData={}
        $.each(data,function(index,item){
            jsonData[item.name] = item.value;
        })

        $.ajax(
            {
                type: 'POST',
                url: "/sys/config/update",
                contentType: 'application/json',
                data: JSON.stringify(jsonData),
                success: function (resp) {
                    console.log(resp);
                    if(resp.code === 0){
                        location.reload()
                    }
                    else{
                        window.alert(resp.msg||'error')
                    }
                }
            }
        )
    })


})
