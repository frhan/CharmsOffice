package com.charmsoffice.mobilestudio.util;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.widget.Toast;

public class Util {
    public static void writeLogFileIntoExternalStorage(Context context, String dirName) {
        if (Util.isExternalStorageWritable()) {

            String fileFullPath = Environment.getExternalStorageDirectory() + dirName;
            File dir = new File(fileFullPath);
            boolean isDirCreated = dir.mkdirs();

            if (isDirCreated) {
                // writeLogToFile(dir);

            }

        } else {
            Toast.makeText(context, "External storage not available", Toast.LENGTH_SHORT).show();
        }

    }

    public static boolean isExternalStorageWritable() 
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) 
        {
            return true;
        }
        return false;
    }
    
    public static void showOkDialog(Context context, String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    

}
