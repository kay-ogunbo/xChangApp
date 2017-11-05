package com.example.android.xchangapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class currency_conversion extends AppCompatActivity {

    // Set up the views
    EditText mInputAmount;
    TextView mSelectedCurr;
    TextView mEthResult;
    TextView mBtcResult;
    Button mConvertCurr;
    Button mClose;

    // set up variables for capture intent
    String currSym;
    double ethRate;
    double btcRate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_conversion);

        // Retrieve saved data from intent action.
        Intent currIntent = getIntent();
        currSym = currIntent.getStringExtra(appConstant.CURR_SYM_Data);
        ethRate = currIntent.getDoubleExtra(appConstant.CURR_ETH_DATA_EXT, 0);
        btcRate = currIntent.getDoubleExtra(appConstant.CURR_BTC_DATA_EXT, 0);

        //Intialize the required views
        mInputAmount = (EditText)findViewById(R.id.input_amt);
        mSelectedCurr = (TextView)findViewById(R.id.sel_curr_data);
        mEthResult = (TextView)findViewById(R.id.eth_result_tv);
        mBtcResult = (TextView)findViewById(R.id.btc_result_tv);
        mConvertCurr = (Button) findViewById(R.id.btnConvert);
        mClose = (Button) findViewById(R.id.btnClose);

        // Calculation and parse to the required Textviews
        mConvertCurr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mInputAmount.setTransformationMethod(null);
                mSelectedCurr.setText(currSym);
                double inputAmount = Double.parseDouble(mInputAmount.getText().toString());
                mEthResult.setText(String.format("%1$,.2f", (inputAmount * ethRate)));
                mBtcResult.setText(String.format("%1$,.2f", (inputAmount * btcRate)));

            }
        });

        // Close
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
