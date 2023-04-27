<!DOCTYPE HTML>
<html lang="en">
<#assign description>${blogInfo.title!},${blogInfo.desc!}</#assign>
<#assign keywords>${blogInfo.title!},${blogInfo.desc!}</#assign>
<#include "./comps/head.ftl"/>

<body>
<header>

    <div class="u-logo">
        <img src="${blogInfo.logoUrl!"/logo.jpg"} "alt="">
    </div>

    <h2>${blogInfo.title!"j4b"}</h2>

    <div class="u-b-desc">
        <p>${blogInfo.desc!"j4b desc"}</p>
        <div></div>
    </div>

    <style>
        .u-extract{
            border: #a4a4a4 1px solid;
            text-align: left;
            margin-bottom: 2rem;
        }
        .u-extract-action-bar{
            padding:0.5rem 1rem;
            border-bottom: #eceff1 1px solid;
        }
        .u-extract-source{
            border-bottom: #eceff1 1px solid;
            padding: 0.5rem 1rem;
            color: #333333;
            display: flex;
            justify-content: space-between;
        }
        .u-extract-source span{

        }
        .u-extract-img img{
            max-width: 100%;
            max-height: 50vh;
        }
        .u-extract-cnt{
            padding: 1rem;
            white-space: pre-wrap;
            word-break: break-word;
        }
        .u-extract-memo{
            border-top: #eceff1 1px solid;
            padding: 1rem;
            color: #9b9b9b;
        }
    </style>
</header>

<#include "./comps/link.ftl"/>

<div class="container">
    <@hasLogin>
        <div>
            <a href="?status=10">不公开列表</a>
            <a href="?status=0">已删除列表</a>
        </div>
    </@hasLogin>
    <#if page.records?size == 0>
        空
    </#if>
    <#list page.records! as page>
        <div class="u-extract" >
            <@hasLogin>
            <div class="u-extract-action-bar">
                <#if page.status == 1>
                    [正常]
                <#elseif page.type == 0>
                    [已删]
                <#elseif page.type == 10>
                    [不公开]
                </#if>
                <button class="u-button f-update-state" data-id="${page.id}" data-state="0">删除</button>
                <button class="u-button f-update-state" data-id="${page.id}" data-state="10">私有</button>
            </div>
            </@hasLogin>
            <div class="u-extract-source">
                <a href="${page.sourceUrl!'#'}" target="_blank">来源：${page.title!}</a>
                <span>${page.createAt?string('yyyy-MM-dd')}</span>
            </div>
            <#if page.type == 1>
                <div class="u-extract-img"><img src="${page.content!''}"></div>
            <#elseif page.type == 2>
            <#elseif page.type == 3>
                <div class="u-extract-cnt">${page.content!}</div>
            </#if>
            <div class="u-extract-memo">
                <span>${page.memo!}</span>
            </div>
        </div>

    </#list>
    <div class="u-pagition">
        <#if page.current gt  1>
            <a href="?pageNum=${page.current - 1}" target="_blank">上一页</a>
        </#if>
        <span>第${page.current}页 共${total}页</span>
        <#if page.current lt total>
            <a href="?pageNum=${page.current + 1}" target="_blank">下一页</a>
        </#if>
    </div>
</div>

<#include "./comps/foot.ftl"/>
</body>
<script src="/libs/zepto/zepto.1.2.min.js"></script>
<script>
    Zepto(function($){
        $('.f-update-state').on("click",function(){
            if(!confirm("确认删除？")){
                return
            }
            var jsonData={
                id: $(this).data("id"),
                status: $(this).data("state")
            }
            $.ajax({
                type:'POST',
                url:'/extract/updateStatus',
                contentType: 'application/json',
                data: JSON.stringify(jsonData),
                success: function (resp) {
                    if(resp.code === 0){
                        console.log(resp);
                        location.reload();
                    }
                    else{
                        window.alert(resp.msg||'error')
                    }
                }
            })
        })
    })
</script>
</html>