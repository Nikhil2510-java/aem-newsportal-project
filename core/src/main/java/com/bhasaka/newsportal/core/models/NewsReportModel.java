package com.bhasaka.newsportal.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        resourceType = "/newsportal/components/download-doc",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class NewsReportModel {

    @ValueMapValue(name = "category")
    private String category;

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "NewsReportModel{category='" + category + "'}";
    }
}
