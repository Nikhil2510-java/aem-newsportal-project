package com.bhasaka.newsportal.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "= Json Data in dynamic Dropdown from REST API",
                "sling.servlet.paths=" + "/bin/jsonDataCountry",
                "sling.servlet.methods=GET"
        }
)
public class CountryDataSourceServlet extends SlingSafeMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryDataSourceServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {

        List<Resource> resourceList = new ArrayList<>();
        ResourceResolver resourceResolver = request.getResourceResolver();

        try {

            URL url = new URL("https://api.first.org/data/v1/countries");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (connection.getResponseCode() == 200) {
                String jsonResponse = IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8);
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONObject dataObject = jsonObject.getJSONObject("data");


                Iterator<String> keys = dataObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = dataObject.getJSONObject(key).getString("country");

                    Map<String, Object> properties = new HashMap<>();
                    properties.put("value", key);
                    properties.put("text", value);

                    resourceList.add(
                            new ValueMapResource(resourceResolver, new ResourceMetadata(), "nt:unstructured", new ValueMapDecorator(properties))
                    );
                }
            } else {
                LOGGER.error("Failed to fetch data from API. HTTP response code: {}", connection.getResponseCode());
            }

            connection.disconnect();
            DataSource dataSource = new SimpleDataSource(resourceList.iterator());
            request.setAttribute(DataSource.class.getName(), dataSource);

        } catch (Exception e) {
            LOGGER.error("Error while fetching data from API: {}", e.getMessage());
        }
    }
}