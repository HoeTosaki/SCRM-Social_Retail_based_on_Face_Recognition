package com.scrm.why1139.BioReferenceModule;


import com.baidu.aip.face.AipFace;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * 用于向百度API请求人脸识别功能的类
 * @author why
 */
public class AIBound
{
    //设置APPID/AK/SK
    public static final String APP_ID = "17057164";
    public static final String API_KEY = "CNRYw8iicNDuQ8DYHjsZHfZF";
    public static final String SECRET_KEY = "Cq9do1wYkbilwO1PftgdNZLn8eTbx2kR";

    private static AipFace s_FaceClient = null;

    /**
     * 服务初始化函数
     * @author why
     */
    public static void initService()
    {
        if(s_FaceClient != null)
        {
            return;
        }

        // 初始化一个AipFace
        AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        s_FaceClient = client;
//        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

//        // 调用接口
//        String image = "取决于image_type参数，传入BASE64字符串或URL字符串或FACE_TOKEN字符串";
//        String imageType = "BASE64";
//
//        // 人脸检测
//        JSONObject res = client.detect(image, imageType, options);
//        System.out.println(res.toString(2));
    }

    /**
     * 获取人脸识别客户端对象，如果当前没有初始化，则自动进行初始化。
     * 该方法始终保持Client的单例性。
     * @return AipFace
     * @author why
     */
    private static AipFace getFaceClient()
    {
        if(s_FaceClient != null)
            return s_FaceClient;
        else
        {
            initService();
            return s_FaceClient;
        }
    }

    /**
     * 验证当前用户组是否可用，如果用户组不存在，则新建一个用户组
     * @param _strGroupID in 用户组ID
     * @return 可用性boolean值
     * @author why
     */
    public static boolean verifyUserGroup(String _strGroupID)
    {
        AipFace client = getFaceClient();
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();

        // 创建用户组
        JSONObject res = client.groupAdd(_strGroupID, options);
//        System.out.println(res.toString(2));
        if(res.getInt("error_code") == 0)
            return true;
        else if(res.getInt("error_code") == 223101)
            return true;//已存在用户组
        else
        {
            System.out.println(res.getString("error_msg"));
            return false;
        }
        //        {
//            "result": null,
//                "log_id": 304569263738694331,
//                "error_msg": "SUCCESS",
//                "cached": 0,
//                "error_code": 0,
//                "timestamp": 1566373869
//        }

//        {
//            "result": null,
//                "log_id": 305486863739392191,
//                "error_msg": "group is already exist",
//                "cached": 0,
//                "error_code": 223101,
//                "timestamp": 1566373939
//        }

    }

    /**
     * 更新用户人脸信息，如果当前用户不存在，则自动调用用户的创建方法。
     * @param _strUserID in 用户ID
     * @param _strBase64 in 用户人脸信息
     * @return boolean
     * @author why
     */
    public static boolean updateUserFace(String _strUserID,String _strBase64)
    {
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("user_info", _strUserID);
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "NONE");//TODO:当前人脸信息不支持活体检测
        options.put("action_type", "REPLACE");// 该方法保证只存一张人脸
        //TODO: 后期请用APPEND，以实现多张人脸存储。

        String image = _strBase64;
        String imageType = "BASE64";
        String groupId = "user";
        String userId = _strUserID;

        System.out.println("!userid!=\t"+userId);
        // 人脸更新
        JSONObject res = getFaceClient().updateUser(image, imageType, groupId, userId, options);
        System.out.println(res.toString(2));

        if(res.getInt("error_code") == 223120)
            return addUserFace(_strUserID,_strBase64);//user不存在
        else
            return res.getInt("error_code") == 0;
//        {
//            "face_token": "2fa64a88a9d5118916f9a303782a97d3",
//                "location": {
//            "left": 117,
//                    "top": 131,
//                    "width": 172,
//                    "height": 170,
//                    "rotation": 4
//        }
//        }
    }

    /**
     * 增加新的用户人脸信息，适用于当前用户不存在的情形。
     * @param _strUserID in 用户ID
     * @param _strBase64 in 用户人脸信息
     * @return boolean
     * @author why
     */
    public static boolean addUserFace(String _strUserID,String _strBase64)
    {
        System.out.println("添加新用户的人脸！");
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("user_info", _strUserID);
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "NONE");
        options.put("action_type", "REPLACE");

        String image = _strBase64;
        String imageType = "BASE64";
        String groupId = "user";
        String userId = _strUserID;

        // 人脸注册
        JSONObject res = getFaceClient().addUser(image, imageType, groupId, userId, options);
//        System.out.println(res.toString(2));
        return res.getInt("error_code") == 0;
    }

    /**
     * 根据人脸信息查询匹配的用户ID列表。
     * TODO:后期应对score进行限定。
     * @param _strBase64 in 用户人脸信息
     * @return 用户ID的list
     * @author why
     */
    public static List<String> FaceQuery(String _strBase64)
    {
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("max_face_num", "3");
        options.put("match_threshold", "70");
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "NONE");//LOW失败
//        options.put("user_id", _strUserID);
        options.put("max_user_num", "3");

//        取决于image_type参数，传入BASE64字符串或URL字符串或FACE_TOKEN字符串
        String image = _strBase64;
        String imageType = "BASE64";
        String groupIdList = "user";

        // 人脸搜索
        JSONObject res = getFaceClient().search(image, imageType, groupIdList, options);


        List<String> lstUserID = new CopyOnWriteArrayList<>();

        System.out.println("人脸搜索");
        System.out.println(res.toString(2));
//        System.out.println(res.getString("result"));
//        System.out.println(res.getJSONObject("result").toString(2));

        if(res.getInt("error_code") != 0)
            return lstUserID;//无匹配项

        JSONArray lstUser = res.getJSONObject("result").getJSONArray("user_list");
//        System.out.println(lstUser.getJSONObject(0).getString("user_id"));

        int len = lstUser.length();
        for(int i = 0; i<len;++i)
        {
            String userid = lstUser.getJSONObject(i).getString("user_id");
            if(userid != null && !(userid.trim().equals("")))
                lstUserID.add(userid);
        }

//        System.out.println("LstUserid:\t");
//        lstUserID.stream().forEach(System.out::println);
//        System.out.println("LstUserid End\t");
        return lstUserID;
//        {
//            "face_token": "fid",
//                "user_list": [
//                  {
//                      "group_id" : "test1",
//                      "user_id": "u333333",
//                      "user_info": "Test User",
//                      "score": 99.3
//                  }
//                 ]
//        }
    }

    /**
     * 删除用户，包括其前期更新的全部人脸信息。
     * @param _strUserID in 用户ID
     * @return 删除成功性的boolean值
     * @author why
     */
    public static boolean delUser(String _strUserID)
    {
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();

        String groupId = "user";
        String userId = _strUserID;

        // 删除用户
        JSONObject res = getFaceClient().deleteUser(groupId, userId, options);
//        System.out.println(res.toString(2));
        return res.getInt("error_code") == 0;
    }

//orgin:
    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
//
    public static void main(String s[])
    {
    }
}

