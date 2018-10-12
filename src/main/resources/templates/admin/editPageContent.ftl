<!DOCTYPE HTML>
<html lang="en">
<#include "../comps/head.ftl"/>
<body>
<#include "../comps/adminLink.ftl"/>

<hr>

<div style="max-width: 50rem;margin: auto">

    <form id="savePageForm">
        <input type="hidden" name="id" value="${(pageEntity.id)!''}"/>

        <div class="inline-left">
            <span> 标题：</span>
            <label >
               <input type="text" style="width: 40rem" name="title" value="${(pageEntity.title)!''}"/>
            </label>
        </div>

        <div class="inline-left">
            <span>状态:</span>
            <#assign foo=(((pageEntity.status)!1) == 1) />
            <label>
                <input type="radio" <#if foo>checked</#if> name="status" value="1"> 上线
            </label>
            <label >
                <input type="radio" <#if !foo>checked</#if> name="status" value="0"> 屏蔽
            </label>
        </div>

        <div class="inline-left">
            <span>标签:</span>
            <#list tagEntities! as tagEntity>
                <label >
                    <input type="checkbox"  <#if (tagIdList?seq_contains(tagEntity.id))!false>checked</#if>
                           name="tags"
                           value="${tagEntity.id}">
                    ${tagEntity.tag}
                </label>
            </#list>
            <input id="newTag" ><input type="button" id="addTagBtn" value="新建标签">
        </div>

        <div id="editor" style="text-align:left">
            ${(pageEntity.content)!''}
        </div>
        <input id="savePageBtn" type="button" value="提交">
    </form>
</div>
<script src="/libs/tinymce/tinymce.min.js"></script>
<script src="https://zeptojs.com/zepto.min.js"></script>
<script src="/admin/editPageContent.js"></script>
</body>
</html>