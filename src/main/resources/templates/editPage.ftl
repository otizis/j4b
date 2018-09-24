<!DOCTYPE HTML>
<html lang="en">

<#include "./comps/head.ftl"/>

<body>
<a href="/index.html">首页</a>

<hr>
<table style="margin: auto">
    <#list page.records! as page>
        <tr>
            <td>${page.id!""}</td>
            <td>${page.title!""}</td>
            <td>${page.status!""}</td>
            <td>
                <input class="updatePage"
                type="button"
                data-id="${page.id!''}" value="修改">
            </td>
            <td>
                <input class="delPage"
                type="button"
                data-c="${page.id!''}" value="屏蔽">
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
<div>
    <h5>新增文章</h5>
    <form id="addPageForm">
        <input type="hidden" name="id"/>
        <div>
            <label >
                标题：<input type="text" style="width: 40rem" name="title"/>
            </label>
        </div>
        <div id="editor" style="text-align:left"></div>
        <input id="addPageBtn" type="button" value="提交">
    </form>
</div>
<!-- 注意， 只需要引用 JS，无需引用任何 CSS ！！！-->
<script type="text/javascript" src="//unpkg.com/wangeditor/release/wangEditor.min.js"></script>
<script type="text/javascript">

</script>
<script src="https://zeptojs.com/zepto.min.js"></script>
<script src="/admin/editPage.js"></script>
</body>
</html>