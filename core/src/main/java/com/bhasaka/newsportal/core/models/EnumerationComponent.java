package com.bhasaka.newsportal.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Model(
        adaptables = Resource.class,
        adapters = {EnumerationComponent.class, ComponentExporter.class},
        resourceType = "newsportal/components/enumeration",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class EnumerationComponent implements ComponentExporter {

    // Multi-field items
    @ChildResource(name = "items/card/field/items")
    private Resource cards;

    // Getter for multi-field items
    @JsonProperty("cards")
    public List<CardItem> getCards() {
        if (cards != null) {
            List<CardItem> cardItems = new ArrayList<>();
            for (Resource card : cards.getChildren()) {
                cardItems.add(card.adaptTo(CardItem.class));
            }
            return cardItems;
        }
        return Collections.emptyList();
    }

    @Override
    public String getExportedType() {
        return "newsportal/components/enumeration";
    }

    /**
     * Nested Sling Model for individual card items in the multi-field.
     */
    @Model(
            adaptables = Resource.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    public static class CardItem {

        @ValueMapValue
        private String heading;

        @ValueMapValue
        private String title;

        @ValueMapValue
        private String image;

        @ValueMapValue
        private String description;

        @ValueMapValue
        private String ctaButton;

        @ValueMapValue
        private String dropdownCta;

        // Getters
        public String getHeading() {
            return heading;
        }

        public String getTitle() {
            return title;
        }

        public String getImage() {
            return image;
        }

        public String getDescription() {
            return description;
        }

        public String getCtaButton() {
            return ctaButton;
        }

        public String getDropdownCta() {
            return dropdownCta;
        }
    }
}
