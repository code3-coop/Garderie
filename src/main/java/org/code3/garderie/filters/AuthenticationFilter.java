package org.code3.garderie;

import java.util.List;
import java.util.Arrays;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
class AuthenticationFilter implements Filter {

  private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

 static final List<Pattern> PUBLIC_URLS_PATTERNS = Arrays.asList(
  "/login",
  ".*\\.css",
  ".*\\.js",
  ".*\\.png",
  ".*\\.map"
 ).stream().map((str) -> Pattern.compile(str)).collect(Collectors.toList());

 static final String LOGIN_URL = "/login";

 @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

    HttpServletResponse response = (HttpServletResponse) res;
    HttpServletRequest request = (HttpServletRequest) req;

    HttpSession session = request.getSession(true);
    String uri = request.getRequestURI();

    log.debug("request with uri {} and username {}", uri, session.getAttribute("username"));

    var requestedPublicResource = PUBLIC_URLS_PATTERNS.stream().anyMatch((pattern) -> pattern.matcher(uri).matches() );
    var userIsNotAuthentified = session.getAttribute("username") == null;

    if(!requestedPublicResource && userIsNotAuthentified){
      log.debug("will be redirected to login page");
      response.sendRedirect(request.getContextPath() + LOGIN_URL);

    } else {
      log.debug("procced to request");
      chain.doFilter(req, res);
    }

  }

  @Override
  public void destroy() {}

  @Override
  public void init(FilterConfig arg0) throws ServletException {}
}
