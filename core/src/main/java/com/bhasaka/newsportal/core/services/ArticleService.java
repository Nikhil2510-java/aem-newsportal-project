package com.bhasaka.newsportal.core.services;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

@Component(service=ArticleService.class)
@Designate(ocd=ArticleConfiguration.class)
public class ArticleService {

    private static final Logger LOG= LoggerFactory.getLogger(ArticleService.class);
    String articleRestAPIURL;
    private String clientId;
    private boolean enable;

    @Activate
    public void activate(ArticleConfiguration config){
        articleRestAPIURL= config.articleRestAPI();
        clientId= config.clientId();
        enable=config.enable();
        LOG.info("Activate-Article Rest URL:{},ClientId:{},Enable:{}",articleRestAPIURL,clientId,enable);
    }

    @Deactivate
    public void deactivate(ArticleConfiguration config){
        LOG.info("inside deactivate method");
    }

    @Modified
    public void update(ArticleConfiguration config){
        articleRestAPIURL= config.articleRestAPI();
        clientId= config.clientId();
        enable=config.enable();
        LOG.info("Modified-Article Rest URL:{},ClientId:{},Enable:{}",articleRestAPIURL,clientId,enable);

    }

    public String getArticle(){
        CloseableHttpClient client= HttpClients.createDefault();
        HttpGet getRequest=new HttpGet(articleRestAPIURL);
        try {
           CloseableHttpResponse response= client.execute(getRequest);
           if(response.getStatusLine().getStatusCode()==200){
               return response.getEntity().getContent().toString();
           }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
