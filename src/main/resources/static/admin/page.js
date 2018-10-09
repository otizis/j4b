var page = 1;
Zepto(function($){
    $('#addReply').on("click",function(){
        var data = $("#addReplyForm").serializeArray();
        console.log(data)
        var jsonData={}
        $.each(data,function(index,item){
            jsonData[item.name] = item.value;
        });
        $.ajax({
            type:'POST',
            url:'/reply/save',
            contentType: 'application/json',
            data: JSON.stringify(jsonData),
            success: function (resp) {
                if(resp.code === 0){
                    console.log(resp);
                    location.reload();
                }
                else{
                    window.alert(resp.msg||'error')
                }
            }
        })
    })
    $('#loadReply').on("click",function(){
        var pageId = $(this).data("pid");
        loadReply(pageId);
    })
    function loadReply(pageId){
        $.ajax({
            type:'GET',
            url:'/reply/list',
            contentType: 'application/json',
            data: {pageId:pageId,page:page},
            success: function (resp) {
                if(resp.code === 0){
                    console.log(resp);
                    if(resp.page.total == 0){
                        window.alert("无评论")
                    }else{
                        page++;
                        if(page > resp.page.pages){
                            $("#loadReply").hide();
                        }
                        var tmp = '<div class="u-reply"><span class="form">{{ip}}</span><span class="time">{{createAt}}</span><p>{{content}}</p></div>'
                        var records = resp.page.records;
        
                        for (var index = 0; index < records.length; index++) {
                            var element = records[index];
                            var repHtml = tmp.replace("{{ip}}",element.ip)
                            .replace("{{createAt}}",element.createAt)
                            .replace("{{content}}",element.content)
                            $(".m-reply").append(repHtml)
                        }
                    }
                }
                else{
                    window.alert(resp.msg||'error')
                }
            }
        })
    }
    loadReply($("#loadReply").data("pid"));
})