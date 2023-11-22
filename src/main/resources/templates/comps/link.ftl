<div class="u-link">
    <a href="/">首页</a>
    <a href="/extract.html">摘录</a>
    <a href="/pageFilter/search">搜索</a>
    <#if blogInfo.replyOpen>
       <a href="/reply/page">留言</a>
    </#if>
    <@hasLogin>
        <a href="/editPageContent.html">+新增一页</a>
    </@hasLogin>
    <#list blogInfo.linkList! as link>
    <a href="${link.link!}">${link.name!}</a>
    </#list>
</div>