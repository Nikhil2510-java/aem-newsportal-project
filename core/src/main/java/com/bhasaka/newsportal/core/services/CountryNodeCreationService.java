package com.bhasaka.newsportal.core.services;

import com.bhasaka.newsportal.core.models.CountryDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

@Component(service = CountryNodeCreationService.class, immediate = true)
public class CountryNodeCreationService {

    private static final String COUNTRIES_PATH = "/etc/countries";
    private static final Logger log = LoggerFactory.getLogger(CountryNodeCreationService.class);

    @Reference
    private CountriesAPIServices countriesServices;

    @Reference
    private NPUtilService npUtilService;

    @Activate
    @Modified
    public void createCountryNodes() {
        List<CountryDTO> countries = countriesServices.getCountries();
        log.info("Fetched {} countries from the API", countries.size());

        try (ResourceResolver resolver = npUtilService.getResourceResolver()) {
            Resource countriesResource = resolver.getResource(COUNTRIES_PATH);
            if (countriesResource == null) {
                Resource etcResource = resolver.getResource("/etc");
                if (etcResource != null) {
                    countriesResource = resolver.create(etcResource, "countries", Collections.singletonMap("jcr:primaryType", "sling:Folder"));
                    log.info("Created /etc/countries node.");
                } else {
                    log.error("Parent node /etc does not exist, aborting node creation.");
                    return;
                }
            }

            for (CountryDTO country : countries) {
                createCountryResource(countriesResource, country.getCountryCode(), country.getCountryName());
            }
            resolver.commit();
            log.info("Country nodes created successfully under {}", COUNTRIES_PATH);

        } catch (Exception e) {
            log.error("Error while creating country nodes: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create country nodes in JCR", e);
        }
    }

    private void createCountryResource(Resource parentResource, String countryCode, String countryName) {
        Resource countryResource = parentResource.getChild(countryCode);
        if (countryResource == null) {
            try {
                ResourceResolver resolver = parentResource.getResourceResolver();
                Resource newCountryResource = resolver.create(parentResource, countryCode, Collections.singletonMap("jcr:primaryType", "nt:unstructured"));
                ModifiableValueMap properties = newCountryResource.adaptTo(ModifiableValueMap.class);
                if (properties != null) {
                    properties.put("name", countryName);
                    log.info("Created country node for {} with name {}", countryCode, countryName);
                }
            } catch (Exception e) {
                log.error("Error while creating resource for country code {}: {}", countryCode, e.getMessage(), e);
            }
        } else {
            log.info("Node for country code {} already exists, skipping creation.", countryCode);
        }
    }
}
