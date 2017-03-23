package com.appdimension.pruebaopencv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.security.Principal;

public class PrincipalActivity extends AppCompatActivity {

//    static {
//        System.loadLibrary("opencv_java");
//        System.loadLibrary("MyLibs");
//    }
    Button buttonNormal, buttonGrises, buttonBordes, buttonFaceDetection, buttonHumanDetection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //((TextView)findViewById(R.id.textView)).setText(NativeClass.getMessageFromJNI());

        ((TextView)findViewById(R.id.textView)).setText("OpenCV");

        buttonNormal = (Button)findViewById(R.id.buttonNormal);
        buttonGrises = (Button)findViewById(R.id.buttonGrises);
        buttonBordes = (Button)findViewById(R.id.buttonBordes);
        buttonFaceDetection = (Button)findViewById(R.id.buttonFaceDetection);
        buttonHumanDetection = (Button)findViewById(R.id.buttonHumanDetection);

        buttonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, MainActivity.class);
                Bundle b = new Bundle();
                b.putString("tipo", "normal"); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);

            }
        });

        buttonGrises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, MainActivity.class);
                Bundle b = new Bundle();
                b.putString("tipo", "grises"); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);

            }
        });

        buttonBordes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, MainActivity.class);
                Bundle b = new Bundle();
                b.putString("tipo", "bordes"); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);

            }
        });

        buttonFaceDetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("logs","entro a facedetection");
                Intent intent = new Intent(PrincipalActivity.this, MainActivity.class);
                Bundle b = new Bundle();
                b.putString("tipo", "facedetection"); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });

        buttonHumanDetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("logs","entro a humandetection");
                Intent intent = new Intent(PrincipalActivity.this, MainActivity.class);
                Bundle b = new Bundle();
                b.putString("tipo", "humandetection"); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });
    }


}
