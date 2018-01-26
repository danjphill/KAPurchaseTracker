package com.danacosoftware.purchasetracker;

/**
 * Created by Daniel Phillips on 8/23/2016.
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class ZipManager {
    private static final int BUFFER = 80000;
    private static final int BUFFER_SIZE = 80000;


    public static void zip(String[] files, String zipFile) throws IOException {
        BufferedInputStream origin = null;
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        try {
            byte data[] = new byte[BUFFER_SIZE];

            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
                try {

                    ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                        out.write(data, 0, count);
                    }
                }
                finally {
                    origin.close();
                }
            }
        }
        finally {
            out.close();
        }
    }

    public void unzip(String _zipFile, Context context) {
        File Dir1 = new File (context.getExternalFilesDir(null)+"/DS_KAT/Images/");
        File Dir2 = new File (context.getExternalCacheDir()+"/");
        //create target location folder if not exist
        dirChecker(Dir1.toString());

        try {
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {

                //create dir if required while unzipping
                if (ze.isDirectory()) {
                    dirChecker(ze.getName());
                } else {
                    FileOutputStream fout;
                    if(ze.getName().contains(".jpeg")) {
                        fout = new FileOutputStream(Dir1.toString() + "/" + ze.getName());
                    }else{
                        fout = new FileOutputStream(Dir2.toString() + "/" + ze.getName());
                        Log.d("ZipManager - Unzip",Dir2 + ze.getName());
                    }
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }

                    zin.closeEntry();
                    fout.close();
                }

            }
            zin.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void dirChecker(String dir) {
        File f = new File(dir);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }

}
