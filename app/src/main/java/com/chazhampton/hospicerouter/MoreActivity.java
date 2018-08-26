package com.chazhampton.hospicerouter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class MoreActivity extends AppCompatActivity {

    private TextView moretextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        moretextView = (TextView) findViewById(R.id.more_textView);
        moretextView.setMovementMethod(new ScrollingMovementMethod());
        moretextView.setText(R.string.privacy_policy);//display privacy policy
    }

}
