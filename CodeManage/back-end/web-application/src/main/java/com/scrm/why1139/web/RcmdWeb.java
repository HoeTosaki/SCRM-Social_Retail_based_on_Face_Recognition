package com.scrm.why1139.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.User;
import com.scrm.why1139.service.AccntService;
import com.scrm.why1139.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户推荐的Controller类。
 * @author why
 */
@RestController
public class RcmdWeb
{
    private UserService m_userService;

    @Autowired
    private AccntService m_accntService;

    /**
     * setter注入
     * @param _userService in
     * @author why
     */
    @Autowired
    private void setUserService(UserService _userService)
    {
        m_userService = _userService;
    }

    /**
     * 映射到rcmd界面的Controller方法
     * @return 页面模型
     * @author why
     */
    @RequestMapping(
            value = {"/rcmd"}
    )
    public ModelAndView rcmd()
    {
        return new ModelAndView("Web/rcmd/html/rcmd.html");
    }

    /**
     * 映射到rcmdPull的Controller方法
     * @return 请求响应String
     * @author why
     */
    @ResponseBody
    @RequestMapping(
            value = {"/userRcmd"}
    )
    public String rcmdPull(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        System.out.println(objReq.toJSONString());
        String strUserID = (String)(objReq.getJSONArray("userid").get(0));
        int cmd = Integer.parseInt((String)(objReq.getJSONArray("cmd").get(0)));
        int offset = Integer.parseInt((String)(objReq.getJSONArray("offset").get(0)));
        int limit = Integer.parseInt((String)(objReq.getJSONArray("limit").get(0)));

        User user = m_userService.findUserByUserID(strUserID);
        JSONObject objRet = new JSONObject();

        if(user == null || user.isEmpty())
        {
            objRet.put("stat","invalid");
        }
        else
        {
            objRet.put("stat","success");
            JSONArray arrRet = new JSONArray();
            switch (cmd)
            {
                case 0:
                {
                    List<Goods> lstRcmd = m_userService.getGoodsRcmdGeneral(user,offset,limit);
                    lstRcmd.stream().map(gds->{
                        JSONObject objGds = new JSONObject();
                        objGds.put("name",gds.getGoodsName());
                        objGds.put("prc",gds.getPrice());
                        objGds.put("cnt",gds.getGoodsCnt());
                        objGds.put("pic",gds.getPic());
                        objGds.put("desc",gds.getGoodsDesc());
                        objGds.put("id",gds.getGoodsID());
                        objGds.put("type",gds.getGoodsType());
                        return objGds;
                    }).forEach(arrRet::add);
                    break;
                }
                case 1:
                {
                    String type = (String)(objReq.getJSONArray("args").get(0));
                    List<Goods> lstRcmd = m_userService.goodsQueryByType(user,type,offset,limit);
                    lstRcmd.stream().map(gds->{
                        JSONObject objGds = new JSONObject();
                        objGds.put("name",gds.getGoodsName());
                        objGds.put("prc",gds.getPrice());
                        objGds.put("cnt",gds.getGoodsCnt());
                        objGds.put("pic",gds.getPic());
                        objGds.put("desc",gds.getGoodsDesc());
                        objGds.put("id",gds.getGoodsID());
                        objGds.put("type",gds.getGoodsType());
                        return objGds;
                    }).forEach(arrRet::add);
                    break;
                }
                case 2:
                {
                    int nGoodsID = Integer.parseInt((String)(objReq.getJSONArray("args").get(0)));
                    Goods curGds = m_accntService.findGoodsByGoodsID(nGoodsID);
                    List<Goods> lstRcmd = m_userService.getGoodsRelated(curGds,offset,limit);
                    lstRcmd.stream().map(gds->{
                        JSONObject objGds = new JSONObject();
                        objGds.put("name",gds.getGoodsName());
                        objGds.put("prc",gds.getPrice());
                        objGds.put("cnt",gds.getGoodsCnt());
                        objGds.put("pic",gds.getPic());
                        objGds.put("desc",gds.getGoodsDesc());
                        objGds.put("id",gds.getGoodsID());
                        objGds.put("type",gds.getGoodsType());
                        return objGds;
                    }).forEach(arrRet::add);
                    break;
                }
            }
            objRet.put("gds_lst",arrRet);
        }

        System.out.println(objRet.toJSONString());
        return objRet.toJSONString();
    }

}
