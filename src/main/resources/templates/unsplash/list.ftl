<div>
    <input id="unsplashQuery" type="text" value="${searchReq.query}" placeholder="输入关键字">
    <input id="unsplashSearchHandler" class="u-button" type="button" value="搜索">
    <#if searchReq.page gt 1>
        <input id="unsplashPageHandler" data-page="${searchReq.page - 1}" class="u-button prev" type="button" value="上一页">
    </#if>
    <span>第${searchReq.page}页 共${resp.total_pages}页</span>
    <#if searchReq.page lt resp.total_pages>
        <input id="unsplashPageHandler" data-page="${searchReq.page + 1}" class="u-button next" type="button" value="下一页">
    </#if>
</div>
<ul class="m-unsplash-ul">
    <#list resp.results! as photo>
        <li class="m-unsplash-li">
            <img src="${(photo.urls.domain)!''}${(photo.urls.small)!''}" class="m-unsplash-img">
            <input class="u-button select" data-domain="${(photo.urls.domain)!''}" data-src="${(photo.urls.regular)!''}" type="button" value="选择">
        </li>
    </#list>
</ul>