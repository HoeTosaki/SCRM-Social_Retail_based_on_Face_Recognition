package com.scrm.why1139.DataAnalysisModule.DataProc;


import com.scrm.why1139.DataAnalysisModule.DAMConfig;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class DataProcessor
{
    private static DAMConfig m_dam = new DAMConfig();

    public boolean buyListAnalyzeFigForAdmin(int _nCmd, int _nLimit, List<String> _x, List<String> _y, List<Object> _args)
    {
        if(_x == null || _y == null || _args == null)
            return false;

        Process proc = null;
        if(_nCmd == 9)
            proc = m_dam.execPy("processor.py", ""+_nCmd,  ""+_nLimit);
        else if(_nCmd == 8)
            proc = m_dam.execPy("processor.py", ""+_nCmd,  ""+_nLimit, (String)_args.get(0), (String)_args.get(1));
        else if(_nCmd == 10 || _nCmd == 11 || _nCmd == 12)
            proc = m_dam.execPy("processor.py", ""+_nCmd,  ""+_nLimit, (String)_args.get(0), (String)_args.get(1), (String)_args.get(2), (String)_args.get(3));
        else
            return false;

        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = null;
        try {
            while((line = br.readLine()) != null)
            {
//				System.out.println(line);
                String[] x_y = line.split(",");
                _x.add(x_y[0]);
                _y.add(x_y[1]);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean buyListAnalyzeValForAdmin(int _nCmd, int _nOffset, int _nLimit, List<String> _vals, List<Object> _args)
    {
        if(_vals == null || _args == null)
            return false;

        Process proc = null;
        if(_nCmd == 13)
            proc = m_dam.execPy("processor.py", ""+_nCmd, ""+_nOffset, ""+_nLimit);
        else if(_nCmd == 14)
            proc = m_dam.execPy("processor.py", ""+_nCmd, ""+_nOffset, ""+_nLimit, (String)_args.get(0));
        else if(_nCmd == 15)
            proc = m_dam.execPy("processor.py", ""+_nCmd, ""+_nOffset, ""+_nLimit, (String)_args.get(0), (String)_args.get(1));
        else
            return false;

        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = null;
        try {
            while((line = br.readLine()) != null){
//				System.out.println(line);
                _vals.add(line);
            }
            _args.add(new CopyOnWriteArrayList<>());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean buyListAnalyzeFigForUser(String _strUserID, int _nCmd, int _nLimit, List<String> _x, List<String> _y, List<Object> _args)
    {
        if(_strUserID == null || _x == null || _y == null || _args == null)
            return false;

        Process proc = null;
        if(_nCmd == 2 || _nCmd == 3 || _nCmd == 4)
            proc = m_dam.execPy("processor.py", ""+_nCmd,  ""+_nLimit, _strUserID);
        else if(_nCmd == 0)
            proc = m_dam.execPy("processor.py", ""+_nCmd,  ""+_nLimit, _strUserID, (String)_args.get(0));
        else if(_nCmd == 1)
            proc = m_dam.execPy("processor.py", ""+_nCmd,  ""+_nLimit, _strUserID, (String)_args.get(0), (String)_args.get(1));
        else
            return false;

        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = null;
        try {
            while((line = br.readLine()) != null)
            {
//				System.out.println(line);
                String[] x_y = line.split(",");
                _x.add(x_y[0]);
                _y.add(x_y[1]);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean buyListAnalyzeValForUser(String _strUserID, int _nCmd, int _nOffset, int _nLimit, List<String> _vals, List<Object> _args)
    {
        if(_strUserID == null || _vals == null || _args == null)
            return false;

        Process proc = null;
        if(_nCmd == 5)
            proc = m_dam.execPy( "processor.py", ""+_nCmd, ""+_nOffset, ""+_nLimit, _strUserID);
        else if(_nCmd == 6)
            proc = m_dam.execPy( "processor.py", ""+_nCmd, ""+_nOffset, ""+_nLimit, _strUserID, (String)_args.get(0));
        else if(_nCmd == 7)
            proc = m_dam.execPy( "processor.py", ""+_nCmd, ""+_nOffset, ""+_nLimit, _strUserID, (String)_args.get(0), (String)_args.get(1));
        else
            return false;

        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = null;
        List<String> date_index = new ArrayList<>();
        try {
            if(_nCmd == 6 || _nCmd == 7)
            {
                line = br.readLine();
                String[] index = line.split(",");
                for(String i : index)
                    date_index.add(i);
            }
            while((line = br.readLine()) != null) {
//				System.out.println(line);
                _vals.add(line);
            }
            _args.add(date_index);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean goodsAnalyze(List<Integer> _gds1ID,List<Integer> _gds2ID,List<Double> _relNum, List<Integer> _gdsID,List<Integer> _buyCnt, List<Double> _cntNum)
    {
        if(_gds1ID == null || _gds2ID == null || _relNum == null || _gdsID == null || _cntNum == null)
            return false;

        Process proc = m_dam.execPy("processor.py", ""+16);

        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line;
        try {
            for(int i = 0; i<30; i++) {
                line = br.readLine();
                System.out.println(line + "__________" + i);
                String[] arr = line.split(",");
                _gdsID.add(Integer.parseInt(arr[0]));
                _buyCnt.add(Integer.parseInt(arr[1]));
                _cntNum.add(Double.parseDouble(arr[2]));
            }
            while((line = br.readLine()) != null){
                System.out.println(line);
                String[] arr = line.split(",");
                _gds1ID.add(Integer.parseInt(arr[0]));
                _gds2ID.add(Integer.parseInt(arr[1]));
                _relNum.add(Double.parseDouble(arr[2]));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean mngrAnalyzeFig(List<String> _mngrID, List<String> _sale)
    {
        if(_mngrID == null || _sale == null)
            return false;

        Process proc = m_dam.execPy("processor.py", ""+17);
        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = null;
        try {
            while((line = br.readLine()) != null) {
//				System.out.println(line);
                String[] arr = line.split(",");
                _mngrID.add(arr[0]);
                _sale.add(arr[1]);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean serverMonitor(int _nCmd, List<String> _info)
    {
        if(_info == null)
            return false;
        Process proc = m_dam.execPy("monitor.py", ""+_nCmd);
        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = null;
        try {
            if(_nCmd == 18)
                for(int i = 0; i<5; i++)
                {
                    line = br.readLine();
                    System.out.println(line);
                    _info.add(line);
                }
            else if(_nCmd == 19)
                for(int i = 0; i<3; i++)
                {
                    line = br.readLine();
                    System.out.println(line);
                    _info.add(line);
                }
            else if(_nCmd == 20)
                for(int i = 0; i<3; i++)
                {
                    line = br.readLine();
                    System.out.println(line);
                    _info.add(line);
                }
            else if(_nCmd == 21)
                for(int i = 0; i<2; i++)
                {
                    line = br.readLine();
                    System.out.println(line);
                    _info.add(line);
                }
            else
                return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println(e);
            return false;
        }

        return true;
    }


//    public static void main(String[] args)
//    {
//        List<Double> result = new ArrayList<>();
//        List<Integer> x = new ArrayList<>();
//        List<Integer> y = new ArrayList<>();
//        List<Integer> id = new ArrayList<>();
//        List<Integer> buy_cnt = new ArrayList<>();
//        List<Double> cnt_num = new ArrayList<>();
//        List<String> mngr_ids = new ArrayList<>();
//        List<String> mngr_sales = new ArrayList<>();
//        List<String> server_info = new ArrayList<>();
//        List<String> server_info2 = new ArrayList<>();
//        List<String> server_info3 = new ArrayList<>();
//
//        DataProcessor prcr = new DataProcessor();
////		List<Object> pyArgs = new ArrayList<>();
////		pyArgs.add("week");
////		pyArgs.add("snacks");
////		pyArgs.add("12");
////		pyArgs.add("cnt");
////		pyArgs.add("2019-08-15");
////		pyArgs.add("2019-08-25");
////		pyArgs.add("2019-05");
////		pyArgs.add("2019-08");
////		prcr.buyListAnalyzeFigForUser("49", 0, 100, x, y, pyArgs);
////		prcr.buyListAnalyzeValForAdmin("\\py\\main.py", 15, 0, 100, result, pyArgs);
////		prcr.buyListAnalyzeValForAdmin(14, 0, 100, result, pyArgs);
////		prcr.mngrAnalyzeFig(mngr_ids, mngr_sales);
//        prcr.serverMonitor(20, server_info, server_info2, server_info3);
//    }
}