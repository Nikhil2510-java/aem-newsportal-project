package com.bhasaka.newsportal.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ArticleDetailsModelTest {

    AemContext context=new AemContext();

    ArticleDetailsModel articleDetailsModel;

    @BeforeEach
    void init(){
        context.addModelsForClasses(ArticleDetailsModel.class);
        //----------------------json-----------------------------------------------------
        context.load().json("/articledetails.json","/content");
        Resource articleJsonResource=context.currentResource("/content/article-details");
        articleDetailsModel=articleJsonResource.adaptTo(ArticleDetailsModel.class);
        //----------------------json-End----------------------------------------------------


        /*Map<String,Object> props=new HashMap<>();
        props.put("articleTitle","jailer Trailer");
        props.put("articleDesc","jailer Trailer Desc");
        props.put("articleImage","/content/dam/newsportal/images/np.jpg");
        props.put("sling:resourceType","newsportal/components/article-details");
        Resource resource=context.create().resource("/content/newsportal/article-details",props);
        articleDetailsModel=resource.adaptTo(ArticleDetailsModel.class);*/


    }

    @Test
    void articlePropsTest() {
        assertEquals("Json Newsportal Features",articleDetailsModel.getArticleTitle());
        assertEquals("Json Newsportal About Information",articleDetailsModel.getArticleDesc());
        assertEquals("/content/dam/newsportal/asset.jpg",articleDetailsModel.getArticleImage());
        assertEquals(null,articleDetailsModel.getArticleExpiry());
        assertFalse(articleDetailsModel.isArticleIsExpiry());
    }

    @Test
    void articleExpiryTest(){
        //----------------------json-----------------------------------------------------
        Resource articleJsonResource = context.currentResource("/content/article-details-expired");
        articleDetailsModel = articleJsonResource.adaptTo(ArticleDetailsModel.class);
      //----------------------json-End----------------------------------------------------

//        Map<String,Object> props=new HashMap<>();
//        props.put("articleTitle","jailer Trailer");
//
//        Calendar cal=Calendar.getInstance();
//        cal.add(Calendar.DAY_OF_MONTH,-15);
//        props.put("articleExpiry",cal);
//
//        Resource resource=context.create().resource("/content/newsportal/article-details-article-expiry",props);
//        articleDetailsModel=resource.adaptTo(ArticleDetailsModel.class);
        //System.out.println(articleDetailsModel.getArticleExpiry());

        assertNotNull(articleDetailsModel.getArticleExpiry(), "Article expiry date should not be null");
       // assertTrue(articleDetailsModel.isArticleIsExpiry(), "Article should be marked as expired");
        assertEquals(true,articleDetailsModel.isArticleIsExpiry());

    }
}
