package com.bhasaka.newsportal.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface CountryConfiguration {

    @AttributeDefinition(name="Country Rest API")
    public String countryRestAPI() default "https://api.first.org/data/v1/countries";

    @AttributeDefinition
    public String countryNodePath() default "/etc/countries";


}
