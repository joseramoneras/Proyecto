package com.izv.dam.newquip.util;


        import android.content.Context;
        import android.os.Environment;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;

        import org.w3c.dom.Document;

/**
 * Created by dam on 24/10/16.
 */

public class ImprimirPDF {

    private final static String NOMBRE_DOCUMENTO = "prueba.pdf";
    private final static String NOMBRE_DIRECTORIO = "pdfs";
    private static Context c;

    public static void imprimir(Context c){

        // Creamos el documento.
                Document documento = new Document();
        // Creamos el fichero con el nombre que deseemos.
                File f = crearFichero(NOMBRE_DOCUMENTO);

        // Creamos el flujo de datos de salida para el fichero donde guardaremos el pdf.
                FileOutputStream ficheroPdf = new FileOutputStream(f.getAbsolutePath());

        // Asociamos el flujo que acabamos de crear al documento.
                PdfWriter.getInstance(documento, ficheroPdf);

        // Abrimos el documento.
                documento.open();
    }

    public static File crearFichero(String nombreFichero) throws IOException {
        File ruta = getRuta();
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreFichero);
        return fichero;
    }

    public static File getRuta() {

        // El fichero será almacenado en un directorio dentro del directorio
        // Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            ruta = new File(
                    Environment
                            .getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DOWNLOADS), NOMBRE_DIRECTORIO);

            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                        return null;
                    }
                }
            }
        } else {
        }

        return ruta;
    }
