package com.example.bitlogicmobleapp.data;

import android.content.Context;
import android.net.Uri;

import com.example.bitlogicmobleapp.models.Articulo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/** Lee y guarda los artículos sin mezclar esta tarea con la pantalla. */
public class ArticuloJsonRepository {

    private static final String NOMBRE_ARCHIVO_JSON = "articulos.json";
    private final ObjectMapper mapper = new ObjectMapper();

    /** Usa primero el catálogo importado y recurre al de assets si todavía no existe. */
    public List<Articulo> cargarCatalogo(Context context) throws IOException {
        File archivoInterno = new File(context.getFilesDir(), NOMBRE_ARCHIVO_JSON);

        if (archivoInterno.exists()) {
            try (InputStream archivoJson = new FileInputStream(archivoInterno)) {
                return leerArticulos(archivoJson);
            }
        }

        return cargarDesdeAssets(context);
    }

    /** Lee la lista inicial incluida en app/src/main/assets. */
    public List<Articulo> cargarDesdeAssets(Context context) throws IOException {
        try (InputStream archivoJson = context.getAssets().open(NOMBRE_ARCHIVO_JSON)) {
            return leerArticulos(archivoJson);
        }
    }

    /** Lee el archivo que el usuario ha elegido con el selector de Android. */
    public List<Articulo> cargarDesdeUri(Context context, Uri uri) throws IOException {
        InputStream archivoJson = context.getContentResolver().openInputStream(uri);

        if (archivoJson == null) {
            throw new IOException("No se pudo abrir el archivo seleccionado");
        }

        try (InputStream archivo = archivoJson) {
            return leerArticulos(archivo);
        }
    }

    private List<Articulo> leerArticulos(InputStream archivoJson) throws IOException {
        Articulo[] articulos = mapper.readValue(archivoJson, Articulo[].class);

        if (articulos == null) {
            throw new IOException("El JSON no contiene una lista de artículos");
        }

        for (Articulo articulo : articulos) {
            if (articulo == null
                    || articulo.getNombre() == null
                    || articulo.getCategoria() == null) {
                throw new IOException("Hay un artículo incompleto en el JSON");
            }
        }

        return Arrays.asList(articulos);
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
