package test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mongodb.Jdbc;

import org.bson.Document;

import tools.FileUtil;

public class TestJdbc {
	static Set<String> mark = new HashSet<String>();
	static String file_path = "user_program_data.txt"; 
	public static void main(String[] args) {
		List<Document> docs = new ArrayList<Document>();
		List<String> file_lines = FileUtil.readLogByList(file_path);
		for (int i = 0; i < file_lines.size(); i++) {
			String file_line = file_lines.get(i);
			Document doc = null;
			try{
				doc = Document.parse(file_line);
			} catch(Exception e) {
				System.out.println("InsertToCollectionFromFile 中, " + file_line + "无法转换为Document");
			}
			if (doc != null) {
				String id = doc.getString("id");
				if (mark.contains(id)) {
					continue;
				}
				mark.add(id);
				docs.add(doc);
			}
		}
		System.out.println(docs.size());
		List<Document> local_docs = Jdbc.find("user_program_data");
		for (int i = 0; i< local_docs.size(); i++) {
			Document local_doc = local_docs.get(i);
			String id = local_doc.getString("id");
			if (mark.contains(id)) {
				continue;
			}
			mark.add(id);
			docs.add(local_doc);
		}
		
		System.out.println(docs.size());
		
		for (int i = 0; i < docs.size(); i++) {
			Document doc = docs.get(i);
			String str_doc = doc.toJson();
			FileUtil.writeLog(file_path + "_new", str_doc);
		}
	}
		//Jdbc.SaveToFileFromCollection("user_program_data.txt", "user_program_data");
		//Jdbc.InsertToCollectionFromFile("user_program_data.txt", "user_program_data");
}
