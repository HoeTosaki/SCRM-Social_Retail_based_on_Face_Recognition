package com.scrm.why1139.BioReferenceModule;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class BioCertificater {

	 /**
	  *返回人脸的用户id， 仅返回最高概率的用户
	  * @param _strBase64
	  * @return 用户id的String
	  */

    public static List<String> certificateUser(String _strBase64)
    {
		System.out.println("start save");
		PrintWriter pw = null;
		try {
			pw = new PrintWriter("liukai.txt");
			pw.println(_strBase64);
			pw.flush();
			pw.close();
			System.out.println("end save");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return FaceManage.FaceQuery(_strBase64);
    }
}
