package hotix.myhotix.checkin.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class TextDrawable extends Drawable {

    private final String text;
    private final Paint paint;
    private final float sizee;

    public TextDrawable(String text, float size) {
        this.text = text;
        this.paint = new Paint();
        paint.setColor(Color.BLACK);
        Log.i("size", String.valueOf(size));
        paint.setTextSize(size);
        paint.setAntiAlias(true);
        this.sizee=size;
        paint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(text, 0, 5, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}