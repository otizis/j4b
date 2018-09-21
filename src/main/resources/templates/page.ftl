<!DOCTYPE HTML>
<html lang="en">
<#include "./comps/head.ftl"/>
<body>

<header>
    <h3>${page.title!}</h3>
    <div>${page.createAt?string("yyyy-MM-dd HH:mm")}</div>
</header>

<#include "./comps/link.ftl"/>

<div class="container">
    <div class="u-page">
    ${page.content}
    </div>

</div>

<#include "./comps/foot.ftl"/>
</body>
</html>