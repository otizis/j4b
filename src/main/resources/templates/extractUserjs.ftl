// ==UserScript==
// @name extract
// @namespace http://jaxer.cc/
// @version 0.7.2
// @require http://jaxer.cc/libs/zepto/zepto.1.2.min.js
// @description 网页图片，文本等发送到服务器记录
// @author jaxer
// @match *://*/*
// @icon http://jaxer.cc/oss/201810284/5530a058-6cd2-4a36-bd43-a8adbdabb6da.png
// @grant GM_registerMenuCommand
// @grant GM_xmlhttpRequest
// @grant GM_addStyle
// @grant GM_openInTab
// ==/UserScript==

(function () {
'use strict';
var _extract = {
    params: null,
    auth: '${apiAuth}',
    domain: '${domain}',
}
<#noparse>
// 全局右键
GM_registerMenuCommand('记录本网页', (e) => {
    if ($("._extract_preview.expend").length !== 0) { return }
    //console.log(e)
    _extract.params = {
        "type": 2,
        "title": htmlEncode(document.title),
        "content": htmlEncode(document.title),
        "sourceUrl": location.href,
        "memo": ""
    }
    $('._extract_preview').remove()
    appendDialog(`<div>${htmlEncode(_extract.params.title)}</div><div>${htmlEncode(_extract.params.sourceUrl)}</div>`)
    $("._extract_preview").toggleClass("expend")
});

    GM_registerMenuCommand('查看记录', (e) => {
        GM_openInTab(`http://${_extract.domain}/extract.html`);
    });

document.addEventListener("mouseup", (e) => {
    //console.log("enter mouseup",e)
    if ($("._extract_preview").length !== 0 && $.contains($("._extract_preview")[0], e.target)) {
        // 内部点击不算
        return
    }

    if ($("._extract_preview.expend").length !== 0) { return }
    if (e.target.tagName === 'IMG') {
        var imageUrl = e.target.currentSrc
        _extract.params = {
            "type": 1,
            "title": htmlEncode(document.title),
            "content": imageUrl,
            "sourceUrl": location.href,
            "memo": ""
        }
        appendDialog(`<div><img style='height:15rem' src='${imageUrl}'/></div>`)
        return
    }
    var selected = window.getSelection();
    var selecteStr = selected.toString();
    if (selecteStr !== '') {
        _extract.params = {
            "type": 3,
            "title": htmlEncode(document.title),
            "content": htmlEncode(selecteStr),
            "sourceUrl": location.href,
            "memo": ""
        }
        $('._extract_preview').remove()
        appendDialog(`<h6>${htmlEncode(_extract.params.title)}</h6><div style='color:grey'>${_extract.params.sourceUrl}</div><div style='padding:1rem 0'>${htmlEncode(_extract.params.content)}</div>`)
    }
});

    $(document.body).on("click", "#_ex_close", function () { $('._extract_preview').remove() })

    $(document.body).on("click", "._extract_content", function () { $("._extract_preview").toggleClass("expend") })
    // 发送
    $(document.body).on("click", "._ex_send", function () {
        _extract.params.memo = $('#_ex_memo').val();
        _extract.params.status = $('input[name="_extract_"]:checked').val();
        //console.log(_extract.params)

        GM_xmlhttpRequest({
            method: "POST",
            url: `http://${_extract.domain}/extract/add`,
            headers: {
                "Content-Type": "application/json",
                "auth": _extract.auth
            },
            data: JSON.stringify(_extract.params),
            onloadstart: function () {
                $("._extract_preview").removeClass("expend")
                $('._extract_preview ._ex_send').prop('disabled', 'disabled')
            },
            onload: function (response) {
                //console.log(response.responseText);
                var resp = JSON.parse(response.responseText)
                if (resp.code === 0) {
                    // 成功
                    $('._extract_preview').html('发送成功').hide('slow',function(){$(this).remove()})
                } else {
                    $('._extract_preview ._ex_send').removeProp('disabled')
                    alert('faild')
                }
            }
        });
    })
    function appendDialog(html) {
        $(document.body).append($(`<div class='_extract_preview'>
                                        <button class="_ex_send" >发送</button>
                                        <button id="_ex_close" style='float:right'>关闭</button>
                                        <div class='_extract_content' style="">${html}</div>
                                        <div><textarea style='width:28rem;padding:0.5rem' rows=2 id="_ex_memo" placeholder="输入备注" ></textarea>
                                            <br>
                                            <label>默认<input type="radio" value="1" name="_extract_status"></label>
                                            <label>私有<input type="radio" value="10"  name="_extract_status"></label>
                                            <br/><button class="_ex_send" style='width:28rem'>发送</button></div>
                                    </div>`))
    }

    GM_addStyle(`
    ._extract_preview{
        position:fixed;
        left:1rem;
        bottom:1rem;
        width:6rem;
        height:6rem;
        background:white;
        text-align:left;
        z-index:9999999999;
        box-shadow: #888888 2px 2px 5px 0px;
        overflow:hidden;
        transition: all 0.5s;
    }
    ._extract_preview.expend{
        left:25%;
        width:30rem;
        height:23rem;
        overflow:auto;
        padding:1rem
    }
    ._extract_content{
        clear:both;
        width:100%;
        height:15rem;
        cursor: pointer;
        overflow:hidden;
    }
    .expend  ._extract_content{
        overflow:auto;
    }
    ._extract_preview button{
        width:3rem;
        height:1.5rem;
        border: 1px solid grey;
        background: lightgrey;
    }
    `)

    function htmlEncode(html) {
        var s = "";
        if (html.length == 0) return "";
        s = html.replace(/&/g, "&amp;");
        s = s.replace(/</g, "&lt;");
        s = s.replace(/>/g, "&gt;");

        s = s.replace(/\'/g, "&#39;");
        s = s.replace(/\"/g, "&quot;");
        s = s.replace(/\n/g, "<br/>");
        return $.trim(s);
    }

})();
</#noparse>