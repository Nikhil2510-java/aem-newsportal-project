package com.bhasaka.newsportal.core.utils;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * This is a utility class for aem specific utilities like getting resource etc.
 */
public class AemUtils {

    private AemUtils() {

    }

    public static final Logger LOGGER = LoggerFactory.getLogger(AemUtils.class);
    public static final String HTTP = "http";
    public static final String WWW = "www";
    public static final String MAIL_TO = "mailto:";
    public static final String TEL = "tel:";


    public static final String DE_ROOT_PATH = "/content/lebara/de";
    public static final String FR_ROOT_PATH = "/content/lebara/fr";
    public static final String NL_ROOT_PATH = "/content/lebara/nl";
    public static final String DK_ROOT_PATH = "/content/lebara/dk";
    //public static final String UK_ROOT_PATH = "/content/lebara/gb/en";
    public static final String UK_ROOT_PATH = "/content/lebara/gb";

    public static final String LEBARA_GROUP_ROOT_PATH = "/content/lebara/lebara";
    public static final String DIGITAL_ACADEMY_ROOT_PATH = "/content/lebara/digitalacademy";

    public static final String DE_DOMAIN_NAME = "https://www.lebara.de";
    public static final String FR_DOMAIN_NAME = "https://www.lebara.fr";
    public static final String NL_DOMAIN_NAME = "https://www.lebara.nl";
    public static final String DK_DOMAIN_NAME = "https://www.lebara.dk";
    public static final String UK_DOMAIN_NAME = "https://www.lebara.co.uk";
    public static final String LEBARA_GROUP_DOMAIN_NAME = "https://www.lebara.com";
    public static final String DIGITAL_ACADEMY_DOMAIN_NAME = "https://digitalacademy.lebara.com";

    public static final String JCR_CONTENT_RENDITIONS = "/jcr:content/renditions/";
    public static final String JCR_CONTENT_METADATA = "/jcr:content/metadata";
    public static final String RENDITION = "rendition";
    private static final Set<String> whitelistedFormats = new HashSet<>(Arrays.asList("image/jpeg", "image/png", "image/webp"));
    private static final String[] renditionsPriority = {"-w.webp", "-p.png", "-png", ".jpeg"};
    private static final List<String> excludeRenditions = new ArrayList<>(Arrays.asList("image/svg", "image/svg+xml"));
    public static final String UK_COUNTRY_CODE = "GB";


    /**
     * Gets property.
     *
     * @param <T>          the type parameter
     * @param resource     the resource
     * @param propertyName the property name
     * @param clazz        the clazz
     * @return property with propertyName of given type defined by clazz from resource
     */
    public static <T> T getProperty(final Resource resource, final String propertyName, final Class<T> clazz) {
        ValueMap propertiesMap = resource != null ? resource.adaptTo(ValueMap.class) : null;
        return propertiesMap != null ? propertiesMap.get(propertyName, clazz) : null;
    }

    /**
     * Gets string property.
     *
     * @param resource     the resource
     * @param propertyName the property name
     * @return String property with propertyName for resource
     */
    public static String getStringProperty(final Resource resource, final String propertyName) {
        return getProperty(resource, propertyName, String.class);
    }

    /**
     * Gets string[] property as a List.
     *
     * @param resource     the resource
     * @param propertyName the property name
     * @return String property with propertyName for resource
     */
    public static List<String> getStringListProperty(final Resource resource, final String propertyName) {
        final String[] result = getProperty(resource, propertyName, String[].class);
        if (result == null) {
            return new ArrayList<>();
        }

        return Arrays.asList(result);
    }

    public static JsonObject getExcelAsJson(String path, ResourceResolver resourceResolver) throws IOException {
        JsonObject sheetsJsonObject = new JsonObject();
        Resource resource = resourceResolver.getResource(path);
        Asset asset = resource.adaptTo(Asset.class);
        if (null != asset) {
            Rendition rendition = asset.getOriginal();
            if (null != rendition) {
                InputStream inputStream = rendition.getStream();
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    JsonArray sheetArray = new JsonArray();
                    ArrayList<String> columnNames = new ArrayList<>();
                    Sheet sheet = workbook.getSheetAt(i);
                    Iterator<Row> sheetIterator = sheet.iterator();

                    while (sheetIterator.hasNext()) {

                        Row currentRow = sheetIterator.next();
                        JsonObject jsonObject = new JsonObject();

                        if (currentRow.getRowNum() != 0) {

                            for (int j = 0; j < columnNames.size(); j++) {

                                if (currentRow.getCell(j) != null) {
                                    if (currentRow.getCell(j).getCellType() == CellType.STRING) {
                                        jsonObject.addProperty(columnNames.get(j), currentRow.getCell(j).getStringCellValue());
                                    } else if (currentRow.getCell(j).getCellType() == CellType.NUMERIC) {
                                        jsonObject.addProperty(columnNames.get(j), currentRow.getCell(j).getNumericCellValue());
                                    } else if (currentRow.getCell(j).getCellType() == CellType.BOOLEAN) {
                                        jsonObject.addProperty(columnNames.get(j), currentRow.getCell(j).getBooleanCellValue());
                                    } else if (currentRow.getCell(j).getCellType() == CellType.BLANK) {
                                        jsonObject.addProperty(columnNames.get(j), org.apache.commons.lang.StringUtils.EMPTY);
                                    }
                                }

                            }

                            sheetArray.add(jsonObject);

                        } else {
                            // store column names
                            for (int k = 0; k < currentRow.getPhysicalNumberOfCells(); k++) {
                                columnNames.add(currentRow.getCell(k).getStringCellValue());
                            }
                        }

                    }
                    sheetsJsonObject.add(workbook.getSheetName(i), sheetArray);
                }
            }
        }
        return sheetsJsonObject;
    }
}