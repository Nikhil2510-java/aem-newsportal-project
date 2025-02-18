package com.bhasaka.newsportal.core.servlets;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/selectorCount",
                "sling.servlet.methods=GET",
                "sling.servlet.selectors={one,two}",
                "sling.servlet.extensions=txt"
        })
public class SelectorServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        String[] selectors = request.getRequestPathInfo().getSelectors();
        if(ArrayUtils.isNotEmpty(selectors)) {
            response.getWriter().write("Selector value : "+selectors.length +"\n");
            for (int i = 0; i < selectors.length; i++) {
                response.getWriter().write("Selector value "+(i+1)+" : "+ selectors[i]+"\n");
            }
        }
        else {
            response.getWriter().write("No selector available");
        }

    }
}
