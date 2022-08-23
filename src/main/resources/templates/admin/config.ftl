<!DOCTYPE HTML>
<html lang="en">

<#include "../comps/head.ftl"/>

<body>
<#include "../comps/adminLink.ftl"/>
<hr>

<table style="margin: auto" class="hover-line">
    <#list configList! as config>
        <tr>
            <td>${config.code!""}</td>
            <td>${config.v!""}</td>
            <td>
                <input class="u-button updateConfig"
                    type="button" data-c="${config.code!''}" value="修改">
                    |
                <input class="u-button deleteConfig"
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
        <input class="u-button" id="addConfigBtn" type="button" value="提交">
    </form>
    <hr>
    <h6>支持的配置</h6>
    <table style="margin: auto">
    <#list supportConfigList! as config>
        <tr>
            <td>${config!""}</td>
            <td>${config.desc!""}</td>
        </tr>
    </#list>
    </table>
</div>



<script src="/libs/zepto/zepto.1.2.min.js"></script>
<script src="/admin/config.js"></script>
</body>
</html>