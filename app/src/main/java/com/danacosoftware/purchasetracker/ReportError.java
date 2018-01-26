package com.danacosoftware.purchasetracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.google.android.gms.drive.Drive;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.TimeZone;

/**
 * Created by Daniel Phillips on 8/25/2016.
 */
public class ReportError {
    private String GetLogCat() {
        StringBuilder log = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec("logcat -d -v threadtime *:*");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));


            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
                log.append('\n');
            }
            return log.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return log.toString();
            //handle exception
        }
    }
    public java.io.File WriteErrorLog(String Log, Context context) {
        DateTime dt = new DateTime(DateTimeZone.forTimeZone(TimeZone.getDefault()));
        String DateandTime = dt.toString("MMM_dd_yyyy-HH_mm_ss_SS");
        File Folder = new File(context.getExternalCacheDir()+"/Reports/");
        File ItemsListFile = new File(context.getExternalCacheDir() + "/Reports/Report_"+DateandTime+".txt");
        Folder.mkdirs();
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(ItemsListFile.toString()));
            outputStreamWriter.write(Log);
            outputStreamWriter.close();
            return ItemsListFile;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }

    }
    public void NewReport(final Context context){
        DateTime dt = new DateTime(DateTimeZone.forTimeZone(TimeZone.getDefault()));
        final String DateandTime = dt.toString(" MMM dd yyyy - HH:mm:ss.SS");
        new AlertDialogWrapper.Builder(context)
                .setTitle("Error Report")
                .setMessage("Would You Like To Report An Error?")
                .setPositiveButton("Report", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/txt");
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Purchase Tracker Error Report");
                            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "danacosoftwaretester@gmail.com" });
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + WriteErrorLog(GetLogCat(),context)));
                            intent.putExtra(Intent.EXTRA_TEXT, "Description of Error:\n\n\n\n\n\nDanaco Software Purchase Tracker Error Report \nDate and Time:"+DateandTime);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            context.startActivity(Intent.createChooser(intent, "Send Email"));
                            Toast.makeText(context, "Report Generation Successful" , Toast.LENGTH_LONG).show();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                            Toast.makeText(context.getApplicationContext(), "Report Generation Unsuccessful" , Toast.LENGTH_LONG).show();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
