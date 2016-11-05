package com.izv.dam.newquip.util;


import android.content.Context;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.io.StreamUtil;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

public class ImprimirPDF {

    private final static String NOMBRE_CARPETA_APP = "pdfs";
    private static Context c;

    public static void imprimir(Context c) {
        Document documento = new Document(PageSize.LETTER);
        String nombreArchivo = "nota.pdf";
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();

        File pdfDir = new File(tarjetaSD + File.separator + NOMBRE_CARPETA_APP);

        if(!pdfDir.exists()){
            pdfDir.mkdir();
        }

        String nombre_completo = Environment.getExternalStorageDirectory() + File.separator + NOMBRE_CARPETA_APP + File.separator + nombreArchivo;

        File outPutFile = new File(nombre_completo);

        if(outPutFile.exists()){
            outPutFile.delete();
        }

        try {
            PdfWriter pdf = PdfWriter.getInstance(documento, new FileOutputStream(nombre_completo));

            documento.open();
            documento.addAuthor("Quip");
            documento.addCreator("Quip");
            documento.addSubject("Pdf creado para probar");
            documento.addCreationDate();
            documento.addTitle("Nota de prueba");

            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            String htmlPDF = "<html><head></head><body><h1>Funciona</h1></body></html>";

            try {
                worker.parseXHtml(pdf, documento, new StringReader(htmlPDF));
                documento.close();
                Toast.makeText(c, "PDF esta generado", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}