<!DOCTYPE HTML>
<html lang="en">

<#include "./comps/head.ftl"/>

<body>
<header>

    <div class="u-logo">
        <img src="${blogInfo.logoUrl!"/logo.jpg"} "alt="">
    </div>

    <h2>${blogInfo.title!"j4b"}</h2>

    <div class="u-b-desc">
        <p>${blogInfo.desc!"j4b desc"}</p>
    </div>

</header>

<#include "./comps/link.ftl"/>

<div class="container">
   页面丢了，<a href="/">回首页看看吧</a>
</div>

</body>
</html>