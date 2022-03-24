package com.example.androidmergesort;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int listLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.listLength = 1;
        Button button = (Button)findViewById(R.id.buttonMerge);
        button.setOnClickListener(this);
    }

    public int[] generateRandomArray(int n) {
        int[] lista = new int[n];
        Random ran;
        try {
            ran = new Random(12);
            for (int i = 0; i < lista.length; i++) {
                lista[i] = ran.nextInt(10000);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return lista;
    }

    private int getListLength(){
        EditText campoTexto = (EditText) findViewById(R.id.campoLength);
        String num = campoTexto.getText().toString();
        return Integer.parseInt(num);
    }

    @Override
    public void onClick(View view) {
        this.listLength = getListLength();
        int[] list = generateRandomArray(this.listLength);
        changeTextViewText("Lista original = " + ArrayToString(list),R.id.ListaOG);
        MergeSort mergeSort = new MergeSort();
        mergeSort.sort(list,0,list.length - 1);
        changeTextViewText("Lista ordenada = " + ArrayToString(list),R.id.ListaOrd);
    }

    private void changeTextViewText(String s, int i){
        TextView textView = (TextView) findViewById(i);
        textView.setText(s);
    }

    private String ArrayToString(@NonNull int[] arr){
        String listString = "";
        for (int i = 0; i< arr.length;i++){
            if (i == 0){
                listString += arr[i];
            }else{
                listString += " ," + arr[i];
            }
        }
        return listString;
    }

}