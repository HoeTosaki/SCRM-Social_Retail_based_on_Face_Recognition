/**
 * 页面初始化
 */
$(function () {
    Boxlayout.init();
});
document.mngrid = window.location.search.split('?')[1];
alert(document.mngrid);

/**
 * Vue初始绑定
 */
var recg = new Vue({
    el: '#recg',
    data: {
        hints: 'hints',
        inputuserid: '',
        stat: 'stat',
        userid: 'userid',
        username: 'username',
        recgImgData: ''
    },
    mounted: function () {
        hints = "请将人脸摆放在摄像头前方：";
        var opt = {
            audio: true,
            video: {
                width: 230,
                height: 230
            }
        };
        navigator.mediaDevices.getUserMedia(opt)
            .then(function (mediaStream) {
                var video = document.querySelector('video');
                video.srcObject = mediaStream;
                video.onloadedmetadata = function (e) {
                    video.play();
                };
            })
            .catch(function (err) {
                console.log(err.name + ": " + err.message);
            }); // always check for errors at the end.
    }
});

var counter = new Vue({
    el: "#counter",
    data: {
        userid: '',
        username: '',
        buylist: [],
        sumprc: 0,
        hints: '',
        goodsidCmd: ''
    },
    methods: {
        countSumPrc: function () {
            var sum = 0;
            for (var i = 0;i<this.buylist.length;++i) {
                sum += this.buylist[i].prc * this.buylist[i].cnt;
            }
            this.sumprc = sum;
        }
    }
});

var recommend = new Vue({
    el: "#recommend",
    data: {
        userid: '',
        username: '',
        rcmdlist: []
    },
});

/**
 * 页面元素的回调方法
 */
function onClickRecg() {
    alert("post recgBio");
    var videoEle = document.querySelector('video');
    canvas.getContext('2d').drawImage(videoEle, 0, 0, canvas.width, canvas.height);

    var imgData=document.getElementById("canvas").toDataURL("image/png");
    var data=imgData.substr(22);

    recg.recgImgData = data;
    // $.post('recorder/target/sc',{'sj':data});

    alert(recg.recgImgData);
    $.post("loginRecgBio", {
        imgrecg: recg.recgImgData
    }, function (result) {
        alert(result);
        var obj = JSON.parse(result);
        if (obj["stat"] != "success") {
            recg.hints = "人脸信息识别失败，请重试。";
        } else {
            recg.hints = "用户识别完成,请核对是否正确";
            recg.userid = obj["userid"];
            recg.username = obj["username"];
            recg.stat = obj["stat"];
        }
        refreshCnter();
        refreshRcmd();
    });
    recg.hints = "人脸信息识别中，请稍候...";
}

function onClickSignUp() {
    alert("sign up");

    var videoEle = document.querySelector('video');
    canvas.getContext('2d').drawImage(videoEle, 0, 0, canvas.width, canvas.height);

    var imgData=document.getElementById("canvas").toDataURL("image/png");
    var data=imgData.substr(22);

    recg.recgImgData = data;

    $.post("signUpRecgBio", {
        imgrecg: recg.recgImgData
    }, function (result) {
        alert(result);
        var obj = JSON.parse(result);
        if (obj["stat"] == "invalid") {
            recg.hints = "注册失败！请重新录入人脸";
        } else {
            recg.hints = "用户注册完成,请核对您的信息";
            recg.userid = obj["userid"];
            recg.username = obj["username"];
            recg.stat = obj["stat"];
        }
        refreshCnter();
        refreshRcmd();
    });
    recg.hints = "正在根据您的人脸信息注册用户，请稍候...";
}

$("#recg-input").keyup(function (e) {
    if (e.keyCode == 13) {
        alert("confirm userid verification.");
        $.post("accntRecgMan", {
            userid: recg.inputuserid
        }, function (result) {
            alert(result);
            var obj = JSON.parse(result);
            if (obj["stat"] == "invalid") {
                recg.hints = "用户id信息识别失败，请重试。";
            } else {
                recg.hints = "用户识别完成,请核对是否正确";
                recg.userid = obj["userid"];
                recg.username = obj["username"];
                recg.stat = obj["stat"];
            }
            refreshCnter();
            refreshRcmd();
        });
        recg.hints = "用户id信息识别中，请稍候...";
    }
});


$("#counter-btnPay").click(function (e) {
    alert("post counterPay");

    // var lstUserID = [];
    // var lstAccntID = [];
    // var lstGoodsID = [];
    // var lstGoodsCnt = [];

    // var arrObj = JSON.stringify(counter.buylist);
    // alert(arrObj);

    // for (var buy in counter.buylist) {
    //     lstUserID.push(counter.userid);
    //     lstGoodsID.push(buy.id);
    //     lstAccntID.push(document.mngrid);
    //     lstGoodsCnt.push(buy.cnt);
    // }
    for(var i = 0 ;i<counter.buylist.length;++i)
    {
        $.post("accntPay", {
            userid:counter.userid,
            accntid:document.mngrid,
            goodsid:counter.buylist[i].id,
            goodscnt:counter.buylist[i].cnt
        }, function (result) {
            alert(result);
            var obj = JSON.parse(result);
            if (obj["stat"] == "success") {
                counter.hints = "支付信息提交成功！";
            } else {
                counter.hints = "支付信息提交失败，请重试";
            }
        });
    }

    counter.hints = "支付信息提交中，请稍候...";
});

$("#counter-input").keyup(function (e) {
    if (e.keyCode == 13) {
        alert("confirm goodid request");
        $.post("accntGdsQuery", {
            goodsid: counter.goodsidCmd.split(";")[0],
            goodscnt: counter.goodsidCmd.split(";")[1]
        }, function (result) {
            alert(result);
            var obj = JSON.parse(result);
            if (obj["stat"] == "invalid") {
                alert("当前商品不存在或库存不足，请重试。");
            } else {
                var ind = counter.buylist.length + 1;
                var newbuy = {
                    pnl: "panel-" + ind,
                    name: obj["name"],
                    id: obj["id"],
                    img: "res/gdspic/"+obj["img"],
                    cnt: obj["cnt"],
                    prc: obj["prc"],
                    desc: obj["desc"]
                };
                counter.buylist.push(newbuy);
            }
        });
        counter.hints = "商品内容拉取中，请稍候...";
    }
});

// var buy = {
//     pnl: "panel-1",
//     name: "name",
//     id: "0001",
//     img: "img",
//     cnt: 1,
//     prc: 12,
// };

// var buy2 = {
//     pnl: "panel-2",
//     name: "name2",
//     id: "0002",
//     img: "img2",
//     cnt: 12,
//     prc: 122,
// };

// counter.buylist.push(buy);
// counter.buylist.push(buy2);


$("#recommend-btn").click(function (e) {
    alert("post rcmd");
    $.post("accntRcmd", {
        userid: recommend.userid
    }, function (result) {
        alert(result);
        var obj = JSON.parse(result);
        if (obj["stat"] == "invalid") {
            alert("当前用户不存在或无可推荐，请重试。");
        } else {
            for (var i = 0; i < obj.length; ++i) {
                var newRcmd = {
                    pnl: "panel-" + (i + 1),
                    name: obj[i].name,
                    id: obj[i].id,
                    img: "res/gdspic/"+obj[i].img,
                    prc: obj[i].prc,
                    desc: obj[i].desc
                };
                recommend.rcmdlist.push(newRcmd);
            };
        }
    });
});

/**
 * 工具方法
 */

var refreshCnter = function () {
        counter.userid = recg.userid;
        counter.username = recg.username;
        counter.buylist = [];
        counter.sumprc = 0;
        counter.hints = "";
        counter.goodsidCmd = "";
    }



var refreshRcmd = function () {
        recommend.userid = recg.userid;
        recommend.username = recg.username;
        recommend.rcmdlist = [];
    }

