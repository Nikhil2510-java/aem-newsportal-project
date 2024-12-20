package com.bhasaka.newsportal.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/newsportal/news-downloadServlet")
public class NewsDownloadServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String category = request.getParameter("category");

        String fileName = null;
        if ("politics".equalsIgnoreCase(category)) {
            fileName = "politics.docx";
        } else if ("sports".equalsIgnoreCase(category)) {
            fileName = "sports.docx";
        } else if ("entertainment".equalsIgnoreCase(category)) {
            fileName = "entertainment.docx";
        } else if ("technology".equalsIgnoreCase(category)) {
            fileName = "technology.docx";
        }

        if (fileName != null) {
            ResourceResolver resourceResolver = request.getResourceResolver();
            Resource fileResource = resourceResolver.getResource("/content/dam/news-report/" + fileName);

            if (fileResource != null) {
                response.setContentType("application/msword");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

                File file = fileResource.adaptTo(File.class);
                if (file != null) {
                    try (FileInputStream fis = new FileInputStream(file);
                         OutputStream out = response.getOutputStream()) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) > 0) {
                            out.write(buffer, 0, length);
                        }
                    }
                } else {
                    response.getWriter().write("File not found: " + fileName);
                }
            } else {
                response.getWriter().write("File not found: " + fileName);
            }
        } else {
            response.getWriter().write("Invalid category: " + category);
        }
    }
}
