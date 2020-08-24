package com.example.randomnumbergenerate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
TextView textViewRandomNumber;
ClipboardManager clipboardManager;
ClipData clipData;
public static final String PREFERENCE = "GENERATENUMER";
public static final String startingNumber = "NUMBER";
SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewRandomNumber = (TextView) findViewById(R.id.textViewRandomNumber);
        clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        String channel = (sharedPreferences.getString(startingNumber,"0000"));
        textViewRandomNumber.setText(channel);
    }
    public int generateRandomNumber(int size)
    {
        int min = 0;
        int max = 9;
        int[] random = new int[size];
        int generatedNumber = 0;
        for(int i = 1;i < size+1;i++)
        {
            random[i-1] = new Random().nextInt(max - min)+1;
            int test = (int) (random[i-1]*Math.pow(10,i-1));
            generatedNumber = generatedNumber+test;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(startingNumber,Integer.toString(generatedNumber));
        editor.commit();
        return generatedNumber;
    }

    public void onClickGenerate(View view) {
        int number = generateRandomNumber(4);
        String numberString = Integer.toString(number);
        textViewRandomNumber.setText(numberString);
    }

    public void onClickCopyText(View view) {
        String text = textViewRandomNumber.getText().toString();
        clipData = ClipData.newPlainText("text",text);
        clipboardManager.setPrimaryClip(clipData);
    }

    public void onClickSend(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String title = "Send Message...";
        String message = textViewRandomNumber.getText().toString();
        intent.putExtra(Intent.EXTRA_TEXT,message);
        intent.setType("text/plain");
        Intent chooser = Intent.createChooser(intent,title);
        startActivity(chooser);
    }
}