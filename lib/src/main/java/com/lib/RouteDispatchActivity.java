package com.lib;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;


public class RouteDispatchActivity extends ActionBarActivity {
    private static final String TAG = "DEBUG";
    private RouteUtils routeUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routeUtils = new RouteUtils(this, "route.json");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                next();
            }
        }, 3000);
    }

    public void next() {
        try {
            Intent intent = new Intent(this, Class.forName(routeUtils.getTarget()));
            addParams(intent);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            try {
                if (routeUtils.getDefaultTarget() != null) {
                    Intent intent = new Intent(this, Class.forName(routeUtils.getDefaultTarget()));
                    addParams(intent);
                    startActivity(intent);
                }
            } catch (ClassNotFoundException e1) {
                try {
                    if (routeUtils.getGlobalDefaultTarget() != null) {
                        Intent intent = new Intent(this, Class.forName(routeUtils.getGlobalDefaultTarget()));
                        addParams(intent);
                        startActivity(intent);
                    }
                } catch (ClassNotFoundException e2) {
                    e2.printStackTrace();
                }
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        finish();
    }

    private void addParams(Intent intent) {
        if (intent == null)
            return;
        Uri uri = routeUtils.getUri();
        Set<String> queryNames = uri.getQueryParameterNames();
        for (String queryName : queryNames) {
            intent.putExtra(queryName, uri.getQueryParameter(queryName));
        }
        JSONObject params = routeUtils.getParams();
        if (params != null) {
            Iterator<String> paramNames = params.keys();
            while (paramNames.hasNext()) {
                String name = paramNames.next();
                intent.putExtra(name, params.opt(name) == null ? null : params.opt(name).toString());
            }
        }
    }

}
