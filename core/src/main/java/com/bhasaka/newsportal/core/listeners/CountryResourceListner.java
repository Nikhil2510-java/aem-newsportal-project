package com.bhasaka.newsportal.core.listeners;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Component(
        service= ResourceChangeListener.class,
        immediate = true,
        property = {
                ResourceChangeListener.PATHS+"=/content/countries",
                ResourceChangeListener.CHANGES+"="+ResourceChangeListener.CHANGE_ADDED,
                ResourceChangeListener.CHANGES+"="+ResourceChangeListener.CHANGE_REMOVED,
                ResourceChangeListener.CHANGES+"="+ResourceChangeListener.CHANGE_CHANGED
        }

          )
public class CountryResourceListner implements ResourceChangeListener {

    private static final Logger LOG= LoggerFactory.getLogger(CountryResourceListner.class);

    @Override
    public void onChange(List<ResourceChange> list) {
             LOG.info("Resource Change Listner....");
    }
}
