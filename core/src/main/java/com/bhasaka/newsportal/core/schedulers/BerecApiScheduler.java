package com.bhasaka.newsportal.core.schedulers;

import com.bhasaka.newsportal.core.utils.AemUtils;
import com.day.cq.dam.api.AssetManager;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import com.lebara.core.utils.AemUtils;

import org.apache.poi.util.StringUtil;
import org.apache.sling.api.resource.*;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import static com.lebara.core.utils.LebaraConstants.CONTENT_TYPE_JSON;
import static org.apache.sling.api.servlets.HttpConstants.METHOD_GET;
import static org.apache.sling.api.servlets.HttpConstants.HEADER_ACCEPT;

import javax.jcr.Session;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component(service = Runnable.class, immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = BerecApiConfiguration.class)
public class BerecApiScheduler implements Runnable {
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String COUNTRY_CODE = "countrycode";
    private static final String COUNTRY_NAME = "countryname";
    private static final String API_COUNTRY_CODE = "country";
    private static final String COUNTRY_NAME_JSON = "country_name";

    private String cronExpression;
    private String berecAPI;
    private boolean isEnabled;
    private String damPath;
    private String schedulerName;

    @Reference
    private Scheduler scheduler;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private static final String SERVICE_NAME = "npsystemuser";

    private static final Map<String, Object> SERVICE_AUTH_INFO = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE,
            (Object) SERVICE_NAME);

    private static final Logger LOG = LoggerFactory.getLogger(BerecApiScheduler.class);

    private static final String COUNTRY_INFO_PATH = "/content/dam/newsportal/countryinfo/countryCodeWithName.xlsx";

    @Reference
    Replicator replicator;

    @Activate
    @Modified
    public void activate(BerecApiConfiguration config) {
        this.cronExpression = config.cronExpression();
        this.berecAPI = config.berecAPI();
        this.isEnabled = config.enable();
        this.damPath = config.damPath();
        this.schedulerName = config.schedulerName();
        LOG.info("berecAPI : {}", berecAPI);
        LOG.info("cronExpression : {}, berecAPI : {}, isEnabled: {}, damPath : {}, schedulerName : {}", cronExpression, berecAPI, isEnabled, damPath, schedulerName);
        if (isEnabled) {
            ScheduleOptions options = scheduler.EXPR(cronExpression);
            options.canRunConcurrently(false);
            options.name(schedulerName);
            scheduler.schedule(this, options);
        } else {
            scheduler.unschedule(schedulerName);
            LOG.info("Emergency Update Scheduler is disabled.");
        }
    }

    @Override
    public void run() {
        try {
            String jsonData = fetchDataFromApi();
            LOG.debug("Fetched data from API: {}", jsonData);
            if (jsonData != null && !jsonData.isEmpty()) {
                Map<String, String> countries = getExcelData(COUNTRY_INFO_PATH);
                if (!countries.isEmpty()) {
                    String modifiedData = updatedJson(jsonData, countries);
                    if (!modifiedData.isEmpty()) {
                        uploadJsonToDam(modifiedData);
                        LOG.info("Successfully uploaded modified data to DAM.");
                    } else {
                        LOG.error("Modified JSON data is empty.");
                    }
                } else {
                    LOG.error("No country data retrieved from the Excel file.");
                }
            } else {
                LOG.error("Fetched data from API is null or empty.");
            }
        } catch (Exception e) {
            LOG.error("Error occurred during Emergency Update Scheduler run", e);
        }
    }

    private String fetchDataFromApi() throws Exception {
        LOG.info("Fetching data from API: {}", berecAPI);
        String jsonData = null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(berecAPI);
            if (url != null) {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(METHOD_GET);
                connection.setRequestProperty(HEADER_ACCEPT, CONTENT_TYPE_JSON);
                connection.setConnectTimeout(60000);
                connection.setReadTimeout(60000);
                inputStream = connection.getInputStream();
                jsonData = new String(inputStream.readAllBytes());
            }
        } catch (Exception e) {
            LOG.error("Error fetching data from API: {}", berecAPI, e);
        } finally {
            if (connection != null)
                connection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }
        return jsonData;
    }

    private Map<String, String> getExcelData(String excelPath) {
        Map<String, String> countries = new HashMap<>();
        if (StringUtil.isBlank(excelPath)) {
            LOG.warn("Excel path is empty or null.");
            return countries;
        }

        try (ResourceResolver resolver = resourceResolverFactory.getServiceResourceResolver(SERVICE_AUTH_INFO)) {
            JsonObject jsonObject = AemUtils.getExcelAsJson(excelPath, resolver);
            if (jsonObject != null && !jsonObject.isJsonNull()) {
                LOG.info("ExcelJsonData: {}", jsonObject);
                jsonObject.entrySet().forEach(entry -> {
                    JsonArray sheetArray = entry.getValue().getAsJsonArray();
                    sheetArray.forEach(element -> {
                        JsonObject countryData = element.getAsJsonObject();
                        if (countryData.has(COUNTRY_CODE) && countryData.has(COUNTRY_NAME)) {
                            String countryCode = countryData.get(COUNTRY_CODE).getAsString();
                            String countryName = countryData.get(COUNTRY_NAME).getAsString();
                            countries.put(countryCode, countryName);
                        }
                    });
                });
            }
        } catch (LoginException | IOException e) {
            LOG.error("Error retrieving Excel data or processing JSON", e);
        }
        return countries;
    }

    private String updatedJson(String jsonDataFromAPI, Map<String, String> countries) {
        JsonArray jsonElements = new JsonParser().parse(jsonDataFromAPI).getAsJsonArray();
        jsonElements.forEach(element -> {
            JsonObject jsonObject = element.getAsJsonObject();
            if (jsonObject.has(API_COUNTRY_CODE)) {
                String countryCode = jsonObject.get(API_COUNTRY_CODE).getAsString();
                String countryName = countries.getOrDefault(countryCode, countryCode);
                jsonObject.addProperty(COUNTRY_NAME_JSON, countryName);
            }
        });
        return jsonElements.toString();
    }

    private void uploadJsonToDam(String jsonData) throws LoginException,
            IOException {
        AssetManager assetManager = null;
        InputStream inputStream = null;
        try (ResourceResolver resolver = resourceResolverFactory.getServiceResourceResolver(SERVICE_AUTH_INFO)) {
            if (resolver != null) {
                assetManager = resolver.adaptTo(AssetManager.class);
                if (assetManager != null) {
                    inputStream = new ByteArrayInputStream(jsonData.getBytes());
                    assetManager.createAsset(damPath, inputStream, CONTENT_TYPE_JSON, true);
                    resolver.commit();
                    replicator.replicate(resolver.adaptTo(Session.class), ReplicationActionType.ACTIVATE, damPath);
                    LOG.info("Successfully uploaded JSON to DAM at: {}", damPath);
                }
            }
        } catch (PersistenceException | ReplicationException e) {
            LOG.error("Error committing changes to resolver for DAM upload", e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}