<!DOCTYPE HTML>
<html lang="en">

<#include "./comps/head.ftl"/>

<body>
<a href="/index.html">首页</a>
<a href="/editPage.html">文章编辑</a>

<hr>

<table style="margin: auto">
    <#list configList! as config>
        <tr>
            <td>${config.code!""}</td>
            <td>${config.v!""}</td>
            <td><input class="updateConfig"
             type="button" data-c="${config.code!''}" value="修改"></td>
        </tr>
    </#list>
</table>
<hr>
<div>
    <h5>新增或修改配置</h5>
    <form id="addConfig">
        <label >
            code：<input type="text" name="code"/>
        </label>
        <label >
            value：<input type="text" name="v"/>
        </label>
        <input id="addConfigBtn" type="button" value="提交">
    </form>
</div>



<script src="https://zeptojs.com/zepto.min.js"></script>
<script src="/admin/config.js"></script>
</body>
</html>