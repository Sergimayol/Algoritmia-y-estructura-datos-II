package com.example.paraulogic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int[] listaIDbotones;
    private UnsortedArraySet<Character> conjuntoLetras;
    private BSTMapping<String, Integer> mapping;

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
        this.mapping = new BSTMapping();
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
        if (clear) {
            textView.setText(s);
        } else {
            textView.append(s);
        }
    }

    private void changeTextButton(@NonNull Character s, int i) {
        Button b = (Button) findViewById(i);
        b.setText(s.toString());
    }

    /*
     * Configuración aleatoria del conjunto de letras e adición letras botones.
     * Este método crea un conjunto de 7 letras alearotias y asigna a los botones
     * las letars del cojunto.
     */
    private void configLetters() {
        this.conjuntoLetras = new UnsortedArraySet<>(7);
        Random ran = new Random();
        for (int i = 0; i < 7; i++) {
            if (!conjuntoLetras.add((char) (ran.nextInt(26) + 'A'))) {
                i--;
            }
        }
        Iterator it = conjuntoLetras.iterator();
        int j = 0;
        while (it.hasNext()) {
            changeTextButton((Character) it.next(), this.listaIDbotones[j]);
            j++;
        }
    }

    private void shuffleLetters() {
        Iterator it = this.conjuntoLetras.iterator();
        ArrayList<Character> arr = new ArrayList<>();
        while (it.hasNext()) {
            arr.add((Character) it.next());
        }
        Character c = arr.remove(0);
        this.conjuntoLetras = new UnsortedArraySet(7);
        Random rand = new Random();
        for (int i = 0; i < arr.size(); i++) {
            int randomIndexToSwap = rand.nextInt(arr.size());
            Character temp = arr.get(randomIndexToSwap);
            arr.set(randomIndexToSwap, arr.get(i));
            arr.set(i, temp);
        }
        this.conjuntoLetras.add(c);
        changeTextButton(c, this.listaIDbotones[0]);
        for (int i = 0; i < arr.size(); i++) {
            changeTextButton(arr.get(i), this.listaIDbotones[i + 1]);
            this.conjuntoLetras.add(arr.get(i));
        }
    }

}