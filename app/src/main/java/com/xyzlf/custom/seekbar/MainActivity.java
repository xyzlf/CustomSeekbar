package com.xyzlf.custom.seekbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.xyzlf.seekbar.lib.CustomSeekbar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private CustomSeekbar customSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customSeekBar = (CustomSeekbar) findViewById(R.id.seek_bar);
        customSeekBar.setLable("10000元", "10000.01", "10020.86", "4.1%", "8.5%", "5.5%");
        customSeekBar.setMax(100);

        int initProgress = (int) ((0.055  - 0.041) / (0.085 - 0.041) * 100);
        customSeekBar.setProgress(initProgress);
        customSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                double seekbarProfit = 4.1 + progress * (8.5-4.1) / 100;
                String floatProfitStr = String.format(Locale.CHINA,"%.1f", seekbarProfit) + "%";
                customSeekBar.setFloatProfit(floatProfitStr);

                customSeekBar.setTransferMoney(String.format(Locale.CHINA,"%.2f元", 10000 + 10000 * seekbarProfit / 365 / 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
