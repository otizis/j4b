Zepto(function($){
    var E = window.wangEditor
    var editor = new E('#editor')
    editor.customConfig.menus = [
        'head',  // 标题
        'bold',  // 粗体
        'fontSize',  // 字号
        'strikeThrough',  // 删除线

        'link',  // 插入链接
        'list',  // 列表
        'justify',  // 对齐方式
        'quote',  // 引用
        'emoticon',  // 表情
        'image',  // 插入图片

        'code',  // 插入代码
        'undo',  // 撤销
        'redo'  // 重复
    ]
    editor.create()

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
                    editor.txt.html(resp.page.content)
                    $("#addPageForm")[0].title.value = resp.page.title;
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
            jsonData.content = editor.txt.html();
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