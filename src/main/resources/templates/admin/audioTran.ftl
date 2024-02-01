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
        background-color: #f5f5f5;
    }
    .u-line{
        padding: 5px;
    }
    .u-line-1 .u-speaker{
        background-color: red;
    }
    .u-line-2 .u-speaker{
        background-color: blue;
    }
    .u-line-3 .u-speaker{
        background-color: green;
    }
    .u-line-4 .u-speaker{
        background-color: yellow;
    }
    .u-line-4 .u-speaker{
        background-color: black;
    }
    .u-speaker{
        display: inline-block;
        height: 30px;
        width: 30px;
        border: 1px solid #e9e9e9;
        border-radius: 5px;
        font-size: 24px;
        vertical-align: top;
        text-align: center;
    }
    .u-text{
        display: inline-block;
        max-width: 50%;
        vertical-align: top;
        border: 1px solid #e9e9e9;
        border-radius: 5px;
        background-color: white;
        padding: 5px;
    }
</style>
<body>
<script src="/libs/vue/vue.min.js"></script>

<div id="app">
    <div class="u-content">
        <div>
            taskId:${taskId!'空'} ， message:${message!""}
        </div>
        <div>
            段落停顿间隔：<input style="width: 300px"
                          type="range"
                          v-model.number="duration"
                          max="10000"
                          min="1000"> {{duration}}ms
        </div>

        <hr>

        <div v-for="(se,idx) of sentences"
             @dblclick="gotoTime(se)"
             title="双击从这里播放"
             class="u-line"
             :class="'u-line-'+se.SpeakerId"
             :style="(se.BeginTime < currentTime && se.EndTime > currentTime) ? 'background:yellow':''">
            <div class="u-speaker" >&nbsp; </div> <div class="u-text">{{se.Text}}</div>
        </div>
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
