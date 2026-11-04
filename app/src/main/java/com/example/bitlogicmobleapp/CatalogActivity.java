package com.example.bitlogicmobleapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    private final List<Articulo> todosLosArticulos = new ArrayList<>();
    private final ArticuloJsonRepository repositorio = new ArticuloJsonRepository();

    private ArticuloAdapter adapter;
    private ArrayAdapter<String> adapterCategorias;
    private EditText buscador;
    private Spinner filtroCategoria;

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
        buscador = findViewById(R.id.input_search);
        filtroCategoria = findViewById(R.id.spinner_category);
        Button btnImportar = findViewById(R.id.btn_import_json);

        adapter = new ArticuloAdapter(this, new ArrayList<>());
        listaCatalogo.setAdapter(adapter);
        configurarFiltros();

        header.setOnClickListener(v -> finish());

        listaCatalogo.setOnItemClickListener((parent, fila, posicion, idFila) -> {
            Articulo seleccionado = adapter.getItem(posicion);
            if (seleccionado != null) {
                abrirDetalle(seleccionado);
            }
        });

        ActivityResultLauncher<String[]> selectorJson = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        importarJson(uri);
                    }
                }
        );

        btnImportar.setOnClickListener(v -> selectorJson.launch(new String[]{
                "application/json",
                "text/json",
                "text/plain"
        }));

        cargarCatalogoInicial();
    }

    /** Carga el JSON importado anteriormente o, si no existe, el incluido en assets. */
    private void cargarCatalogoInicial() {
        try {
            actualizarCatalogo(repositorio.cargarCatalogo(this));
        } catch (IOException e) {
            Log.e(TAG, "No se pudo cargar el catálogo", e);
            Toast.makeText(this, R.string.catalog_load_error, Toast.LENGTH_LONG).show();
        }
    }

    /** Lee el archivo elegido, guarda una copia interna y actualiza la pantalla. */
    private void importarJson(Uri uri) {
        try {
            List<Articulo> articulos = repositorio.cargarDesdeUri(this, uri);
            repositorio.guardarEnAlmacenamientoInterno(this, articulos);
            actualizarCatalogo(articulos);
            Toast.makeText(
                    this,
                    getString(R.string.import_json_success, articulos.size()),
                    Toast.LENGTH_LONG
            ).show();
        } catch (IOException e) {
            Log.e(TAG, "El archivo seleccionado no es un catálogo válido", e);
            Toast.makeText(this, R.string.import_json_error, Toast.LENGTH_LONG).show();
        }
    }

    private void actualizarCatalogo(List<Articulo> articulos) {
        todosLosArticulos.clear();
        todosLosArticulos.addAll(articulos);
        actualizarCategorias();

        filtroCategoria.setSelection(0);
        buscador.setText("");
        aplicarFiltros();
    }

    /** Abre la pantalla de detalle y le entrega los datos del artículo. */
    private void abrirDetalle(Articulo articulo) {
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra(ArticleActivity.EXTRA_ID, articulo.getId());
        intent.putExtra(ArticleActivity.EXTRA_NOMBRE, articulo.getNombre());
        intent.putExtra(ArticleActivity.EXTRA_CATEGORIA, articulo.getCategoria());
        intent.putExtra(ArticleActivity.EXTRA_STOCK, articulo.getStock());
        startActivity(intent);
    }

    /** Configura el buscador y el selector de categoría. */
    private void configurarFiltros() {
        List<String> categorias = new ArrayList<>();
        categorias.add(getString(R.string.all_categories));

        adapterCategorias = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categorias
        );
        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filtroCategoria.setAdapter(adapterCategorias);

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
                aplicarFiltros();
            }
        });

        filtroCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(
                    AdapterView<?> parent,
                    View view,
                    int position,
                    long id
            ) {
                aplicarFiltros();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Siempre hay al menos la opción "Todas las categorías".
            }
        });
    }

    private void actualizarCategorias() {
        List<String> categorias = new ArrayList<>();
        categorias.add(getString(R.string.all_categories));

        for (Articulo articulo : todosLosArticulos) {
            if (!categorias.contains(articulo.getCategoria())) {
                categorias.add(articulo.getCategoria());
            }
        }

        adapterCategorias.clear();
        adapterCategorias.addAll(categorias);
        adapterCategorias.notifyDataSetChanged();
    }

    /** Aplica a la vez el texto buscado y la categoría seleccionada. */
    private void aplicarFiltros() {
        String consulta = buscador.getText().toString().trim().toLowerCase(Locale.ROOT);
        String categoriaSeleccionada = filtroCategoria.getSelectedItem().toString();
        List<Articulo> coincidencias = new ArrayList<>();

        for (Articulo articulo : todosLosArticulos) {
            String nombre = articulo.getNombre().toLowerCase(Locale.ROOT);
            String categoria = articulo.getCategoria().toLowerCase(Locale.ROOT);

            boolean coincideTexto = consulta.isEmpty()
                    || nombre.contains(consulta)
                    || categoria.contains(consulta);
            boolean coincideCategoria = categoriaSeleccionada.equals(getString(R.string.all_categories))
                    || articulo.getCategoria().equals(categoriaSeleccionada);

            if (coincideTexto && coincideCategoria) {
                coincidencias.add(articulo);
            }
        }

        adapter.clear();
        adapter.addAll(coincidencias);
    }
}
