package com.bhasaka.newsportal.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


//@SlingServletResourceTypes(
//        resourceTypes = "/newsportal/services/recent-articles",
//        extensions = {"txt","json"},
//        methods={"GET","POST"},
//        selectors={"recent","popular"}
//                                                  )
@Component(service= Servlet.class)
@SlingServletPaths("/bin/newsportal/services/users")
public class RecentArticleServlet extends SlingAllMethodsServlet {
    @Override
    protected void doGet(SlingHttpServletRequest request,SlingHttpServletResponse response) throws ServletException, IOException {
        ResourceResolver resolver=request.getResourceResolver();
        Resource usersResource=resolver.getResource("/content/users");

        JsonArrayBuilder userJsonList=Json.createArrayBuilder();

        if(usersResource!=null){
            Iterator<Resource>users=usersResource.listChildren();
            while(users.hasNext()){
                Resource userResource=users.next();
                ValueMap props=userResource.getValueMap();

                JsonObjectBuilder userJson= Json.createObjectBuilder();
                userJson.add("firstName",props.get("firstName",String.class));
                userJson.add("lastName",props.get("lastName",String.class));
                userJson.add("email",props.get("email",String.class));
                userJson.add("phone",props.get("phone",String.class));
                userJson.add("password",props.get("password",String.class));
                userJsonList.add(userJson);

            }
        }

        response.setContentType("application/json");
        response.getWriter().write(userJsonList.build().toString());
    }
    @Override
    protected void doPost(SlingHttpServletRequest request,SlingHttpServletResponse response) throws ServletException, IOException {

        String userId=request.getParameter("userId");
        String firstName=request.getParameter("firstName");
        String lastName=request.getParameter("lastName");
        String email=request.getParameter("email");
        String phone=request.getParameter("phone");
        String password=request.getParameter("password");


        ResourceResolver resolver=request.getResourceResolver();
        Resource usersResource=resolver.getResource("/content/users");
        Resource userResource=resolver.getResource("/content/users/"+userId);

        if(usersResource!=null && userResource==null){
            Map<String,Object> props=new HashMap<>();
            props.put("firstName",firstName);
            props.put("lastName",lastName);
            props.put("email",email);
            props.put("phone",phone);
            props.put("password",password);

            resolver.create(usersResource,userId,props);
            resolver.commit();
        }

        response.getWriter().write("User Created Successfully");
    }
    @Override
    protected void doPut(SlingHttpServletRequest request,SlingHttpServletResponse response) throws ServletException, IOException {

        String userId=request.getParameter("userId");
        String firstName=request.getParameter("firstName");
        String lastName=request.getParameter("lastName");
        String email=request.getParameter("email");
        String phone=request.getParameter("phone");
        String password=request.getParameter("password");


        ResourceResolver resolver=request.getResourceResolver();
        //Resource usersResource=resolver.getResource("/content/users");
        Resource userResource=resolver.getResource("/content/users/"+userId);

        if(userResource!=null){
            ModifiableValueMap mProp=userResource.adaptTo(ModifiableValueMap.class);
            if(firstName!=null){
                mProp.put("firstName",firstName);
            }
            if(lastName!=null){
                mProp.put("lastName",lastName);
            }
            if(email!=null){
                mProp.put("email",email);
            }
            if(phone!=null){
                mProp.put("phone",phone);
            }
            if(password!=null){
                mProp.put("password",password);
            }

            resolver.commit();
        }

        response.getWriter().write("User Update Suceessfully");
    }
    @Override
    protected void doDelete(SlingHttpServletRequest request,SlingHttpServletResponse response) throws ServletException, IOException {
        ResourceResolver resolver=request.getResourceResolver();
        String userId=request.getParameter("userId");
        Resource userResource=resolver.getResource("/content/users/"+userId);
        if(userResource!=null){
            resolver.delete(userResource);
            resolver.commit();
        }


        response.getWriter().write("User Deletded Successfully");
    }

}
