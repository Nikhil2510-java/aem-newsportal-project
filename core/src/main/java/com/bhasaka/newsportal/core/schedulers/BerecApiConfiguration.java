package com.bhasaka.newsportal.core.schedulers;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface BerecApiConfiguration {

    @AttributeDefinition(name = "Cron Expression")
    public String cronExpression() default "*/15 * * ? * *";   //0 0 0 ? * *

    @AttributeDefinition(name = "API path")
    public String berecAPI() default "https://www.berec.europa.eu/en/emergency-means-export/json?page&_format=json";

    @AttributeDefinition(name = "Enable /Disable Scheduler")
    public boolean enable() default true;

    @AttributeDefinition(name = "DAM path")
    public String damPath() default "/content/dam/newsportal/json/emergency-means/emergencyMeansReport.json";

    @AttributeDefinition(name = "Scheduler name")
    public String schedulerName() default "berac-Api";
}