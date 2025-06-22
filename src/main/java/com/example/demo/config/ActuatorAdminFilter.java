package com.example.demo.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ActuatorAdminFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ActuatorAdminFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String path = request.getRequestURI();
        logger.debug("Incoming request to path: {}", path);

        boolean isRestrictedActuatorEndpoint =
                path.startsWith("/actuator") &&
                        !path.equals("/actuator/health") &&
                        !path.equals("/actuator/info");

        boolean isDashboardAccess =
                path.equals("/metrics-dashboard.html") || path.equals("/metrics-dashboard");

        if ((isRestrictedActuatorEndpoint || isDashboardAccess) &&
                (session == null || session.getAttribute("isAdmin") == null)) {
            logger.warn("Unauthorized access attempt to: {}", path);
            response.sendRedirect("/login/admin");
            return;
        }

        logger.debug("Access granted to: {}", path);
        chain.doFilter(req, res);
    }
}
