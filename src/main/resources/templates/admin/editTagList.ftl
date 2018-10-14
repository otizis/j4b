<!DOCTYPE HTML>
<html lang="en">
<#include "../comps/head.ftl"/>
<body>
<#include "../comps/adminLink.ftl"/>

<hr>

<table style="margin: auto">
    <tr>
        <th>id</th>
        <th>tag</th>
        <th>操作</th>
    </tr>
    <#list tagList! as tag>
        <tr>
            <td>${(tag.id)!""}</td>
            <td>
                <input id='${(tag.id)!""}' value='${(tag.tag)!""}'/>
            </td>
            <td>
                <input class="updateTag"
                type="button"
                data-id="${tag.id!''}" value="修改">
                |
                <input class="delTag"
                type="button"
                data-id="${tag.id!''}" value="删除">
            </td>
        </tr>
    </#list>
</table>
<script src="https://zeptojs.com/zepto.min.js"></script>
<script src="/admin/editTagList.js"></script>
</body>
</html>