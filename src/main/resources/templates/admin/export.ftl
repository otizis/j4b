<!DOCTYPE HTML>
<html lang="en">

<#include "../comps/head.ftl"/>

<body>
<#include "../comps/adminLink.ftl"/>
<hr>
<h3>备份</h3>
<p>会将数据库和上传目录备份到upload中，可以在<a href="/fileList.html">云盘</a>根目录下载</p>
<input id="backup" type="button" class="u-button" value="开始备份">

<script src="https://zeptojs.com/zepto.min.js"></script>
<script>
    Zepto(function($){
        function backup(query,page){
            $.ajax({
                type: 'POST',
                url: "/sys/backup",
                data: {},
                success: function (resp) {
                   console.log(resp)
                    if(resp.code === 0){
                        alert("文件已经备份")
                    }
                }
            })
        }
        $("#backup").on("click",function(){
            backup("")
        })
    })
</script>
</body>
</html>