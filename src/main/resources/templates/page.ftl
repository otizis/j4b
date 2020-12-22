<!DOCTYPE HTML>
<html lang="en">
<#include "./comps/head.ftl"/>
<body>

<header>
    <h3>${page.title!}</h3>
    <div>
        <span class="header-info">${page.createAt?string("yyyy-MM-dd HH:mm")}</span>
    </div>
</header>

<#include "./comps/link.ftl"/>

<div class="container">
    <div class="u-page">
        ${page.content}
    </div>
    <div class="u-tag">
        <#list (page.tagList)! as tag>
            <a href="/pageFilter/tag/${tag.id}">${tag.tag}</a>
        </#list>
        <a href="/editPageContent.html?id=${page.id}">编辑</a>

    </div>
    <div class="u-append">
        <form id="appendForm">
            <input type="hidden" name="id" value="${page.id!}">
            <textarea name="content"></textarea>
            <br>
            <input type="button" id="append" value="内容追加" />
        </form>
    </div>
    <hr>

    <div class="m-reply">
    </div>

    <div class="o-reply">
        <input class="u-button"  type="button" id="loadReply" data-pid="${page.id!}" value="加载更多留言">
        <hr>
        <form id="addReplyForm">
            <input type="hidden" name="pageId" value="${page.id!}">
            <textarea name="content" placeholder="留言审后可见"></textarea>
            <br>
            <input class="u-button" type="button" id="addReply" value="留言">
        </form>

    </div>

</div>

<#include "./comps/foot.ftl"/>
<script src="https://zeptojs.com/zepto.min.js"></script>
<script src="/admin/page.js"></script>
</body>
</html>