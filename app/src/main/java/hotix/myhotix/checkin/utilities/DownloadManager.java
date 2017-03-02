package hotix.myhotix.checkin.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import hotix.myhotix.checkin.R;

public class DownloadManager extends AsyncTask<String, Integer, String> {

    private ProgressDialog progressDialog;
    private String TAG = "UpdateDownloadManager";
    private boolean installAfterDownload = true;
    private boolean downloaded = false;
    private Context mContext;

    /**
     * Constructor for the Download Manager. DO NOT USE ON YOUR OWN. All calls are through UpdateChecker
     *
     * @param context Activity context
     * @since API 1
     */
    public DownloadManager(Context context) {
        this(context, true);
    }

    /**
     * Constructor for the download manager. DO NOT USE ON YOUR OWN. All calls are handled through UpdateChecker.
     *
     * @param context              Activity context
     * @param installAfterDownload true if you want to install immediately after download, false otherwise
     * @since API 2
     */
    public DownloadManager(Context context, boolean installAfterDownload) {
        mContext = context;
        this.installAfterDownload = installAfterDownload;
    }

    /**
     * Checks to see if we have an active internet connection
     *
     * @return true if online, false otherwise
     * @since API 1
     */
    public boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Downloads the update file in a background task
     *
     * @since API 1
     */
    @Override
    protected String doInBackground(String... sUrl) {
        if (isOnline()) {
            try {
                URL url = new URL(sUrl[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                //set up some things on the connection
                connection.setRequestMethod("GET");
                connection.connect();
                Log.e(TAG, connection.getResponseMessage());

                // this will be useful so that you can show a typical 0-100% progress bar
                int fileLength = connection.getContentLength();
                Log.e(TAG, String.valueOf(fileLength));

                // download the file
                InputStream input = connection.getInputStream();

                FileOutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/MyHotix Check In.apk");

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / fileLength));

                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (IOException e) {
                Log.e(TAG, "There was an IOException when downloading the update file" + e.toString());
            }
        }
        return null;
    }

    /**
     * Updates our progress bar with the download information
     *
     * @since API 1
     */
    @Override
    protected void onProgressUpdate(Integer... changed) {
        progressDialog.setProgress(changed[0]);
    }

    /**
     * Sets up the progress dialog to notify user of download progress
     *
     * @since API 1
     */
    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage(mContext.getResources().getText(
                R.string.updating));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * Dismissed progress dialog, calls install() if installAfterDownload is true
     *
     * @since API 1
     */
    @Override
    protected void onPostExecute(String result) {
        progressDialog.dismiss();
        downloaded = true;
        if (installAfterDownload) {
            install();
        }
    }

    /**
     * Launches an Intent to install the apk update.
     *
     * @since API 2
     */
    public void install() {
        if (downloaded) {
            String filepath = Environment.getExternalStorageDirectory().getPath() + "/MyHotix Check In.apk";
            Uri fileLoc = Uri.fromFile(new File(filepath));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileLoc, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }
}
