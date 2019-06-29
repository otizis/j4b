<div class="u-link">
    <a href="/">首页</a>
    <a href="/editPageContent.html">新页</a>
    <#list blogInfo.linkList! as link>
    <a href="${link.link!}">${link.name!}</a>
    </#list>
</div>