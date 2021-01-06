<!DOCTYPE HTML>
<html lang="en">

<#include "../comps/head.ftl"/>

<body>
<#include "../comps/adminLink.ftl"/>
<hr>
<h3>当前目录：${currPath!"/"}</h3>
<h4>上传文件</h4>
<form action="/uploadOrig"  method="POST" enctype="multipart/form-data">
<input type="file" name="file">
<input type="hidden" name="path" value="${currPath}">
<input type="submit" class="u-button" value="上传">
</form>
<h4>创建目录</h4>
<form action="/createDir"  method="POST" enctype="multipart/form-data">
    <input type="text" name="dirName" placeholder="新建文件夹名称">
    <input type="hidden" name="currPath" value="${currPath}">
    <input type="submit" class="u-button" value="新建文件夹">
</form>
<h4>文件列表 <a href="?currPath=${currPath}&path=.."> 向上一级</a></h4>

<table style="margin: auto">
    <tr>
        <th>序号</th>
        <th>文件名</th>
        <th>url</th>
        <th>操作</th>
    </tr>
<#list fileList! as file>
    <tr>
        <td>${file?index}</td>
        <td>
            <#if (file.directory)>
            <a href="?currPath=${currPath}&path=${file.name}">${file.name!""}</a>
            <#else>
            ${file.name!""}
            </#if>
        </td>
        <td>
            <#if (file.directory)>
                -
            <#else>
                <a href="/oss/${currPath}/${file.name}">${file.name}</a>
            </#if>
        </td>
        <td>
            <form action="/delFile"  method="POST" enctype="multipart/form-data">
                <input type="hidden" name="path" value="${currPath}/${file.name}">
                <input type="submit" class="u-button" value="删除">
            </form>
        </td>
    </tr>
</#list>
</table>
</body>
</html>