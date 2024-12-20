package com.bhasaka.newsportal.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.Date;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductCardItemsListModel {

    @ValueMapValue
    private Date productExpiry;

    @ValueMapValue
    private int productPrice;

    @ValueMapValue
    private String productImage;

    @ValueMapValue
    private String productColour;

    @ValueMapValue
    private String productTags;

    public Date getProductExpiry() {
        return productExpiry;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductColour() {
        return productColour;
    }

    public String getProductTags() {
        return productTags;
    }
}
