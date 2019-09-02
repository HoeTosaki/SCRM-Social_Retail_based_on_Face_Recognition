package com.scrm.why1139.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.domain.Mngr;
import com.scrm.why1139.domain.User;
import com.scrm.why1139.service.AccntService;
import com.scrm.why1139.service.AdminService;
import com.scrm.why1139.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 提供数据分析的Controller类。
 * @author why
 */
@RestController
public class AnalWeb
{
    @Autowired
    private UserService m_userService;
    @Autowired
    private AccntService m_accntService;
    @Autowired
    private AdminService m_adminService;

    @RequestMapping(
            value = {"/testAnal"}
    )
    public ModelAndView testAnal()
    {
        return new ModelAndView("Web/test/test-anal.html");
    }

    @ResponseBody
    @RequestMapping(
            value = {"/userValQuery"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String userValQuery(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        System.out.println("控制器接入："+objReq.toJSONString());

        String strUserID = (String)(objReq.getJSONArray("userid").get(0));
        int cmd = Integer.parseInt( (String)(objReq.getJSONArray("cmd").get(0)));
        int offset = Integer.parseInt( (String)(objReq.getJSONArray("offset").get(0)));
        int limit = Integer.parseInt( (String)(objReq.getJSONArray("limit").get(0)));

        JSONArray arrArgs = new JSONArray();
        if(objReq.keySet().contains("args"))
        {
            /* wechat resolve*/
            arrArgs = JSONArray.parseArray(objReq.getJSONArray("args").get(0).toString());
        }
        else
        {
            /* web resolve*/
            arrArgs = objReq.getJSONArray("args[]");
        }
        List<Object> lstVal = new CopyOnWriteArrayList<>();
        List<Object> lstArgs = new CopyOnWriteArrayList<>();
        JSONObject objRet = new JSONObject();

        switch(cmd)
        {
            case 5:
            {
                if(m_userService.getVal(strUserID,cmd,offset,limit,lstVal,lstArgs))
                {
                    objRet.put("stat","success");
                    JSONArray arrVal = new JSONArray();
                    for(int i = 0;i<lstVal.size();++i)
                    {
                        if(i <= 3)
                            arrVal.add(lstVal.get(i));
                        else
                        {
                            arrVal.add((JSONObject)lstVal.get(i));
                        }
                    }
                    objRet.put("vals",arrVal);
                    objRet.put("args",(JSONArray)(lstArgs.get(lstArgs.size()-1)));
                }
                else
                {
                    objRet.put("stat","invalid");
                }
                break;
            }
            case 6:
            {
                String args0 = arrArgs.getString(0);
                lstArgs.add(args0);
                if(m_userService.getVal(strUserID,cmd,offset,limit,lstVal,lstArgs))
                {
                    objRet.put("stat","success");
                    JSONArray arrVal = new JSONArray();
                    for(int i = 0;i<lstVal.size();++i)
                    {
                        if(i <= 0)
                            arrVal.add(lstVal.get(i));
                        else
                        {
                            arrVal.add((JSONObject)lstVal.get(i));
                        }
                    }
                    objRet.put("vals",arrVal);
                    objRet.put("args",(JSONArray)(lstArgs.get(lstArgs.size()-1)));
                }
                else
                {
                    objRet.put("stat","invalid");
                }
                break;
            }
            case 7:
            {
                String args0 = arrArgs.getString(0);
                String args1 = arrArgs.getString(1);
                lstArgs.add(args0);
                lstArgs.add(args1);
                if(m_userService.getVal(strUserID,cmd,offset,limit,lstVal,lstArgs))
                {
                    objRet.put("stat","success");
                    JSONArray arrVal = new JSONArray();
                    for(int i = 0;i<lstVal.size();++i)
                    {
                        if(i <= 0)
                            arrVal.add(lstVal.get(i));
                        else
                        {
                            arrVal.add((JSONObject)lstVal.get(i));
                        }
                    }
                    objRet.put("vals",arrVal);
                    objRet.put("args",(JSONArray)(lstArgs.get(lstArgs.size()-1)));
                }
                else
                {
                    objRet.put("stat","invalid");
                }
                break;
            }
            default:
            {
                objRet.put("stat","invalid");
            }
        }
        System.out.println("控制器返回："+objRet.toJSONString());
        return objRet.toJSONString();
    }

    @ResponseBody
    @RequestMapping(
            value = {"/userFigQuery"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String userFigQuery(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        System.out.println("控制器接入："+objReq.toJSONString());

        String strUserID = (String)(objReq.getJSONArray("userid").get(0));
        int cmd = Integer.parseInt( (String)(objReq.getJSONArray("cmd").get(0)));
        int limit = Integer.parseInt( (String)(objReq.getJSONArray("limit").get(0)));
        JSONArray arrArgs = new JSONArray();
        if(objReq.keySet().contains("args"))
        {
            /* wechat resolve*/
            arrArgs = JSONArray.parseArray(objReq.getJSONArray("args").get(0).toString());
        }
        else
        {
            /* web resolve*/
            arrArgs = objReq.getJSONArray("args[]");
        }

        List<String> lstX = new CopyOnWriteArrayList<>();
        List<String> lstY = new CopyOnWriteArrayList<>();
        List<Object> lstArgs = new CopyOnWriteArrayList<>();
        JSONObject objRet = new JSONObject();

        switch(cmd)
        {
            case 0:
            {
                String args0 = arrArgs.getString(0);
                lstArgs.add(args0);
                if(m_userService.getFig(strUserID,cmd,limit,lstX,lstY,lstArgs))
                {
                    objRet.put("stat","success");
                    JSONArray arrX = new JSONArray();
                    JSONArray arrY = new JSONArray();
                    for(int i = 0;i<lstX.size();++i)
                    {
                        arrX.add(lstX.get(i));
                    }

                    for(int i = 0;i<lstY.size();++i)
                    {
                        arrY.add(lstY.get(i));
                    }
                    objRet.put("x",arrX);
                    objRet.put("y",arrY);
                }
                else
                {
                    objRet.put("stat","invalid");
                }
                break;
            }
            case 1:
            {
                String args0 = arrArgs.getString(0);
                String args1 = arrArgs.getString(1);
                lstArgs.add(args0);
                lstArgs.add(args1);
                if(m_userService.getFig(strUserID,cmd,limit,lstX,lstY,lstArgs))
                {
                    objRet.put("stat","success");
                    JSONArray arrX = new JSONArray();
                    JSONArray arrY = new JSONArray();
                    for(int i = 0;i<lstX.size();++i)
                    {
                        arrX.add(lstX.get(i));
                    }

                    for(int i = 0;i<lstY.size();++i)
                    {
                        arrY.add(lstY.get(i));
                    }
                    objRet.put("x",arrX);
                    objRet.put("y",arrY);
                }
                else
                {
                    objRet.put("stat","invalid");
                }
                break;

            }
            case 2:
            case 3:
            case 4:
            {
                if(m_userService.getFig(strUserID,cmd,limit,lstX,lstY,lstArgs))
                {
                    objRet.put("stat","success");
                    JSONArray arrX = new JSONArray();
                    JSONArray arrY = new JSONArray();
                    for(int i = 0;i<lstX.size();++i)
                    {
                        arrX.add(lstX.get(i));
                    }

                    for(int i = 0;i<lstY.size();++i)
                    {
                        arrY.add(lstY.get(i));
                    }
                    objRet.put("x",arrX);
                    objRet.put("y",arrY);
                }
                else
                {
                    objRet.put("stat","invalid");
                }
                break;
            }
            default:
            {
                objRet.put("stat","invalid");
            }
        }
        System.out.println("控制器返回："+objRet.toJSONString());
        return objRet.toJSONString();
    }

    @ResponseBody
    @RequestMapping(
            value = {"/userGdsQuery"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String userGdsQuery(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        System.out.println(objReq.toJSONString());
//        String strUserID = objReq.getString();

        Map<String,Object> mp = new ConcurrentHashMap<>();

        JSONObject obj = new JSONObject(mp);
//        System.out.println("ret:"+obj.toJSONString());
        return obj.toJSONString();
    }

    @ResponseBody
    @RequestMapping(
            value = {"/adminValQuery"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String adminValQuery(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        System.out.println("控制器接入："+objReq.toJSONString());
        System.out.println("wun");
        String strMngrID = (String)(objReq.getJSONArray("mngrid").get(0));
        int cmd = Integer.parseInt( (String)(objReq.getJSONArray("cmd").get(0)));
        int offset = Integer.parseInt( (String)(objReq.getJSONArray("offset").get(0)));
        int limit = Integer.parseInt( (String)(objReq.getJSONArray("limit").get(0)));

        JSONArray arrArgs = new JSONArray();
        if(objReq.keySet().contains("args"))
        {
            /* wechat resolve*/
            arrArgs = JSONArray.parseArray(objReq.getJSONArray("args").get(0).toString());
        }
        else
        {
            /* web resolve*/
            arrArgs = objReq.getJSONArray("args[]");
        }
        List<Object> lstVal = new CopyOnWriteArrayList<>();
        List<Object> lstArgs = new CopyOnWriteArrayList<>();
        JSONObject objRet = new JSONObject();

        Mngr mngr = m_accntService.findMngrByMngrID(strMngrID);
        if(mngr == null ||mngr.isEmpty())
        {
            objRet.put("stat","invalid/no mngr");
        }
        else
        {
            switch(cmd)
            {
                case 13:
                {
                    if(m_adminService.getVal(cmd,offset,limit,lstVal,lstArgs))
                    {
                        objRet.put("stat","success");
                        JSONArray arrVal = new JSONArray();
                        for(int i = 0;i<lstVal.size();++i)
                        {
                            if(i <= 3)
                                arrVal.add(lstVal.get(i));
                            else
                            {
                                arrVal.add((JSONObject)lstVal.get(i));
                            }
                        }
                        objRet.put("vals",arrVal);
                        objRet.put("args",(JSONArray)(lstArgs.get(lstArgs.size()-1)));
                    }
                    else
                    {
                        objRet.put("stat","invalid/no val");
                    }
                    break;
                }
                case 14:
                {
                    String args0 = arrArgs.getString(0);
                    lstArgs.add(args0);
                    if(m_adminService.getVal(cmd,offset,limit,lstVal,lstArgs))
                    {
                        objRet.put("stat","success");
                        JSONArray arrVal = new JSONArray();
                        for(int i = 0;i<lstVal.size();++i)
                        {
                            if(i <= 0)
                                arrVal.add(lstVal.get(i));
                            else
                            {
                                arrVal.add((JSONObject)lstVal.get(i));
                            }
                        }
                        objRet.put("vals",arrVal);
                        objRet.put("args",(JSONArray)(lstArgs.get(lstArgs.size()-1)));
                    }
                    else
                    {
                        objRet.put("stat","invalid");
                    }
                    break;
                }
                case 15:
                {
                    String args0 = arrArgs.getString(0);
                    String args1 = arrArgs.getString(1);
                    lstArgs.add(args0);
                    lstArgs.add(args1);
                    if(m_adminService.getVal(cmd,offset,limit,lstVal,lstArgs))
                    {
                        objRet.put("stat","success");
                        JSONArray arrVal = new JSONArray();
                        for(int i = 0;i<lstVal.size();++i)
                        {
                            if(i <= 0)
                                arrVal.add(lstVal.get(i));
                            else
                            {
                                arrVal.add((JSONObject)lstVal.get(i));
                            }
                        }
                        objRet.put("vals",arrVal);
                        objRet.put("args",(JSONArray)(lstArgs.get(lstArgs.size()-1)));
                    }
                    else
                    {
                        objRet.put("stat","invalid");
                    }
                    break;
                }
                default:
                {
                    objRet.put("stat","invalid");
                }
            }

        }
        System.out.println("控制器返回："+objRet.toJSONString());
        return objRet.toJSONString();
    }

    @ResponseBody
    @RequestMapping(
            value = {"/adminFigQuery"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String adminFigQuery(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        System.out.println("控制器接入："+objReq.toJSONString());

        String strMngrID = (String)(objReq.getJSONArray("mngrid").get(0));
        int cmd = Integer.parseInt( (String)(objReq.getJSONArray("cmd").get(0)));
        int limit = Integer.parseInt( (String)(objReq.getJSONArray("limit").get(0)));
        JSONArray arrArgs = new JSONArray();
        if(objReq.keySet().contains("args"))
        {
            /* wechat resolve*/
            arrArgs = JSONArray.parseArray(objReq.getJSONArray("args").get(0).toString());
        }
        else
        {
            /* web resolve*/
            arrArgs = objReq.getJSONArray("args[]");
        }

        List<String> lstX = new CopyOnWriteArrayList<>();
        List<String> lstY = new CopyOnWriteArrayList<>();
        List<Object> lstArgs = new CopyOnWriteArrayList<>();
        JSONObject objRet = new JSONObject();

        switch(cmd)
        {
            case 8:
            {
                String args0 = arrArgs.getString(0);
                String args1 = arrArgs.getString(1);
                lstArgs.add(args0);
                lstArgs.add(args1);
                if(m_adminService.getFig(cmd,limit,lstX,lstY,lstArgs))
                {
                    objRet.put("stat","success");
                    JSONArray arrX = new JSONArray();
                    JSONArray arrY = new JSONArray();
                    for(int i = 0;i<lstX.size();++i)
                    {
                        arrX.add(lstX.get(i));
                    }

                    for(int i = 0;i<lstY.size();++i)
                    {
                        arrY.add(lstY.get(i));
                    }
                    objRet.put("x",arrX);
                    objRet.put("y",arrY);
                }
                else
                {
                    objRet.put("stat","invalid");
                }
                break;
            }
            case 9:
            {
                if(m_adminService.getFig(cmd,limit,lstX,lstY,lstArgs))
                {
                    objRet.put("stat","success");
                    JSONArray arrX = new JSONArray();
                    JSONArray arrY = new JSONArray();
                    for(int i = 0;i<lstX.size();++i)
                    {
                        arrX.add(lstX.get(i));
                    }

                    for(int i = 0;i<lstY.size();++i)
                    {
                        arrY.add(lstY.get(i));
                    }
                    objRet.put("x",arrX);
                    objRet.put("y",arrY);
                }
                else
                {
                    objRet.put("stat","invalid");
                }
                break;

            }
            case 10:
            case 11:
            case 12:
            {
                String args0 = arrArgs.getString(0);
                String args1 = arrArgs.getString(1);
                String args2 = arrArgs.getString(2);
                String args3 = arrArgs.getString(3);
                lstArgs.add(args0);
                lstArgs.add(args1);
                lstArgs.add(args2);
                lstArgs.add(args3);
                if(m_adminService.getFig(cmd,limit,lstX,lstY,lstArgs))
                {
                    objRet.put("stat","success");
                    JSONArray arrX = new JSONArray();
                    JSONArray arrY = new JSONArray();
                    for(int i = 0;i<lstX.size();++i)
                    {
                        arrX.add(lstX.get(i));
                    }

                    for(int i = 0;i<lstY.size();++i)
                    {
                        arrY.add(lstY.get(i));
                    }
                    objRet.put("x",arrX);
                    objRet.put("y",arrY);
                }
                else
                {
                    objRet.put("stat","invalid");
                }
                break;
            }

            default:
            {
                objRet.put("stat","invalid");
            }
        }
        System.out.println("控制器返回："+objRet.toJSONString());
        return objRet.toJSONString();
    }

}
