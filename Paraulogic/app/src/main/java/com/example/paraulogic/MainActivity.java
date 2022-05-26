package com.example.paraulogic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Clave para acceder a la lista de soluciones.
    public static final String EXTRA_MESSAGE = "show_info";
    // Array contenedor de los id de los botones.
    private int[] listaIDbotones;
    // Array contenedor de las letras.
    private char[] listaLetras;
    // Conjunto contenedor de las letras.
    private UnsortedArraySet<Character> conjuntoLetras;
    // Conjunto contenedor de las soluciones del usuario.
    private BSTMapping<String, Integer> mapping;
    // Lista contenedora de las claves del conjunto de las palabras encontradas.
    private ArrayList<String> clavesMapping;

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
        button = (Button) findViewById(R.id.introducir);
        button.setOnClickListener(this);
        this.mapping = new BSTMapping();
        this.listaLetras = new char[7];
        this.clavesMapping = new ArrayList<>();
        configLetters();
    }

    /**
     * Borra la última letra en el display de letras.
     * 
     * @param view
     */
    private void suprimir(View view) {
        int id = R.id.displayletras;
        TextView res = (TextView) findViewById(id);
        StringBuilder str = new StringBuilder(res.getText().toString());
        str.deleteCharAt(str.length() - 1);
        changeTextViewText(str.toString(), id, true);
    }

    /**
     * Mezcla las letras displonibles menos la letra obligatoria.
     * 
     * @Todo: Cambiar mezcla, no es neceserio que se mezcle el conjunto sino
     *        que solo se mezcle "listaLetras".
     */
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

    /**
     * Permite comporbar si la palabra introducida es correcta. Si es correcta
     * se añade al conjunto de soluciones encontradas por el usuario y se elimina
     * la palabra del display.
     * 
     * @Todo: Cambiar las condiciones de comprobación de la palabra. Adaptar al
     *        último modelo.
     */
    private void introducir() {
        int id = R.id.displayletras;
        TextView res = (TextView) findViewById(id);
        String pal = res.getText().toString();
        String aux = String.valueOf(this.listaLetras[0]);
        // Min 3 letras y Contener letra principal
        if ((pal.length() >= 3) && pal.contains(aux)) {
            Integer val = this.mapping.get(pal);
            if (val == null) {
                this.mapping.put(pal, 1);
                this.clavesMapping.add(pal);
                Collections.sort(this.clavesMapping);
            } else {
                this.mapping.put(pal, val + 1);
            }
            updateDisplayWords();
        }
        changeTextViewText(" ", id, false);
    }

    /**
     * 
     * @Todo: Eliminar --> Hacer un método para cada opción.
     */
    @Override
    public void onClick(View view) {
        String texto = "";
        boolean clear = false;
        int id = view.getId();
        if (id == R.id.suprimir) {
            TextView res = (TextView) findViewById(R.id.displayletras);
            StringBuilder str = new StringBuilder(res.getText().toString());
            str.deleteCharAt(str.length() - 1);
            texto = str.toString();
            clear = true;
        } else if (id == R.id.shuffle) {
            shuffleLetters();
        } else if (id == R.id.introducir) {
            TextView res = (TextView) findViewById(R.id.displayletras);
            String pal = res.getText().toString();
            String aux = String.valueOf(this.listaLetras[0]);
            // Min 3 letras y Contener letra principal
            if ((pal.length() >= 3) && pal.contains(aux)) {
                Integer val = this.mapping.get(pal);
                if (val == null) {
                    this.mapping.put(pal, 1);
                    this.clavesMapping.add(pal);
                    Collections.sort(this.clavesMapping);
                } else {
                    this.mapping.put(pal, val + 1);
                }
                updateDisplayWords();
                texto = " ";
            }
        } else {
            Button button = (Button) findViewById(id);
            texto = button.getText().toString();
        }
        changeTextViewText(texto, R.id.displayletras, clear);
    }

    /**
     * Añade la letra pulsada al display de letras.
     */
    private void addLettertoDisplay(View view) {
        int id = view.getId();
        Button button = (Button) findViewById(id);
        changeTextViewText(button.getText().toString(), id, false);
    }

    /**
     * Actualiza el display de palabras. Se muestran las palabras que se han
     * encontrado, además de la cantidad de veces que se han encontrado.
     */
    private void updateDisplayWords() {
        StringBuilder res = new StringBuilder("Has encontrado ");
        res.append(this.clavesMapping.size());
        res.append(" palabras: ");
        for (int i = 0; i < this.clavesMapping.size(); i++) {
            res.append(this.clavesMapping.get(i));
            res.append("(");
            res.append(this.mapping.get(this.clavesMapping.get(i)));
            res.append("), ");
        }
        res.deleteCharAt(res.length() - 1);
        changeTextViewText(res.toString(), R.id.palEncontradas, true);
    }

    /**
     * Cambia el texto de un TextView.
     * 
     * @param text
     *              Texto a mostrar.
     * @param id
     *              ID del TextView.
     * @param clear
     *              Indica si se borra el texto o no.
     */
    private void changeTextViewText(String s, int i, boolean clear) {
        TextView textView = (TextView) findViewById(i);
        if (clear) {
            textView.setText(s);
        } else {
            textView.append(s);
        }
    }

    /**
     * Cambia el texto de un Button.
     * 
     * @param c
     *           Letra a mostrar.
     * @param id
     *           ID del Button.
     */
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
        char aux;
        for (int i = 0; i < 7; i++) {
            aux = (char) (ran.nextInt(26) + 'A');
            if (!conjuntoLetras.add(aux)) {
                i--;
            } else {
                this.listaLetras[i] = aux;
            }
        }
        System.out.println(this.listaLetras);
        Iterator it = conjuntoLetras.iterator();
        int j = 0;
        while (it.hasNext()) {
            changeTextButton((Character) it.next(), this.listaIDbotones[j]);
            j++;
        }
    }

    /**
     * Muestra el conjunto disponible de soluciones de palabras que se pueden
     * crear a partir del conjunto de letras que se proporciona.
     * 
     * @Todo Implementar
     * 
     * @param view
     */
    private void showInfo(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        String message = " Hello world !";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}