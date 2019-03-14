package e.mamtanegi.vehicledetection;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;


public class Utils {

    private static ProgressDialog progressDialog = null;

    public static String getFileExtension(Uri uri, Context context) {
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    public static void showProgressDialog(Context context, boolean flag) {
        progressDialog = new ProgressDialog(context);
        if (flag) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        progressDialog.setContentView(R.layout.custom_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

    }

    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}

