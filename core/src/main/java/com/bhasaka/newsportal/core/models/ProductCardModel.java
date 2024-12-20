package com.bhasaka.newsportal.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import java.util.List;

@Model(
        adaptables = Resource.class,
        adapters = {ProductCardModel.class},
        resourceType = ProductCardModel.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ProductCardModel implements ComponentExporter {

    protected static final String RESOURCE_TYPE = "newsportal/components/productcard";

    @ValueMapValue
    private String containerTitle;

    @ValueMapValue
    private String containerDescription;

    @ValueMapValue
    private boolean status;

    @ValueMapValue
    private String category;

    @ValueMapValue
    private boolean loadProductsFromCF;

    @ValueMapValue
    private List<String> cfProductPaths;

    @ChildResource
    private List<ProductCardItemsListModel> productList;

    @ChildResource
    private List<ProductCardItemsListModel> popularProductsList;


    public String getContainerTitle() {
        return containerTitle;
    }

    public String getContainerDescription() {
        return containerDescription;
    }

    public boolean isStatus() {
        return status;
    }

    public String getCategory() {
        return category;
    }

    public boolean isLoadProductsFromCF() {
        return loadProductsFromCF;
    }

    public List<String> getCfProductPaths() {
        return cfProductPaths;
    }

    public List<ProductCardItemsListModel> getProductList() {
        return productList;
    }

    public List<ProductCardItemsListModel> getPopularProductsList() {
        return popularProductsList;
    }

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
}
