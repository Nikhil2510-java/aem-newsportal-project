package com.bhasaka.newsportal.core.servlets;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.framework.Constants;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Country DataSource Servlet",
                "sling.servlet.paths=/bin/toGetJsonData",
                "sling.servlet.methods=GET"
        }
)
public class ToGetJsonDataServlet extends SlingAllMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        try {
            // Fetch data from the external API
            URL url = new URL("https://api.first.org/data/v1/countries");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // Check if response is successful
            if (connection.getResponseCode() == 200) {
                // Parse the JSON response
                JsonObject jsonResponse = JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                JsonObject countries = jsonResponse.getAsJsonObject("data");

                // Create a JSON array for the dropdown
                JsonArray jsonArray = new JsonArray();
                for (String key : countries.keySet()) {
                    JsonObject country = countries.getAsJsonObject(key);
                    String name = country.get("country").getAsString();
                    JsonObject countryJson = new JsonObject();
                    countryJson.addProperty("value", key);
                    countryJson.addProperty("text", name);
                    jsonArray.add(countryJson);
                }

                // Write the JSON array as the response
                response.getWriter().write(jsonArray.toString());
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Error fetching country data\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error: " + e.getMessage() + "\"}");
        }
    }
}
