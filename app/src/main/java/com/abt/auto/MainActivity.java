package com.abt.auto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.auto_fit)
    TextView mAutoFit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    private boolean mToggle = false;

    public void onClick(View view) {
        if (mToggle) {
            mAutoFit.setText(getResources().getString(R.string.k4_definition));
            mToggle = false;
        } else {
            mAutoFit.setText(getResources().getString(R.string.smooth_definition));
            mToggle = true;
        }
    }
}
