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
import com.izv.dam.newquip.vistas.notas.VistaNota;

/**
 * Created by dam on 27/10/16.
 */

public class Permisos {

    private static final int SOLICITUD_PERMISO_CAMARA = 1;
    private static Context c;

    public Permisos ( Context c ) {
        this.c = c;
    }

    public static boolean PermisosCamara( String[] permisos){
        boolean isPer = false;

        for ( String permiso : permisos ) {
            isPer = ActivityCompat.checkSelfPermission((VistaNota)c, permiso) == PackageManager.PERMISSION_GRANTED;
        }
        if (!isPer){
           solicitarPermisoHacerLlamada(permisos, c);
        }
        return isPer;
    }

    private static void solicitarPermisoHacerLlamada( String[] permisos, Context c) {
        //Pedimos el permiso o los permisos con un cuadro de dialogo del sistema
        ActivityCompat.requestPermissions((VistaNota)c, permisos, SOLICITUD_PERMISO_CAMARA);
    }


    public static boolean PermisosCamara1(){
        boolean isPer = false;

        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            isPer = ActivityCompat.checkSelfPermission(c, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        } else {
            solicitarPermisoHacerLlamada( c);
        }
        return isPer;
    }

    private static void solicitarPermisoHacerLlamada(Context c) {
        //Pedimos el permiso o los permisos con un cuadro de dialogo del sistema
        ActivityCompat.requestPermissions((VistaNota)c, new String[]{Manifest.permission.CAMERA}, SOLICITUD_PERMISO_CAMARA);
    }
}
