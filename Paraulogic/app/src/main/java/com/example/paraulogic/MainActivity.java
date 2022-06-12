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
    // Atributo de tipo boolean para comprobar si por lo menos hay un tuti
    // en el conjunto de soluciones.
    private boolean isTuti;
    // Contiene el mensaje a pasar a la pantalla de soluciones.
    private StringBuilder mensajeSoluciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    /**
     * Inicializa la aplicación y los conjuntos.
     */
    private void init() {
        this.listaIDbotones = new int[7];
        this.listaIDbotones[0] = R.id.letraObligatoria;
        this.listaIDbotones[1] = R.id.letra1;
        this.listaIDbotones[2] = R.id.letra2;
        this.listaIDbotones[3] = R.id.letra3;
        this.listaIDbotones[4] = R.id.letra4;
        this.listaIDbotones[5] = R.id.letra5;
        this.listaIDbotones[6] = R.id.letra6;
        this.isTuti = false;
        // Hasta que el conjunto no tenga un tuti no se inicializa
        while (!isTuti) {
            this.mapping = new BSTMapping();
            this.listaLetras = new char[7];
            this.clavesMapping = new ArrayList<>();
            this.listaPalabras = new TreeSet<>();
            this.mensajeSoluciones = new StringBuilder();
            initLetters();
        }
    }

    /**
     * Comprueba si hay algun tuti en el conjunto de soluciones.
     */
    private void initLetters() {
        configLetters();
        getDictionary();
        Iterator it = this.listaPalabras.iterator();
        String res = "";
        while (it.hasNext()) {
            res = (String) it.next();
            // Comprobar si hay algun tuti
            if (checkTuti(res.toUpperCase())) {
                this.isTuti = true;
                String aux = "<font color = 'red'>";
                this.mensajeSoluciones.append(aux);
                this.mensajeSoluciones.append(res);
                this.mensajeSoluciones.append(" </ font >");
            } else {
                this.mensajeSoluciones.append(res);
            }
            this.mensajeSoluciones.append(", ");
        }
        if (this.mensajeSoluciones.length() > 0) {
            // Elimina el espacio y coma de la última solución
            this.mensajeSoluciones.deleteCharAt(this.mensajeSoluciones.length() - 1);
            this.mensajeSoluciones.deleteCharAt(this.mensajeSoluciones.length() - 1);
        }
    }

    /**
     * Comprueba si la palabra pasada por parámetro es un tuti
     *
     * @param pal Palabra a comprobar.
     * @return true si es tuti, false si no lo es.
     */
    private boolean checkTuti(@NonNull String pal) {
        return pal.contains("" + listaLetras[0]) && pal.contains("" + listaLetras[1])
                && pal.contains("" + listaLetras[2]) && pal.contains("" + listaLetras[3])
                && pal.contains("" + listaLetras[4]) && pal.contains("" + listaLetras[5])
                && pal.contains("" + listaLetras[6]);
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
        Random rand = new Random();
        int randomIndexToSwap;
        for (int i = 1; i < listaLetras.length; i++) {
            randomIndexToSwap = 0;
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
            CharSequence text = " PALABRA INCORRECTA! ";
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
     */
    private void configLetters() {
        this.conjuntoLetras = new UnsortedArraySet<>(7);
        Random ran = new Random();
        char[] vocales = {'A', 'E', 'I', 'O', 'U'};
        char aux = vocales[ran.nextInt(5)];
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
        //System.out.println(this.listaLetras);
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
     */
    public void showInfo(View view) {
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra(EXTRA_MESSAGE, this.mensajeSoluciones.toString());
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
            boolean contiene;
            while (line != null) {
                contiene = true;
                // Comprobar si cumple las conciones para ser una posible solucion
                if (line.length() >= 3) { // Minimo 3 caracteres
                    for (int i = 0; i < line.length(); i++) {
                        if (!this.conjuntoLetras.contains(line.toUpperCase().charAt(i))) {
                            contiene = false;
                            break;
                        }
                    }
                    if (contiene) {
                        // Añadir a la lista de palabras
                        this.listaPalabras.add(line);
                    }
                }
                line = br.readLine();
            }
            br.close();
            //System.out.println("Lista: " + this.listaPalabras.toString());
        } catch (IOException e) {
            System.out.println("Error al leer el diccionario, error: " + e.getMessage());
        }
    }
}