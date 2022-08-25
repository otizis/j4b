<!DOCTYPE HTML>
<html lang="en">

<#include "./comps/head.ftl"/>

<body>
<header>

<div>
    <h2>${(tag.tag)!'标签'}</h2>
</div>

</header>

<#include "./comps/link.ftl"/>


<div class="container">
    <#if pageList?size == 0>
        空
    </#if>
    <#list pageList! as page>
        <a class="u-card <#if page.status==2>u-self-page</#if>" href="/page/${page.id!}">
            <h3>${page.title!}</h3>
            <div class="content">
                <#assign summary=page.content?replace("<.*?>","","r")>
                <#assign summary=summary?replace("&nbsp;","")>
                <#assign summary=summary?replace("  "," ")>
                <#if summary?length gt 30>${summary[0..29]}...<#else>${summary}</#if>
            </div>
            <div class="u-card-plane">${page.createAt?string('yyyy-MM-dd')}</div>
        </a>
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