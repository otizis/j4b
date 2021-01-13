<!DOCTYPE HTML>
<html lang="en">
<#include "./comps/head.ftl"/>
<body>
<h3>关键字搜索</h3>
<#include "./comps/link.ftl"/>
<br>
<br>
<div>
    <form action="/pageFilter/search">
        <input type="text" name="keyword" placeholder="搜索关键字" value="${keyword!}">
        <input type="submit" class="u-button" value="搜索">
    </form>
</div>
<div class="container">

    <#if pageList?size == 0>
        空
    </#if>
    <#list pageList! as page>
        <div class="u-card"  onclick="location.href='/page/${page.id!}'">
            <h3>${page.title!}</h3>
            <div class="u-card-plane">${page.createAt?string('yyyy-MM-dd')}</div>
        </div>
    </#list>
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
</body>
</html>