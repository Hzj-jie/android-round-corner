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
  private final Bitmap bitmap;
  private final Matrix matrix;
  private final int width;
  private final int height;
  private final float scale;

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
    {
      Bitmap bitmap = null;
      bitmap = loadImage(degrees);
      if (bitmap == null) {
        this.bitmap = BitmapFactory.decodeResource(
            getResources(), R.drawable.default_png);
        this.scale = 0.18f;
      } else {
        this.bitmap = bitmap;
        this.scale = 1.0f; // Resources.getSystem().getDisplayMetrics().density;
      }
    }
    this.matrix = new Matrix();
    {
      final int center = Math.min(bitmap.getWidth() / 2, bitmap.getHeight() / 2);
      this.matrix.postRotate(degrees, center, center);
      this.matrix.postScale(this.scale, this.scale);
    }
    {
      int width = (int)(bitmap.getWidth() * this.scale);
      int height = (int)(bitmap.getHeight() * this.scale);
      if (degrees == 90 || degrees == 270) {
        int temp = height;
        height = width;
        width = temp;
      }
      this.width = width;
      this.height = height;
    }
  }

  private static Bitmap loadImage(final float degrees) {
    String[] files = {
      degrees + ".png",
      degrees + ".jpg",
      degrees + ".jpeg",
      degrees + ".bmp",
      "png",
      "jpg",
      "jpeg",
      "bmp",
    };
    Bitmap bitmap = null;
    for (final String file : files) {
      bitmap = loadImage(file);
      if (bitmap != null) {
        return bitmap;
      }
    }
    return null;
  }

  private static Bitmap loadImage(final String type) {
    return BitmapFactory.decodeFile(
        Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES) + "/org.gemini.round_corner/" + type);
  }

  public int width() {
    return width;
  }

  public int height() {
    return height;
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
