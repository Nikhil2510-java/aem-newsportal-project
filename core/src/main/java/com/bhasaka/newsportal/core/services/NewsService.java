package com.bhasaka.newsportal.core.services;

import com.bhasaka.newsportal.core.models.NewsInfoListModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpHeaders;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;

@Component(service = NewsService.class, immediate = true)
public class NewsService {

    private static final String API_URL = "http://localhost:4502/graphql/execute.json/newsportal/fetchingAllData";
    private static final Logger LOG = LoggerFactory.getLogger(NewsService.class);

    public NewsInfoListModel fetchDataFromJson() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(API_URL);

            // Add Basic Authentication header
            String username = "admin";
            String password = "admin";
            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            request.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuth);

            HttpResponse response = client.execute(request);
            LOG.info("HTTP Response Status: " + response.getStatusLine().getStatusCode());

            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream content = response.getEntity().getContent();
                InputStreamReader reader = new InputStreamReader(content);

                // Parse JSON response
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                JsonObject data = jsonObject.getAsJsonObject("data");
                JsonObject newsInfoList = data.getAsJsonObject("newsinfoList");

                // Get the "items" array from the JSON response
                Gson gson = new Gson();

                // Directly parse the first item in the array as a NewsInfoListModel
                if (newsInfoList.has("items") && newsInfoList.get("items").isJsonArray()) {
                    JsonObject item = newsInfoList.getAsJsonArray("items").get(0).getAsJsonObject();

                    // Map JSON fields to NewsInfoListModel fields
                    NewsInfoListModel model = gson.fromJson(item, NewsInfoListModel.class);


                    return model;
                } else {
                    LOG.error("No items array or items is empty in the response.");
                }
            } else {
                LOG.error("Failed to fetch data, HTTP Status: " + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            LOG.error("Error fetching data from GraphQL endpoint", e);
        }
        return null;
    }

}
