package com.rakshit.one.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.OpenableColumns;

import com.google.android.gms.common.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class FileLoader extends AsyncTask<Uri, Void, String> {
    private final WeakReference<Context> contextRef;
    public AsyncResponse delegate = null;

    public interface AsyncResponse {
        void fileLoadFinish(String result);
    }

    public FileLoader(Context ctx, AsyncResponse delegate) {
        contextRef = new WeakReference<>(ctx);
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(Uri... uris) {
        Context context = contextRef.get();
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = uris[0];
        try {
            String mimeType = contentResolver.getType(uri);
            Cursor returnCursor =
                    contentResolver.query(uri, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            String fileName = returnCursor.getString(nameIndex);
            InputStream inputStream = contentResolver.openInputStream(uri);
            File downloadDir =
                    context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            File f = new File(downloadDir + "/" + fileName);
            FileOutputStream out = new FileOutputStream(f);
            IOUtils.copyStream(inputStream, out);
            returnCursor.close();
            return f.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String result) {
        delegate.fileLoadFinish(result);
        super.onPostExecute(result);
    }
}
