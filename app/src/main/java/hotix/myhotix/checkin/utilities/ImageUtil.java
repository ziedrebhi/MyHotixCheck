package hotix.myhotix.checkin.utilities;

/**
 * Created by ziedrebhi on 28/03/2017.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class ImageUtil {
    public static Bitmap convert(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int bytes = bitmap.getByteCount();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
        Log.i("Zied",String.valueOf(bytes));
        Log.i("Zied 2",String.valueOf(outputStream.toByteArray().length));
         /*

         */

        int base64Length = outputStream.toByteArray().length / 3 * 4; // Strictly integer division
        if (outputStream.toByteArray().length % 3 != 0)
        {
            base64Length += 4; // Extra padding characters will be added
        }

        Log.i("Zied 3",outputStream.toByteArray() + " bytes will be encoded in " + base64Length + " characters.");

        return Base64.encodeToString(outputStream.toByteArray(), Base64.URL_SAFE | Base64.NO_WRAP);
    }

}