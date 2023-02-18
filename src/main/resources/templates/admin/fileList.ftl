<!DOCTYPE HTML>
<html lang="en">

<#include "../comps/head.ftl"/>

<body>
<#include "../comps/adminLink.ftl"/>
<hr>
<h3>当前目录：${currPath!"/"}</h3>
<h4>远程下载文件</h4>
<form action="/downloadByUrl" method="POST">
    <input type="text" name="url" placeholder="http地址">
    <input type="submit" class="u-button" value="提交下载">
</form>
<a href="/clearFinishDownload.html">清理已经下载完成的任务列表</a>
<table style="margin: auto"  class="hover-line">
    <tr>
        <th style="max-width: 10rem">url</th>
        <th>保存路径</th>
        <th>下载进度</th>
        <th>状态</th>
        <th>创建时间</th>
        <th>更新时间</th>
    </tr>
    <#list downloadList! as download>
        <tr>
            <td style="max-width: 15rem;word-break: break-all;">${download.url}</td>
            <td>
                <a href="?currPath=${currPath}&path=${download.savePath}">${download.savePath}</a>
            </td>
            <td>${download.progressSize}</td>
            <td>
                <#switch download.status >
                    <#case 1>初始化
                        <#break>
                    <#case 2>进行中
                        <#break>
                    <#case 3>已完成
                        <#break>
                    <#case 4>错误
                        <#break>
                </#switch>
            </td>
            <td>${download.createAt?string("yyyy-MM-dd HH:mm:ss")}</td>
            <td>${download.updateAt?string("yyyy-MM-dd HH:mm:ss")}</td>
        </tr>
    </#list>
</table>
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

<table style="margin: auto"  class="hover-line">
    <tr>
        <th>序号</th>
        <th>文件名</th>
        <th>大小</th>
        <th>最后修改时间</th>
        <th>url</th>
        <th>操作</th>
    </tr>
<#list fileList! as file>
    <tr>
        <td>${file?index}</td>
        <td>
            <#if (file.directory)>
            📁<a href="?currPath=${currPath}&path=${file.name}">${file.name!""}</a>
            <#else>
            ${file.name!""}
            </#if>
        </td>
        <td>
            ${file.length()}
        </td>
        <td>
            ${file.lastModified()?number_to_datetime?string("yyyy/MM/dd HH:mm:ss")}
        </td>
        <td>
            <#if (file.directory)>
                -
            <#else>
                <a href="/oss/${currPath}/${file.name}">${file.name}</a>
            </#if>
        </td>
        <td>
            <form action="/delFile"  method="POST" enctype="multipart/form-data" onsubmit="return confirm('是否删除？')">
                <input type="hidden" name="path" value="${currPath}/${file.name}">
                <input type="submit" class="u-button" value="删除">
            </form>
        </td>
    </tr>
</#list>
</table>
</body>
</html>