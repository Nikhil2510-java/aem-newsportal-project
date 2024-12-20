package com.bhasaka.newsportal.core.schedulers;

import com.day.cq.dam.api.AssetManager;
import com.day.cq.replication.Replicator;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class BerecApiSchedulerTest {
    private final AemContext context = new AemContext();

    @Mock
    private ResourceResolverFactory resourceResolverFactory;

    @Mock
    private Scheduler scheduler;

    @Mock
    private ScheduleOptions scheduleOptions;

    @Mock
    private BerecApiConfiguration config;

    @Mock
    private Replicator replicator;

    private ResourceResolver resolver;

    @InjectMocks
    private BerecApiScheduler berecApiScheduler;

    private static final String CRONEXPRESSION = "* */2 * ? * *";
    private static final String BEREC_API = "https://www.berec.europa.eu/en/emergency-means-export/json?page&_format=json";
    private static final Boolean ENABLE = true;
    private static final String DAM_PATH = "/content/dam/newsportal/json/emergency-means/emergencyMeansReport.json";
    private static final String SCHEDULER_NAME = "test-api";


    @BeforeEach
    void init() {
        context.registerService(BerecApiScheduler.class, berecApiScheduler);
        context.registerService(Scheduler.class, scheduler);
        context.registerService(Replicator.class, replicator);
        context.registerService(ResourceResolverFactory.class, resourceResolverFactory);

        when(config.cronExpression()).thenReturn(CRONEXPRESSION);
        when(config.berecAPI()).thenReturn(BEREC_API);
        when(config.enable()).thenReturn(ENABLE);
        when(config.damPath()).thenReturn(DAM_PATH);
        when(config.schedulerName()).thenReturn(SCHEDULER_NAME);

        resolver = context.resourceResolver();
//        context.create().resource(DAM_PATH);

    }

    @Test
    void testActivateScheduler_WhenEnabled() throws LoginException {
        context.create().resource(DAM_PATH);
        when(scheduler.EXPR(CRONEXPRESSION)).thenReturn(scheduleOptions);
        when(resourceResolverFactory.getServiceResourceResolver(any())).thenReturn(resolver);
        berecApiScheduler.activate(config);
        verify(scheduler).schedule(berecApiScheduler, scheduleOptions);
        verify(scheduleOptions).name(SCHEDULER_NAME);
        verify(scheduleOptions).canRunConcurrently(false);

    }

    @Test
    void testActivateScheduler_WhenDisabled() throws LoginException {
        context.create().resource(DAM_PATH);
        when(config.enable()).thenReturn(false);
        when(scheduler.EXPR(CRONEXPRESSION)).thenReturn(scheduleOptions);
        when(resourceResolverFactory.getServiceResourceResolver(any())).thenReturn(resolver);
        berecApiScheduler.activate(config);

    }

    @Test
    void testActivateScheduler_WithEmptyApiUrl() throws LoginException {
        context.create().resource(DAM_PATH);
        when(config.berecAPI()).thenReturn("");
        when(scheduler.EXPR(CRONEXPRESSION)).thenReturn(scheduleOptions);
        when(resourceResolverFactory.getServiceResourceResolver(any())).thenReturn(resolver);
        berecApiScheduler.activate(config);

    }

    @Test
    void testActivateScheduler_WithEmptyDamPath() throws LoginException {
        when(scheduler.EXPR(CRONEXPRESSION)).thenReturn(scheduleOptions);
        when(resourceResolverFactory.getServiceResourceResolver(any())).thenReturn(resolver);
        berecApiScheduler.activate(config);

    }

    @Test
    void testNullResourceResolverFactoryScheduler() throws LoginException {
        context.create().resource(DAM_PATH);
        when(scheduler.EXPR(CRONEXPRESSION)).thenReturn(scheduleOptions);
        when(resourceResolverFactory.getServiceResourceResolver(any())).thenReturn(null);
        berecApiScheduler.activate(config);

    }

}