package com.example.bitlogicmobleapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bitlogicmobleapp.R;
import com.example.bitlogicmobleapp.models.Articulo;

import java.util.List;

/** Convierte cada artículo en una fila visible del catálogo. */
public class ArticuloAdapter extends ArrayAdapter<Articulo> {

    public ArticuloAdapter(Context context, List<Articulo> articulos) {
        super(context, 0, articulos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View fila = convertView;

        // Reutilizamos filas antiguas para no crear una vista nueva al hacer scroll.
        if (fila == null) {
            fila = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_layout, parent, false);
        }

        TextView id = fila.findViewById(R.id.txtId);
        TextView nombre = fila.findViewById(R.id.txtNombre);
        TextView categoria = fila.findViewById(R.id.txtCategoria);
        TextView stock = fila.findViewById(R.id.txtStock);
        ImageView imagen = fila.findViewById(R.id.imgArt);

        Articulo articulo = getItem(position);
        if (articulo != null) {
            id.setText(getContext().getString(R.string.article_id_format, articulo.getId()));
            nombre.setText(articulo.getNombre());
            categoria.setText(articulo.getCategoria());
            stock.setText(getContext().getString(R.string.stock_format, articulo.getStock()));

            // De momento todos usan el icono de la tienda; más adelante puede venir del modelo.
            imagen.setImageResource(R.drawable.ic_dice);
        }

        return fila;
    }
}
