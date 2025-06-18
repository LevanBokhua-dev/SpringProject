package com.example.demo.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ActuatorAdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String path = request.getRequestURI();

        // Allow /health and /info to everyone
        if (path.startsWith("/actuator") &&
                !path.equals("/actuator/health") &&
                !path.equals("/actuator/info")) {

            if (session == null || session.getAttribute("isAdmin") == null) {
                response.sendRedirect("/login/admin");
                return;
            }
        }

        chain.doFilter(req, res);
    }
}
