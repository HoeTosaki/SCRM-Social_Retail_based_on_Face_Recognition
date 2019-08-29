package com.scrm.why1139.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.User;
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
        return new ModelAndView("Web/html/rcmd.html");
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
        String strUserID = (String)(objReq.getJSONArray("userid").get(0));
        int cmd = Integer.parseInt((String)(objReq.getJSONArray("cmd").get(0)));
        int offset = Integer.parseInt((String)(objReq.getJSONArray("offset").get(0)));
        int limit = Integer.parseInt((String)(objReq.getJSONArray("limit").get(0)));

        User user = m_userService.findUserByUserID(strUserID);

        JSONArray arrRet = new JSONArray();

        switch (cmd)
        {
            case 0:
            {

                List<Goods> lstRcmd = m_userService.getGoodsRcmdGeneral(user,offset,limit);
                lstRcmd.stream().map(gds->{
                    JSONObject objRet = new JSONObject();
                    objRet.put("title",gds.getGoodsName());
                    objRet.put("src",gds.getPic());
                    objRet.put("height", ""+(Math.random()*100 + 50));
                    objRet.put("width",122.5);
                    return objRet;
                }).map(obj->(Object)obj).forEach(arrRet::add);
                break;
            }
            case 1:
            {
                String type = (String)(objReq.getJSONArray("args").get(0));

                List<Goods> lstRcmd = m_userService.goodsQueryByType(user,type,offset,limit);
                lstRcmd.stream().map(gds->{
                    JSONObject objRet = new JSONObject();
                    objRet.put("title",gds.getGoodsName());
                    objRet.put("src",gds.getPic());
                    objRet.put("height", ""+(Math.random()*100 + 50));
                    objRet.put("width",122.5);
                    return objRet;
                }).map(obj->(Object)obj).forEach(arrRet::add);
                break;
            }
        }

        System.out.println();
        System.out.println(arrRet.toJSONString());
        System.out.println();
        return arrRet.toJSONString();
    }

}
