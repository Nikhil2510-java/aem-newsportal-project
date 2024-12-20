package com.bhasaka.newsportal.core.services;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ArticleServiceTest {

    AemContext context=new AemContext();

    @Mock
    ArticleConfiguration config;

    ArticleService articleService=new ArticleService();


    @BeforeEach
    void init(){
        // config=mock(ArticleConfiguration.class);
        Mockito.when(config.articleRestAPI()).thenReturn("https://gorest.co.in/public/v2/posts");
        Mockito.when(config.enable()).thenReturn(true);
        Mockito.when(config.clientId()).thenReturn("89078");
        articleService.activate(config);


    }

    @Test
    void testArticleDetailsLifeCycleMethod(){
           articleService.activate(config);
           articleService.deactivate(config);
           articleService.update(config);
           assertEquals("https://gorest.co.in/public/v2/posts",articleService.articleRestAPIURL);
    }

    @Test
    void testGetArticles(){
        String article=articleService.getArticle();
    }
}