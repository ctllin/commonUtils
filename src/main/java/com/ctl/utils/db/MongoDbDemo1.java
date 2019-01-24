package com.ctl.utils.db;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import net.sf.json.JSONObject;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MongoDbDemo1 {
    static final Logger logger = LoggerFactory.getLogger(MongoDbDemo1.class);

    public static void main( String args[] ){
        try{
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient( "192.168.42.29" , 27017 );

            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
            System.out.println("Connect to database successfully"+mongoDatabase);
            //获取数据库名称
            String name = mongoDatabase.getName();
            logger.info("database-name={}",name);
            //创建表名
            try {
               // 创建固定集合person capped，大小限制为1024个字节，文档数量限制为2。
                CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions();
                createCollectionOptions.capped(false);
                createCollectionOptions.sizeInBytes(1024);
                createCollectionOptions.maxDocuments(2);
                mongoDatabase.createCollection("person",createCollectionOptions);
            } catch (Exception e) {
                logger.error("创建表名失败",e);
            }

            MongoCollection<Document> personCollection = mongoDatabase.getCollection("person");
            logger.info("集合{}选择成功,","person");

            //插入文档
            /**
             * 1. 创建文档 org.bson.Document 参数为key-value的格式
             * 2. 创建文档集合List<Document>
             * 3. 将文档集合插入数据库集合中 mongoCollection.insertMany(List<Document>) 插入单个文档可以用 mongoCollection.insertOne(Document)
             * */
            Document document = new Document("title", "MongoDB").append("description", "database").append("likes", 100).append("by", "Fly").append("_id",System.currentTimeMillis());
            List<Document> documents = new ArrayList<>();
            documents.add(document);
            personCollection.insertMany(documents);
            logger.info("文档插入成功");

            FindIterable<Document> findPersonDocuments = personCollection.find();
            MongoCursor<Document> mongoCursor = findPersonDocuments.iterator();
            while(mongoCursor.hasNext()){
                Document documentDb = mongoCursor.next();
                logger.info("person={},personJSON={}",documentDb,documentDb.toJson());
            }

            //更新文档   将文档中likes=100的文档修改为likes=200
            personCollection.updateMany(Filters.eq("likes", 100), new Document("$set",new Document("likes",200)));
            findPersonDocuments = personCollection.find();
            mongoCursor = findPersonDocuments.iterator();
            logger.info("----------------------------------------------------------------------------------------------");
            while(mongoCursor.hasNext()){
                Document documentDb = mongoCursor.next();
                logger.info("person={},personJSON={}",documentDb,documentDb.toJson());
            }
            logger.info("----------------------------------------------------------------------------------------------");

            //删除符合条件的第一个文档
            DeleteResult deleteResult = personCollection.deleteOne(Filters.eq("likes", 2000));
            logger.info("deleteResult={}", deleteResult.getDeletedCount());
            //删除所有符合条件的文档
            DeleteResult deleteManyResult = personCollection.deleteMany(Filters.eq("likes", 2000));
            logger.info("deleteManyResult={}", deleteManyResult.getDeletedCount());
            FindIterable<Document> getById = personCollection.find().filter(Filters.eq("_id", 1548323029675l));
            logger.info(getById.iterator().next().toJson());
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}
