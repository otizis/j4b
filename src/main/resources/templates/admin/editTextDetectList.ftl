<!DOCTYPE HTML>
<html lang="en">
<#include "../comps/head.ftl"/>
<body>
<#include "../comps/adminLink.ftl"/>

<hr>
<form >
    <input placeholder="搜索" type="text" name="search" value="${search!''}"/>
</form>

<table style="margin: auto"  class="hover-line">
    <tr>
        <th>word</th>
        <th>type</th>
        <th>操作</th>
    </tr>
    <#list list.records! as item>
        <tr>
            <td>${item.word!""}</td>
            <td>${item.type!""}</td>
            <td>
                <input class="u-button delWord"
                type="button"
                data-word="${item.word!''}" value="删除">
            </td>
        </tr>
    </#list>
</table>
<div>
    <#if list.current gt  1>
        <a href="?pageNum=${list.current - 1}&search=${search!''}">上一页</a>
    </#if>
    <span>第${list.current}页 共${total}页</span>
    <#if list.current lt total>
        <a href="?pageNum=${list.current + 1}&search=${search!''}">下一页</a>
    </#if>
</div>
<hr>
<input placeholder="新增关键字" id="addWord" type="text"/>
<button class="u-button" id="addWordBtn">新增</button>
<hr>
<script src="/libs/zepto/zepto.1.2.min.js"></script>
<script src="/admin/editTextDetectList.js"></script>
</body>
</html>
