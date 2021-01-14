package com.newgrand.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class EntityConverter {
    /*
     * 功能描述: 设置前端form表单的字段值
     * @Param: [baseData, map]
     * @Return: java.lang.String
     * @Author: xienb
     * @Date: 2020/10/21
     */
    public String SetField(String baseData, HashMap<String,Object> map){
        JSONObject root = JSON.parseObject(baseData);
        JSONObject form = root.getJSONObject("form").getJSONObject("newRow");
        if(form==null)form= root.getJSONObject("form").getJSONObject("modifiedRow");
        for (String key : map.keySet()) {
            form.put(key,map.get(key));
        }
        return root.toJSONString();
    }

    public JSONObject SetFieldEx(String baseData, HashMap<String,Object> map){
        JSONObject root = JSON.parseObject(baseData);
        //JSONObject form = root.getJSONObject("form");
        for (String key : map.keySet()) {
            root.put(key,map.get(key));
        }
        return root;
    }

    public String SetTableRow(String rowDataTmp, ArrayList<HashMap<String,Object>> list) throws Exception {
        if(list.size()==0)return rowDataTmp;
        JSONArray rvarr = new JSONArray();
        JSONArray jaa = JSON.parseArray(rowDataTmp);
        JSONObject jo= jaa.getJSONObject(0).getJSONObject("row");
        for(int i=0;i<list.size();i++){

            for (String key: list.get(i).keySet()) {
                jo.put(key,list.get(i).get(key));
            }
            JSONObject joo= new JSONObject();
            joo.put("row",jo);
            rvarr.add(joo);
        }
        return "{\"table\":{\"key\":\"PhId\",\"newRow\":" + rvarr.toJSONString()+ "},\"isChanged\":true}";
    }

    public String SetTableRow(String rowDataTmp, ArrayList<HashMap<String,Object>> list ,String rowStatus) throws Exception {
        if(list.size()==0)return rowDataTmp;
        JSONArray rvarr = new JSONArray();
        JSONArray jaa = JSON.parseArray(rowDataTmp);
        JSONObject jo= jaa.getJSONObject(0).getJSONObject("row");
        for(int i=0;i<list.size();i++){

            for (String key: list.get(i).keySet()) {
                jo.put(key,list.get(i).get(key));
            }
            JSONObject joo= new JSONObject();
            joo.put("row",jo);
            rvarr.add(joo);
        }
        return "{\"table\":{\"key\":\"PhId\",\""+rowStatus+"\":" + rvarr.toJSONString()+ "},\"isChanged\":true}";
    }

    public JSONArray SetTableRowEx(String rowDataTmp, ArrayList<HashMap<String,Object>> list) throws Exception {
        JSONArray rvarr = new JSONArray();
        JSONArray jaa = JSON.parseArray(rowDataTmp);
        for(int i=0;i<list.size();i++){
            JSONObject jo = (JSONObject) jaa.getJSONObject(0).getJSONObject("row").clone(); //特殊写法（原有写法，引用地址相同，导致赋值相同问题）
            for (String key: list.get(i).keySet()) {
                jo.put(key,list.get(i).get(key));
            }
            JSONObject joo= new JSONObject();
            joo.put("row",jo);
            rvarr.add(joo);
        }
        return rvarr;
    }

    public String SetSingleRow(String baseData, HashMap<String,Object> map){
        JSONObject root = JSON.parseObject(baseData);
        JSONObject form = root.getJSONObject("row");
        for (String key : map.keySet()) {
            form.put(key,map.get(key));
        }
        return root.toJSONString();
    }
}
