<!DOCTYPE HTML>
<html lang="en">

<#include "./comps/head.ftl"/>

<body>
<a href="/index.html">首页</a>

<hr>

<table style="margin: auto">
    <#list configList! as config>
        <tr>
            <td>${config.code!""}</td>
            <td>${config.v!""}</td>
            <td><input class="updateConfig" type="button" data-c="${config.code!""}" value="修改"></td>
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
<hr>
<div>
    <h5>新增文章</h5>

    <form id="addPageForm">
        <div>
            <label >
                标题：<input type="text" style="width: 40rem" name="title"/>
            </label>
        </div>
        <div id="editor"></div>
        <input id="addPageBtn" type="button" value="提交">
    </form>
</div>
<!-- 注意， 只需要引用 JS，无需引用任何 CSS ！！！-->
<script type="text/javascript" src="//unpkg.com/wangeditor/release/wangEditor.min.js"></script>
<script type="text/javascript">
    var E = window.wangEditor
    var editor = new E('#editor')
    editor.create()
</script>
<script src="https://zeptojs.com/zepto.min.js"></script>
<script src="/admin/config.js"></script>
</body>
</html>