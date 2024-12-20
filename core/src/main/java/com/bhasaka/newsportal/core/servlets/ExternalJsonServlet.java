package com.bhasaka.newsportal.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.servlet.Servlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component(
        service = { Servlet.class },
        property = {
                "sling.servlet.paths=/bin/example/externaljson",
                "sling.servlet.methods=GET"
        }
)
@ServiceDescription("Servlet to fetch JSON data from an external source")
public class ExternalJsonServlet extends SlingAllMethodsServlet {

    private static final String EXTERNAL_URL = "https://microsoftedge.github.io/Demos/json-dummy-data/64KB.json";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // Fetch data from the external URL
            URL url = new URL(EXTERNAL_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Read response
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            // Set response type
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Return fetched JSON data
            response.getWriter().write(json.toString());

        } catch (Exception e) {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Unable to fetch external data\"}");
        } finally {
            if (reader != null) reader.close();
            if (connection != null) connection.disconnect();
        }
    }
}
