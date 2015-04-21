package com.aroused;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

/**
 * Created by yushilong on 2015/4/14.
 */
public class TargetActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        for (String str:bundle.keySet()){
            Log.e("bug",str+"="+bundle.get(str));
        }
    }
}
