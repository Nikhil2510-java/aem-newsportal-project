package com.bhasaka.newsportal.core.models;

import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.ResourceBundle;

@Model(adaptables = ResourceBundle.class)
public class RelatedArticleItemModel {

    @ValueMapValue
    private String pageTitle;

    @ValueMapValue
    private String pagePath;

    public String getPageTitle() {
        return pageTitle;
    }

    public String getPagePath() {
        return pagePath;
    }
}
