package cnaio.imooc.com.cniao5.utils;

import android.util.Log;

/**
*作者:candy
*创建时间:2017/12/3 20:47
*邮箱:1601796593@qq.com
*功能描述: 日志管理
**/
public class LogUtils {
    private static final String TAG="TAG";
    private static final int BASE_LEVEL=6;
    private static final int BASE_DEBUG=1;
    private static final int BASE_INFO=2;
    private static final int BASE_WARN=3;
    private static final int BASE_ERROR=4;

    public static void d(String msg)
    {
        if (BASE_DEBUG<BASE_LEVEL) {
            Log.d(TAG, msg);
        }
    }
    public static void i(String msg)
    {
        if (BASE_INFO<BASE_LEVEL) {
            Log.i(TAG, msg);
        }
    }
    public static void w(String msg)
    {
        if (BASE_WARN<BASE_LEVEL) {
            Log.w(TAG, msg);
        }
    }
    public static void e(String msg)
    {
        if (BASE_ERROR<BASE_LEVEL) {
            Log.e(TAG, msg);
        }
    }

}
