package com.bhasaka.newsportal.core.models;

import com.adobe.cq.wcm.core.components.models.Button;
import com.adobe.cq.wcm.core.components.models.Image;
import com.adobe.cq.wcm.core.components.models.Text;
import com.adobe.cq.wcm.core.components.models.Title;
import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(
        adaptables = Resource.class,
        adapters = {ProductDetailsModel.class, ComponentExporter.class},
        resourceType = ProductDetailsModel.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ProductDetailsModel implements ComponentExporter {

    protected static final String RESOURCE_TYPE = "newsportal/components/product-details";

    @ValueMapValue
    private String category;

    @ValueMapValue
    private String productTag;

    @ChildResource(name = "npTitle")
    private Title npTitle;

    @ChildResource(name = "npText")
    private Text npText;

    @ChildResource(name = "npImage")
    private Image npImage;

    @ChildResource(name = "npButton")
    private Button npButton;

    private String titleText;
    private String descriptionText;
    private String imageSrc;
    private String buttonText;
    private String buttonLink;

    @PostConstruct
    protected void init() {
        // Extract values from the Core Component models
        if (npTitle != null) {
            titleText = npTitle.getText();
        }
        if (npText != null) {
            descriptionText = npText.getText();
        }
        if (npImage != null) {
            imageSrc = npImage.getSrc();
        }
        if (npButton != null) {
            buttonText = npButton.getText();
            buttonLink = npButton.getLink();
        }
    }

    public String getTitleText() {
        return titleText;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getButtonLink() {
        return buttonLink;
    }

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
}
