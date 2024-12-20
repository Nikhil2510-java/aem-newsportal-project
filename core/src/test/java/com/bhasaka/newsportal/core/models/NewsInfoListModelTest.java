package com.bhasaka.newsportal.core.models;

import com.bhasaka.newsportal.core.services.NewsService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NewsInfoListModelTest {

    @Mock
    private NewsService newsService;

    @InjectMocks
    private NewsInfoListModel newsInfoListModel;

    @Mock
    private Resource resource;

    @Mock
    private SlingHttpServletRequest slingHttpServletRequest;

    @BeforeEach
    public void setup() {
        // Initialize the mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInit_shouldPopulateFields_whenDataIsFetchedSuccessfully() {
        // Arrange: Mocking a valid response from the NewsService
        NewsInfoListModel mockData = new NewsInfoListModel();
        mockData.set_path("/path/to/news");
        mockData.set_variation("variation1");
        mockData.setAgencyName("AgencyName");
        mockData.setAboutNews(new PlainText());
        mockData.setMobileNo("1234567890");
        mockData.setNewsDate(new Date());
        mockData.setNewsImage(new Path());
        mockData.setGovernmentApproveAgency(true);

        when(newsService.fetchDataFromJson()).thenReturn(mockData);

        // Act: Initialize the model
        newsInfoListModel.init();

        // Assert: Check that all fields were populated correctly
        assertNotNull(newsInfoListModel.get_path());
        assertEquals("/path/to/news", newsInfoListModel.get_path());
        assertEquals("variation1", newsInfoListModel.get_variation());
        assertEquals("AgencyName", newsInfoListModel.getAgencyName());
        assertEquals("1234567890", newsInfoListModel.getMobileNo());
        assertNotNull(newsInfoListModel.getNewsDate());
        assertNotNull(newsInfoListModel.getNewsImage());
        assertTrue(newsInfoListModel.getGovernmentApproveAgency());
    }

    @Test
    public void testInit_shouldLogError_whenFetchedDataIsNull() {
        when(newsService.fetchDataFromJson()).thenReturn(null);
        newsInfoListModel.init();

    }

    @Test
    public void testSettersAndGetters() {
        newsInfoListModel.set_path("/new/path");
        newsInfoListModel.set_variation("newVariation");
        newsInfoListModel.setAgencyName("NewAgency");
        PlainText plainText = new PlainText();
        newsInfoListModel.setAboutNews(plainText);
        newsInfoListModel.setMobileNo("9876543210");
        newsInfoListModel.setNewsDate(new Date());
        newsInfoListModel.setNewsImage(new Path());
        newsInfoListModel.setGovernmentApproveAgency(false);

        assertEquals("/new/path", newsInfoListModel.get_path());
        assertEquals("newVariation", newsInfoListModel.get_variation());
        assertEquals("NewAgency", newsInfoListModel.getAgencyName());
//        assertNull(newsInfoListModel.getAboutNews());
        assertEquals("9876543210", newsInfoListModel.getMobileNo());
        assertNotNull(newsInfoListModel.getNewsDate());
        assertNotNull(newsInfoListModel.getNewsImage());
        assertFalse(newsInfoListModel.getGovernmentApproveAgency());
    }
}
