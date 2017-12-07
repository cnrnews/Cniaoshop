package cnaio.imooc.com.cniao5.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.dou361.dialogui.DialogUIUtils;
import com.mob.MobSDK;

import cnaio.imooc.com.cniao5.model.User;
import cnaio.imooc.com.cniao5.utils.UserLocalData;

/**
*作者:candy
*创建时间:2017/12/4 21:01
*邮箱:1601796593@qq.com
*功能描述:BaseApplication
**/
public class BaseApplication extends Application {
    private static  BaseApplication mInstance;
    public static  BaseApplication getInstance(){
        return  mInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this, "1d053596c7fce", "49cf6634b9d5c81be530388d8a97316a");
        //Dialog
        DialogUIUtils.init(this);
        mInstance = this;
    }
    public User getUser(){
        return UserLocalData.getUser(this);
    }
    public void putUser(User user,String token){
        UserLocalData.putUser(this,user);
        UserLocalData.putToken(this,token);
    }

    public void clearUser(){
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }
    public String getToken(){
        return  UserLocalData.getToken(this);
    }
    private Intent intent;
    public void putIntent(Intent intent){
        this.intent = intent;
    }
    public Intent getIntent() {
        return this.intent;
    }

    public void jumpToTargetActivity(Context context){
        context.startActivity(intent);
        this.intent =null;
    }
}
