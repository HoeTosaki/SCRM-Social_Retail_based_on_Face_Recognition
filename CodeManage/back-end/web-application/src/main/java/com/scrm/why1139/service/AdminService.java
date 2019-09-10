package com.scrm.why1139.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.dao.AnalDao;
import com.scrm.why1139.dao.BuyDao;
import com.scrm.why1139.dao.GoodsDao;
import com.scrm.why1139.domain.Buy;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.Mngr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 面向Admin的Service类。
 * @author 王浩宇
 * @date 9.2
 */
@Service
public class AdminService
{
    @Autowired
    private AnalService m_analService;
    @Autowired
    private ManService m_manService;
    @Autowired
    private GoodsService m_goodsService;
    @Autowired
    private MonitorService m_monitorService;

    /**
     * 向管理员提供用户购物记录的分析图
     * @param _nCmd in 查询指令
     * @param _nLimit in 长度限制
     * @param _x in x坐标列表
     * @param _y in y坐标列表
     * @param _args in/out 额外传递参数
     * @return 逻辑值
     */
    public boolean getBuyListAnalyzeFigForAdmin(int _nCmd, int _nLimit, List<String> _x, List<String> _y, List<Object> _args)
    {
        return m_analService.getBuyListAnalyzeFigForAdmin(_nCmd, _nLimit, _x, _y, _args);
    }

    /**
     * 向管理员提供用户购物记录的分析表
     * @param _nCmd in 查询指令
     * @param _nOffset in 偏移量
     * @param _nLimit in 长度限制
     * @param _vals in val列表
     * @param _args in 额外传递参数
     * @return 逻辑值
     */
    public boolean getBuyListAnalyzeValForAdmin(int _nCmd,int _nOffset, int _nLimit, List<Object> _vals,List<Object> _args)
    {
        return m_analService.getBuyListAnalyzeValForAdmin(_nCmd, _nOffset, _nLimit, _vals, _args);
    }

    /**
     * 通过MngrID获取Mngr对象
     * @param _strMngrID in 表示MngrID的字符串
     * @return Mngr对象
     * @author 王浩宇
     * @date 9.2
     */
    public Mngr findMngrByMngrID(String _strMngrID)
    {
        return m_manService.findMngrByMngrID(_strMngrID);
    }

    /**
     * 通过Mngr的个人信息，获取在数据库中的匹配结果
     * @param _strMngrID in MngrID
     * @param _strPassword in Mngr密码
     * @return 标志匹配结果的boolean值
     * @author 王浩宇
     * @date 9.2
     */
    public boolean hasMatchMngr(String _strMngrID,String _strPassword)
    {
        return m_manService.hasMatchMngr(_strMngrID, _strPassword);
    }

    /**
     * 获取所有收银员
     * @return Mngr列表
     */
    public List<Mngr> getMngrAllForAdmin()
    {
        return m_manService.getMngrAllForAdmin();
    }

    /**
     * 更新Mngr信息
     * @param _mngr in Mngr对象
     * @author 王浩宇
     * @date 9.2
     */
    public void updateMngr(Mngr _mngr)
    {
        m_manService.updateMngr(_mngr);
    }

    /**
     * 删除当前Mngr信息
     * @param _mngr in Mngr对象
     * @author 王浩宇
     * @date 9.2
     */
    public void delMngr(Mngr _mngr)
    {
        m_manService.delMngr(_mngr);
    }

    /**
     * 创建新的Accnt对象
     * @param _strAccntID in AccntID
     * @param _strPassword in 密码
     * @return 创建是否成功的boolean值
     * @author 王浩宇
     * @date 9.2
     */
    public boolean creatNewAccnt(String _strAccntID,String _strPassword)
    {
        return m_manService.creatNewAccnt(_strAccntID, _strPassword);
    }

    /**
     * 通过GoodsID获取Goods对象
     * @param _nGoodsID in GoodsID
     * @return Goods对象
     * @author 王浩宇
     * @date 9.2
     */
    public Goods findGoodsByGoodsID(int _nGoodsID)
    {
        return m_goodsService.findGoodsByGoodsID(_nGoodsID);
    }


    public void updateGoods(Goods _gds)
    {
        m_goodsService.updateGoods(_gds);
    }

    public void delGoods(Goods _gds)
    {
        m_goodsService.delGoods(_gds);
    }

    public void addGoods(Goods _gds)
    {
        m_goodsService.addGoods(_gds);
    }

    public boolean getGoodsAnalyzeForAdmin(List<Goods> _gds1,List<Goods> _gds2,List<Double> _relNum,List<Goods> _gds,List<Integer> _buyCnt,List<Double> _cntNum)
    {
        return m_analService.getGoodsAnalyzeForAdmin(_gds1, _gds2, _relNum, _gds, _buyCnt, _cntNum);
    }

    public boolean getMngrAnalyzeFigForAdmin(List<Mngr> _mngr, List<String> _sale)
    {
        return m_analService.getMngrAnalyzeFigForAdmin(_mngr, _sale);
    }

    public void dataExtract()
    {
        m_monitorService.dataExtract();
    }

    public void dataTransfer()
    {
        m_monitorService.dataTransfer();
    }

    public void dataModeling(int _nCmd)
    {
        m_monitorService.dataModeling(_nCmd);
    }

    public boolean monitorBaseInfo(List<String> _info)
    {
        return m_monitorService.monitorBaseInfo(_info);
    }

    public boolean monitorMach(List<String> _info)
    {
        return m_monitorService.monitorMach(_info);
    }

    public boolean monitorFig(List<String> _info)
    {
        return m_monitorService.monitorFig(_info);
    }

}
