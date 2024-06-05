package com.gitaction.dynamicproperty.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;
import com.gitaction.dynamicproperty.model.DynamicPropertyDetails;
import com.gitaction.dynamicproperty.model.ServerConfigDetails;
import com.gitaction.dynamicproperty.model.SprPropertyDetails;
import com.gitaction.dynamicproperty.service.PropertyService;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ActionController {

    @Autowired
    private PropertyService propertyService;

    @PostMapping("/dp")
    public void gitwebhook(@RequestBody List<JsonNode> payloads) {
        try {
            for (JsonNode payload : payloads) 
            {
                
                String filepath= payload.get("file").asText();
                String authorName= payload.get("author").get("name").asText();
                String authorEmail= payload.get("author").get("email").asText();
                JsonNode changes= payload.get("changes");
                String database=filepath.split("/")[1];
                String collection=filepath.split("/")[2];
                
                if(database.equals("dynamicProperty"))
                {
                    
                    System.out.println("Dynamic Property");
                    DynamicPropertyDetails dynamicPropertyDetails = new DynamicPropertyDetails();
                    dynamicPropertyDetails.setAuthorName(authorName);
                    dynamicPropertyDetails.setAuthorEmail(authorEmail);
                    dynamicPropertyDetails.setKey(payload.get("key").asText());
                    if(changes.has("property"))
                    {
                        dynamicPropertyDetails.setProperty(changes.get("property").asText());
                    }
                    if(changes.has("value"))
                    {
                        dynamicPropertyDetails.setValue(changes.get("value").asText());
                    }
                    if(changes.has("reason"))
                    {
                        dynamicPropertyDetails.setReason(changes.get("reason").asText());
                    }
                    if((changes.has("deleted")))
                    {
                        dynamicPropertyDetails.setDeleted(changes.get("deleted").asBoolean());
                    }
                    propertyService.saveProperty(dynamicPropertyDetails,database,collection,"key");
                }
                else if(database.equals("serverConfig"))
                {
                   
                    System.out.println("Server Config");
                    ServerConfigDetails serverConfigDetails = new ServerConfigDetails();
                    serverConfigDetails.setAuthorName(authorName);
                    serverConfigDetails.setAuthorEmail(authorEmail);
                    serverConfigDetails.setName(payload.get("name").asText()); //name is the unique field
                    if(changes.has("dbName"))
                    {
                        serverConfigDetails.setDbName(changes.get("dbName").asText());
                    }
                    if(changes.has("url"))
                    {
                        serverConfigDetails.setUrl(changes.get("url").asText());
                    }
                    if(changes.has("partnerId"))
                    {
                        serverConfigDetails.setPartnerId(changes.get("partnerId").asLong());
                    }
                    if(changes.has("clientId"))
                    {
                        serverConfigDetails.setClientId(changes.get("clientId").asLong());
                    }
                    if(changes.has("serverCategory"))
                    {
                        serverConfigDetails.setServerCategory(changes.get("serverCategory").asText());
                    }
                    if(changes.has("serverType"))
                    {
                        serverConfigDetails.setServerType(changes.get("serverType").asText());
                    }
                    
                    if(changes.has("_class"))
                    {
                        serverConfigDetails.set_class(changes.get("_class").asText());
                    }
                    propertyService.saveProperty(serverConfigDetails,database,collection,"name");
                   
                }
                else if(database.equals("sprProperty"))
                {
                    
                    System.out.println("Spr Property");
                    SprPropertyDetails sprPropertyDetails = new SprPropertyDetails();
                    sprPropertyDetails.setAuthorName(authorName);
                    sprPropertyDetails.setAuthorEmail(authorEmail);
                    sprPropertyDetails.setKey(payload.get("key").asText());
                    if(changes.has("value"))
                    {
                        sprPropertyDetails.setValue(changes.get("value").asText());
                    }
                    if(changes.has("_class"))
                    {
                        sprPropertyDetails.set_class(changes.get("_class").asText());
                    }
                    if(changes.has("isSecure"))
                    {
                        sprPropertyDetails.setSecure(changes.get("isSecure").asBoolean());
                    }
                    propertyService.saveProperty(sprPropertyDetails,database,collection,"key");
                    
                }
                else
                {
                    System.out.println("Invalid Database");
                }

                
            }
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


