Zepto(function($){

    $('.delTag').on("click",function(){
        var id = $(this).data("id");
        $.ajax({
            type:'POST',
            url:'/tag/del',
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
    $('.updateTag').on("click",function(){
        var id = $(this).data("id");

        $.ajax({
            type:'POST',
            url:'/tag/save',
            contentType: 'application/json',
            data: JSON.stringify({id:id,tag:$("#"+id).val()}),
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
})