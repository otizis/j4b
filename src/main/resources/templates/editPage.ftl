<!DOCTYPE HTML>
<html lang="en">
<#include "./comps/head.ftl"/>
<body>
<#include "./comps/adminLink.ftl"/>

<hr>
<table style="margin: auto">
    <tr>
        <th>标题</th>
        <th>状态</th>
        <th>创建时间</th>
        <th>最后修改时间</th>
        <th>操作</th>
    </tr>
    <#list page.records! as page>
        <tr>
            <td>${page.title!""}</td>
            <td>
                <#if (page.status == 1)> 正常
                <#else> 已屏蔽
                </#if>
            </td>
            <td>${page.createAt?string("yyyy-MM-dd HH:mm:ss")}</td>
            <td>${page.updateAt?string("yyyy-MM-dd HH:mm:ss")}</td>
            <td>
                <input class="updatePage"
                type="button"
                data-id="${page.id!''}" value="修改">
                |
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
<div style="max-width: 50rem;margin: auto">
    <h5>文章编辑</h5>
    <form id="addPageForm">
        <input type="hidden" name="id"/>
        <div>
            <label >
                标题：<input type="text" style="width: 40rem" name="title"/>
            </label>
        </div>
        <br>
        <div id="editor" style="text-align:left"></div>
        <input id="addPageBtn" type="button" value="提交">
    </form>
</div>
<!-- 注意， 只需要引用 JS，无需引用任何 CSS ！！！-->
<script src="/libs/tinymce/tinymce.min.js"></script>
<script type="text/javascript">

</script>
<script src="https://zeptojs.com/zepto.min.js"></script>
<script src="/admin/editPage.js"></script>
</body>
</html>