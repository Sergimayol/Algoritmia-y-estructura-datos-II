package com.example.paraulogic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

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
    // Lista contenedora de las palabras que se pueden formar a partir 
    // del conjunto de letras.
    private TreeSet<String> listaPalabras;

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
        init();
    }

    private void init() {
        this.mapping = new BSTMapping();
        this.listaLetras = new char[7];
        this.clavesMapping = new ArrayList<>();
        this.listaPalabras = new TreeSet<>();
        configLetters();
        getDictionary();
    }

    /**
     * Borra la última letra en el display de letras.
     */
    public void suprimir(View view) {
        int id = R.id.displayletras;
        TextView res = (TextView) findViewById(id);
        StringBuilder str = new StringBuilder(res.getText().toString());
        if (str.length() > 0) {
            str.deleteCharAt(str.length() - 1);
        }
        changeTextViewText(str.toString(), id, true);
    }

    /**
     * Mezcla las letras displonibles menos la letra obligatoria.
     */
    public void shuffleLetters(View view) {
        /*Iterator it = this.conjuntoLetras.iterator();
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
        }*/
        Random rand = new Random();
        for (int i = 1; i < listaLetras.length; i++) {
            int randomIndexToSwap = 0;
            while (randomIndexToSwap == 0) {
                randomIndexToSwap = rand.nextInt(listaLetras.length);
            }
            char temp = listaLetras[randomIndexToSwap];
            listaLetras[randomIndexToSwap] = listaLetras[i];
            listaLetras[i] = temp;
        }
        for (int i = 0; i < listaLetras.length; i++) {
            changeTextButton(listaLetras[i], listaIDbotones[i]);
        }
    }

    /**
     * Permite comporbar si la palabra introducida es correcta. Si es correcta
     * se añade al conjunto de soluciones encontradas por el usuario y se elimina
     * la palabra del display.
     */
    public void introducir(View view) {
        int id = R.id.displayletras;
        TextView res = (TextView) findViewById(id);
        String pal = res.getText().toString();
        if (this.listaPalabras.contains(pal.toLowerCase())) {
            Integer val = this.mapping.get(pal);
            if (val == null) {
                this.mapping.put(pal, 1);
                this.clavesMapping.add(pal);
                Collections.sort(this.clavesMapping);
            } else {
                this.mapping.put(pal, val + 1);
            }
            updateDisplayWords();
            changeTextViewText("", id, true);
        } else {
            Context context = getApplicationContext();
            CharSequence text = " PALABRA INCORRECTA :c !";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    /**
     * Añade la letra pulsada al display de letras.
     */
    public void addLettertoDisplay(View view) {
        int id = view.getId();
        Button button = (Button) findViewById(id);
        changeTextViewText(button.getText().toString(), R.id.displayletras, false);
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
            res.append(this.clavesMapping.get(i).toLowerCase());
            res.append("(");
            res.append(this.mapping.get(this.clavesMapping.get(i)));
            res.append("), ");
        }
        res.deleteCharAt(res.length() - 1);
        res.deleteCharAt(res.length() - 1);
        changeTextViewText(res.toString(), R.id.palEncontradas, true);
    }

    /**
     * Cambia el texto de un TextView.
     *
     * @param text  Texto a mostrar.
     * @param id    ID del TextView.
     * @param clear Indica si se borra el texto o no.
     */
    private void changeTextViewText(String text, int id, boolean clear) {
        TextView textView = (TextView) findViewById(id);
        if (clear) {
            textView.setText(text);
        } else {
            textView.append(text);
        }
    }

    /**
     * Cambia el texto de un Button.
     *
     * @param c  Letra a mostrar.
     * @param id ID del Button.
     */
    private void changeTextButton(@NonNull Character c, int id) {
        Button b = (Button) findViewById(id);
        b.setText(c.toString());
    }

    /**
     * Configuración aleatoria del conjunto de letras e adición letras botones.
     * Este método crea un conjunto de 7 letras alearotias y asigna a los botones
     * las letars del cojunto.
     *
     * @TODO: Hacer que se cree sí o sí un tuti
     */
    private void configLetters() {
        this.conjuntoLetras = new UnsortedArraySet<>(7);
        Random ran = new Random();
        char aux = (char) (ran.nextInt(5) + 'A');
        this.listaLetras[0] = aux;
        conjuntoLetras.add(aux);
        for (int i = 1; i < 7; i++) {
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
     * @Todo Comprobar si hay algun tuti
     */
    public void showInfo(View view) {
        Intent intent = new Intent(this, InfoActivity.class);
        //Comprobar si hay algun tuti

        String message = this.listaPalabras.toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    /**
     * Lee el diccionario de palabras y analiza las palabras que pueden ser
     * una posible solución.
     */
    private void getDictionary() {
        try {
            InputStream is = getResources().openRawResource(R.raw.catala_filtrat);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            line = br.readLine();
            while (line != null) {
                boolean contiene = false;
                // Comprobar si cumple las conciones para ser una poasible solucion
                for (int i = 0; i < line.length(); i++) {
                    if (this.conjuntoLetras.contains(line.toUpperCase().charAt(i))) {
                        contiene = true;
                    } else {
                        contiene = false;
                        break;
                    }
                }
                if (contiene) {
                    // Añadir a la lista de palabras
                    this.listaPalabras.add(line);
                }
                line = br.readLine();
            }
            br.close();
            System.out.println("Lista: " + this.listaPalabras.toString());
        } catch (IOException e) {
            System.out.println("Error al leer el diccionario, error: " + e.getMessage());
        }
    }

    /*private boolean isTuti(String pal) {
        return pal.contains(listaLetras[0]) && pal.contains() && pal.contains() && pal.contains() && pal.contains()
                && pal.contains() && pal.contains();
    }*/
}