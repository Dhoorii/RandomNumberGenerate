package com.example.randomnumbergenerate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
TextView textViewRandomNumber;
ClipboardManager clipboardManager;
ClipData clipData;
public static final String PREFERENCE = "GENERATENUMER";
public static final String STTARTINGNUMBER = "NUMBER";
public static final String TEXTTOSEND = "TEXTTOSEND";
SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewRandomNumber = (TextView) findViewById(R.id.textViewRandomNumber);
        clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        String channel = (sharedPreferences.getString(STTARTINGNUMBER,"0000"));
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
        editor.putString(STTARTINGNUMBER,Integer.toString(generatedNumber));
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
        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        String textSaved = (sharedPreferences.getString(TEXTTOSEND,"${key}"));
        textSaved = textSaved.replaceAll("\\$\\(key\\)",message);
        intent.putExtra(Intent.EXTRA_TEXT,textSaved);
        intent.setType("text/plain");
        Intent chooser = Intent.createChooser(intent,title);
        startActivity(chooser);
    }

    public void onClickEdit(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View dView = getLayoutInflater().inflate(R.layout.dialoglayout,null);
        final EditText editText = dView.findViewById(R.id.editText);
        String channel = (sharedPreferences.getString(TEXTTOSEND, String.valueOf(R.string.InfoDetail)));
        editText.setText(channel);
        Button buttonSave = dView.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String savedText = editText.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TEXTTOSEND,savedText);
                editor.commit();
            }
        });
        builder.setView(dView);
        final AlertDialog dialog = builder.create();
        dialog.show();
    }
}