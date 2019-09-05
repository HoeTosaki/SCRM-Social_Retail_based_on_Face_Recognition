package com.scrm.why1139.DataAnalysisModule.DataProc;


import com.scrm.why1139.DataAnalysisModule.DAMConfig;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class DataProcessor {

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

//    public static void main(String[] args)
//    {
//    	List<String> result = new ArrayList<String>();
//		List<String> x = new ArrayList<String>();
//		List<String> y = new ArrayList<String>();
//		DataProcessor prcr = new DataProcessor();
//		List<Object> pyArgs = new ArrayList<>();
//        pyArgs.add("week");
//        pyArgs.add("snacks");
//        pyArgs.add("12");
//        pyArgs.add("cnt");
//        pyArgs.add("2019-08-15");
//        pyArgs.add("2019-08-25");
//        pyArgs.add("2019-05");
//        pyArgs.add("2019-08");
//        prcr.buyListAnalyzeValForUser("90", 5, 0, 100, result,  pyArgs);
//        prcr.buyListAnalyzeValForAdmin("\\py\\main.py", 15, 0, 100, result, pyArgs);
//		prcr.buyListAnalyzeValForAdmin(14, 0, 100, result, pyArgs);
}