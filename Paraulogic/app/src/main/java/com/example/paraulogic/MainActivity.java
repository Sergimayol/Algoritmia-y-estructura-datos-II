package com.example.paraulogic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Iterator;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int[] listaIDbotones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.listaIDbotones = new int[7];
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
            configLetters();
        } else {
            Button button = (Button) findViewById(id);
            texto = button.getText().toString();
        }
        changeTextViewText(texto, R.id.displayletras, clear);
    }

    private void changeTextViewText(String s, int i, boolean clear) {
        TextView textView = (TextView) findViewById(i);
        if (clear) {
            textView.setText(s);
        } else {
            textView.append(s);
        }
    }

    private void changeTextButton(String s, int i) {
        Button b = (Button) findViewById(i);
        b.setText(s);
    }

    private void configLetters() {
        UnsortedArraySet<Character> letras = new UnsortedArraySet<>(7);
        Random rand;
        for (int i = 0; i < 7; i++) {
            rand = new Random();
            letras.add((char) (rand.nextInt(26) + 'a'));
        }
        rand = new Random();
        Iterator it = letras.iterator();
        for (int i = 0; it.hasNext(); i++) {
            int randomIndexToSwap = rand.nextInt(7);
            //Character aux =
            int temp = aux[randomIndexToSwap];
            aux[randomIndexToSwap] = aux[i];
            aux[i] = (char) temp;
        }
        for (int j = 0; j < aux.length; j++) {
            changeTextButton(String.valueOf(aux[j]), this.listaIDbotones[j]);
        }
    }

}