package mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import tools.FileUtil;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;


/**
 * ��������mongodb���ݿ�
 * @author chenjiashou
 *
 */
public class Jdbc {
	static MongoDatabase MDB = null;
	static final String database_name = "sina";//�������Ĭ��ֻ��ʹ����һ�����ݿ�
	
	/**
	 * ��֤��������ֻ��������һ�Ρ�
	 * @param database
	 * @return
	 */
	public static MongoDatabase getDataBase() {
		if (null == MDB) {
			 MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
			 MDB= mongoClient.getDatabase(database_name);
		}
		return MDB;
	}
	
	/**
	 * �õ�MongoColleciton by collectionName
	 * @param collectionName
	 * @return
	 */
	public static MongoCollection<Document> getCollection(String collectionName) {
		if (MDB == null) {
			getDataBase();
		}
		return MDB.getCollection(collectionName);
	}
	
	/**
	 * ����doc �� collectionName��
	 * @param collectionName
	 * @param doc
	 * @return �ɹ�����true,���򷵻�false��
	 */
	public static boolean insert_doc(String collectionName, Document doc) {
		MongoCollection<Document> coll = getCollection(collectionName);
		coll.insertOne(doc);
		return true;
	}
	
	/**
	 * ����many doc into collection
	 * @param collectionName
	 * @param docs
	 * @return
	 */
	public static boolean insert_docs(String collectionName, List<Document> docs) {
		MongoCollection<Document> coll = getCollection(collectionName);
		coll.insertMany(docs);
		return true;
	}
	
	/**
	 * �õ�collectionName������
	 * @param collectionName
	 * @return List<Document>
	 */
	public static List<Document> find(String collectionName) {
		MongoCollection<Document> coll = getCollection(collectionName);
		FindIterable<Document> findIterable = coll.find();
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		List<Document> docs = new ArrayList<Document>();
		while(mongoCursor.hasNext()){  
			Document doc = mongoCursor.next();
			docs.add(doc);
		}
		return docs;
	}
	
	/**
	 * ��Ҫ��ѯ����Ϣд��find_doc��
	 * @param collectionName
	 * @param find_doc
	 * @return List<Document>
	 */
	public static List<Document> find(String collectionName, Document find_doc) {
		MongoCollection<Document> coll = getCollection(collectionName);
		
		FindIterable<Document> iter = coll.find(find_doc);
		MongoCursor<Document> mongoCursor = iter.iterator();
		
		List<Document> docs = new ArrayList<Document>();
		while(mongoCursor.hasNext()){  
			Document doc = mongoCursor.next();
			docs.add(doc);
		}
		return docs;
	}
	
	/**
	 * ɾ��һ��collection
	 */
	public static void deleteCollection(String collectionName) {
		MongoCollection<Document> coll = getCollection(collectionName);
		Document doc = new Document();
		coll.deleteMany(doc);
	}
	
	/**
	 * ���ļ��ж�ȡdocs������Collection��ȥ
	 */
	public static void InsertToCollectionFromFile(String file_path, String collection_name) {
		List<String> file_lines = FileUtil.readLogByList(file_path);
		for (int i = 0; i < file_lines.size(); i++) {
			String file_line = file_lines.get(i);
			Document doc = null;
			try{
				doc = Document.parse(file_line);
			} catch(Exception e) {
				System.out.println("InsertToCollectionFromFile ��, " + file_line + "�޷�ת��ΪDocument");
			}
			if (doc != null) {
				insert_doc(collection_name, doc);//���ȥ
			}
		}
	} 
	
	/**
	 * ��collection�������ļ���
	 */
	public static void SaveToFileFromCollection(String file_save_path, String collection_name) {
		List<Document> docs = find(collection_name);
		for (int i = 0; i < docs.size(); i++) {
			Document doc = docs.get(i);
			String str_doc = doc.toJson();
			FileUtil.writeLog(file_save_path, str_doc);
		}
	}
}
