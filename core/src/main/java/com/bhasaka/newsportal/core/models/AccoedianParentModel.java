package com.bhasaka.newsportal.core.models;

import com.adobe.cq.export.json.ExporterConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import java.util.List;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},resourceType="/newsportal/components/comp-multifield")
@Exporter(name= ExporterConstants.SLING_MODEL_EXPORTER_NAME,extensions=ExporterConstants.SLING_MODEL_EXTENSION)
public class AccoedianParentModel {

    public List<AccordianChildModel> getQuestionAnswer() {
        return questionAnswer;
    }

    @ChildResource
    List<AccordianChildModel> questionAnswer;

}