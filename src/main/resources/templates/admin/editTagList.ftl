<!DOCTYPE HTML>
<html lang="en">
<#include "../comps/head.ftl"/>
<body>
<#include "../comps/adminLink.ftl"/>

<hr>

<table style="margin: auto"  class="hover-line">
    <tr>
        <th>id</th>
        <th>tag</th>
        <th>操作</th>
    </tr>
    <#list tagList! as tag>
        <tr>
            <td>${(tag.id)!""}</td>
            <td>
                <input type="text" id='${(tag.id)!""}' value='${(tag.tag)!""}'/>
            </td>
            <td>
                <input class="u-button updateTag"
                type="button"
                data-id="${tag.id!''}" value="修改">
                |
                <input class="u-button delTag"
                type="button"
                data-id="${tag.id!''}" value="删除">
            </td>
        </tr>
    </#list>
</table>
<script src="/libs/zepto/zepto.1.2.min.js"></script>
<script src="/admin/editTagList.js"></script>
</body>
</html>