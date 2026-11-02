package com.example.bitlogicmobleapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bitlogicmobleapp.adapters.ArticuloAdapter;
import com.example.bitlogicmobleapp.data.ArticuloJsonRepository;
import com.example.bitlogicmobleapp.models.Articulo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CatalogActivity extends AppCompatActivity {

    private static final String TAG = "CatalogActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catalog);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.catalog_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout header = findViewById(R.id.catalog_brand_header);
        ListView listaCatalogo = findViewById(R.id.listView);
        EditText buscador = findViewById(R.id.input_search);

        header.setOnClickListener(v -> finish());
        cargarCatalogo(listaCatalogo, buscador);
        listaCatalogo.setOnItemClickListener((parent, fila, posicion, idFila) -> {
            Articulo seleccionado = adapter.getItem(posicion);

            if (seleccionado == null) {
                return;
            }

            // TODO: crear el Intent
            // TODO: introducir los datos del artículo
            // TODO: abrir ArticleActivity
        });
    }

    /** Une los datos del JSON con la lista que aparece en pantalla. */
    private void cargarCatalogo(ListView listaCatalogo, EditText buscador) {
        ArticuloJsonRepository repositorio = new ArticuloJsonRepository();

        try {
            List<Articulo> articulos = repositorio.cargarDesdeAssets(this);
            ArticuloAdapter adapter = new ArticuloAdapter(this, new ArrayList<>(articulos));

            listaCatalogo.setAdapter(adapter);
            configurarBuscador(buscador, articulos, adapter);
        } catch (IOException e) {
            Log.e(TAG, "No se pudo leer articulos.json", e);
            Toast.makeText(this, R.string.catalog_load_error, Toast.LENGTH_LONG).show();
        }
    }

    /** Actualiza la lista al escribir un nombre o una categoría. */
    private void configurarBuscador(
            EditText buscador,
            List<Articulo> todosLosArticulos,
            ArticuloAdapter adapter
    ) {
        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence texto, int inicio, int cantidad, int despues) {
                // No necesitamos actuar antes del cambio.
            }

            @Override
            public void onTextChanged(CharSequence texto, int inicio, int anterior, int cantidad) {
                // Esperamos a que el texto termine de cambiar.
            }

            @Override
            public void afterTextChanged(Editable texto) {
                String consulta = texto.toString().trim().toLowerCase(Locale.ROOT);
                List<Articulo> coincidencias = new ArrayList<>();

                for (Articulo articulo : todosLosArticulos) {
                    String nombre = articulo.getNombre().toLowerCase(Locale.ROOT);
                    String categoria = articulo.getCategoria().toLowerCase(Locale.ROOT);

                    if (consulta.isEmpty()
                            || nombre.contains(consulta)
                            || categoria.contains(consulta)) {
                        coincidencias.add(articulo);
                    }
                }

                adapter.clear();
                adapter.addAll(coincidencias);
            }
        });
    }

    public statis void
}
