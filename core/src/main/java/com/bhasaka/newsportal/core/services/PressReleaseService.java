package com.bhasaka.newsportal.core.services;

import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PressReleaseService {
    private static final Logger LOG= LoggerFactory.getLogger(PressReleaseService.class);

    @Reference
    ArticleService articleService;

    @Activate
    public void activate(){
        LOG.info(articleService.getArticle());
        LOG.info("inside activate method");
    }

    @Deactivate
    public void deactivate(){
        LOG.info("inside deactivate method");
    }

    @Modified
    public void update(){
        LOG.info("inside modified method");
    }



}
