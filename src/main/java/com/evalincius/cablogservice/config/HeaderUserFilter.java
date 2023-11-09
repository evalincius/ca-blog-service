package com.evalincius.cablogservice.config;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class HeaderUserFilter extends GenericFilterBean {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();
        String requestMethod = req.getMethod();

        if(path.startsWith("/api") == false){
            chain.doFilter(request, response);
            return;
        }

        String user = req.getHeader("X-User") == null ? "" : req.getHeader("X-User");
        log.info("User: " + user);

        if(requestMethod.equalsIgnoreCase("DELETE") && !user.equals("admin")){
            HttpServletResponse resp = (HttpServletResponse) response;
            String error = "Unouthorized access";

            resp.reset();
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentLength(error .length());
            response.getWriter().write(error);
        }else{
            chain.doFilter(request, response);
        }

    }

}
