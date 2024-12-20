package com.bhasaka.newsportal.core.models;

import com.bhasaka.newsportal.core.services.NewsService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Date;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewsInfoListModel {

    private static final Logger LOG = LoggerFactory.getLogger(NewsInfoListModel.class);

    @OSGiService
    private NewsService newsService;

    // Fields to match the JSON structure
    private String _path;
    private String _variation;
    private String agencyName;
    private PlainText aboutNews;
    private String mobileNo;
    private Date newsDate;
    private Path newsImage;
    private Address1 addOtherInfo;
    private Boolean governmentApproveAgency;


    @PostConstruct
    public void init() {
        LOG.info("Initializing NewsInfoListModel with data from GraphQL endpoint.");

        // Fetch data from the service
        NewsInfoListModel fetchedData = newsService.fetchDataFromJson();

        if (fetchedData != null) {
            this._path = fetchedData.get_path();
            this._variation = fetchedData.get_variation();
            this.agencyName = fetchedData.getAgencyName();
            this.aboutNews = fetchedData.getAboutNews();
            this.mobileNo = fetchedData.getMobileNo();
            this.newsDate = fetchedData.getNewsDate();
            this.newsImage = fetchedData.getNewsImage();
            this.addOtherInfo=fetchedData.getAddOtherInfo();
            this.governmentApproveAgency = fetchedData.getGovernmentApproveAgency();


            LOG.info("Data successfully populated from GraphQL endpoint.");
        } else {
            LOG.error("Failed to initialize NewsInfoListModel as fetched data is null.");
        }
    }

    // Getters and Setters
    public String get_path() {
        return _path;
    }

    public void set_path(String _path) {
        this._path = _path;
    }

    public String get_variation() {
        return _variation;
    }

    public void set_variation(String _variation) {
        this._variation = _variation;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public PlainText getAboutNews() {
        return aboutNews;
    }

    public void setAboutNews(PlainText aboutNews) {
        this.aboutNews = aboutNews;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Date getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(Date newsDate) {
        this.newsDate = newsDate;
    }

    public Path getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(Path newsImage) {
        this.newsImage = newsImage;
    }


    public Address1 getAddOtherInfo() {
        return addOtherInfo;
    }

    public Boolean getGovernmentApproveAgency() {
        return governmentApproveAgency;
    }

    public void setGovernmentApproveAgency(Boolean governmentApproveAgency) {
        this.governmentApproveAgency = governmentApproveAgency;
    }


}

