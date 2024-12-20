package com.bhasaka.newsportal.core.servlets;

import com.bhasaka.newsportal.core.services.NPUtilService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class PageInfoServletTest {

    @Mock
    NPUtilService npUtilService;

    AemContext context=new AemContext();

    MockSlingHttpServletRequest request;

    MockSlingHttpServletResponse response;

    @InjectMocks
    PageInfoServlet pageInfoServlet;

    @BeforeEach
    void init(){
       request=context.request();
       response=context.response();
       context.registerService(PageInfoServlet.class,pageInfoServlet);
       context.registerService(NPUtilService.class,npUtilService);
        //context.registerInjectActivateService(NPUtilService.class);
    }

    @Test
    void testDoGetMethod() throws ServletException, IOException {
        Mockito.when(npUtilService.getResourceResolver()).thenReturn(context.resourceResolver());

        context.create().page("/content/newsportal/us/en/articles");
        context.create().page("/content/newsportal/us/en/articles/article-1","article-template","Article 1");
        context.create().page("/content/newsportal/us/en/articles/article-2","article-template","Article 2");

        pageInfoServlet.doGet(request,response);
    }

    @Test
    void testDoPostmethod() throws ServletException, IOException {
        request.addRequestParameter("pageName","article-22");
        request.addRequestParameter("pageTitle","Article 22");
        context.create().page("/content/newsportal/us/en/articles");  //add
        pageInfoServlet.doPost(request,response);
        assertNotNull(context.resourceResolver().getResource("/content/newsportal/us/en/articles/article-22"));
    }

    @Test
    void testDoDeleteMethod() throws ServletException, IOException {
        context.create().page("/content/newsportal/us/en/articles/article-22","article-template","Article 22");
        request.addRequestParameter("pageName","article-22");
        pageInfoServlet.doDelete(request,response);
        assertEquals(null,context.resourceResolver().getResource("/content/newsportal/us/en/articles/article-22"));


    }


}