package com.example.quachtaibuu.phuotapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Quach Tai Buu on 2017-08-08.
 */

public class ImageUtils {

    public interface OnGetFileListener {
        public void OnComplete(File file);
    }

    public static void getFileFromUrl(Context context, Uri uri, final OnGetFileListener onGetFileListener) {

        final String tmpPath = context.getApplicationInfo().dataDir + "/tmp/" + new Date().getTime() + ".jpg";

        Glide.with(context).load(uri).asBitmap().toBytes(Bitmap.CompressFormat.JPEG, 100).into(new SimpleTarget<byte[]>() {
            @Override
            public void onResourceReady(final byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                new AsyncTask<Void, Void, Void>() {
                    @Override protected Void doInBackground(Void... params) {
                        File file = new File(tmpPath);
                        File dir = file.getParentFile();
                        try {
                            if (!dir.mkdirs() && (!dir.exists() || !dir.isDirectory())) {
                                throw new IOException("Cannot ensure parent directory for file " + file);
                            }
                            BufferedOutputStream s = new BufferedOutputStream(new FileOutputStream(file));
                            s.write(resource);
                            s.flush();
                            s.close();

                            onGetFileListener.OnComplete(file);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                }.execute();
            }
        });
    }

}
