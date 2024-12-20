package com.bhasaka.newsportal.core.schedulers;

import com.bhasaka.newsportal.core.services.NPUtilService;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.Date;
import java.util.Iterator;

@Component(
        service=Runnable.class,
        immediate = true
          )
@Designate(ocd = ArticleExpiryConfiguration.class)

public class ArticleExpiryScheduler implements Runnable{
    private static  final Logger LOG= LoggerFactory.getLogger(ArticleExpiryScheduler.class);

    @Reference
    NPUtilService npUtilService;

    @Reference
    Replicator replicator;

    @Reference
    Scheduler scheduler;

    @Activate
    @Modified
    public void activate(ArticleExpiryConfiguration config){
       if( config.enable()){
           ScheduleOptions options=scheduler.EXPR(config.cronExpression());
           options.name(config.schedulerName());
           options.canRunConcurrently(false);
           scheduler.schedule(this,options);
       }
       else{
             scheduler.unschedule(config.schedulerName());
       }
    }

    @Override
    public void run() {
        //ResouceResolver -->PageManager
        //iterate all article pages
        //read articleExpiry property from jcr:content node of the page
        //compare article expiry with todays date,
        //deactivate programatically if article is expired

        ResourceResolver resolver=npUtilService.getResourceResolver();
        PageManager pageManager=resolver.adaptTo(PageManager.class);
        Page articlePage=pageManager.getPage("content/newsportal/us/en/articles");
        if(articlePage!=null){
            Iterator<Page> childPages=articlePage.listChildren();
            while (childPages.hasNext()){
                Page childPage= childPages.next();
                ValueMap pageProperties=childPage.getProperties();
                Date articleExpiry=pageProperties.get("articleExpiry", Date.class);
                Date today=new Date();
                if(articleExpiry!=null && articleExpiry.compareTo(today)<0){
                    try {
                        replicator.replicate(resolver.adaptTo(Session.class), ReplicationActionType.ACTIVATE,childPage.getPath());
                    } catch (ReplicationException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

      LOG.info("Inside The Run Method....");
    }
}
