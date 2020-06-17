package cn.thundergaba.todaysfootline;

import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class NewsJsonConversion {
    public static News GetNewsFromJson(String TAG, JSONObject object){

        try {
            if(object.has("user_info") ){
                JSONObject user_object = object.getJSONObject("user_info");
                NewsUserInfo newsUserInfo = new NewsUserInfo(user_object.getString("name")
                        ,user_object.getString("avatar_url"));
//                String main_url_encoded = video_info_object.getJSONObject("video_list")
//                        .getJSONObject("video_2")
//                        .getString("main_url");
//                String main_url_decoded = new String(Base64.decode(main_url_encoded, Base64.DEFAULT));
//                Log.d(TAG,"DECODED URL:" + main_url_decoded);
                String item_id = new JSONObject(object.getString("pread_params")).getString("item_id");
                Integer publish_date = new Integer(object.getInt("publish_time"));
                News result = new News();
                result.setIs_liked(false);
                result.setNewsUserInfo(newsUserInfo);
//                result.setPlay_url(main_url_decoded);
                result.setNewsItem_id(item_id);
                result.setNewsPublishTime(publish_date);
                return result;
            }
            else{
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
        return null;
    }
}
