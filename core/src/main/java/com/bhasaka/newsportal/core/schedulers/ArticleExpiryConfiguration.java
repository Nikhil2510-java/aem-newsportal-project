package com.bhasaka.newsportal.core.schedulers;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface ArticleExpiryConfiguration {

        @AttributeDefinition(name = "Cron Expression")
        public String cronExpression() default "*/15 * * ? * *";

        @AttributeDefinition(name = "Scheduler Name")
        public String schedulerName() default "article-expiry";

        @AttributeDefinition(name = "Enable /Disable Scheduler")
        public boolean enable() default true;


}
