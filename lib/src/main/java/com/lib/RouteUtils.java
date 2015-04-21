package com.lib;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by yushilong on 2015/4/15.
 */
public class RouteUtils {
    private static final String TAG = "RouteUtils";
    private Uri uri;
    private Context context;
    private String globalDefaultTarget;//默认打开的页面标识符，全局的
    private String target;//需要打开的页面标识符
    private String defaultTarget;//默认打开的页面标识符，局部的
    private JSONObject routes;//所有可路由页面集合
    private JSONObject route;//当前可路由页面
    private JSONObject params;//当前可路由页面参数,JSON文件中配置的
    private String routeValue;

    /**
     * 构造函数
     *
     * @param context:可转化为Activity的Context
     * @param jsonFileName:放在assets目录下的JSON格式文件名，不填的话默认为route.json
     */
    public RouteUtils(Context context, String jsonFileName) {
        if (context == null || !(context instanceof Activity))
            throw new RuntimeException("context is null or context is not activity!");
        this.context = context;
        this.uri = ((Activity) context).getIntent().getData();
        init(isEmpty(jsonFileName) ? "route.json" : jsonFileName);
    }

    /**
     * 解析JSON，初始化数据
     *
     * @param jsonFileName
     */
    private void init(String jsonFileName) {
        JSONObject jsonObject = getRouteJson(jsonFileName);
        if (uri == null || jsonObject == null)
            return;
        if (!jsonObject.has("routeKey")) {
            throw new RuntimeException("JSON has not a mapping for [routkey]");
        }
        String routeKey = jsonObject.optString("routeKey");
        if (uri.getQueryParameter(routeKey) == null) {
            throw new RuntimeException("Uri has not a mapping for [" + routeKey + "] or the value is null");
        }
        String routeStr = uri.getQueryParameter(routeKey);
        if (jsonObject.has("defaultTarget"))
            globalDefaultTarget = jsonObject.optString("defaultTarget");
        if (!jsonObject.has("routes")) {
            throw new RuntimeException("JSON has not a mapping for [routes]");
        }
        routes = jsonObject.optJSONObject("routes");
        if (!routes.has(routeStr)) {
            throw new RuntimeException(routes + " has not a mapping for [" + routeStr + "]");
        }
        route = routes.optJSONObject(routeStr);
        if (route != null) {
            if (!route.has("target")) {
                throw new RuntimeException(route + " has not a mapping for target");
            }
            target = route.optString("target");
            if (route.has("defaultTarget"))
                defaultTarget = route.optString("defaultTarget");
            if (route.has("params"))
                params = route.optJSONObject("params");
        }
    }

    /**
     * 将JSON格式文件转化为JSONObject对象
     *
     * @param jsonFileName
     * @return
     */
    public JSONObject getRouteJson(String jsonFileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(jsonFileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            Log.e(TAG, Result);
            if (!TextUtils.isEmpty(Result))
                return new JSONObject(Result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isEmpty(String str) {
        return str == null || str.trim().length() < 1;
    }

    public Uri getUri() {
        return uri;
    }

    public String getGlobalDefaultTarget() {
        return globalDefaultTarget;
    }

    public String getTarget() {
        return target;
    }

    public String getDefaultTarget() {
        return defaultTarget;
    }

    public JSONObject getRoutes() {
        return routes;
    }

    public JSONObject getRoute() {
        return route;
    }

    public JSONObject getParams() {
        return params;
    }
}
