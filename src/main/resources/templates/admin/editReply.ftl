<!DOCTYPE HTML>
<html lang="en">
<#include "../comps/head.ftl"/>
<body>
<#include "../comps/adminLink.ftl"/>

<hr>
<table style="margin: auto">
    <tr>
        <th>来源</th>
        <th>创建时间</th>
        <th>内容</th>
        <th>状态</th>
        <th>操作</th>
    </tr>
    <#list page.records! as reply>
        <tr class="u-table-line">
            <td style="width: 7rem">
                ${reply.ip!""}
                <span>
                <#if reply.pageId?? >
                    <a href="/page/${reply.pageId}">文章留言</a>
                <#else>
                    blog留言
                </#if>
                </span>
            </td>
            <td style="width: 8rem">${reply.createAt?string("yyyy-MM-dd HH:mm:ss")}</td>
            <td style="max-width: 15rem">${reply.content!""}</td>
            <td>
                <#if (reply.status == 1)> 正常
                <#else> 已屏蔽
                </#if>
            </td>
            <td>
                <input class="u-button onlineReply"
                type="button"
                data-id="${reply.id!''}" value="上线">
                |
                <input class="u-button offlineReply"
                type="button"
                data-id="${reply.id!''}" value="屏蔽">
            </td>
        </tr>
    </#list>
</table>
<div>
        <#if page.current gt  1>
            <a href="?pageNum=${page.current - 1}">上一页</a>
        </#if>
        <span>第${page.current}页 共${total}页</span>
        <#if page.current lt total>
            <a href="?pageNum=${page.current + 1}">下一页</a>
        </#if>
    </div>
<hr>
<style>
    .u-table-line:hover{
        background-color: #d7ffb5;
    }
</style>
<script src="https://zeptojs.com/zepto.min.js"></script>
<script src="/admin/editReply.js"></script>
</body>
</html>