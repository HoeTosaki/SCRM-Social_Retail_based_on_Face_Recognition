package com.scrm.why1139.DataAnalysisModule.Rcmd;

import com.scrm.why1139.DataAnalysisModule.DAMConfig;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class Rcmder
{
    private DAMConfig m_dam = new DAMConfig();
    /*
    nCmd:
        0   --  全商品推荐
     */
    public List<Integer> getRcmdByGoods(int _nGoodsID,int _nCmd, int _offSet, int _nLimit,List<String> _strlstArgs)
    {
        List<Integer> ret = new CopyOnWriteArrayList<>();
        Process proc = null;
        proc = m_dam.execPy("RcmdGdsCF_Predict.py",""+_nGoodsID,""+_nCmd,""+_offSet,""+_nLimit);
        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String str = null;
        String retStr = "";
        try
        {
            while((str = br.readLine()) != null)
            {
                System.out.println("Str output[1]:"+str);
                retStr+=str;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
//        listToListInt(retStr).forEach(System.out::println);
        ret = listToListInt(retStr);
        return ret;
    }

    /*
    nCmd:
        0   --  全商品推荐
        1   --  按商品类别推荐 args[0] = type
     */
    public List<Integer> getRcmdByUser(String _strUserID,int _nCmd, int _offSet, int _nLimit,List<String> _strlstArgs)
    {
//        Optional.ofNullable(_strlstArgs).filter(lst->lst.size()>0).orElseThrow();
        List<Integer> ret = new CopyOnWriteArrayList<>();
        Process proc = null;
        if(_strlstArgs.size() == 0)
            proc = m_dam.execPy("RcmdUserCF_Predict.py",_strUserID,""+_nCmd,""+_offSet,""+_nLimit);
        else
            proc = m_dam.execPy("RcmdUserCF_Predict.py",_strUserID,""+_nCmd,""+_offSet,""+_nLimit,_strlstArgs.get(0));
//        try {
//            proc.waitFor();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String str = null;
        String retStr = "";
        try
        {
            while((str = br.readLine()) != null)
            {
                System.out.println("Str output[0]:"+str);
                retStr+=str;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        listToListInt(retStr).forEach(System.out::println);
        ret = listToListInt(retStr);
        return ret;
    }

    public void doModelingUser()
    {
        m_dam.execPy("RcmdUserCF_HOMO.py");
    }

    public void doModelingGds()
    {
        m_dam.execPy("RcmdGdsCF_HOMO.py");
    }



    private List<Integer> listToListInt(String _strLst)
    {
        List<Integer> ret = new CopyOnWriteArrayList<>();
        if (_strLst == null ||_strLst.equals("") || _strLst.equals("[]"))
            ;
        else
        {
//            System.out.println(Arrays.toString(_strLst.substring(1,_strLst.length()-2).split(", ")));
//            System.out.println((int)Double.parseDouble("1."));
            Arrays.asList(_strLst.substring(1,_strLst.length()-2).split(", ")).stream().map(str->(int)Double.parseDouble(str)).forEach(ret::add);
        }
        return ret;
    }

//    public List<String> listToListStr(String _strLst)
//    {
//        List<String> ret = new CopyOnWriteArrayList<>();
//        Arrays.asList(_strLst.substring(1,_strLst.length()-2).split(". ")).stream().forEach(ret::add);
//        return ret;
//    }

    public static void main(String s[])
    {
        Rcmder r = new Rcmder();
        List<String> strlst = new CopyOnWriteArrayList<>();
        strlst.add("beverages");
        r.getRcmdByUser("1",1,0,10,strlst);
//        r.getRcmdByUser("1",0,0,10,new CopyOnWriteArrayList<>());
//        r.getRcmdByGoods(21,0,0,10,new CopyOnWriteArrayList<>());
    }

}