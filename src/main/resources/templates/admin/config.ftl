<!DOCTYPE HTML>
<html lang="en">

<#include "../comps/head.ftl"/>

<body>
<#include "../comps/adminLink.ftl"/>
<hr>

<table style="margin: auto">
    <#list configList! as config>
        <tr>
            <td>${config.code!""}</td>
            <td>${config.v!""}</td>
            <td>
                <input class="updateConfig"
                    type="button" data-c="${config.code!''}" value="修改">
                    |
                <input class="deleteConfig"
                    type="button" data-c="${config.code!''}" value="删除">
             </td>
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