Zepto(function($){
    
    $('.onlineReply').on("click",function(){
        var id = $(this).data("id");
        updateStatus(id,1);
    })
    
    $('.offlineReply').on("click",function(){
        var id = $(this).data("id");
        updateStatus(id,0);
    })

    function updateStatus(id,status){
        $.ajax({
            type:'POST',
            url:'/reply/update',
            contentType: 'application/json',
            data: JSON.stringify({id:id,status:status}),
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
    }

})