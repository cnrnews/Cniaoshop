/*
*JSONUtil.java
*Created on 2014-9-29 上午9:54 by Ivan
*Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
*http://www.cniao5.com
*/
package cnaio.imooc.com.cniao5.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cnaio.imooc.com.cniao5.model.Order;
import cnaio.imooc.com.cniao5.model.OrderItem;
import cnaio.imooc.com.cniao5.model.Wares;

/**
 * Created by Ivan on 14-9-29.
 * Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
 * http://www.cniao5.com
 */
public class JSONUtil {


    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();



    public static  Gson getGson(){
        return  gson;
    }



    public static <T> T fromJson(String json,Class<T> clz){

        return  gson.fromJson(json,clz);
    }


    public static <T> T fromJson(String json,Type type){
        return  gson.fromJson(json,type);
    }


    public static String toJSON(Object object){

       return gson.toJson(object);
    }
    public static List<Order> parseOrders(String json) {
        List<Order> orders = new ArrayList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(json);
            int size = jsonArray.length();
            JSONObject jsonObject;
            Order order=null;
            Gson gson=new Gson() ;
            for (int i = 0; i < size; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                //基础数据
                order = new Order();
                order.setId(jsonObject.getLong("id"));
                order.setOrderNum(jsonObject.getString("orderNum"));
                order.setCreatedTime(jsonObject.getString("createdTime"));
                order.setAmount(Float.valueOf(jsonObject.getString("amount")));
                order.setStatus(jsonObject.getInt("status"));


                //OrderItem List
                List<OrderItem>orderItems=new ArrayList<>();
                JSONArray orderItemArray=jsonObject.getJSONArray("items");
                int orderItemSize=orderItemArray.length();
                OrderItem orderItem;
                JSONObject orderItemJson;
                for (int i1 = 0; i1 < orderItemSize; i1++) {
                    orderItem=new OrderItem();
                    orderItemJson=orderItemArray.getJSONObject(i1);
                    orderItem.setId(orderItemJson.getLong("id"));
                    Wares wares=gson.fromJson(orderItemArray.getJSONObject(i1).getJSONObject("wares").toString(),Wares.class);
                    orderItem.setWares(wares);
                    orderItems.add(orderItem);
                }
                order.setItems(orderItems);
                orders.add(order);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
