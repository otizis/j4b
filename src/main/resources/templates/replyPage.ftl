<!DOCTYPE HTML>
<html lang="en">
<#include "./comps/head.ftl"/>
<body>
<h3>留言版</h3>
<#include "./comps/link.ftl"/>
<br>
<br>

<div class="container">
    <div class="o-reply">
        <form id="addReplyForm">
            <textarea name="content" placeholder="留言审后可见"></textarea>
            <br>
            <input class="u-button" type="button" id="addReply" value="留言">
        </form>
    </div>
    <hr>
    <#if replyList?size == 0>
        暂无留言
    </#if>
    <div class="m-reply">
        <#list replyList! as reply>
            <div class="u-reply">
                <span>
                <#if reply.pageId?? >
                    <a href="/page/${reply.pageId}">文章留言</a>
                <#else>
                    blog留言
                </#if>
                ：</span>
                <span class="form">${reply.ip}</span>
                <span class="time">${reply.createAt?string('yyyy-MM-dd')}</span>
                <p>${reply.content}</p>
            </div>
        </#list>
    </div>

    <div class="u-pagition">
        <#if pNum gt  1>
            <a href="?pageNum=${pNum - 1}">上一页</a>
        </#if>
        <span>第${pNum}页 共${total}页</span>
        <#if pNum lt total>
            <a href="?pageNum=${pNum + 1}">下一页</a>
        </#if>
    </div>
</div>

<#include "./comps/foot.ftl"/>
<script src="/libs/zepto/zepto.1.2.min.js"></script>
<script >
    var page = 1;
    Zepto(function($) {
        $('#addReply').on("click", function () {
            var data = $("#addReplyForm").serializeArray();
            console.log(data)
            var jsonData = {}
            $.each(data, function (index, item) {
                jsonData[item.name] = item.value;
            });
            $.ajax({
                type: 'POST',
                url: '/reply/save',
                contentType: 'application/json',
                data: JSON.stringify(jsonData),
                success: function (resp) {
                    if (resp.code === 0) {
                        console.log(resp);
                        location.reload();
                    } else {
                        window.alert(resp.msg || 'error')
                    }
                }
            })
        })
    })
</script>
</body>
</html>