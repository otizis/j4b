<!DOCTYPE HTML>
<html lang="en">
<#assign description>${blogInfo.title!},${blogInfo.desc!}</#assign>
<#assign keywords>${blogInfo.title!},${blogInfo.desc!}</#assign>
<#include "./comps/head.ftl"/>

<body>
<header>

    <div class="u-logo">
        <img src="${blogInfo.logoUrl!"/logo.jpg"} "alt="">
    </div>

    <h2>${blogInfo.title!"j4b"}</h2>

    <div class="u-b-desc">
        <p>${blogInfo.desc!"j4b desc"}</p>
        <div>
            <#if pNum==1 && bgmUrl?? >
                <audio src="${bgmUrl}" controls autoplay loop></audio>
            </#if>
        </div>
    </div>

</header>

<#include "./comps/link.ftl"/>

<div class="container">
    <#if pageList?size == 0>
        空
    </#if>
    <#list pageList! as page>
        <a class="u-card <#if page.status==2>u-self-page</#if>"
           target="_blank"
           href="/page/${page.id!}">
            <#if page.bgUrl?? && page.bgUrl?length gt 1>
            <div class="bg" style="background-image: url('<#if !page.bgUrl?contains("http")>${unsplashDomain}</#if>${page.bgUrl}')"></div>
            <#elseif  page.status==2>
            <div class="bg" style="background: darkred"></div>
            <#else>
            <div class="bg" style="background: darkgray"></div>
            </#if>
            <div class="content">
                <#assign summary=page.content?replace("<.*?>","","r")>
                <#assign summary=summary?replace("&nbsp;","")>
                <#assign summary=summary?replace("  "," ")>
                <#if summary?length gt 50>${summary[0..49]}...<#else>${summary}</#if>
            </div>

            <h3>${page.title!}</h3>

            <div class="u-card-plane">
                <div >
                    ${page.createAt?string('yyyy-MM-dd')}
                </div>


                <#if page.tagList?? && page.tagList?size gt 0>
                <div class="u-tag-list">
                    <object>
                        <#list page.tagList! as tag>
                            <a href="/pageFilter/tag/${tag.id}" target="_blank">#${tag.tag}</a>
                        </#list>
                    </object>
                </div>

                </#if>
            </div>

        </a>
    </#list>
    <div class="u-pagition">
        <#if pNum gt  1>
            <a href="?pageNum=${pNum - 1}" target="_blank">上一页</a>
        </#if>
        <span>${pNum}/${total}</span>
        <#if pNum lt total>
            <a href="?pageNum=${pNum + 1}" <#if pNum==1>target="_blank"</#if>>下一页</a>
        </#if>

    </div>
</div>

<#include "./comps/foot.ftl"/>
</body>
</html>
