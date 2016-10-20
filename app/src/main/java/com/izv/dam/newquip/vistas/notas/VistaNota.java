package com.izv.dam.newquip.vistas.notas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.contrato.ContratoNota;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.util.ListaPermisos;

import java.io.IOException;

import static android.R.attr.focusable;
import static android.widget.Toast.LENGTH_SHORT;

public class VistaNota extends AppCompatActivity implements ContratoNota.InterfaceVista {

    private EditText editTextTitulo, editTextNota;
    private Nota nota = new Nota();
    private PresentadorNota presentador;

    private ImageView imageView;
    Bitmap bmp = null;
    private ListaPermisos lp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        presentador = new PresentadorNota(this);

        editTextTitulo = (EditText) findViewById(R.id.etTitulo);
        editTextNota = (EditText) findViewById(R.id.etNota);

        if (savedInstanceState != null) {
            nota = savedInstanceState.getParcelable("nota");
        } else {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                nota = b.getParcelable("nota");
            }
        }
        mostrarNota(nota);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nota, menu);
        return true;
    }

    @Override


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == R.id.recordatorio) {
            //EditText titulo = (EditText) findViewById(R.id.recordatorio);
            Toast.makeText(getApplicationContext(), "aa", Toast.LENGTH_SHORT).show();
        }
        if(id == R.id.editar) {
            editTextTitulo.setEnabled(true);
            editTextNota.setEnabled(true);
        }
        if(id == R.id.galeria) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");//para que busque cualquier tipo de imagen
            startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), 200);//el 200 es para ver si da un valor positivo
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 200:
                if(resultCode == RESULT_OK) {

                    try {
                        lp =  new ListaPermisos();
                        bmp = MediaStore.Images.Media.getBitmap( getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(bmp);
                }
                break;
        }
       // http://android.pedro-varela.com/2015/11/solicitar-permisos-en-tiempo-de.html
    }

    @Override
    protected void onPause() {
        saveNota();
        presentador.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        presentador.onResume();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("nota", nota);
    }

    @Override
    public void mostrarNota(Nota n) {
        editTextTitulo.setText(nota.getTitulo());
        editTextNota.setText(nota.getNota());
    }

    private void saveNota() {
        nota.setTitulo(editTextTitulo.getText().toString());
        nota.setNota(editTextNota.getText().toString());
        long r = presentador.onSaveNota(nota);
        if (r > 0 & nota.getId() == 0) {
            nota.setId(r);
        }
    }

}