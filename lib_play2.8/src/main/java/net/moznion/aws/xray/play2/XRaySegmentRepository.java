package net.moznion.aws.xray.play2;

import static net.moznion.aws.xray.play2.XRayTraceAction.XRAY_SEGMENT_KEY;

import com.amazonaws.xray.entities.Segment;
import java.util.Optional;
import play.mvc.Http;

public class XRaySegmentRepository {
  public static Optional<Segment> extractSegmentFrom(final Http.Request req) {
    return req.attrs().getOptional(XRAY_SEGMENT_KEY);
  }
}
