<!DOCTYPE HTML>
<html lang="en">
<#include "./comps/head.ftl"/>
<body>

<header>
    <h3>${page.title!}</h3>
    <div>${page.createAt?string("yyyy-MM-dd HH:mm")}</div>
</header>

<#include "./comps/link.ftl"/>

<div class="container">
    <div class="u-page">
    ${page.content}
    </div>
    <hr>
    <div class="m-reply">
    </div>
    <div class="o-reply">
        <input type="button" id="loadReply" data-pid="${page.id!}" value="加载更多评论">
        <hr>
        <form id="addReplyForm">
            <input type="hidden" name="pageId" value="${page.id!}">
            <textarea name="content" placeholder="评论审核后可显示"></textarea>
            <br>
            <input type="button" id="addReply" value="发表评论">
        </form>

    </div>

</div>

<#include "./comps/foot.ftl"/>
<script src="https://zeptojs.com/zepto.min.js"></script>
<script src="/admin/page.js"></script>
</body>
</html>