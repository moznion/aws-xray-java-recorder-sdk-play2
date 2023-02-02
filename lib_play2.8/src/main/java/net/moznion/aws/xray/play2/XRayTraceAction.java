package net.moznion.aws.xray.play2;

import static net.moznion.aws.xray.play2.Constants.TOO_MANY_REQUESTS_HTTP_STATUS_CODE;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Segment;
import com.amazonaws.xray.entities.TraceHeader;
import com.amazonaws.xray.entities.TraceID;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import play.Logger;
import play.libs.typedmap.TypedKey;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

public class XRayTraceAction extends Action.Simple {
  private static final Logger.ALogger logger = Logger.of(XRayTraceAction.class);

  public static final TypedKey<Segment> XRAY_SEGMENT_KEY = TypedKey.create("xraySegment");

  @Override
  public CompletionStage<Result> call(Http.Request req) {
    final Optional<String> maybeTraceId = req.getHeaders().get(TraceHeader.HEADER_KEY);
    final TraceHeader traceHeader =
        maybeTraceId.map(TraceHeader::fromString).orElse(new TraceHeader(TraceID.create()));
    if (traceHeader.getRootTraceId() == null) {
      traceHeader.setRootTraceId(TraceID.create());
    }

    final Segment segment = AWSXRay.beginSegment(XRayTraceAction.class.getName());
    segment.setTraceId(traceHeader.getRootTraceId());
    segment.putHttp(
        "request",
        Map.of(
            "url", URLUtil.buildURLForXRay(req),
            "method", req.method(),
            "user_agent", req.getHeaders().get("user-agent").orElse(""),
            "client_ip", req.getHeaders().get("x-forwarded-for").orElse(req.remoteAddress())));
    logger.debug(
        "begin a segment; traceId={}, id={}", traceHeader.getRootTraceId(), segment.getId());

    try {
      return delegate
          .call(req.addAttr(XRAY_SEGMENT_KEY, segment))
          .whenCompleteAsync(
              (res, error) ->
                  segment.run(
                      () -> {
                        try {
                          segment.putHttp(
                              "response",
                              Map.of(
                                  "status", res.status(),
                                  "content_length", res.body().contentLength().orElse(-1L)));

                          final int statusCode = res.status();
                          if (400 <= statusCode && statusCode <= 499) {
                            segment.setError(true);
                            if (statusCode == TOO_MANY_REQUESTS_HTTP_STATUS_CODE) {
                              segment.setThrottle(true);
                            }
                          }
                          if (500 <= statusCode && statusCode <= 599) {
                            segment.setFault(true);
                          }
                        } finally {
                          try {
                            segment.close();
                            logger.debug(
                                "segment closed; traceId={}, id={}",
                                traceHeader.getRootTraceId(),
                                segment.getId());
                          } catch (RuntimeException ee) {
                            logger.error(
                                "failed to close a segment; traceId={}, id={}",
                                traceHeader.getRootTraceId(),
                                segment.getId());
                          }
                        }
                      }));
    } catch (RuntimeException e) {
      // to catch the error from `delegate.call()`.
      try {
        segment.close();
        logger.debug(
            "segment closed exceptionally; traceId={}, id={}",
            traceHeader.getRootTraceId(),
            segment.getId());
      } catch (RuntimeException ee) {
        logger.error(
            "failed to close a segment in exception catching; traceId={}, id={}",
            traceHeader.getRootTraceId(),
            segment.getId());
      }
      throw e;
    }
  }
}
