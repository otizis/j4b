Zepto(function($){
    tinymce.init({
        selector:'#editor',
        language:'zh_CN',
        plugins: "hr lists link image imagetools",
        height: 600,
        menubar: false,
        toolbar:"undo redo | formatselect bold italic underline alignleft aligncenter fontsizeselect image | hr bullist blockquote | link | removeformat",
        images_upload_base_path: '/',
        images_upload_url: '/upload'
    });

    // 文章部分
    $("#savePageBtn").one("click",function(){
        var data = $("#savePageForm").serializeArray();
        console.log(data)
        var jsonData={tagList:[]}
        $.each(data,function(index,item){
            jsonData[item.name] = item.value;
        });

        $.each($("input[name='tags']:checked"), function(i,item){
            console.log(item.value)
            jsonData.tagList.push({id:item.value})
        })
        jsonData.content = tinymce.activeEditor.getContent({format: 'raw'});
        $.ajax(
            {
                type: 'POST',
                url: "/page/save",
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

    $("#addTagBtn").on("click",function(){
        var tag = $("#newTag").val();
        console.log(tag);
        var btn = this;
        $.ajax(
            {
                type: 'POST',
                url: "/tag/save",
                contentType: 'application/json',
                data: JSON.stringify({tag:tag}),
                success: function (resp) {
                    console.log(resp);
                    if(resp.code === 0){
                        console.log(resp)
                        $(btn).parent().prepend(" <label >\n" +
                            "                    <input type='checkbox' name='tags' value='"+resp.tag.id+"'> " +resp.tag.tag+
                            "                </label>")
                    }
                    else{
                        window.alert(resp.msg||'error')
                    }
                }
            }
        )
    })
})