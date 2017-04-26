package org.gemini.round_corner;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import java.util.concurrent.atomic.AtomicInteger;

public class RCService extends Service {
  private static final AtomicInteger running = new AtomicInteger();
  private RCView tl;
  private RCView tr;
  private RCView br;
  private RCView bl;

  @Override
  public IBinder onBind(final Intent intent) {
    return null;
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

  private int x(final float degree, final int size) {
    switch ((int)degree) {
      case 0: return 0;
      case 90: return screenWidth() - size;
      case 180: return screenWidth() - size;
      case 270: return 0;
    }
    assert false;
    return 0;
  }

  private int y(final float degree, final int size) {
    switch ((int)degree) {
      case 0: return 0;
      case 90: return 0;
      case 180: return screenHeight() - size;
      case 270: return screenHeight() - size;
    }
    assert false;
    return 0;
  }

  @SuppressWarnings( "deprecation" )
  @TargetApi(17)
  private int screenWidth() {
    WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
    try {
      Point size = new Point();
      wm.getDefaultDisplay().getRealSize(size);
      return size.x;
    } catch (Exception ex) {
      return wm.getDefaultDisplay().getWidth();
    }
  }

  @SuppressWarnings( "deprecation" )
  @TargetApi(17)
  private int screenHeight() {
    WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
    try {
      Point size = new Point();
      wm.getDefaultDisplay().getRealSize(size);
      return size.y;
    } catch (Exception ex) {
      return wm.getDefaultDisplay().getHeight();
    }
  }

  private RCView bind(final WindowManager wm, final float degree) {
    RCView v = new RCView(this, degree);
    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
        v.width(),
        v.height(),
        x(degree, v.width()),
        y(degree, v.height()),
        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
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

  @Override
  public int onStartCommand(
      final Intent intent, final int flags, final int startId) {
    if (running.compareAndSet(0, 1)) {
      return START_STICKY;
    } else {
      stopSelf(startId);
      return START_NOT_STICKY;
    }
  }
}
