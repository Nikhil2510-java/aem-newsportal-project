package com.bhasaka.newsportal.core.schedulers;

import com.day.cq.dam.api.AssetManager;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component(service = Runnable.class, immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = BerecApiConfiguration.class)
public class BerecApiScheduler implements Runnable {

    private String cronExpression;
    private String berecAPI;
    private boolean isEnabled;
    private String damPath;
    private String schedulerName;

    @Reference
    private Scheduler scheduler;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private static final String CONTENT_TYPE_JSON = "application/json";

    private static final String SERVICE_NAME = "npsubservice";

    private static final Map<String, Object> SERVICE_AUTH_INFO = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE,
            (Object) SERVICE_NAME);

    private static final Logger LOG=LoggerFactory.getLogger(BerecApiScheduler.class);

    @Reference
    Replicator replicator;

    @Activate
    @Modified
    public void activate(BerecApiConfiguration config) {
        this.cronExpression = config.cronExpression();
        this.berecAPI = config.berecAPI();
        this.isEnabled = config.enable();
        this.damPath = config.damPath();
        this.schedulerName=config.schedulerName();
        LOG.info("berecAPI : {}",berecAPI);
        LOG.info("cronExpression : {}, berecAPI : {}, isEnabled: {}, damPath : {}, schedulerName : {}", cronExpression, berecAPI, isEnabled, damPath, schedulerName);
        if (isEnabled) {
            ScheduleOptions options = scheduler.EXPR(cronExpression);
            options.canRunConcurrently(false);
            options.name(schedulerName);
            scheduler.schedule(this, options);
            run();
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
            if(jsonData != null && !jsonData.isEmpty()) {
                uploadJsonToDam(jsonData);
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
            if(url != null) {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(METHOD_GET);
                connection.setRequestProperty(HEADER_ACCEPT, CONTENT_TYPE_JSON);
                inputStream = connection.getInputStream();
                jsonData = new String(inputStream.readAllBytes());
            }
        } catch (Exception e) {
            LOG.error("Error fetching data from API: {}", berecAPI, e);
        } finally {
            if(connection != null )
                connection.disconnect();
            if(inputStream != null)
                inputStream.close();
        }
        return jsonData;
    }


    private void uploadJsonToDam(String jsonData) throws LoginException {
        try (ResourceResolver resolver = resourceResolverFactory.getServiceResourceResolver(SERVICE_AUTH_INFO)) {
            String parentPath = damPath.substring(0, damPath.lastIndexOf('/'));
            String fileName = damPath.substring(damPath.lastIndexOf('/') + 1);

            Resource parentResource = resolver.getResource(parentPath);
            if (parentResource == null) {
                LOG.error("Parent path {} does not exist. Unable to create file.", parentPath);
                return;
            }
            Resource existingFile = resolver.getResource(damPath);
            if (existingFile != null) {
                resolver.delete(existingFile);
                LOG.info("file Deleted {}",existingFile);
            }
            Map<String, Object> fileProperties = new HashMap<>();
            fileProperties.put("jcr:primaryType", "nt:file");
            Resource fileResource = resolver.create(parentResource, fileName, fileProperties);

            Map<String, Object> contentProperties = new HashMap<>();
            contentProperties.put("jcr:primaryType", "nt:resource");
            contentProperties.put("jcr:mimeType", CONTENT_TYPE_JSON);
            contentProperties.put("jcr:data", jsonData);
            contentProperties.put("jcr:lastModified", Calendar.getInstance());
            resolver.create(fileResource, "jcr:content", contentProperties);

            resolver.commit();
            LOG.info("Successfully uploaded JSON to DAM at: {}", damPath);
            replicator.replicate(resolver.adaptTo(Session.class), ReplicationActionType.ACTIVATE, damPath);

        } catch (PersistenceException | ReplicationException e) {
            LOG.error("Error while uploading JSON to DAM", e);
        }
    }

}