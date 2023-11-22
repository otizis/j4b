<!DOCTYPE HTML>
<html lang="en">
<#assign description="${page.title}"/>
<#assign keywords="${page.title}"/>
<#include "./comps/head.ftl"/>
<body >
<#if page.bgUrl?? && page.bgUrl?length gt 1>
<div style="background-image: url('<#if !page.bgUrl?contains("http")>${unsplashDomain}</#if>${page.bgUrl}');
        background-size: cover;
        background-position: center;
        height: 50vh"></div>
</#if>
<header>
    <h3 >${page.title!}</h3>
    <div>
        <span class="header-info">${page.createAt?string("yyyy-MM-dd HH:mm")}</span>
        <#if page.status == 2>
            <span style="color: darkred">私密文章</span>
        </#if>
        <@hasLogin>
            <a href="/editPageContent.html?id=${page.id}">编辑</a>
        </@hasLogin>
    </div>
</header>

<#include "./comps/link.ftl"/>

<div class="container">
    <div class="u-page <#if page.status == 2>u-self-page</#if>" >
        ${page.content}
    </div>
    <div class="u-tag">
        <#list (page.tagList)! as tag>
            <a href="/pageFilter/tag/${tag.id}"># ${tag.tag}</a>
        </#list>
    </div>
    <@hasLogin>
        <div class="u-append">
            <form id="appendForm">
                <input type="hidden" name="id" value="${page.id!}">
                <textarea name="content"></textarea>
                <br>
                <input class="u-button" type="button" id="append" value="内容追加" />
            </form>
        </div>
    </@hasLogin>
    <#if replyOpen>

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
    </#if>
</div>

<#include "./comps/foot.ftl"/>
<script src="/libs/zepto/zepto.1.2.min.js"></script>
<#if replyOpen>
<script src="/admin/page.js"></script>
</#if>
</body>
</html>
