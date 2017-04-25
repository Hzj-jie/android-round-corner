package org.gemini.round_corner;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;

public class RCService extends Service {
  private RCView tl;
  private RCView tr;
  private RCView br;
  private RCView bl;

  @Override
  public IBinder onBind(final Intent intent) {
    return null;
  }

  private static int screenWidth() {
    return Resources.getSystem().getDisplayMetrics().widthPixels;
  }

  private static int screenHeight() {
    return Resources.getSystem().getDisplayMetrics().heightPixels;
  }

  private static int gravity(final float degree) {
    return Gravity.TOP | Gravity.LEFT;
    /*
    switch ((int)degree) {
      case 0: return Gravity.TOP | Gravity.LEFT;
      case 90: return Gravity.TOP | Gravity.RIGHT;
      case 180: return Gravity.BOTTOM | Gravity.RIGHT;
      case 270: return Gravity.BOTTOM | Gravity.LEFT;
    }
    assert false;
    return 0;
    */
  }

  private static int x(final float degree, final int size) {
    switch ((int)degree) {
      case 0: return 0;
      case 90: return screenWidth() - size;
      case 180: return screenWidth() - size;
      case 270: return 0;
    }
    assert false;
    return 0;
  }

  private static int y(final float degree, final int size) {
    switch ((int)degree) {
      case 0: return 0;
      case 90: return 0;
      case 180: return screenHeight() - size;
      case 270: return screenHeight() - size;
    }
    assert false;
    return 0;
  }

  private RCView bind(final WindowManager wm, final float degree) {
    RCView v = new RCView(this, degree);
    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
        v.size(),
        v.size(),
        x(degree, v.size()),
        y(degree, v.size()),
        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSPARENT);
    params.gravity = gravity(degree);
    params.setTitle("RCView-" + degree);
    wm.addView(v, params);
    return v;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
    tl = bind(wm, 0);
    tr = bind(wm, 90);
    br = bind(wm, 180);
    bl = bind(wm, 270);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(tl);
    ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(tr);
    ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(br);
    ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(bl);
  }
}
