package org.gemini.round_corner;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;
import android.view.View;

public class RCView extends View {
  private static final String TAG = "RoundCornerView";
  private static final boolean DEBUGGING = false;
  private final Paint paint;
  private final float degrees;
  private final Bitmap bitmap;
  private final Matrix matrix;
  private final int width;
  private final int height;

  public RCView(final Context context, final float degrees) {
    super(context);
    if (DEBUGGING) {
      this.paint = new Paint();
      this.paint.setAntiAlias(true);
      this.paint.setTextSize(10);
      this.paint.setARGB(255, 255, 0, 0);
    } else {
      this.paint = null;
    }
    this.degrees = degrees;
    this.bitmap = loadImage();
    this.matrix = new Matrix();
    this.matrix.postRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
    this.matrix.postScale(scale(), scale());
    // Log.w(TAG, "bitmap size " + bitmap.getWidth() + " x " + bitmap.getHeight());
    this.width = (int)(bitmap.getWidth() * scale());
    this.height = (int)(bitmap.getHeight() * scale());
  }

  private Bitmap loadImage() {
    String[] files = {
      degrees + ".png",
      degrees + ".jpg",
      degrees + ".jpeg",
      degrees + ".bmp",
      ".png",
      ".jpg",
      ".jpeg",
      ".bmp",
    };
    Bitmap bitmap = null;
    for (final String file : files) {
      bitmap = loadImage(file);
      if (bitmap != null) {
        return bitmap;
      }
    }
    return BitmapFactory.decodeResource(getResources(), R.drawable.default_png);
  }

  private static Bitmap loadImage(final String type) {
    return BitmapFactory.decodeFile(
        Environment.DIRECTORY_PICTURES + "/org.gemini.round_corner" + type);
  }

  public int width() {
    return width;
  }

  public int height() {
    return height;
  }

  private float scale() {
    // return Resources.getSystem().getDisplayMetrics().density;
    return 1;
  }

  @Override
  protected void onDraw(final Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawBitmap(bitmap, matrix, null);
    if (DEBUGGING) {
      canvas.drawText("org.gemini.round_corner", 5, 15, paint);
    }
  }
}
