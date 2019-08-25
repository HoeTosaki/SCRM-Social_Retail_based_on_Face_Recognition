/**
 * 页面初始化
 */
isLoginShowToken = false;
isTokenOK = false;

/**
 * Vue数据绑定
 */
var login = new Vue({
    el: '#login-model',
    data: {
        userid: '',
        recgImgData: '',
        hints: "请将人脸摆放在摄像头前方："
    },
    mounted: function () {
        // alert("login-model is mounted.");
        document.loginReq = 0;
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

/**
 * btn初始化回调逻辑
 */
[].slice.call(document.querySelectorAll('.progress-button')).forEach(function (bttn, pos) {
    if (pos === 0) {
        /*login*/
        new UIProgressButton(bttn, {
            callback: function (instance) {
                var progress = 0;
                if (isTokenOK == false) {
                    instance.setProgress(1);
                    instance.stop(-1);
                    setTimeout(() => {
                        alert("请完成验证操作，以表明您不是机器人。");
                }, 1000);
                    return;
                }
                setTimeout(() => {
                    postLoginRecgBio(login.mngrid, login.passwd);
                alert(login.mngrid + "\t" + login.passwd);
            }, 100);
                var interval = setInterval(() => {
                    if (document.loginReq == 0) {
                    progress = Math.min(progress + Math.random() * 0.1, 0.5);
                    instance.setProgress(progress);
                } else {
                    progress = 1;
                    instance.setProgress(progress);
                    instance.stop(document.loginReq);
                    document.loginReq = 0;
                    var timeout = setTimeout(() => {
                        if (document.loginStat != "invalid")
                    alert("html target:$" + window
                        .jumpTarget + "$");
                    window.location.href = window.jumpTarget;
                }, 2000);
                    clearInterval(interval);
                }
            }, 1000);
            }
        });
    }
});

/**
 * Token初始化
 */
var myCaptchaLogin = _dx.Captcha(document.getElementById('login-token'), {
    appId: 'dfbcdad62304cb024d8a02efcc126200', //appId，在控制台中“应用管理”或“应用配置”模块获取
    success: function (token) {
        alert('token\t' + token);
        postLoginToken(token);
    }
})

/**
 * 工具函数
 */
function postLoginRecgBio() {
    alert("post recgBio");
    var videoEle = document.querySelector('video');
    canvas.getContext('2d').drawImage(videoEle, 0, 0, canvas.width, canvas.height);

    var imgData = document.getElementById("canvas").toDataURL("image/png");
    var data = imgData.substr(22);

    login.recgImgData = data;
    // $.post('recorder/target/sc',{'sj':data});

    alert(login.recgImgData);
    $.post("loginRecgBio", {
        imgrecg: login.recgImgData
    }, function (result) {
        alert(result);
        var obj = JSON.parse(result);
        if (obj["stat"] != "success") {
            login.hints = "人脸信息识别失败，请重试。";
            document.loginReq = -1;
            window.jumpTarget = "login-face";
        } else {
            login.hints = "用户识别完成,请核对是否正确";
            login.userid = obj["userid"];
            login.stat = obj["stat"];
            document.loginReq = 1;
            window.jumpTarget = "rcmd?" + login.userid;
        }
    });
    login.hints = "人脸信息识别中，请稍候...";
    document.loginReq = 0;
}

function postLoginToken(logintoken) {
    $.post("loginCheckToken", {
        token: logintoken
    }, function (result) {
        alert(result);
        var obj = JSON.parse(result);
        document.signupStat = obj["stat"];
        if (document.signupStat == "success") {
            isTokenOK = true;
        } else {
            isTokenOK = false;
        }
    });
}

function showLoginToken() {
    if (!isLoginShowToken) {
        $("#login-token").css({
            "width": "80%",
            "left": "10%",
            "top": "20%",
            "position": "absolute",
            "visibility": "visible"
        });
        isLoginShowToken = true;
    } else {
        $("#login-token").css({
            "width": "80%",
            "left": "10%",
            "top": "20%",
            "position": "absolute",
            "visibility": "hidden"
        });
        isLoginShowToken = false;
    }
}
