Zepto(function($){
    var item = localStorage.getItem("token");
    if(item){
        $.ajaxSettings.headers={
            "token" : item
        }
    }
    new Vue({
        el: '#app',
        data: function (){
            return {
                sentences: [],
                currentTime:0,
                duration: 2000
            }
        },
        created(){
            var _this = this;
            var resultJsonUrl = $("#resultJson").val();
            if(resultJsonUrl){
                $.ajax(
                    {
                        type: 'GET',
                        url: "/oss/"+resultJsonUrl,
                        success: function (resp) {
                            console.log(resp);
                            _this.sentences = resp.Sentences
                            console.log(_this.sentences);

                        }
                    }
                )
            }

        },
        methods:{
            gotoTime(se){
                this.$refs.audio.currentTime = se.BeginTime / 1000;
                this.$refs.audio.play()
            },
            audiotimeupdate(event){
                // console.log(event.timeStamp,this.$refs.audio.currentTime)
                this.currentTime = this.$refs.audio.currentTime * 1000
            }
        }
    })

    $("#addConfigBtn").one("click",function(){
        var data = $("#addConfig").serializeArray();
        console.log(data)
        var jsonData={}
        $.each(data,function(index,item){
            jsonData[item.name] = item.value;
        })

        $.ajax(
            {
                type: 'POST',
                url: "/sys/config/update",
                contentType: 'application/json',
                data: JSON.stringify(jsonData),
                success: function (resp) {
                    console.log(resp);
                    if(resp.code === 0){
                        location.reload()
                    }
                    else{
                        window.alert(resp.msg||'error')
                    }
                }
            }
        )
    })


})
