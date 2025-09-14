package mx.santander.h2h.monitoreoapi.config;
//config
//para
//evitar
//errores 
//del /
//queda
//ok
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
//cachamos el error si es que lo hubiera
//en esta aprte
//del head
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RootOkFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {
    if ("/".equals(req.getRequestURI()) && ("GET".equals(req.getMethod()) || "HEAD".equals(req.getMethod()))) {
      res.setStatus(HttpServletResponse.SC_OK);
      res.setContentType("text/plain");
      if (!"HEAD".equals(req.getMethod())) {
        res.getWriter().write("ok");
      }
      return;
    }
    chain.doFilter(req, res);
  }
}
