<!DOCTYPE HTML>
<html lang="en">

<#include "../comps/head.ftl"/>
<style>
    html,body{
        margin: 0;
    }
    .u-audio{
        height: 50px;
        padding: 10px;
    }
    .u-content{
        height: calc(100vh - 100px);
        overflow: auto;
        padding: 10px;
        text-align: left;
    }
</style>
<body>
<script src="/libs/vue/vue.min.js"></script>

<div id="app">
    <div class="u-content">
        <h3>taskId:${taskId!'空'}</h3>
        <hr>

        <span v-for="(se,idx) of sentences"
                  @click="gotoTime(se)"
                  :style="(se.BeginTime < currentTime && se.EndTime > currentTime) ? 'background:yellow':''">
                <template v-if="idx != 0">
                    <p v-if="se.BeginTime - sentences[idx-1].EndTime > duration">
                </template>
                {{se.Text}}

            </span>
    </div>
    <div class="u-audio">
        <audio ref="audio"
               src="/oss/${audioUrl}"
               style="width: 100%;"
               controls
               @timeupdate="audiotimeupdate"></audio>
    </div>

</div>
<input id="resultJson" type="hidden" value="${resultJson!''}">
<script src="/libs/zepto/zepto.1.2.min.js"></script>
<script src="/admin/audioTran.js"></script>
</body>
</html>
