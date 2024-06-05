package com.gitaction.dynamicproperty.service;

import com.gitaction.dynamicproperty.model.DynamicPropertyDetails;
import com.gitaction.dynamicproperty.model.ServerConfigDetails;
import com.gitaction.dynamicproperty.model.SprPropertyDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;


@Service
public class PropertyServiceImplements implements PropertyService {

    @Value("${custom.mongodb.url}")
    private String mongoUrl;

    @Override
    public String saveProperty(DynamicPropertyDetails dynamicPropertyDetails, String databaseName, String collectionName, String uniqueFieldName) {
        MongoTemplate customMongoTemplate = getMongoTemplateForDatabase(databaseName);
        upsertProperty(customMongoTemplate, dynamicPropertyDetails, collectionName, uniqueFieldName);
        return "saved";
    }

    @Override
    public String saveProperty(ServerConfigDetails serverConfigDetails, String databaseName, String collectionName,String uniqueFieldName) {
        MongoTemplate customMongoTemplate = getMongoTemplateForDatabase(databaseName);
        upsertProperty(customMongoTemplate, serverConfigDetails, collectionName, uniqueFieldName);
        return "saved";
    }

    @Override
    public String saveProperty(SprPropertyDetails sprPropertyDetails, String databaseName, String collectionName,String uniqueFieldName) {
        MongoTemplate customMongoTemplate = getMongoTemplateForDatabase(databaseName);
        upsertProperty(customMongoTemplate, sprPropertyDetails, collectionName, uniqueFieldName);
        return "saved";
    }

    private MongoTemplate getMongoTemplateForDatabase(String databaseName) {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoUrl + databaseName));
    }

    private <T> void upsertProperty(MongoTemplate mongoTemplate, T property, String collectionName, String uniqueFieldName) {
        Query query = new Query();
        query.addCriteria(Criteria.where(uniqueFieldName).is(getUniqueFieldValue(property, uniqueFieldName)));

        Update update = new Update();
        for (Field field : property.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(property);
                if (value != null) {
                    update.set(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error accessing field value", e);
            }
        }

        LocalDateTime now = LocalDateTime.now();
        update.set("modifiedDate", now);
        if (mongoTemplate.findOne(query, property.getClass(), collectionName) == null) {
            update.set("createdDate", now);
        }

        mongoTemplate.upsert(query, update, collectionName);
    }

    private <T> Object getUniqueFieldValue(T property, String uniqueFieldName) {
        try {
            Field field = property.getClass().getDeclaredField(uniqueFieldName);
            field.setAccessible(true);
            return field.get(property);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error getting unique field value", e);
        }
    }
}

// package com.gitaction.dynamicproperty.service;

// import com.gitaction.dynamicproperty.model.DynamicPropertyDetails;
// import com.gitaction.dynamicproperty.model.ServerConfigDetails;
// import com.gitaction.dynamicproperty.model.SprPropertyDetails;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.data.mongodb.core.MongoTemplate;
// import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
// import org.springframework.data.mongodb.core.query.Criteria;
// import org.springframework.data.mongodb.core.query.Query;
// import org.springframework.data.mongodb.core.query.Update;
// import org.springframework.stereotype.Service;

// import java.lang.reflect.Field;

// @Service
// public class PropertyServiceImplements implements PropertyService {

//     @Value("${custom.mongodb.url}")
//     private String mongoUrl;

//     @Override
//     public String saveProperty(DynamicPropertyDetails dynamicPropertyDetails, String databaseName, String collectionName) {
//         MongoTemplate customMongoTemplate = getMongoTemplateForDatabase(databaseName);
//         upsertProperty(customMongoTemplate, dynamicPropertyDetails, collectionName, "key");
//         return "saved";
//     }

//     @Override
//     public String saveProperty(ServerConfigDetails serverConfigDetails, String databaseName, String collectionName) {
//         MongoTemplate customMongoTemplate = getMongoTemplateForDatabase(databaseName);
//         upsertProperty(customMongoTemplate, serverConfigDetails, collectionName, "name");
//         return "saved";
//     }

//     @Override
//     public String saveProperty(SprPropertyDetails sprPropertyDetails, String databaseName, String collectionName) {
//         MongoTemplate customMongoTemplate = getMongoTemplateForDatabase(databaseName);
//         upsertProperty(customMongoTemplate, sprPropertyDetails, collectionName, "key");
//         return "saved";
//     }

//     private MongoTemplate getMongoTemplateForDatabase(String databaseName) {
//         return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoUrl + databaseName));
//     }

//     private <T> void upsertProperty(MongoTemplate mongoTemplate, T property, String collectionName, String uniqueFieldName) {
//         Query query = new Query();
//         query.addCriteria(Criteria.where(uniqueFieldName).is(getUniqueFieldValue(property, uniqueFieldName)));

//         Update update = new Update();
//         for (Field field : property.getClass().getDeclaredFields()) {
//             field.setAccessible(true);
//             try {
//                 Object value = field.get(property);
//                 if (value != null) {
//                     update.set(field.getName(), value);
//                 }
//             } catch (IllegalAccessException e) {
//                 throw new RuntimeException("Error accessing field value", e);
//             }
//         }

//         mongoTemplate.upsert(query, update, collectionName);
//     }

//     private <T> Object getUniqueFieldValue(T property, String uniqueFieldName) {
//         try {
//             Field field = property.getClass().getDeclaredField(uniqueFieldName);
//             field.setAccessible(true);
//             return field.get(property);
//         } catch (NoSuchFieldException | IllegalAccessException e) {
//             throw new RuntimeException("Error getting unique field value", e);
//         }
//     }
// }

// package com.gitaction.dynamicproperty.service;

// import com.gitaction.dynamicproperty.model.DynamicPropertyDetails;
// import com.gitaction.dynamicproperty.model.ServerConfigDetails;
// import com.gitaction.dynamicproperty.model.SprPropertyDetails;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.data.mongodb.core.MongoTemplate;
// import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
// import org.springframework.stereotype.Service;


// @Service
// public class PropertyServiceImplements implements PropertyService {

//     @Value("${custom.mongodb.url}")
//     private String mongoUrl;

//     @Override
//     public String saveProperty(DynamicPropertyDetails dynamicPropertyDetails,String databaseName,String collectionName) {
       
//         MongoTemplate customMongoTemplate = getMongoTemplateForDatabase(databaseName);
//         customMongoTemplate.save(dynamicPropertyDetails, collectionName);
//         return "saved";
//     }

//     @Override
//     public String saveProperty(ServerConfigDetails serverConfigDetails, String databaseName, String collectionName) {
//         MongoTemplate customMongoTemplate = getMongoTemplateForDatabase(databaseName);
        
//         customMongoTemplate.save(serverConfigDetails, collectionName);
//         return "saved";
//     }
//     @Override
//     public String saveProperty(SprPropertyDetails sprPropertyDetails, String databaseName, String collectionName) {
//         MongoTemplate customMongoTemplate = getMongoTemplateForDatabase(databaseName);

//         customMongoTemplate.save(sprPropertyDetails, collectionName);
//         return "saved";
//     }

//     private MongoTemplate getMongoTemplateForDatabase(String databaseName) {
//         return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoUrl+ databaseName));
//     }

   
    
// }
