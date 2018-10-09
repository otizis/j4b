Zepto(function($){
    tinymce.init({
        selector:'#editor',
        language:'zh_CN',
        plugins: "hr lists link image imagetools",
        height: 600,
        menubar: false,
        toolbar:"undo redo | formatselect bold italic underline fontsizeselect image | hr bullist | link | removeformat",
        file_browser_callback:function(){

        },
        images_upload_url: '/upload'
    });


    $('.updatePage').on("click",function(){
        var id = $(this).data("id");
        $("#addPageForm")[0].id.value = id;
        
        $.ajax({
            type:'POST',
            url:'/page/info',
            contentType: 'application/json',
            data: JSON.stringify({id:id}),
            success: function (resp) {
                if(resp.code === 0){
                    console.log(resp);
                    tinymce.activeEditor.setContent(resp.page.content,{format: 'raw'})
                    $("#addPageForm")[0].title.value = resp.page.title;
                }
                else{
                    window.alert(resp.msg||'error')
                }
            }
        })
    })
    $('.delPage').on("click",function(){
        var id = $(this).data("id");
        $.ajax({
            type:'POST',
            url:'/page/del',
            contentType: 'application/json',
            data: JSON.stringify({id:id}),
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

        // 文章部分
        $("#addPageBtn").one("click",function(){
            var data = $("#addPageForm").serializeArray();
            console.log(data)
            var jsonData={}
            $.each(data,function(index,item){
                jsonData[item.name] = item.value;
            });
            jsonData.content = tinymce.activeEditor.getContent({format: 'raw'});
            $.ajax(
                {
                    type: 'POST',
                    url: jsonData.id?"/page/edit":"/page/save",
                    contentType: 'application/json',
                    data: JSON.stringify(jsonData),
                    success: function (resp) {
                        console.log(resp);
                        if(resp.code === 0){
                            window.alert('操作成功');
                            location.reload();
                        }
                        else{
                            window.alert(resp.msg||'error')
                        }
                    }
                }
            )
        })
})