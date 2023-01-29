package net.moznion.aws.xray.play2;

import com.amazonaws.xray.entities.Segment;
import java.io.IOException;
import java.util.Optional;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;

public class TracedWSClient implements WSClient {
  private final WSClient wsClient;
  private final Optional<Segment> maybeSegment;

  public TracedWSClient(final WSClient wsClient, final Optional<Segment> maybeSegment) {
    this.wsClient = wsClient;
    this.maybeSegment = maybeSegment;
  }

  @Override
  public Object getUnderlying() {
    return wsClient.getUnderlying();
  }

  @Override
  public play.api.libs.ws.WSClient asScala() {
    return wsClient.asScala();
  }

  @Override
  public WSRequest url(String url) {
    return new TracedWSRequest(wsClient.url(url), this.maybeSegment);
  }

  @Override
  public void close() throws IOException {
    wsClient.close();
  }
}
