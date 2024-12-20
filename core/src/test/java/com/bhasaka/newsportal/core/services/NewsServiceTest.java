package com.bhasaka.newsportal.core.services;

import com.bhasaka.newsportal.core.models.NewsInfoListModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.wcm.testing.mock.aem.junit5.AemContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;

import java.io.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class NewsServiceTest {

    AemContext context = new AemContext();

    @Mock
    CloseableHttpClient client;

    @Mock
    HttpGet request;


    @Mock
    StatusLine statusLine;

    @Mock
    HttpEntity entity;

    @Mock
    InputStream inputStream;

    @Mock
    HttpResponse mockResponse;

    @Mock
    StatusLine mockStatusLine;

    @Mock
    InputStreamReader inputStreamReader;

    @Mock
    Logger mockLogger;

    @Mock
    NewsInfoListModel newsInfoListModel;

    @InjectMocks
    NewsService newsService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        context.registerService(CloseableHttpClient.class, client);
        context.registerService(NewsService.class, newsService);

    }

    @Test
    public void fetchDataFromJsonTest() throws IOException {
        newsInfoListModel = newsService.fetchDataFromJson();

    }


}

