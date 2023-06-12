var area_len = 0.5;// 设置线条长度  0~1范围

var s_width = 240;
var s_height = 100;
var initFontSize = 5;
var font_size = initFontSize;


var color = 0;
var canvas = document.getElementById('tutorial');
canvas.width = s_width;
canvas.height = s_height;
var ctx = canvas.getContext('2d');
var width = canvas.width;
var height = canvas.height;
var mid = { x: width / 2, y: height / 2 };
var arr = [];//三角形坐标参数
var strStep = 0;// 一个字串 从最小到最大的 步骤
// 文字列表
var strArr,gif,strArrIdx,interval,strStep;
var oneStrTime = 1000;
var intervalDur = 30;

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
var fontColorMode = false;
function switchFontColorMode(event){
    fontColorMode = !fontColorMode;
    console.log(event)
    if(fontColorMode){
        event.target.textContent = "切换字色纯黑"
    }else{
        event.target.textContent = "切换字色闪彩"
    }
}
function clean(){
    clearCtx()
    var img = document.getElementById('result')
    img.src = ""
}

function createGif() {
    strArrIdx = 0;
    strArr=[]
    let tmpStrArr = document.getElementById("textarea").value.split("\n");
    tmpStrArr.forEach(element => {
        if(element){
            strArr.push(element)
        }
    });
    if(strArr.length === 0){
        strArr.push()
    }
    console.log(strArr)
    gif = new GIF({
        width: s_width,
        height: s_height,
        workers: 2,
        quality: 50,
        workerScript: './worker.js',
        transparent:  'rgba(0,0,0,0)'
    });
    gif.on('finished', function (blob) {
        console.log("end..")
        var img = document.getElementById('result')
        img.src = URL.createObjectURL(blob)
    });
    interval = setInterval(function () {
        createArr();
        clearCtx()
         // drawAllTriangleArr();
        drawText();
        gif.addFrame(ctx, { copy: true, delay: intervalDur });
    }, intervalDur);
}


/**
 * 画一个三角尖尖
 * @param x1 {Number}
 * @param y1 asdfasdfadsf
 */
function drawTriangle(x1, y1, x2, y2, r) {
    ctx.fillStyle = nextColor();
    // console.log(ctx.fillStyle)
    var center = { x: (x1 + x2) / 2, y: (y1 + y2) / 2 };
    r = r || Math.random() * 0.5;
    var x = center.x + (mid.x - center.x) * r;
    var y = center.y + (mid.y - center.y) * r
    ctx.beginPath();
    ctx.moveTo(x1, y1);
    ctx.lineTo(x2, y2);
    ctx.lineTo(x, y);
    ctx.fill();
}
function clearCtx(){
    ctx.clearRect(0, 0, width, height)
}
/**
 * 画一次图
 */
function drawAllTriangleArr() {
    var size = arr.length;
    for (var i = 0; i < size; i++) {
        drawTriangle(arr[i].x1, arr[i].y1, arr[i].x2, arr[i].y2, arr[i].r)
    }
}
/**
 * 创建轮廓延边的三角尖尖参数
 */
function createArr() {
    arr = [];
    var bottom = 10;
    /**上下两侧**/
    var tmp_x = 0;
    while (tmp_x < width) {
        var step = Math.round(Math.random() * bottom);
        arr.push({ x1: tmp_x, y1: 0, x2: tmp_x + step, y2: 0, r: Math.random() * area_len })
        step = Math.round(Math.random() * bottom);
        arr.push({ x1: tmp_x, y1: height, x2: tmp_x + step, y2: height, r: Math.random() * area_len })
        tmp_x += bottom;
    }
    /**左右两侧**/
    var tmp_y = 0;
    while (tmp_y < height) {
        var step = Math.round(Math.random() * bottom);
        arr.push({ x1: 0, y1: tmp_y, x2: 0, y2: tmp_y + step, r: Math.random() * area_len * 0.8 })
        step = Math.round(Math.random() * bottom);
        arr.push({ x1: width, y1: tmp_y, x2: width, y2: tmp_y + step, r: Math.random() * area_len* 0.8 })
        tmp_y += bottom;
    }
}



function drawText() {

    ctx.font = font_size + "px  '自定义字体'"
    ctx.fillStyle = nextColor();
    ctx.textAlign = "center"
    ctx.textBaseline = "middle"
    let str = strArr[(strArrIdx) % strArr.length];
    ctx.fillText(str, mid.x, mid.y)
    
    let maxFontSize = width * 1.2 / str.length
    let stepFontSize = (maxFontSize - initFontSize) / (oneStrTime/intervalDur);
    font_size += stepFontSize;
    if (font_size  > maxFontSize) {
        font_size = initFontSize;
        initFontSize = 5;
        strArrIdx++;
        strStep = 0
    }
    console.log("strArrIdx",strArrIdx)
    if (strArrIdx === strArr.length) {
        clearInterval(interval);
        console.log("clearInterval interval")
        gif.render();
    }
    // if (font_size > (width * 1.5) * 0.5) {
    //     ctx.font = initFontSize + "px  \"自定义字体\" sans-serif"
    //     initFontSize *= 1.15
    //     ctx.fillText(strArr[(strArrIdx + 1) % strArr.length], mid.x, mid.y)
    // }

}
/**
 * 
 * @returns 
 */
function nextColor() {
    if(fontColorMode){
        return "#" + (Math.random()*16777215).toString(16).replace(".","").substr(0,6);
    }
    return "#333"
}


