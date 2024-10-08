<!DOCTYPE HTML>
<html lang="en">

<#include "../comps/head.ftl"/>

<body>
<#include "../comps/adminLink.ftl"/>
<hr>

<table style="margin: auto" class="hover-line">
    <#list configList! as config>
        <tr>
            <td class="id-code">${config.code!""}</td>
            <td class="id-value">${config.v!""}</td>
            <td>
                <input class="u-button updateConfig"
                    type="button" value="修改">
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
        <div>
            <label >
                code：<input type="text" name="code" style="width:20rem;padding:0 8px"/>
            </label>
        </div>
        <div>
            <label >
                value：<textarea name="v" style="width:20rem;padding:3px 8px" rows="5"></textarea>
            </label>
        </div>

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
<hr>
<div  >
    <h5>油猴脚本-记录摘录-切勿公开别人可以往你服务器发消息</h5>
    api auth: <a href="/extract.user.js" >install user js</a>
</div>


<script src="/libs/zepto/zepto.1.2.min.js"></script>
<script src="/admin/config.js"></script>
</body>
</html>
