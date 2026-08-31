package org.fp024.mvcpractice.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.SessionTrackingMode;
import java.util.Set;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    // 💡 URL에 jsessionid가 노출되지 않도록 설정
    servletContext.setSessionTrackingModes(Set.of(SessionTrackingMode.COOKIE));
    super.onStartup(servletContext);
  }

  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class<?>[] {RootConfig.class};
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class<?>[] {ServletConfig.class};
  }

  @Override
  protected String[] getServletMappings() {
    return new String[] {"/"};
  }
}
