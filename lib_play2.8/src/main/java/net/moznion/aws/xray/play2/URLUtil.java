package net.moznion.aws.xray.play2;

import java.net.URI;
import play.mvc.Http;

class URLUtil {
  static String buildURLForXRay(final Http.Request req) {
    return buildURLForXRay(URI.create(req.uri()));
  }

  static String buildURLForXRay(final String url) {
    return buildURLForXRay(URI.create(url));
  }

  private static String buildURLForXRay(final URI uri) {
    return uri.getScheme() + "://" + uri.getHost() + ":" + uri.getPort() + "/" + uri.getPath();
  }
}
