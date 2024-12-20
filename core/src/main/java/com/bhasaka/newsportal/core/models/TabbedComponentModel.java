package com.bhasaka.newsportal.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.google.gson.annotations.SerializedName;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Model(
        adaptables = Resource.class,
        adapters = {TabbedComponentModel.class, ComponentExporter.class},
        resourceType = "your-app/components/tabbedcomponent",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class TabbedComponentModel implements ComponentExporter {


    @ValueMapValue(name = "compositeMultifield")
    private List<Resource> compositeMultifieldResources;

    private List<CompositeMultifieldItem> compositeMultifield;

    @PostConstruct
    protected void init() {
        if (compositeMultifieldResources != null) {
            compositeMultifield = new ArrayList<>();
            for (Resource resource : compositeMultifieldResources) {
                String title = resource.getValueMap().get("title", String.class);
                String description = resource.getValueMap().get("descriptionText", String.class);
                compositeMultifield.add(new CompositeMultifieldItem(title, description));
            }
        } else {
            compositeMultifield = Collections.emptyList();
        }
    }

    public List<CompositeMultifieldItem> getCompositeMultifield() {
        return compositeMultifield;
    }

    @Override
    public String getExportedType() {
        return "your-app/components/tabbedcomponent";
    }

    public static class CompositeMultifieldItem {

        @SerializedName("title")
        private final String title;

        @SerializedName("description")
        private final String description;

        public CompositeMultifieldItem(String title, String description) {
            this.title = title;
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }
}
