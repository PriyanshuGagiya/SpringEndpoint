package com.gitaction.dynamicproperty.service;

import com.gitaction.dynamicproperty.model.DynamicPropertyDetails;
import com.gitaction.dynamicproperty.model.ServerConfigDetails;
import com.gitaction.dynamicproperty.model.SprPropertyDetails;

public interface PropertyService 
{
    public String saveProperty(DynamicPropertyDetails dynamicPropertyDetails,String databaseName,String collectionName,String uniqueFieldName);
    public String saveProperty(ServerConfigDetails serverConfigDetails,String databaseName,String collectionName,String uniqueFieldName);
    public String saveProperty(SprPropertyDetails sprPropertyDetails,String databaseName,String collectionName,String uniqueFieldName);
    
}
