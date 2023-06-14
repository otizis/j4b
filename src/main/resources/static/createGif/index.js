var area_len = 0.5;// 设置线条长度  0~1范围

var width = 240;
var height = 100;
var initFontSize = 5;
var font_size = initFontSize;


var canvas = document.getElementById('tutorial');
canvas.width = width;
canvas.height = height;
var ctx = canvas.getContext('2d');


var arr = [];//三角形坐标参数
// 文字列表
var strArr,gif,strArrIdx,interval,firstStrImage;
var oneStrTime = 1000;
var intervalDur = 30;
// 是否需要简图
var needFirstImage = false;

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

var fontColorMode = 0;
function switchFontColorMode(mode){
    fontColorMode = mode;
}

var textMode = 0;
function switchTextMode(mode){
    textMode = mode;
}

function heightChangeListener(event){
    console.log(event.target,event.target.value)
    height=event.target.value*1
    canvas.height = height;
    document.getElementById("heightInput").innerText = height
}

function bgFileListener(){
    
}
function clean(){
    clearCtx()
}

function createGif(event) {
    event.target.textContent="生成中..."
    canvas.style.display="unset"
    strArrIdx = 0;
    rollAngle = 0;
    xRunLenght = 0
    strArr=[]
    firstStrImage=null;
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
        width: width,
        height: height,
        workers: 1,
        quality: 50,
        workerScript: './worker.js',
        transparent:  'rgba(0,0,0,0)'
    });
    gif.on('finished', function (blob) {
        canvas.style.display="none"
        console.log("end..")
        var reader = new FileReader();
        reader.onload = function(event){
            console.log(event.target.result)
            var result = document.createElement('div')
            var img  = document.createElement("img")
            img.src = event.target.result
            result.append(img)

            var span  = document.createElement("span")
            span.innerText = "👈长按图片保存"
            result.append(span)

            if(needFirstImage){
                var img2  = document.createElement("img")
                img2.src = firstStrImage
                result.append(img2)
            }
            var resultDom = document.getElementById("result")
            resultDom.append(result)

        }; // data url!
        reader.readAsDataURL(blob);

        event.target.textContent="开始生成"


    });
    interval = setInterval(function () {
        clearCtx()

        // drawAllTriangleArr();
        let result = {}
        if(textMode == 0 ){
            result = drawText();
        }else if (textMode == 1){
            result = drawTextRoll();
        }else if (textMode == 2){
            result = drawTextRadiate();
        }else if (textMode == 3){
            result = drawTextAroundHead();
        }

        if(needFirstImage
            &&!firstStrImage
            && strArrIdx === 0
            && result.strWidth > width*0.9){
            // 第一帧最后
            firstStrImage = canvas.toDataURL();
        }
        gif.addFrame(ctx, { copy: true, delay: intervalDur });
        if (result.end) {
            clearInterval(interval);
            console.log("clearInterval interval")
            gif.render();
        }
    }, intervalDur);
}


/**
 * 画一个三角尖尖
 * @param x1 {Number}
 * @param y1 asdfasdfadsf
 */
function drawTriangle(x1, y1, x2, y2, r) {
    var mid = { x: width / 2, y: height / 2 };
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
    // ctx.fillStyle = "#00a2e8"
    // ctx.rect(0, 0, width, height);
    // ctx.fill();
}
/**
 * 画一次图
 */
function drawAllTriangleArr() {
    createArr();
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
    let result = {str:"",strWidth:0,end:false}
    var mid = { x: width / 2, y: height / 2 };
    ctx.font = font_size + "px  '自定义字体'"
    ctx.fillStyle = nextColor();
    ctx.textAlign = "center"
    ctx.textBaseline = "middle"
    let str = strArr[(strArrIdx) % strArr.length];
    result.str = str
    ctx.fillText(str, mid.x, mid.y)

    var metrics = ctx.measureText(str);
    result.strWidth = metrics.width
    var maxFontSize = font_size * width * 1.2 / metrics.width
    let stepFontSize = (maxFontSize - initFontSize) / (oneStrTime/intervalDur);
    font_size += stepFontSize;

    if (metrics.width > width * 1.2) {
        font_size = initFontSize;
        strArrIdx++;
    }
    console.log("strArrIdx",strArrIdx)
    if(strArrIdx === strArr.length){
        result.end=true
    }
    return result;
}


var rollAngle = 0;
function drawTextRoll() {
    let result = {str:"",strWidth:0,end:false}
    var mid = { x: width / 2, y: height / 2 };
    ctx.fillStyle = nextColor();

    ctx.textAlign = "center"
    ctx.textBaseline = "middle"
    strArr.forEach((str,index)=>{
        ctx.font = "16px  '自定义字体'"
        var metrics = ctx.measureText(str);
        var maxFontSize = 16 * width * 0.9 / metrics.width
        let angle = index * Math.PI * 2 / strArr.length
        angle += rollAngle
        ctx.font = maxFontSize*((1 + Math.cos(angle))/2) + "px  '自定义字体'"

        
        ctx.fillText(str, mid.x, mid.y - (Math.sin(angle)*height/2))
    })
    rollAngle-= (Math.PI * 3 / strArr.length) / (oneStrTime/intervalDur)
    console.log("rollAngle",rollAngle)
    if(rollAngle <  - Math.PI * 2){
        result.end=true
    }
    return result;
}

var xRunLenght = 0;
function drawTextRadiate() {
    let result = {str:"",strWidth:0,end:false}

    ctx.fillStyle = nextColor();

    ctx.textAlign = "center"
    ctx.textBaseline = "middle"
    let offest = 0

    strArr.join("").split("").forEach((str,index)=>{
        var maxFontSize = height/3
        let angle =  (Math.PI / 3) * index
        angle += rollAngle
        ctx.font = (maxFontSize/3) + maxFontSize*((1 + Math.cos(angle))/2) + "px  '自定义字体'"
        ctx.fillText(str,  width*0.6 - offest + xRunLenght, height / 2 - (Math.sin(angle)*height/3))

        offest += maxFontSize*0.8
    })

    xRunLenght+=2

    rollAngle-= (Math.PI ) / (oneStrTime/intervalDur)

    if(xRunLenght > offest){
        result.end=true
    }
    return result;
}


function drawTextAroundHead() {
    let result = {str:"",strWidth:0,end:false}
    var mid = { x: width / 2, y: height / 2 };
    ctx.fillStyle = nextColor();

    ctx.textAlign = "center"
    ctx.textBaseline = "middle"
    strArr.forEach((str,index)=>{
        ctx.font = "16px  '自定义字体'"
        var metrics = ctx.measureText(str);
        var maxFontSize = 16 * width * 0.8 / metrics.width
        let angle = index * Math.PI * 2 / strArr.length
        angle += rollAngle
        ctx.font = maxFontSize*((1 + Math.cos(angle))/2) + "px  '自定义字体'"

        
        ctx.fillText(str, mid.x - (Math.sin(angle)*width/2) , mid.y - Math.abs(Math.sin(angle)*height/3))
    })
    rollAngle-= (Math.PI * 3 / strArr.length) / (oneStrTime/intervalDur)
    console.log("rollAngle",rollAngle)
    if(rollAngle <  - Math.PI * 2){
        result.end=true
    }
    return result;
}

/**
 *
 * @returns
 */
function nextColor() {
    if(fontColorMode == 1){
        return "#" + (Math.random()*16777215).toString(16).replace(".","").substr(0,6);
    }
    return "#333"
}


