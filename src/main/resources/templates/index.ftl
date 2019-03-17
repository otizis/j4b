<!DOCTYPE HTML>
<html lang="en">

<#include "./comps/head.ftl"/>

<body>
<header>

    <div class="u-logo">
        <img src="${blogInfo.logoUrl!"/logo.jpg"} "alt="">
    </div>

    <h2>${blogInfo.title!"j4b"}</h2>

    <div class="u-b-desc">
        <p>${blogInfo.desc!"j4b desc"}</p>
    </div>

</header>

<#include "./comps/link.ftl"/>

<div class="container">
    <#if pageList?size == 0>
        空
    </#if>
    <#list pageList! as page>
        <div class="u-card"  onclick="location.href='/page/${page.id!}'">
            <h3>${page.title!}</h3>
            <div>
                <#assign summary=page.content?replace("<.*?>","","r")>
                <#assign summary=summary?replace("&nbsp;","")>
                <#assign summary=summary?replace("  "," ")>
                <#if summary?length gt 30>${summary[0..29]}...<#else>${summary}</#if>
            </div>
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