package org.gemini.round_corner;

import org.gemini.shared.KeepAliveReceiver;

public class RCReceiver extends KeepAliveReceiver {
  protected Class<?> serviceClass() {
    return RCService.class;
  }
}
