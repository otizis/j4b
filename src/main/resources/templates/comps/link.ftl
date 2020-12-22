<div class="u-link">
    <a href="/">首页</a>
    <@hasLogin>
        <a href="/editPageContent.html">+新增一页</a>
    </@hasLogin>
    <#list blogInfo.linkList! as link>
    <a href="${link.link!}">${link.name!}</a>
    </#list>
</div>