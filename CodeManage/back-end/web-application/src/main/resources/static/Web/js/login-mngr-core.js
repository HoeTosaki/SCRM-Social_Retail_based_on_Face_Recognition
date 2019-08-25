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
        mngrid: '',
        passwd: '',
        mngrtype: 3
    },
    mounted: function () {
        // alert("login-model is mounted.");
        document.loginReq = 0;
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
                    postLogin(login.mngrid, login.passwd);
                alert(login.mngrid+"\t"+login.passwd);
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

function postLogin(mngrid, passwd) {
    alert("postLogin start");
    $.post("mngrVerify", {
        mngrid: mngrid,
        passwd: passwd
    }, function (result) {
        alert(result);
        var obj = JSON.parse(result);
        if (obj.stat != "success") {
            document.loginReq = -1;
            window.jumpTarget = "loginMngr";
            return;
        } else if (obj.mngrtype == "2") {
            document.loginReq = 1;
            window.jumpTarget = "accnt?" + obj.mngrid;
        } else if (obj.mngrtype == "1") {
            document.loginReq = 1;
            window.jumpTarget = "admin?" + obj.mngrid;
        } else if (obj.mngrtype == "0") {
            document.loginReq = 1;
            window.jumpTarget = "superadmin";
        } else {
            document.loginReq = -1;
            alert("当前网络状况不佳，请稍后再试。");
            window.jumpTarget = "index";
        }
    });
    document.loginReq = 0;
    alert("postLogin end.");
    // window.localStorage.setItem("userid", obj["userid"]);-->fail in block!

    //  //  can be used for convey complex data
    // var obj = {
    //     "stat": "user",
    //     "userid": "why"
    // };
    // window.jumpTarget = "Web/html/rcmd.html?" + JSON.stringify(obj);
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
