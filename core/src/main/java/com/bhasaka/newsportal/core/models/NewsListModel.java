package com.bhasaka.newsportal.core.models;

import com.adobe.cq.export.json.ExporterConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.text.SimpleDateFormat;
import java.util.Date;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,resourceType = "/newsportal/components/article-list")
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class NewsListModel {

    @ValueMapValue
    private String newsTitle;

    @ValueMapValue
    private String newsDescription;

    @ValueMapValue
    private boolean newsStatus;

    @ValueMapValue
    private Date newsDate;

    @ValueMapValue
    private String newsLocation;

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public String getNewsStatus() {
        return newsStatus ? "Completed" : "Pending";
    }

    public String getNewsDate() {
        if (newsDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(newsDate);
        }
        return "No Date Provided";
    }

    public String getNewsLocation() {
        return newsLocation;
    }
}
