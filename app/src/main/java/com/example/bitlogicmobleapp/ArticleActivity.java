package com.example.bitlogicmobleapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bitlogicmobleapp.adapters.ArticuloAdapter;
import com.example.bitlogicmobleapp.models.Articulo;

import java.util.ArrayList;

/*
 * TODO Próximos pasos:
 * 1. Hacer que esta clase sea una Activity, igual que CatalogActivity.
 * 2. Revisar el import que falta para recibir el estado de la pantalla.
 * 3. Comprobar el nombre exacto del método que Android llama al crear una Activity.
 * 4. Cargar el XML de detalle del artículo, no el XML del inicio de sesión.
 * 5. Usar el ID raíz declarado en activity_article.xml.
 * 6. Registrar esta pantalla en AndroidManifest.xml.
 * 7. Recibir el artículo seleccionado, buscar las vistas por su ID y mostrar sus datos.
 * 8. Hacer que el botón de volver cierre esta pantalla con finish().
 */
public class ArticleActivity extends AppCompatActivity {


    private static final String TAG = "CatalogActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_article);

        TextView volver = findViewById(R.id.btn_back_article);

        volver.setOnClickListener(v -> finish());

    }

    private void cargarArticulo() {

    }

}
