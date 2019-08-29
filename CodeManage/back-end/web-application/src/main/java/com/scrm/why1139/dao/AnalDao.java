package com.scrm.why1139.dao;

import com.scrm.why1139.DataAnalysisModule.Rcmder;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.User;
import com.scrm.why1139.service.AccntService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class AnalDao
{
    @Autowired
    private GoodsDao m_goodsDao;
    @Autowired
    private Rcmder m_rcmder;

    public List<Goods> getRcmdByUser(User _user,int _nOffset,int _nLimit)
    {
        List<Integer> lstGdsInd = m_rcmder.getRcmdByUser(_user.getUserID(),0,_nOffset,_nLimit,new CopyOnWriteArrayList<>());
        List<Goods> ret = new CopyOnWriteArrayList<>();
        lstGdsInd.stream().map(gdsid->m_goodsDao.findGoodsByID(gdsid)).forEach(ret::add);
        return ret;
    }

    public List<Goods> getRcmdByQuery(User _user,String _strQuery,int _nOffset,int _nLimit)
    {
        List<String> lststrQuery = new CopyOnWriteArrayList<>();
        lststrQuery.add(_strQuery);
        List<Integer> lstGdsInd = m_rcmder.getRcmdByUser(_user.getUserID(),1,_nOffset,_nLimit,lststrQuery);
        List<Goods> ret = new CopyOnWriteArrayList<>();
        lstGdsInd.stream().map(gdsid->m_goodsDao.findGoodsByID(gdsid)).forEach(ret::add);
        return ret;
    }

    public List<Goods> getRcmdByGds(Goods _gds,int _nOffset,int _nLimit)
    {
        List<Integer> lstGdsInd = m_rcmder.getRcmdByGoods(_gds.getGoodsID(),0,_nOffset,_nLimit,new CopyOnWriteArrayList<>());
        List<Goods> ret = new CopyOnWriteArrayList<>();
        lstGdsInd.stream().map(gdsid->m_goodsDao.findGoodsByID(gdsid)).forEach(ret::add);
        return ret;
    }
}
