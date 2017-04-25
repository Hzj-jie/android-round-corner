package org.gemini.round_corner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class RCView extends View {
  private final Paint paint;

  public RCView(final Context context, final float degree) {
    super(context);
    paint = new Paint();
    paint.setAntiAlias(true);
    paint.setTextSize(10);
    paint.setARGB(255, 255, 0, 0);
  }

  public int size() {
    return 100;
  }

  @Override
  protected void onDraw(final Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawText("Hello World", 5, 15, paint);
  }
}
