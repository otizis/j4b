<!DOCTYPE HTML>
<html lang="en">
<#--
 # Format Number of Bytes in SI Units
 # -->
<#function si num>
    <#assign order     = num?round?c?length />
    <#assign thousands = ((order - 1) / 3)?floor />
    <#if (thousands < 0)><#assign thousands = 0 /></#if>
    <#assign siMap = [ {"factor": 1, "unit": ""}, {"factor": 1000, "unit": "K"}, {"factor": 1000000, "unit": "M"}, {"factor": 1000000000, "unit":"G"}, {"factor": 1000000000000, "unit": "T"} ]/>
    <#assign siStr = (num / (siMap[thousands].factor))?string("0.#") + siMap[thousands].unit />
    <#return siStr />
</#function>

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
            ${si(file.length())}
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
            <#if file.name?ends_with(".mp3") || file.name?ends_with(".wav") || file.name?ends_with(".m4a")>
                <form action="/tranText"  method="POST" enctype="multipart/form-data" onsubmit="return confirm('是否识别音频？')">
                    <input type="hidden" name="path" value="${currPath}/${file.name}">
                    <input type="submit" class="u-button" value="识别">
                </form>
            </#if>

        </td>
    </tr>
</#list>
</table>

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
            <td>${si(download.progressSize)} / ${si(download.fileSize)}</td>
            <td style="font-weight: 900">
                <#switch download.status >
                    <#case 1>初始化
                        <#break>
                    <#case 2><span style="color: #0086f5">进行中..</span>
                        <#break>
                    <#case 3> <span style="color: green">已完成</span>
                        <#break>
                    <#case 4><span style="color: red">错误</span>
                        <#break>
                </#switch>
            </td>
            <td>${download.createAt?string("yyyy-MM-dd HH:mm:ss")}</td>
            <td>${download.updateAt?string("yyyy-MM-dd HH:mm:ss")}</td>
        </tr>
    </#list>
</table>
</body>
</html>
