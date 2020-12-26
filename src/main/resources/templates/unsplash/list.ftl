<div>
    <input id="unsplashQuery" type="text" value="${searchReq.query}">
    <input id="unsplashSearchHandler" class="u-button" type="button" value="搜索">
    <#if searchReq.page gt  1>
        <input id="unsplashPageHandler" class="u-button" type="button" value="上一页">
    </#if>
    <span>第${searchReq.page}页 共${resp.total_pages}页</span>
    <#if searchReq.page  lt resp.total_pages>
        <input id="unsplashPageHandler" class="u-button" type="button" value="下一页">
    </#if>
</div>
<ul style="display: flex; flex-wrap: wrap;">
<#list resp.results! as photo>
    <li style="width: 160px; height: 200px;">
        <img src="${(photo.urls.domain)!''}${(photo.urls.small)!''}" alt="" style="max-width: 150px; max-height: 150px;">
        <input class="u-button select" data-domain="${(photo.urls.domain)!''}" data-src="${(photo.urls.regular)!''}" type="button" value="选择">
    </li>
</#list>
</ul>


