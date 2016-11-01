package com.izv.dam.newquip.util;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.izv.dam.newquip.R;

/**
 * Created by dam on 27/10/16.
 */

public class Permisos  extends AppCompatActivity {

    private static final int SOLICITUD_PERMISO_CAMARA = 1;
    private Intent camara;

    public void Permisoscamara(Context c ){
        camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        /**
         * ¿Tengo el permiso para hacer la accion?
         */                                                                             ///PERMISO CONCENDIDO
        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(camara);
            Toast.makeText(this, "1 Permiso Concedido", Toast.LENGTH_SHORT).show();
        } else {
            explicarUsoPermiso();
            solicitarPermisoHacerLlamada();
        }
    }

    private void explicarUsoPermiso() {
        //Este IF es necesario para saber si el usuario ha marcado o no la casilla [] No volver a preguntar
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "2.1 Explicamos razonadamente porque necesitamos el permiso", Toast.LENGTH_SHORT).show();
            //Explicarle al usuario porque necesitas el permiso (Opcional)
            alertDialogBasico();
        }
    }

    private void solicitarPermisoHacerLlamada() {
        //Pedimos el permiso o los permisos con un cuadro de dialogo del sistema
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SOLICITUD_PERMISO_CAMARA);

        Toast.makeText(this, "2.2 Pedimos el permiso con un cuadro de dialogo del sistema", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /**
         * Si tubieramos diferentes permisos solicitando permisos de la aplicacion, aqui habria varios IF
         */
        if (requestCode == SOLICITUD_PERMISO_CAMARA) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Realizamos la accion
                startActivity(camara);
                Toast.makeText(this, "3.1 Permiso Concedido", Toast.LENGTH_SHORT).show();
            } else {
                //1-Seguimos el proceso de ejecucion sin esta accion: Esto lo recomienda Google
                //2-Cancelamos el proceso actual
                //3-Salimos de la aplicacion
                Toast.makeText(this, "3.2 Permiso No Concedido", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void alertDialogBasico() {


        // 1. Instancia de AlertDialog.Builder con este constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });


        builder.show();

    }

}
