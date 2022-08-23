<!DOCTYPE HTML>
<html lang="en">
<#include "../comps/head.ftl"/>
<body>
<#include "../comps/adminLink.ftl"/>

<hr>

<div style="max-width: 50rem;margin: auto;padding-bottom: 2rem">

    <form id="savePageForm">
        <input type="hidden" name="id" value="${(pageEntity.id)!''}"/>

        <div class="inline-left">
            <span>标题：</span>
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
            <div id="tagList" style="display: inline-block">
                <#list tagEntities! as tagEntity>
                    <label>
                        <input type="checkbox" <#if (tagIdList?seq_contains(tagEntity.id))!false>checked</#if>
                               name="tags"
                               value="${tagEntity.id}">
                        ${tagEntity.tag}
                    </label>
                </#list>
            </div>

            <input id="newTag" type="text" placeholder="新增标签名称">
            <input class="u-button" type="button" id="addTagBtn" value="新增标签">
        </div>
        <div class="inline-left">
            <span>配图:</span>
            <img id="bgImg" src="${(pageEntity.bgUrl)!''}" style="width: 200px;"/>
            <input id="bgUrl" placeholder="http开头地址，或点击unsplash选择" name="bgUrl" type="text" value="${(pageEntity.bgUrl)!''}">
            <input id="unsplashBtn" class="u-button" type="button" value="unsplash">
            <div id="unsplashList" style="display: none;border: 1px solid;padding:5px"></div>
        </div>

        <div id="editor" style="text-align:left">
            ${(pageEntity.content)!''}
        </div>
        <br>
        <input id="savePageBtn" class="u-button" type="button" value="确认发表">
    </form>
</div>
<script src="/libs/tinymce/tinymce.min.js"></script>
<script src="https://zeptojs.com/zepto.min.js"></script>
<script src="/admin/editPageContent.js"></script>
</body>
</html>