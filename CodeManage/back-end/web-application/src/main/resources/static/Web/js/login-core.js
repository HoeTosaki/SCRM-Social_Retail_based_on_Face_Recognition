/**
 * 页面初始化
 */
const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');

isSignUpShowToken = false;
isLoginShowToken = false;
isTokenOK = false;

/**
 * Vue数据绑定
 */
var login = new Vue({
    el: '#login-model',
    data: {
        userid: '',
        passwd: ''
    },
    mounted: function () {
        // alert("login-model is mounted.");
        document.loginReq = 0;
    }
});

var signup = new Vue({
    el: '#signup-model',
    data: {
        username: '',
        userid: '',
        passwd: '',
        hints: ''
    },
    mounted: function () {
        // alert("signup-model is mounted.")
        document.signupReq = 0;
    }
});

/**
 * btn回调初始化
 */
[].slice.call(document.querySelectorAll('.progress-button')).forEach(function (bttn, pos) {
    if (pos === 1) {
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
                    postLogin(login.userid, login.passwd);
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
    if (pos === 0) {
        /*signup*/
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
                    postSignUp(signup.username, signup.userid, signup.passwd);
            }, 100);
                var progress = 0;
                var interval = setInterval(() => {
                    if (document.signupReq == 0) {
                    progress = Math.min(progress + Math.random() * 0.1, 0.5);
                    instance.setProgress(progress);
                } else {
                    progress = 1;
                    instance.setProgress(progress);
                    instance.stop(document.signupReq);
                    document.signupReq = 0;
                    clearInterval(interval);
                }
            }, 1000);
            }
        });
    }

});

/**
 * Token调用初始化
 */
var myCaptchaSignUp = _dx.Captcha(document.getElementById('sign-up-token'), {
    appId: 'dfbcdad62304cb024d8a02efcc126200', //appId，在控制台中“应用管理”或“应用配置”模块获取
    success: function (token) {
        alert('token\t' + token);
        postSignUpToken(token);
    }
})

var myCaptchaLogin = _dx.Captcha(document.getElementById('login-token'), {
    appId: 'dfbcdad62304cb024d8a02efcc126200', //appId，在控制台中“应用管理”或“应用配置”模块获取
    success: function (token) {
        alert('token\t' + token);
        postLoginToken(token);
    }
})

/**
 * 页面元素的回调函数
 */

signUpButton.onmouseover = function () {
    container.classList.add("right-panel-active");
}

signInButton.onmouseover = function () {
    container.classList.remove("right-panel-active");
}

// signUpButton.addEventListener('click', () => {
//     container.classList.add("right-panel-active");
// });


// signInButton.addEventListener('click', () => {
//     container.classList.remove("right-panel-active");
// });


/**
 * 工具函数
 */

function postLogin(userid, passwd) {
    alert("postLogin start");
    $.post("loginCheck", {
        userid: userid,
        passwd: passwd
    }, function (result) {
        alert(result);
        var obj = JSON.parse(result);
        if (obj.stat != "success") {
            document.loginReq = -1;
            window.jumpTarget = "login";
            return;
        } else {
            document.loginReq = 1;
            window.jumpTarget = "rcmd?"+obj.userid;
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

function postSignUp(username, userid, passwd) {
    $.post("signUpCheck", {
        username: username,
        userid: userid,
        passwd: passwd
    }, function (result) {
        // alert(result);
        var obj = JSON.parse(result);
        document.signupStat = obj["stat"];
        if (document.signupStat == "success") {
            document.signupReq = 1;
        } else {
            document.signupReq = -1;
        }
        signup.userid = "";
        signup.username = "";
        signup.passwd = "";
    });
}

function postSignUpPre() {
    if (signup.userid == "")
        return;
    $.post("signUpCheckPre", {
        userid: signup.userid,
    }, function (result) {
        // alert(result+"$"+signup.userid);
        var obj = JSON.parse(result);
        document.signupStatPre = obj["stat"];
        if (document.signupStatPre == "success") {
            signup.hints = "\n当前用户名可正常使用。";
        } else {
            signup.hints = "\n当前用户名已被占用。";
            signup.userid = "";
        }
    });
}

function postSignUpToken(signuptoken) {
    $.post("signUpCheckToken", {
        token: signuptoken
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

function showSignUpToken() {
    if (!isSignUpShowToken) {
        $("#sign-up-token").css({
            "width": "80%",
            "left": "10%",
            "top": "20%",
            "position": "absolute",
            "visibility": "visible"
        });
        isSignUpShowToken = true;
    } else {
        $("#sign-up-token").css({
            "width": "80%",
            "left": "10%",
            "top": "20%",
            "position": "absolute",
            "visibility": "hidden"
        });
        isSignUpShowToken = false;
    }
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
