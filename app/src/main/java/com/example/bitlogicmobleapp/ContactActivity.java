package com.example.bitlogicmobleapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contact_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnEmail = findViewById(R.id.btn_email);
        Button btnPhone = findViewById(R.id.btn_phone);
        TextView txtBack = findViewById(R.id.txt_back_contact);

        btnEmail.setOnClickListener(v -> abrirCorreo());
        btnPhone.setOnClickListener(v -> abrirTelefono());
        txtBack.setOnClickListener(v -> finish());
    }

    /** Abre una aplicación de correo con la dirección ya escrita. */
    private void abrirCorreo() {
        String email = getString(R.string.contact_email_address);
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_email_subject));
        abrirAplicacion(intent);
    }

    /** Abre el marcador; el usuario decide si finalmente realiza la llamada. */
    private void abrirTelefono() {
        String telefono = getString(R.string.contact_phone_number).replace(" ", "");
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telefono));
        abrirAplicacion(intent);
    }

    private void abrirAplicacion(Intent intent) {
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.contact_app_not_found, Toast.LENGTH_SHORT).show();
        }
    }
}
