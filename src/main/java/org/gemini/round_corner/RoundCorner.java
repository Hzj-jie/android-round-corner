package org.gemini.round_corner;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class RoundCorner extends Activity {
  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    startService(new Intent(
        RCService.USER_ACTION, Uri.EMPTY, this, RCService.class));
    finish();
  }
}
