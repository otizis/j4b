function downloadAll() 
{
    var imgs = document.getElementById("result").getElementsByTagName("img")
    for (let index = 0; index < imgs.length; index++) 
    {
        let aLink = document.createElement('a');
        let blob = base64ToBlob(imgs[index].src); //new Blob([content]);
        let evt = document.createEvent("HTMLEvents");
        evt.initEvent("click", true, true); //initEvent 不加后两个参数在FF下会报错  事件类型，是否冒泡，是否阻止浏览器的默认行为
        aLink.download = (index+1)+"."+blob.type.split("/").pop();
        aLink.href = URL.createObjectURL(blob);
        aLink.click();
    }
};
function base64ToBlob(code) {
    let parts = code.split(';base64,');
    let contentType = parts[0].split(':')[1];
    let raw = window.atob(parts[1]);
    let rawLength = raw.length;
    let uInt8Array = new Uint8Array(rawLength);
    for(let i = 0; i < rawLength; ++i) {
        uInt8Array[i] = raw.charCodeAt(i);
    }
    return new Blob([uInt8Array], {
        type: contentType
    });
};
function switchCreateFirstImg(mode){
    needFirstImage = mode===1
}
function setFontFamily(){

    var fontUrl = document.getElementById("fontUrl").value;
    console.log("fonturl",fontUrl)
    const font = new FontFace("自定义字体", `url(${fontUrl})`)
    font.load().then(()=>{
        document.fonts.add(font)
    }).catch(e=>{
        alert("字体加载失败" + e)
    })
}