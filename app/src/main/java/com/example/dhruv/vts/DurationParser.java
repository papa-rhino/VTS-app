package com.example.dhruv.vts;

import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dhruv Sharma on 01-02-2018.
 */

public class DurationParser {



    public String[] parseDuration(String jsonData)
    {
        JSONArray Destination = null;
        JSONArray Source = null;
        JSONArray rowdata = null;
        JSONObject jsonObject=null;
        JSONObject jsonObjDur=null;

        try {
            jsonObject = new JSONObject(jsonData);
            Destination = jsonObject.getJSONArray("destination_addresses");
            Source = jsonObject.getJSONArray("origin_addresses");
            rowdata=jsonObject.getJSONArray("rows");
            jsonObjDur=rowdata.
                    getJSONObject(0).
                    getJSONArray("Elements").
                    getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getDuration(jsonObjDur);
    }



public String [] getDuration(JSONObject jsonObject){
     String[] dur = new String[jsonObject.length()];
   try {
      dur[0]= jsonObject.getJSONObject("Distance")
               .getString("text");
    dur[1]=jsonObject.getJSONObject("Duration").getString("text");
   }catch (Exception e){
       Log.e("Go to Duration Parser",e.toString());
   }

 return dur;

}
}