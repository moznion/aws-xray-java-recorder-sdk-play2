package net.moznion.aws.xray.play2;

import static net.moznion.aws.xray.play2.Constants.TOO_MANY_REQUESTS_HTTP_STATUS_CODE;

import akka.stream.javadsl.Source;
import akka.util.ByteString;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Namespace;
import com.amazonaws.xray.entities.Segment;
import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.entities.TraceHeader;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.File;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;
import org.w3c.dom.Document;
import play.libs.ws.BodyWritable;
import play.libs.ws.StandaloneWSRequest;
import play.libs.ws.WSAuthInfo;
import play.libs.ws.WSAuthScheme;
import play.libs.ws.WSCookie;
import play.libs.ws.WSRequest;
import play.libs.ws.WSRequestFilter;
import play.libs.ws.WSResponse;
import play.libs.ws.WSSignatureCalculator;
import play.mvc.Http;

public class TracedWSRequest implements WSRequest {
  private final WSRequest req;
  private final Optional<Segment> maybeParentSegment;

  public TracedWSRequest(final WSRequest req, final Optional<Segment> maybeParentSegment) {
    this.req = req;
    this.maybeParentSegment = maybeParentSegment;
  }

  @Override
  public CompletionStage<WSResponse> get() {
    return executeWithWrappingXRaySubSegment(req::get);
  }

  @Override
  public CompletionStage<WSResponse> patch(BodyWritable body) {
    return executeWithWrappingXRaySubSegment(() -> req.patch(body));
  }

  @Override
  public CompletionStage<WSResponse> patch(String body) {
    return executeWithWrappingXRaySubSegment(() -> req.patch(body));
  }

  @Override
  public CompletionStage<WSResponse> patch(JsonNode body) {
    return executeWithWrappingXRaySubSegment(() -> req.patch(body));
  }

  @Override
  public CompletionStage<WSResponse> patch(Document body) {
    return executeWithWrappingXRaySubSegment(() -> req.patch(body));
  }

  @Override
  @Deprecated
  public CompletionStage<WSResponse> patch(InputStream body) {
    return executeWithWrappingXRaySubSegment(() -> req.patch(body));
  }

  @Override
  public CompletionStage<WSResponse> patch(File body) {
    return executeWithWrappingXRaySubSegment(() -> req.patch(body));
  }

  @Override
  public CompletionStage<WSResponse> patch(
      Source<? super Http.MultipartFormData.Part<Source<ByteString, ?>>, ?> body) {
    return executeWithWrappingXRaySubSegment(() -> req.patch(body));
  }

  @Override
  public CompletionStage<WSResponse> post(BodyWritable body) {
    return executeWithWrappingXRaySubSegment(() -> req.post(body));
  }

  @Override
  public CompletionStage<WSResponse> post(String body) {
    return executeWithWrappingXRaySubSegment(() -> req.post(body));
  }

  @Override
  public CompletionStage<WSResponse> post(JsonNode body) {
    return executeWithWrappingXRaySubSegment(() -> req.post(body));
  }

  @Override
  public CompletionStage<WSResponse> post(Document body) {
    return executeWithWrappingXRaySubSegment(() -> req.post(body));
  }

  @Override
  @Deprecated
  public CompletionStage<WSResponse> post(InputStream body) {
    return executeWithWrappingXRaySubSegment(() -> req.post(body));
  }

  @Override
  public CompletionStage<WSResponse> post(File body) {
    return executeWithWrappingXRaySubSegment(() -> req.post(body));
  }

  @Override
  public CompletionStage<WSResponse> post(
      Source<? super Http.MultipartFormData.Part<Source<ByteString, ?>>, ?> body) {
    return executeWithWrappingXRaySubSegment(() -> req.post(body));
  }

  @Override
  public CompletionStage<WSResponse> put(BodyWritable body) {
    return executeWithWrappingXRaySubSegment(() -> req.put(body));
  }

  @Override
  public CompletionStage<WSResponse> put(String body) {
    return executeWithWrappingXRaySubSegment(() -> req.put(body));
  }

  @Override
  public CompletionStage<WSResponse> put(JsonNode body) {
    return executeWithWrappingXRaySubSegment(() -> req.put(body));
  }

  @Override
  public CompletionStage<WSResponse> put(Document body) {
    return executeWithWrappingXRaySubSegment(() -> req.put(body));
  }

  @Override
  @Deprecated
  public CompletionStage<WSResponse> put(InputStream body) {
    return executeWithWrappingXRaySubSegment(() -> req.put(body));
  }

  @Override
  public CompletionStage<WSResponse> put(File body) {
    return executeWithWrappingXRaySubSegment(() -> req.put(body));
  }

  @Override
  public CompletionStage<WSResponse> put(
      Source<? super Http.MultipartFormData.Part<Source<ByteString, ?>>, ?> body) {
    return executeWithWrappingXRaySubSegment(() -> req.put(body));
  }

  @Override
  public CompletionStage<WSResponse> delete() {
    return executeWithWrappingXRaySubSegment(req::delete);
  }

  @Override
  public CompletionStage<WSResponse> head() {
    return executeWithWrappingXRaySubSegment(req::head);
  }

  @Override
  public CompletionStage<WSResponse> options() {
    return executeWithWrappingXRaySubSegment(req::options);
  }

  @Override
  public CompletionStage<WSResponse> execute(String method) {
    return executeWithWrappingXRaySubSegment(() -> req.execute(method));
  }

  @Override
  public CompletionStage<WSResponse> execute() {
    return executeWithWrappingXRaySubSegment(req::execute);
  }

  @Override
  public CompletionStage<WSResponse> stream() {
    return executeWithWrappingXRaySubSegment(req::stream);
  }

  @Override
  public StandaloneWSRequest setUrl(String url) {
    return req.setUrl(url);
  }

  @Override
  public WSRequest setMethod(String method) {
    return req.setMethod(method);
  }

  @Override
  public WSRequest setBody(BodyWritable body) {
    return req.setBody(body);
  }

  @Override
  public WSRequest setBody(String body) {
    return req.setBody(body);
  }

  @Override
  public WSRequest setBody(JsonNode body) {
    return req.setBody(body);
  }

  @Override
  @Deprecated
  public WSRequest setBody(InputStream body) {
    return req.setBody(body);
  }

  @Override
  public WSRequest setBody(File body) {
    return req.setBody(body);
  }

  @Override
  public <U> WSRequest setBody(Source<ByteString, U> body) {
    return req.setBody(body);
  }

  @Override
  public WSRequest addHeader(String name, String value) {
    return req.addHeader(name, value);
  }

  @Override
  @Deprecated
  public WSRequest setHeader(String name, String value) {
    return req.setHeader(name, value);
  }

  @Override
  public WSRequest setHeaders(Map<String, List<String>> headers) {
    return req.setHeaders(headers);
  }

  @Override
  public WSRequest setQueryString(String query) {
    return req.setQueryString(query);
  }

  @Override
  public WSRequest setQueryString(Map<String, List<String>> params) {
    return req.setQueryString(params);
  }

  @Override
  public WSRequest addQueryParameter(String name, String value) {
    return req.addQueryParameter(name, value);
  }

  @Override
  @Deprecated
  public WSRequest setQueryParameter(String name, String value) {
    return req.setQueryParameter(name, value);
  }

  @Override
  public WSRequest addCookie(WSCookie cookie) {
    return req.addCookie(cookie);
  }

  @Override
  public WSRequest addCookie(Http.Cookie cookie) {
    return req.addCookie(cookie);
  }

  @Override
  public WSRequest addCookies(WSCookie... cookies) {
    return req.addCookies(cookies);
  }

  @Override
  public WSRequest setCookies(List<WSCookie> cookies) {
    return req.setCookies(cookies);
  }

  @Override
  public WSRequest setAuth(String userInfo) {
    return req.setAuth(userInfo);
  }

  @Override
  public WSRequest setAuth(String username, String password) {
    return req.setAuth(username, password);
  }

  @Override
  public WSRequest setAuth(String username, String password, WSAuthScheme scheme) {
    return req.setAuth(username, password, scheme);
  }

  @Override
  public StandaloneWSRequest setAuth(WSAuthInfo authInfo) {
    return req.setAuth(authInfo);
  }

  @Override
  public WSRequest sign(WSSignatureCalculator calculator) {
    return req.sign(calculator);
  }

  @Override
  public WSRequest setFollowRedirects(boolean followRedirects) {
    return req.setFollowRedirects(followRedirects);
  }

  @Override
  public WSRequest setVirtualHost(String virtualHost) {
    return req.setVirtualHost(virtualHost);
  }

  @Override
  public WSRequest setRequestTimeout(Duration timeout) {
    return req.setRequestTimeout(timeout);
  }

  @Override
  @Deprecated
  public WSRequest setRequestTimeout(long timeout) {
    return req.setRequestTimeout(timeout);
  }

  @Override
  public WSRequest setRequestFilter(WSRequestFilter filter) {
    return req.setRequestFilter(filter);
  }

  @Override
  public WSRequest setContentType(String contentType) {
    return req.setContentType(contentType);
  }

  @Override
  public String getUrl() {
    return req.getUrl();
  }

  @Override
  public Optional<BodyWritable> getBody() {
    return req.getBody();
  }

  @Override
  public Map<String, List<String>> getHeaders() {
    return req.getHeaders();
  }

  @Override
  public List<String> getHeaderValues(String name) {
    return req.getHeaderValues(name);
  }

  @Override
  public Optional<String> getHeader(String name) {
    return req.getHeader(name);
  }

  @Override
  public Map<String, List<String>> getQueryParameters() {
    return req.getQueryParameters();
  }

  @Override
  public Optional<WSAuthInfo> getAuth() {
    return req.getAuth();
  }

  @Override
  public Optional<WSSignatureCalculator> getCalculator() {
    return req.getCalculator();
  }

  @Override
  public Optional<Duration> getRequestTimeout() {
    return req.getRequestTimeout();
  }

  @Override
  public Optional<Boolean> getFollowRedirects() {
    return req.getFollowRedirects();
  }

  @Override
  public Optional<String> getContentType() {
    return req.getContentType();
  }

  private CompletionStage<WSResponse> executeWithWrappingXRaySubSegment(
      final Supplier<CompletionStage<WSResponse>> requestExecutorSupplier) {
    final Subsegment subsegment = AWSXRay.beginSubsegment(TracedWSRequest.class.getName());
    if (maybeParentSegment.isPresent()) {
      final Segment parentSegment = maybeParentSegment.get();
      subsegment.setTraceId(parentSegment.getTraceId());
      subsegment.setParentId(parentSegment.getId());
      subsegment.setParentSegment(parentSegment);
      parentSegment.addSubsegment(subsegment);
    }
    subsegment.setNamespace(Namespace.REMOTE.toString());

    if (subsegment.shouldPropagate()) {
      addHeader(TraceHeader.HEADER_KEY, TraceHeader.fromEntity(subsegment).toString());
    }

    String httpRequestMethod;
    try {
      httpRequestMethod = req.getMethod();
    } catch (RuntimeException e) {
      // to catch the `UnsupportedOperationException` because `StandaloneWSRequest` might throw that
      // exception to get a method.
      httpRequestMethod = "error";
    }

    subsegment.putHttp(
        "request",
        Map.of("url", URLUtil.buildURLForXRay(req.getUrl()), "method", httpRequestMethod));

    try {
      return requestExecutorSupplier
          .get()
          .whenCompleteAsync(
              (res, error) -> {
                try {
                  final int statusCode = res.getStatus();
                  if (400 <= statusCode && statusCode <= 499) {
                    subsegment.setError(true);
                    if (statusCode == TOO_MANY_REQUESTS_HTTP_STATUS_CODE) {
                      subsegment.setThrottle(true);
                    }
                  }
                  if (500 <= statusCode && statusCode <= 599) {
                    subsegment.setFault(true);
                  }
                  subsegment.putHttp(
                      "response",
                      Map.of("status", statusCode, "content_length", extractContentLength(res)));
                } finally {
                  try {
                    subsegment.close();
                  } catch (RuntimeException e) {
                    // NOP
                  }
                }
              });
    } catch (RuntimeException e) {
      // to catch the error from `req.XXX()`
      try {
        subsegment.close();
      } catch (RuntimeException ee) {
        // NOP
      }
      throw e;
    }
  }

  private int extractContentLength(final WSResponse res) {
    return res.getSingleHeader("content-length")
        .map(length -> Integer.parseInt(length, 10))
        .or(
            () -> {
              final String body = res.getBody();
              if (body == null) {
                return Optional.empty();
              }
              return Optional.of(body.length());
            })
        .orElse(-1);
  }
}
