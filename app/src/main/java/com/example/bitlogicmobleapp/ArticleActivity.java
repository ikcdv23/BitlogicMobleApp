package com.example.bitlogicmobleapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ArticleActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "articulo_id";
    public static final String EXTRA_NOMBRE = "articulo_nombre";
    public static final String EXTRA_CATEGORIA = "articulo_categoria";
    public static final String EXTRA_STOCK = "articulo_stock";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_article);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.article_detail_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cargarArticulo();

        TextView volver = findViewById(R.id.btn_back_article);
        volver.setOnClickListener(v -> finish());
    }

    /** Recibe los datos enviados por el catálogo y los muestra en el XML. */
    private void cargarArticulo() {
        Intent intent = getIntent();
        int id = intent.getIntExtra(EXTRA_ID, -1);
        String nombre = intent.getStringExtra(EXTRA_NOMBRE);
        String categoria = intent.getStringExtra(EXTRA_CATEGORIA);
        int stock = intent.getIntExtra(EXTRA_STOCK, 0);

        TextView vistaNombre = findViewById(R.id.txt_article_name);
        TextView vistaCategoria = findViewById(R.id.txt_article_category);
        TextView vistaStock = findViewById(R.id.txt_article_stock);
        TextView vistaId = findViewById(R.id.txt_article_id);
        ImageView vistaImagen = findViewById(R.id.img_article_detail);

        vistaNombre.setText(nombre);
        vistaCategoria.setText(categoria);
        vistaStock.setText(getString(R.string.stock_format, stock));
        vistaId.setText(getString(R.string.article_id_format, id));

        // El modelo aún no guarda una imagen distinta para cada producto.
        vistaImagen.setImageResource(R.drawable.ic_dice);
    }
}
