package org.gemini.round_corner;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Gravity;
import android.view.WindowManager;
import java.util.concurrent.atomic.AtomicInteger;
import org.gemini.shared.KeepAliveService;

public class RCService extends KeepAliveService {
  private RCView tl;
  private RCView tr;
  private RCView br;
  private RCView bl;
  private int width;
  private int height;

  private static int gravity(float degree) {
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

  private int x(float degree, int size) {
    switch ((int)degree) {
      case 0: return 0;
      case 90: return screenWidth() - size;
      case 180: return screenWidth() - size;
      case 270: return 0;
    }
    assert false;
    return 0;
  }

  private int y(float degree, int size) {
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
    } catch (Exception ex) {}

    try {
      Point size = new Point();
      wm.getDefaultDisplay().getSize(size);
      return size.x;
    } catch (Exception ex) {}

    return wm.getDefaultDisplay().getWidth();
  }

  @SuppressWarnings( "deprecation" )
  @TargetApi(17)
  private int screenHeight() {
    WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
    try {
      Point size = new Point();
      wm.getDefaultDisplay().getRealSize(size);
      return size.y;
    } catch (Exception ex) {}

    try {
      Point size = new Point();
      wm.getDefaultDisplay().getSize(size);
      return size.y;
    } catch (Exception ex) {}

    return wm.getDefaultDisplay().getHeight();
  }

  private RCView bind(WindowManager wm, float degree) {
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

  private void unbind(RCView view) {
    try {
      ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(view);
    } catch (Exception ex) {}
  }

  private boolean isStarted() {
    if (tl == null) {
      assert tr == null;
      assert br == null;
      assert bl == null;
      return false;
    }
    assert tr != null;
    assert br != null;
    assert bl != null;
    return true;
  }

  private boolean start() {
    if (!isStarted()) {
      WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
      tl = bind(wm, 0);
      tr = bind(wm, 90);
      br = bind(wm, 180);
      bl = bind(wm, 270);
      width = screenWidth();
      height = screenHeight();
      return true;
    } else {
      return false;
    }
  }

  private boolean stop() {
    if (isStarted()) {
      unbind(tl);
      unbind(tr);
      unbind(br);
      unbind(bl);
      tl = null;
      tr = null;
      br = null;
      bl = null;
      width = 0;
      height = 0;
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    if (isStarted() && (screenWidth() != width || screenHeight() != height)) {
      stop();
      start();
    }
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent != null &&
        intent.getAction() == null ||
        intent.getAction().length() == 0) {
      if (isStarted()) {
        stop();
        stopStickies();
        return discardCommand(startId);
      } else {
        start();
        return sticky(startId);
      }
    }
    return super.onStartCommand(intent, flags, startId);
  }
}
