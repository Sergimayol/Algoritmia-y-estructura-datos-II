package com.example.paraulogic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        // Asignamos un scrollbar al textview para visualizar
        // todas las soluciones si es necsario
        TextView textViewResultados = findViewById(R.id.solutions);
        textViewResultados.setMovementMethod(new ScrollingMovementMethod());
        // Get the Intent that started this activity
        Intent intent = getIntent();
        // and extract the string
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = (TextView) findViewById(R.id.solutions);
        textView.setText(Html.fromHtml(message));
    }
}