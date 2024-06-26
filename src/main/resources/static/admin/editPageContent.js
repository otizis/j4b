Zepto(function($){

    tinymce.init({
        selector:'#editor',
        language:'zh_CN',
        plugins: "hr lists link image imagetools paste code",
        paste_as_text : true,
        paste_remove_styles: true,
        paste_remove_styles_if_webkit: true,
        paste_strip_class_attributes: true,
        paste_data_images: true, // 设置为“true”将允许粘贴图像，而将其设置为“false”将不允许粘贴图像。
        height: 600,
        menubar: false,
        toolbar:"undo redo | formatselect bold italic underline alignleft aligncenter fontsizeselect image | hr bullist blockquote | link | removeformat code",
        urlconverter_callback : function(url, node, on_save, name) {
            // 不转化最前面的/
            return url;
        },
        images_upload_url: '/upload',
        paste_preprocess: function(plugin, args) {
            if(args.content.indexOf("<img") === -1){
                return
            }
            var bUrl =  args.content.split('"')[1]
            args.content = ""
            if(!bUrl){
                return
            }
            $.ajax({
                type:"GET",
                xhrFields: {responseType: 'blob'},
                url:bUrl,
                success: function (blobFile){
                    console.log(blobFile)
                    var file = new File([blobFile], "blob.png");
                    var formData = new FormData();
                    formData.append("file",file)
                    $.ajax({
                        type:"POST",
                        url:"/upload",
                        data:formData,
                        dataType:"json",
                        processData: false,
                        contentType: false,
                        success:function(resp){
                            tinymce.activeEditor
                                .execCommand('mceInsertContent',
                                    false,
                                    `<img src="${resp.location}">`);
                        }
                    })
                }
            })
        }
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
                        location.replace("/page/"+resp.id);
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
        $.ajax(
            {
                type: 'POST',
                url: "/tag/save",
                contentType: 'application/json',
                data: JSON.stringify({tag:tag}),
                success: function (resp) {
                    if(resp.code === 0){
                        console.log(resp)
                        $("#tagList").prepend("<label >" +
                            "<input type='checkbox' name='tags' value='"+resp.tag.id+"'> " +resp.tag.tag+
                            "</label>")
                        $("#newTag").val("");
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