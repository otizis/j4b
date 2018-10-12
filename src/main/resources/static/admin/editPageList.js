Zepto(function($){

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
    $('.updatePage').on("click",function(){
        var id = $(this).data("id");
        location.href="/editPageContent.html?id="+id
    })
})