<!DOCTYPE HTML>
<html lang="en">

<#include "../comps/head.ftl"/>

<body>
<script src="/libs/vue/vue.min.js"></script>

<h3>${taskId!'任务为空'}</h3>
<hr>
<div id="app">
    <audio ref="audio" src="/oss/${audioUrl}"
           style="width: 100%;"
           controls
           @timeupdate="audiotimeupdate"></audio>
    <div>
            <span v-for="se of sentences"
                  @click="gotoTime(se)"
                  :style="(se.BeginTime < currentTime && se.EndTime > currentTime) ? 'background:yellow':''">
             {{se.Text}}
         </span>
    </div>

</div>
<input id="resultJson" type="hidden" value="${resultJson!''}">
<script src="/libs/zepto/zepto.1.2.min.js"></script>
<script src="/admin/audioTran.js"></script>
</body>
</html>
