// ==UserScript==
// @name extract
// @namespace http://jaxer.cc/
// @version 0.9.4
// @require http://jaxer.cc/libs/zepto/zepto.1.2.min.js
// @description 网页图片，文本，视频 等发送到服务器记录
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
    // 发送剪切板图片
    GM_registerMenuCommand('发送剪切板图片', (e) => {
        if ($("._extract_preview.expend").length !== 0) { return }
        //console.log(e)
        _extract.params = {
            "type": 4,
            "title": "剪切板",
            "content": "",
            "sourceUrl": `http://${_extract.domain}`,
            "memo": ""
        }
        appendDialog(`<div id="pasteDiv" contenteditable="">图片粘贴到这里</div> `)
        $("._extract_preview").toggleClass("expend")
        $("#pasteDiv").on("paste", function (event) {
            if (event.clipboardData || event.originalEvent) {
                let clipboardData = (event.clipboardData || event.originalEvent.clipboardData);
                if (clipboardData.items) {
                    let blob;
                    for (let i = 0; i < clipboardData.items.length; i++) {
                        if (clipboardData.items[i].type.indexOf("image") !== -1) {
                            blob = clipboardData.items[i].getAsFile();
                        }
                    }
                    let render = new FileReader();
                    render.onload = function (evt) {
                        //输出base64编码
                        _extract.params.content = evt.target.result;
                    }
                    if (blob) {
                        render.readAsDataURL(blob);
                    }
                }
            }
        })
    });


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
        appendDialog(`<div class="_ex_hide_before">${htmlEncode(_extract.params.title)}</div><div>${htmlEncode(_extract.params.sourceUrl)}</div>`)
        $("._extract_preview").toggleClass("expend")
    });

    GM_registerMenuCommand('查看记录', (e) => {
        GM_openInTab(`http://${_extract.domain}/extract.html`);
    });

    document.addEventListener("mouseup", (e) => {
        //console.log("enter mouseup",e)
        if (e.target.tagName === 'IMG') {
            var imageUrl = e.target.currentSrc
            _extract.params = {
                "type": 1,
                "title": htmlEncode(document.title),
                "content": imageUrl,
                "sourceUrl": location.href,
                "memo": ""
            }
            appendDialog(`<div><img style='max-height: 250px;max-width: 100%;' src='${imageUrl}'/></div>`)
            return
        }
        else if(e.target.tagName === 'VIDEO')
        {
            var videoUrl = e.target.currentSrc
            if(videoUrl.toLowerCase().startsWith("blob")){
                return
            }
            _extract.params = {
                "type": 5,
                "title": htmlEncode(document.title),
                "content": videoUrl,
                "sourceUrl": location.href,
                "memo": ""
            }
            appendDialog(`<div><video style='max-height: 250px;max-width: 100%;' controls muted="true"  src='${videoUrl}'/></div>`)
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
            appendDialog(`<h6 class="_ex_hide_before">${htmlEncode(_extract.params.title)}</h6><div class="_ex_hide_before" style='color:grey'>${_extract.params.sourceUrl}</div><div style='padding:15px 0'>${htmlEncode(_extract.params.content)}</div>`)
        }
    });

    $(document.body).on("click", function (e) {
        if ($("._extract_preview").hasClass("expend")) {
            $("._extract_preview").removeClass("expend")
        }
    })

    function appendDialog(html) {
        // 先清空
        $('._extract_preview').remove()

        $(document.body).append($(`<div class='_extract_preview'>
            <div class='_extract_top_bar'>
                <button class=" _ex_send" >发送</button>
                <button class="" id="_ex_close" style='float:right'>关闭</button>
            </div>
            <div class='_extract_content' style="">${html}</div>
            <div><textarea style='width:320px;padding:10px;' rows=2 id="_ex_memo" placeholder="输入备注" ></textarea>
                <div style='padding:10px 0'>
                    <label style='margin-right:5px'><input type="radio" value="1" name="_extract_status">默认</label>
                    <label style='margin-right:5px'><input type="radio" value="2"  name="_extract_status">待办事项</label>
                    <label style='margin-right:5px'><input type="radio" value="10"  name="_extract_status">私有</label>
                </div><button class=" _ex_send" style='width:320px'>发送</button></div>
        </div>`))
        $('._extract_preview')
            .on("click", function (e) { e.stopPropagation() })
            .on("mouseup", function (e) { e.stopPropagation() })
            .on("click", "#_ex_close", function () { $('._extract_preview').remove() })
            .on("click", "._ex_send", function () {
                // 发送
                _extract.params.memo = $('#_ex_memo').val();
                _extract.params.status = $('input[name="_extract_status"]:checked').val();
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
                            $('._extract_preview').html('<p class="_extract_msg"> 发送成功！</p>');
                            setTimeout(function () { $('._extract_preview').remove() }, 1000)
                        } else {
                            $('._extract_preview ._ex_send').removeProp('disabled')
                            alert(resp.msg||'faild')
                        }
                    }
                });
            })
            .on("click", "._extract_content", function (e) {
                $("._extract_preview").addClass("expend")
                e.stopPropagation()
            })
    }


    GM_addStyle(`
._extract_preview{
    position:fixed;
    left:-55px;
    bottom:15px;
    width:90px;
    height:90px;
    background:white;
    text-align:left;
    z-index:9999999999;
    box-shadow: #888888 0 2px 5px 0px;
    overflow:hidden;
    transition: all 0.3s;
    border-radius: 15px;
    font-size:14px;
}
._extract_preview:hover{
    left:0;
}
._extract_preview.expend{
    left:25%;
    width:450px;
    height:410px;
    overflow:auto;
    padding:15px
}
._extract_top_bar{
    height:0;
    overflow:hidden;
    transition: all 0.25s;
}
._extract_preview:hover  ._extract_top_bar,
._extract_preview.expend ._extract_top_bar{
    height:25px
}
._extract_preview.expend ._extract_top_bar ._ex_send{
    display:none
}
._extract_preview ._ex_hide_before{
    display:none
}
._extract_preview.expend ._ex_hide_before{
    display:unset
}
._extract_content{
    clear:both;
    width:100%;
    max-height:250px;
    cursor: pointer;
    overflow:hidden;
    margin-bottom:10px;
}
.expend  ._extract_content{
    overflow:auto;
}
._extract_preview button{
    width:45px;
    height:25px;
    border: 1px solid #409eff;
    background: white;
    border-radius: 5px;
    cursor: pointer;
    color: #409eff;
    margin: 0;
    padding: 0;
}
._extract_preview button:hover{
    background: #409eff;
    color:white
}
._extract_preview #pasteDiv{
    width: 350px;
    min-height: 75px;
    border: 1px solid #1890ff;
    overflow: auto;
    max-height: 190px;
}
._extract_preview #pasteDiv img{
    width:100%
}
._extract_preview ._extract_msg{
    background: #2ca339;
    padding: 10px 10px 0 60px;
    color: white;
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
