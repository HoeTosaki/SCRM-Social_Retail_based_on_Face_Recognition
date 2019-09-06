package com.scrm.why1139.dao;

import com.alibaba.fastjson.JSONArray;
import com.scrm.why1139.DataAnalysisModule.DataProc.DataProcessor;
import com.scrm.why1139.DataAnalysisModule.Rcmd.Rcmder;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class AnalDao
{
    @Autowired
    private GoodsDao m_goodsDao;
    @Autowired
    private Rcmder m_rcmder;
    @Autowired
    private DataProcessor m_dataProcessor;


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

    public boolean buyListAnalyzeFigForAdmin(int _nCmd, int _nLimit, List<String> _x, List<String> _y, List<Object> _args)
    {
        System.out.println("DP输入[fig]:"+"\tcmd:"+_nCmd+"\tlim:"+_nLimit+"\targs:"+_args);
//        for(int i = 0;i<20;++i)
//        {
//            _x.add(""+i);
//            _y.add(""+Math.random()*400);
//        }
//        return true;
        return m_dataProcessor.buyListAnalyzeFigForAdmin(_nCmd,_nLimit,_x,_y,_args);
    }

    public boolean buyListAnalyzeValForAdmin(int _nCmd,int _nOffset, int _nLimit, List<String> _vals, List<Object> _args)
    {
        System.out.println("DP输入[val]:"+"\tcmd:"+_nCmd+"\toffset:"+_nOffset+"\tlim:"+_nLimit+"\targs:"+_args);
//        for(int i = 0;i<20;++i)
//        {
//            _vals.add(""+i);
//        }
//        _vals.set(0,""+12520);
//        _vals.set(1,""+1530);
//        _vals.set(2,""+82);

//        List<String> lst = new CopyOnWriteArrayList<>();
//        for(int i = 0;i<3;++i)
//        {
//            lst.add(""+i);
//        }
//        _args.add(lst);
        boolean ret = m_dataProcessor.buyListAnalyzeValForAdmin(_nCmd,_nOffset,_nLimit,_vals,_args);
        if(_nCmd == 13 && _vals.size()<5)
            return false;
        if(_nCmd == 14 && _vals.size()<1)
            return false;
        if(_nCmd == 15 && _vals.size()<1)
            return false;
//        return true;
        return ret;
//        return m_dataProcessor.buyListAnalyzeValForAdmin(_nCmd,_nOffset,_nLimit,_vals,_args);
    }

    public boolean buyListAnalyzeFigForUser(String _strUserID,int _nCmd, int _nLimit, List<String> _x, List<String> _y, List<Object> _args)
    {
        System.out.println("DP输入[fig]:\tUserID:"+_strUserID+"\tcmd:"+_nCmd+"\tlim:"+_nLimit+"\targs:"+_args);
//        for(int i = 0;i<7;++i)
//        {
//            _x.add(""+i);
//            _y.add(""+i);
//        }
//        return true;
        return m_dataProcessor.buyListAnalyzeFigForUser(_strUserID,_nCmd,_nLimit,_x,_y,_args);
    }

    public boolean buyListAnalyzeValForUser(String _strUserID,int _nCmd,int _nOffset, int _nLimit, List<String> _vals, List<Object> _args)
    {
        System.out.println("DP输入[val]:\tUserID:"+_strUserID+"\tcmd:"+_nCmd+"\toffset:"+_nOffset+"\tlim:"+_nLimit+"\targs:"+_args);
//        for(int i = 0;i<7;++i)
//        {
//            _vals.add(""+i);
//        }

//        List<String> lst = new CopyOnWriteArrayList<>();
//        for(int i = 0;i<3;++i)
//        {
//            lst.add(""+i);
//        }
//        _args.add(lst);
        boolean ret = m_dataProcessor.buyListAnalyzeValForUser(_strUserID,_nCmd,_nOffset,_nLimit,_vals,_args);
        if(_nCmd == 5 && _vals.size()<4)
            return false;
        if(_nCmd == 6 && _vals.size()<1)
            return false;
        if(_nCmd == 7 && _vals.size()<1)
            return false;
//        return true;
        return ret;
    }

    public boolean goodsAnalyze(List<Integer> _gds1ID,List<Integer> _gds2ID,List<Double> _relNum,List<Integer> _gdsID,List<Integer> _buyCnt,List<Double> _cntNum)
    {
//        for(int i = 0;i < 20;++i)//edges
//        {
//            _gds1ID.add((int)(Math.random()*29+1));
//            _gds2ID.add((int)(Math.random()*29+1));
//            _relNum.add(Math.random()*2-1);
//        }
//
//        for(int i = 0;i < 30;++i)//nodes
//        {
//            _gdsID.add(i+1);
//            _cntNum.add(Math.random()*2-1);
//            _buyCnt.add((int)(Math.random()*100+1));
//        }
//        return true;
        return m_dataProcessor.goodsAnalyze(_gds1ID, _gds2ID, _relNum, _gdsID, _buyCnt, _cntNum);
    }

    public boolean mngrAnalyzeFig(List<String> _mngrID, List<String> _sale)
{
    return m_dataProcessor.mngrAnalyzeFig(_mngrID, _sale);
}


}
