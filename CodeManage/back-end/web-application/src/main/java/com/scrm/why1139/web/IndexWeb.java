package com.scrm.why1139.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.dao.BuyDao;
import com.scrm.why1139.dao.GoodsDao;
import com.scrm.why1139.dao.MngrDao;
import com.scrm.why1139.dao.UserDao;
import com.scrm.why1139.domain.Buy;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.Mngr;
import com.scrm.why1139.domain.User;
import com.scrm.why1139.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
public class IndexWeb
{

    @RequestMapping(
            value = {"/","/index","/index.html"}
    )
    public ModelAndView index()
    {
        new DaoTest().printLog();
        return new ModelAndView("Web/html/index.html");
    }


//    @RequestMapping(
//            value = "/loginCheck"
//    )
//    public ModelAndView loginCheck(HttpServletRequest _req,String name,String passwd)
//    {
////        System.out.println("username = "+_loginCmd.getUserName());
////        System.out.println("password = "+_loginCmd.getPassword());
//
//        System.out.println("username = "+name);
//        System.out.println("username = "+passwd);
//        return new ModelAndView("Web/html/login.html","error","用户名密码错误。");
////        boolean isValid = m_userService.hasMatchUser(_loginCmd.getUserName(),_loginCmd.getPassword());
////        if(!isValid)
////        {
////            return new ModelAndView("Web/html/login.html","error","用户名密码错误。");
////        }
////        else
////        {
////            return new ModelAndView("Web/html/rcmd.html");
////        }
//    }




    @RequestMapping(
            value = {"/test"}
    )
    public ModelAndView test(HttpServletRequest _req)
    {
        return new ModelAndView("Web/html/test.html");
    }


//    @ResponseBody
//    @RequestMapping(
//            value = {"/test/loginCheck","/loginCheck",}
//    )
//    public String loginCheckT(HttpServletRequest _req, @RequestParam(value = "passwd",required = true) String _passwd,@RequestParam(value = "userid",required = true) String _userid)
//    {
//
////        System.out.println(_req.getParameter("passwd"));
////        System.out.println(_req.getQueryString());//get null
//////        System.out.println();
////        System.out.println("userid = "+_userid);
////        System.out.println("passwd = "+_passwd);
////
//        JSONObject obj = JSON.parseObject(JSON.toJSONString(_req.getParameterMap()));
//        System.out.println("get json obj is:\t"+obj.toJSONString());
//        return obj.toJSONString();
//    }

//    @ResponseBody
//    @RequestMapping(
//            value = {"/test/loginCheck","/loginCheck",},
//            produces = "application/json;charset=UTF-8"
//    )
//    public String loginCheckT(HttpServletRequest _req)
//    {
//        Map<String,String[]> mp = _req.getParameterMap();
//        mp.keySet().forEach(System.out::println);
//        mp.values().forEach(str->System.out.println(Arrays.toString(str)));
//        JSONObject itemJSONObj = JSONObject.parseObject(JSON.toJSONString(mp));
//        System.out.println(itemJSONObj.toJSONString());
//        return "success";
//    }

    public JSONObject getJSONParam(HttpServletRequest request) {
        JSONObject jsonParam = null;
        try {
            // 获取输入流
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

            // 写入数据到Stringbuilder
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }
            System.out.println(sb.toString());
            System.out.println(sb);
            jsonParam = JSONObject.parseObject(sb.toString());
            // 直接将json信息打印出来
            System.out.println(jsonParam.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonParam;
    }













    //    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private class DaoTest
    {

        public void printLog()
        {
//            System.out.println("UserDao:");
//            System.out.println(userDao.getMatchCount("admin","123456"));
//            System.out.println(userDao.getMatchCount("admin","12346"));
//            System.out.println(userDao.findUserByUserID("admin").getPassword());
//            userDao.updateUser(new User("Tosaki","why","passwd","null"));
//            userDao.insertUser(new User("Tosaki","why","passwd","null"));
//            System.out.println("MngrDao:");
//            System.out.println(mngrDao.getMatchCount("admin","123456"));
//            System.out.println(mngrDao.getMatchCount("admin","12346"));
//            System.out.println(mngrDao.findMngrByMngrID("admin").getPassword());
//            mngrDao.updateMngr(new Mngr(0,"admin","passwd"));
//            mngrDao.insertMngr(new Mngr(0,"admin","passwd"));

//            System.out.println("GoodsDao:");
//            System.out.println(goodsDao.findGoodsByID(0));
//            System.out.println(goodsDao.getMatchCountByID(1));
//            goodsDao.updateGoods(new Goods(3,"foods","pork",12,10,"this is pork."));
//            goodsDao.insertGoods(new Goods(3,"foods","pork",12,10,"this is pork."));
//
//            System.out.println("BuyDao:");
//            List<Buy> lst1 = buyDao.findBuyByGoodsID(4,3);
//            lst1.stream().map(buy->buy.getUserID()).forEach(System.out::println);
//            List<Buy> lst2 = buyDao.findBuyByUserID("why1",3);
//            lst2.stream().map(buy->buy.getGoodsID()).forEach(System.out::println);
//            buyDao.updateBuy(new Buy("Tosaki","Tosaki2",3,"now date",12));
            /*param limit may have some problems.*/
        }
    }

//    private UserDao userDao;
//    private MngrDao mngrDao;
//    private GoodsDao goodsDao;
//    private BuyDao buyDao;
//
//    @Autowired
//    public void setUserDao(UserDao _userDao)
//    {
//        this.userDao=_userDao;
//    }
//
//    @Autowired
//    public void setMngrDao(MngrDao mngrDao)
//    {
//        this.mngrDao = mngrDao;
//    }
//
//    @Autowired
//    public void setGoodsDao(GoodsDao goodsDao)
//    {
//        this.goodsDao = goodsDao;
//    }
//
//    @Autowired
//    public void setBuyDao(BuyDao buyDao)
//    {
//        this.buyDao = buyDao;
//    }
}