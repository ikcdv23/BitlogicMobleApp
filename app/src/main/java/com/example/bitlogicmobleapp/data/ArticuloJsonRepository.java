package com.example.bitlogicmobleapp.data;

import android.content.Context;

import com.example.bitlogicmobleapp.models.Articulo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/** Lee y guarda los artículos sin mezclar esta tarea con la pantalla. */
public class ArticuloJsonRepository {

    private static final String NOMBRE_ARCHIVO_JSON = "articulos.json";
    private final ObjectMapper mapper = new ObjectMapper();

    /** Lee la lista inicial incluida en app/src/main/assets. */
    public List<Articulo> cargarDesdeAssets(Context context) throws IOException {
        try (InputStream archivoJson = context.getAssets().open(NOMBRE_ARCHIVO_JSON)) {
            Articulo[] articulos = mapper.readValue(archivoJson, Articulo[].class);
            return Arrays.asList(articulos);
        }
    }

    /** Guarda la lista en el almacenamiento privado de la aplicación. */
    public void guardarEnAlmacenamientoInterno(
            Context context,
            List<Articulo> articulos
    ) throws IOException {
        File archivoJson = new File(context.getFilesDir(), NOMBRE_ARCHIVO_JSON);
        mapper.writeValue(archivoJson, articulos);
    }
}
