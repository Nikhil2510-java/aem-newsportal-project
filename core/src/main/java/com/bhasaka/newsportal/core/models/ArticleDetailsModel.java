package com.bhasaka.newsportal.core.models;

import com.adobe.cq.export.json.ExporterConstants;
import org.apache.jackrabbit.webdav.property.ResourceType;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Model(
        adaptables = Resource.class,
        resourceType="newsportal/components/article-details",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ArticleDetailsModel {
    @ValueMapValue
    private String articleTitle;

    @ValueMapValue
    private String articleDesc;

    @ValueMapValue
    private String articleImage;

    @ValueMapValue
    private Date articleExpiry;

    @ValueMapValue(name = "sling:resourceType")
    @Default(values = "newsportal/component/page")
    private String stringResourceType;

    @ChildResource
    List<RelatedArticleItemModel> relatedArticles;

    private boolean articleIsExpiry=false;


    @PostConstruct
    public void init() {
        if (articleExpiry != null) {
            Date today = new Date();
            articleIsExpiry = articleExpiry.before(today); // Simplified comparison
        }
    }

    public String getArticleDesc() {
        return articleDesc;
    }

    public String getArticleImage() {
        return articleImage;
    }


    public String getArticleTitle() {
        return articleTitle;
    }

    public Date getArticleExpiry() {
        return articleExpiry;
    }

    public boolean isArticleIsExpiry() {
        return articleIsExpiry;
    }


}
