package hotix.myhotix.checkin.activity;

/**
 * Created by ziedrebhi_2 on 08/09/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Images;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import hotix.myhotix.checkin.R;
import hotix.myhotix.checkin.utilities.ImageUtil;

public class CaptureSignature extends Activity {

    LinearLayout mContent;
    signature mSignature;
    Button mClear, mGetSign, mCancel;
    public static String tempDir;
    public int count = 1;
    public String current = null;
    private Bitmap mBitmap;
    View mView;
    File mypath;
    SharedPreferences pref;
    private String uniqueId;
    private EditText yourName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.signature);

        tempDir = Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.external_dir) + "/";
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir(getResources().getString(R.string.external_dir), Context.MODE_PRIVATE);

        prepareDirectory();
        uniqueId = getTodaysDate() + "_" + getCurrentTime() + "_" + Math.random();
        current = uniqueId + ".png";
        mypath = new File(directory, current);

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mContent = (LinearLayout) findViewById(R.id.linearLayout);
        mSignature = new signature(this, null);
        mSignature.setBackgroundColor(Color.WHITE);
        mContent.addView(mSignature, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        mClear = (Button) findViewById(R.id.clear);
        mGetSign = (Button) findViewById(R.id.getsign);
        mGetSign.setEnabled(false);
        mCancel = (Button) findViewById(R.id.cancel);
        mView = mContent;

        yourName = (EditText) findViewById(R.id.yourName);

        mClear.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Cleared");
                mSignature.clear();
                mGetSign.setEnabled(false);
            }
        });

        mGetSign.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Saved");
                boolean error = captureSignature();
                if (!error) {
                    mView.setDrawingCacheEnabled(true);
                    mSignature.save(mView);
                    Bundle b = new Bundle();
                    b.putString("status", "done");
                    b.putString("uniqueid", uniqueId);
                    Log.v("log_tag", "uniqueid =" + uniqueId);
                    String encodedSignature = "";

                    if (mBitmap != null) {
                        //encodedSignature = ImageUtil.convert(mBitmap);
                        // encodedSignature = Base64.encodeToString(byteArray,  Base64.URL_SAFE|Base64.NO_WRAP);
                       /*
                        InputStream inputStream = null;//You can get an inputStream using any IO API
                        try {
                            inputStream = new FileInputStream(mypath);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        byte[] bytes;
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        try {
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                output.write(buffer, 0, bytesRead);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        bytes = output.toByteArray();
                        Log.i("ZIED DEBUG", String.valueOf(bytes.length));
                        encodedSignature = Base64.encodeToString(bytes, Base64.URL_SAFE | Base64.NO_WRAP);
                        */
                        encodedSignature = ImageUtil.convert(mBitmap);
                    }
                    Log.v("log_tag", "image =" + encodedSignature);

                    b.putString("image", encodedSignature);
                    Intent intent = new Intent();
                    intent.putExtras(b);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        mCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Canceled");
                Bundle b = new Bundle();
                b.putString("status", "cancel");
                Intent intent = new Intent();
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        Log.w("GetSignature", "onDestory");
        super.onDestroy();
    }

    private boolean captureSignature() {

        boolean error = false;
        String errorMessage = "";


        if (yourName.getText().toString().equalsIgnoreCase("")) {
            errorMessage = errorMessage + getResources().getString(R.string.plzyourname) + "\n";
            error = true;
        }

        if (error) {
           /* Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();*/
            yourName.setError(errorMessage);
        }

        return error;
    }

    private String getTodaysDate() {

        final Calendar c = Calendar.getInstance();
        int todaysDate = (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));
        Log.w("DATE:", String.valueOf(todaysDate));
        return (String.valueOf(todaysDate));

    }

    private String getCurrentTime() {

        final Calendar c = Calendar.getInstance();
        int currentTime = (c.get(Calendar.HOUR_OF_DAY) * 10000) +
                (c.get(Calendar.MINUTE) * 100) +
                (c.get(Calendar.SECOND));
        Log.w("TIME:", String.valueOf(currentTime));
        return (String.valueOf(currentTime));

    }


    private boolean prepareDirectory() {
        try {
            if (makedirs()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Could not initiate File System.. Is Sdcard mounted properly?",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean makedirs() {
        File tempdir = new File(tempDir);
        if (!tempdir.exists())
            tempdir.mkdirs();

        if (tempdir.isDirectory()) {
            File[] files = tempdir.listFiles();
            for (File file : files) {
                if (!file.delete()) {
                    System.out.println("Failed to delete " + file);
                }
            }
        }
        return (tempdir.isDirectory());
    }

    public class signature extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v) {
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if (mBitmap == null) {
                mBitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(mBitmap);
            try {
                FileOutputStream mFileOutStream = new FileOutputStream(mypath);

                v.draw(canvas);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();
                String url = Images.Media.insertImage(getContentResolver(), mBitmap, uniqueId, null);
                Log.v("log_tag", "url: " + url);
                Log.v("log_tag", "mypath: " + mypath);
                //In case you want to delete the file
                //boolean deleted = mypath.delete();
                //Log.v("log_tag","deleted: " + mypath.toString() + deleted);
                //If you want to convert the image to string use base64 converter

            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }
}