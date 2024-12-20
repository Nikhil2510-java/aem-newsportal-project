package com.bhasaka.newsportal.core.models;

import com.adobe.cq.export.json.ExporterConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},resourceType="/newsportal/components/comp-multifield")
@Exporter(name= ExporterConstants.SLING_MODEL_EXPORTER_NAME,extensions=ExporterConstants.SLING_MODEL_EXTENSION)
public class AccordianChildModel {
    @ValueMapValue
    private String question;

    public String getQuestion() {
        return question;
    }
    @ValueMapValue
    private String answer;

    public String getAnswer() {
        return answer;
    }



}
