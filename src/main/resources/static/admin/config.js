Zepto(function($){
    var item = localStorage.getItem("token");
    if(item){
        $.ajaxSettings.headers={
            "token" : item
        }
    }

    $(".updateConfig").on("click",function(){
        console.log("click .updateConfig")
        $("#addConfig")[0].code.value = $(this).data("c");
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

    // 文章部分
    $("#addPageBtn").one("click",function(){
        var data = $("#addPageForm").serializeArray();
        console.log(data)
        var jsonData={}
        $.each(data,function(index,item){
            jsonData[item.name] = item.value;
        });
        jsonData.content = editor.txt.html();
        $.ajax(
            {
                type: 'POST',
                url: "/page/save",
                contentType: 'application/json',
                data: JSON.stringify(jsonData),
                success: function (resp) {
                    console.log(resp);
                    if(resp.code === 0){
                        window.alert('新增成功');
                    }
                    else{
                        window.alert(resp.msg||'error')
                    }
                }
            }
        )
    })
})