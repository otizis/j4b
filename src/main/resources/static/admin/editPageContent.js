Zepto(function($){

    tinymce.init({
        selector:'#editor',
        language:'zh_CN',
        plugins: "hr lists link image imagetools paste code",
        paste_as_text : true,
        paste_remove_styles: true,
        paste_remove_styles_if_webkit: true,
        paste_strip_class_attributes: true,
        height: 600,
        menubar: false,
        toolbar:"undo redo | formatselect bold italic underline alignleft aligncenter fontsizeselect image | hr bullist blockquote | link | removeformat code",
        urlconverter_callback : function(url, node, on_save, name) {
            // 不转化最前面的/
            return url;
        },
        images_upload_url: '/upload'
    });

    // 文章部分
    $("#savePageBtn").one("click",function(){
        var data = $("#savePageForm").serializeArray();
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
    function unsplashSearch(query,page){
        $.ajax({
            type: 'POST',
            url: "/unsplash/list",
            data: {query:query,page:page||1},
            success: function (resp) {
                $("#unsplashList").show().html(resp)
            }
        })
    }
    $("#unsplashBtn").on("click",function(){
        unsplashSearch("")
    })

    $("#unsplashList")
        .on("click","#unsplashSearchHandler",function(){
            unsplashSearch($("#unsplashQuery").val())
        })
        .on("click","#unsplashPageHandler",function(){
            unsplashSearch($("#unsplashQuery").val(), $(this).data("page"))
        })
        .on("click",".select",function(){
            var src = $(this).data("src")
            var domain = $(this).data("domain")
            $("#bgImg").attr("src",domain+src);
            $("#bgUrl").val(src);
            $("#unsplashList").hide()
        })
})