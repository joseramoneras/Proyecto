package com.izv.dam.newquip.vistas.notas;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.contrato.ContratoNota;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.util.ImprimirPDF;
import com.izv.dam.newquip.vistas.main.VistaQuip;

import java.io.File;
import java.io.IOException;

import static android.R.attr.path;

public class VistaNota extends AppCompatActivity implements ContratoNota.InterfaceVista {

    private EditText editTextTitulo, editTextNota;
    private Nota nota = new Nota();
    private PresentadorNota presentador;
    private static String APP_DIRECTORY = "MyPicture/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "QuipMedia";//carpeta que me crea
    private String nombreImagen;
    private String rutaImage;
    private final int GALERIA = 200;
    private final int CAMARA = 201;


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
            Toast.makeText(getApplicationContext(), "recordatorio", Toast.LENGTH_SHORT).show();
        }
        if(id == R.id.adjuntar){

        }
        if(id == R.id.editar) {
            editTextTitulo.setEnabled(true);
            editTextNota.setEnabled(true);
        }
        if(id == R.id.galeria) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");//para que busque cualquier tipo de imagen
            startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), GALERIA);
        }
        if(id == R.id.guardar){
            saveNota();
            Toast.makeText(getApplicationContext(), "Su nota ha sido guardada", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(VistaNota.this, VistaQuip.class);
            startActivity(intent);
        }
        if(id == R.id.camara){
            File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
            boolean isDirectoryCreated = file.exists();
            if(!isDirectoryCreated) isDirectoryCreated = file.mkdirs();
            if(isDirectoryCreated){
                Long timestamp = System.currentTimeMillis() / 1000;
                nombreImagen = timestamp.toString() + ".jpg";
                rutaImage = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator + nombreImagen;
                File newFile = new File(rutaImage);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
                startActivityForResult(intent, CAMARA);



                //https://www.youtube.com/watch?v=Nt5GMaFUvog
            }

        }
        if(id == R.id.imprimir){
            Toast.makeText(getApplicationContext(), "Se esta imprimiendo la nota...", Toast.LENGTH_SHORT).show();
            ImprimirPDF.imprimir(this);

        }
        if(id == android.R.id.home){
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
            } else {
                NavUtils.navigateUpTo(this, upIntent);
            }
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case GALERIA:
                if(resultCode == RESULT_OK) {
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    Bitmap bmp = null;
                    imageView.setVisibility(View.VISIBLE);
                    try {
                        bmp = MediaStore.Images.Media.getBitmap( getContentResolver(), data.getData());
                        imageView.setImageBitmap(bmp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CAMARA:
                if(resultCode == RESULT_OK) {

                    ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);
                    Bitmap bmp = null;
                    imageView2.setVisibility(View.VISIBLE);

                    //camara
                    MediaScannerConnection.scanFile(this, new String[]{rutaImage}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> Uri = " + uri);
                        }
                    });
                    bmp = BitmapFactory.decodeFile(rutaImage);
                    imageView2.setImageBitmap(bmp);
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        //saveNota();
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