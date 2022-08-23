<!DOCTYPE HTML>
<html lang="en">

<#include "../comps/head.ftl"/>

<body>
<#include "../comps/adminLink.ftl"/>
<hr>

<h4>备份说明</h4>
<p>会将数据库和上传文件目录{fileupload.path}打包，可以在<a href="/fileList.html">云盘</a>根目录找到backup{time}.zip下载</p>
<h4>zip包内含文件</h4>
    <ul style="width: 30rem; list-style-type: auto; text-align: left; margin: auto;">
        <li>包含h2的数据文件zip包，解压后放到jar包同目录配置{spring.datasource.url=jdbc:h2:file:./h2}</li>
        <li>包含{fileupload.path}目录所有文件，放到应用指向的位置</li>
    </ul>
<div style="padding-top: 2rem">
    <input id="backup" type="button" class="u-button" value="开始备份">
</div>

<script src="/libs/zepto/zepto.1.2.min.js"></script>
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