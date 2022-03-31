package com.example.paraulogic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private int[] listaIDbotones;
    private char[] listaLetras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.listaIDbotones = new int[7];
        this.listaLetras = new char[7];
        this.listaIDbotones[0] = R.id.letraObligatoria;
        this.listaIDbotones[1] = R.id.letra1;
        this.listaIDbotones[2] = R.id.letra2;
        this.listaIDbotones[3] = R.id.letra3;
        this.listaIDbotones[4] = R.id.letra4;
        this.listaIDbotones[5] = R.id.letra5;
        this.listaIDbotones[6] = R.id.letra6;
        for (int i = 0; i < this.listaIDbotones.length; i++) {
            Button button = (Button) findViewById(this.listaIDbotones[i]);
            button.setOnClickListener(this);
        }
        Button button = (Button) findViewById(R.id.shuffle);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.suprimir);
        button.setOnClickListener(this);
        configLetters();
    }

    @Override
    public void onClick(View view) {
        String texto = "";
        boolean clear = false;
        int id = view.getId();
        if (id == R.id.suprimir) {
            texto = " ";
            clear = true;
        } else if (id == R.id.shuffle) {
            shuffleLetters();
        } else {
            Button button = (Button) findViewById(id);
            texto = button.getText().toString();
        }
        changeTextViewText(texto, R.id.displayletras, clear);
    }

    private void changeTextViewText(String s, int i, boolean clear) {
        TextView textView = (TextView) findViewById(i);
        if (clear){
            textView.setText(s);
        }else{
            textView.append(s);
        }
    }

    private void shuffleLetters(){
        Random rand = new Random();
        for (int i = 0; i < this.listaLetras.length; i++) {
            int randomIndexToSwap = rand.nextInt(this.listaLetras.length);
            int temp = this.listaLetras[randomIndexToSwap];
            this.listaLetras[randomIndexToSwap] = this.listaLetras[i];
            this.listaLetras[i] = (char) temp;
        }
        for (int j = 0; j<this.listaLetras.length; j++){
            changeTextViewText(String.valueOf(this.listaLetras[j]),this.listaIDbotones[j], false);
        }
    }

    private void configLetters(){

    }
}