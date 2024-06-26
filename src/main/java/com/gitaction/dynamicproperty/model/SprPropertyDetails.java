package com.gitaction.dynamicproperty.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Data;

@Data
public class SprPropertyDetails
{
    
    @Id
    private String id;
    private String authorName;
    private String authorEmail;
    @Indexed(unique = true)
    private String key;
    private String value;
    private boolean isSecure;
    private String _class;
    @CreatedDate
    private String createdDate;
    @LastModifiedDate
    private String modifiedDate;
    
}
