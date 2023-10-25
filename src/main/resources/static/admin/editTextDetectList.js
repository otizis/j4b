Zepto(function($){

    $('.delWord').on("click",function(){
        var word = $(this).data("word");
        $.ajax({
            type:'POST',
            url:'/reply/delTextDetect',
            contentType: 'application/json',
            data: JSON.stringify({word:word}),
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
    $('#addWordBtn').on("click",function(){
        var word = $('#addWord').val();
        $.ajax({
            type:'POST',
            url:'/reply/addTextDetect',
            contentType: 'application/json',
            data: JSON.stringify({word:word}),
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
