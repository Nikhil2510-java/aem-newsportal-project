package com.bhasaka.newsportal.core.services;


import com.bhasaka.newsportal.core.models.CountryDTO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(service = CountriesAPIServices.class,immediate = true)
public class CountriesAPIServices {

    private static final String CountryRestAPI="https://api.first.org/data/v1/countries";

    private static final Logger log = LoggerFactory.getLogger(CountriesAPIServices.class);

    public List<CountryDTO> getCountries() {
        List<CountryDTO> countries = new ArrayList<>();
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(CountryRestAPI);
            try (CloseableHttpResponse response = client.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                JsonObject data = jsonObject.getAsJsonObject("data");
                log.info("DATA: {}",data);

                for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
                    JsonObject countryData = entry.getValue().getAsJsonObject();
                    String name = countryData.get("country").getAsString();
                    countries.add(new CountryDTO(entry.getKey(), name));
                }
            }
        } catch (IOException e) {
            log.error("Error fetching data from CountryRestAPI endpoint ", e);
        }
        return countries;
    }
}



