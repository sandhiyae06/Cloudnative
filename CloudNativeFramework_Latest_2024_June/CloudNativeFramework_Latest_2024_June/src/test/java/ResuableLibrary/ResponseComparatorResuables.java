package ResuableLibrary;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;

public class ResponseComparatorResuables {

	boolean flag = false;
	public static int PRETTY_PRINT_INDENT_FACTOR = 4;
	public BufferedReader loadXMLFile(String strFilePath) throws Exception {
		File f = new File(System.getProperty("user.dir") + "\\" + strFilePath);
		Reader fileReader = new FileReader(f);
		BufferedReader bufReader = new BufferedReader(fileReader);
		return bufReader;
	}

	public String readXMLFile(BufferedReader bufReader) throws Exception {
		StringBuilder sb = new StringBuilder();
		String line = bufReader.readLine();
		while (line != null) {
			sb.append(line).append("\n");
			line = bufReader.readLine();
		}
		String xml2String = sb.toString();
		bufReader.close();
		return xml2String;
	}

	public Document getXMLDocument(String xmlString) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setCoalescing(true);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setIgnoringComments(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new ByteArrayInputStream(xmlString.getBytes()));
		doc.normalizeDocument();
		return doc;
	}

	public void compareXMLKeysValues(List<Map<String, String>> srcNodeMapList, List<Map<String, String>> tgtNodeMapList,
			ExtentTest test) throws Exception {
		boolean flag = false;
		if (srcNodeMapList.size() == tgtNodeMapList.size()) {
			for (int i = 0; i < srcNodeMapList.size(); i++) {
				Map<String, String> srcMap = srcNodeMapList.get(i);
				Map<String, String> tgtMap = tgtNodeMapList.get(i);
				for (String srcKey : srcMap.keySet()) {
					StringBuffer sb = new StringBuffer(srcKey);
					String sourceKey = sb.deleteCharAt(sb.length() - 1).toString();
					//test.log(Status.INFO,
					//		"---------Validation for following attribute ***" + sourceKey + "*** starts ");
					for (String tgtKey : tgtMap.keySet()) {
						StringBuffer sb2 = new StringBuffer(tgtKey);
						String targetKey = sb2.deleteCharAt(sb2.length() - 1).toString();
						if (sourceKey.equalsIgnoreCase(targetKey)) {
							//test.log(Status.INFO, "---------Validation for following attribute value ***" + sourceKey
							//		+ "*** starts ");
							if (srcMap.get(srcKey) != null && tgtMap.get(tgtKey) != null
									&& srcMap.get(srcKey).equalsIgnoreCase(tgtMap.get(tgtKey))) {
								flag = true;
								break;
							}
						}
					}
					if (flag) {
						flag = false;
						test.log(Status.PASS, "---------Value in Source file ( " + srcMap.get(srcKey)
								+ " ) and Target file ( " + tgtMap.get(srcKey) + " ) matches");
						test.log(Status.INFO, "---------Validation for following attribute value ***" + sourceKey
								+ "*** is successfull ");
					} else {
						test.log(Status.FAIL, "---------Value in Source file ( " + srcMap.get(srcKey)
								+ " ) and Target file ( " + tgtMap.get(srcKey) + " ) doesnot matches");
						test.log(Status.INFO, "---------Validation for following attribute value ***" + sourceKey
								+ "*** is not successfull ");
					}
				}
			}
		}
	}

	public void compareXMLKeys(List<Map<String, String>> srcNodeMapList, List<Map<String, String>> tgtNodeMapList,
			ExtentTest test) throws Exception {
		boolean flag = false;
		if (srcNodeMapList.size() == tgtNodeMapList.size()) {
			for (int i = 0; i < srcNodeMapList.size(); i++) {
				Map<String, String> srcMap = srcNodeMapList.get(i);
				Map<String, String> tgtMap = tgtNodeMapList.get(i);
				for (String srcKey : srcMap.keySet()) {
					StringBuffer sb = new StringBuffer(srcKey);
					String sourceKey = sb.deleteCharAt(sb.length() - 1).toString();
					//test.log(Status.INFO,
					//		"---------Validation for following attribute ***" + sourceKey + "*** starts ");
					for (String tgtKey : tgtMap.keySet()) {
						StringBuffer sb2 = new StringBuffer(tgtKey);
						String targetKey = sb2.deleteCharAt(sb2.length() - 1).toString();
						if (sourceKey.equalsIgnoreCase(targetKey)) {
							flag = true;
							break;
						}
					}
					if (flag) {
						flag = false;
						test.log(Status.PASS, "---------Attribute Name in Source file ( " + sourceKey
								+ " ) and Target file ( " + sourceKey + " ) matches");
						test.log(Status.INFO, "---------Validation for following attribute value ***" + sourceKey
								+ "*** is successfull ");
					} else {
						test.log(Status.FAIL, "---------Attribute Name in Source file ( " + sourceKey
								+ " ) and Target file ( " + sourceKey + " ) does not matches");
						test.log(Status.INFO, "---------Validation for following attribute value ***" + sourceKey
								+ "*** is not successfull ");
					}
				}
			}
		}
	}

	public void compareXMLSequence(List<Map<String, String>> srcNodeMapList, List<Map<String, String>> tgtNodeMapList,
			ExtentTest test) throws Exception {
		boolean flag = false;
		int srcIndex = 0, tgtIndex = 0;
		if (srcNodeMapList.size() == tgtNodeMapList.size()) {
			for (int i = 0; i < srcNodeMapList.size(); i++) {
				Map<String, String> srcMap = srcNodeMapList.get(i);
				Map<String, String> tgtMap = tgtNodeMapList.get(i);
				for (String srcKey : srcMap.keySet()) {
					StringBuffer sb = new StringBuffer(srcKey);
					String sourceKey = sb.deleteCharAt(sb.length() - 1).toString();
					//test.log(Status.INFO,
					//		"---------Validation for following attribute ***" + sourceKey + "*** sequence starts ");
					srcIndex++;
					for (String tgtKey : tgtMap.keySet()) {
						tgtIndex++;
						if (srcKey.equalsIgnoreCase(tgtKey)) {
							flag = true;
							break;
						}
					}
					if (flag && srcIndex == tgtIndex) {
						flag = false;
						test.log(Status.PASS, "---------Attribute Name in Source file ( " + sourceKey
								+ " ) and Target file ( " + sourceKey + " ) is in same sequence");
						test.log(Status.INFO, "---------Validation for following attribute  ***" + sourceKey
								+ "*** sequence is successfull ");
					} else {
						test.log(Status.FAIL, "---------Attribute Name in Source file ( " + sourceKey
								+ " ) and Target file ( " + sourceKey + " ) is not in same sequence");
						test.log(Status.INFO, "---------Validation for following attribute  ***" + sourceKey
								+ "*** sequence is not successfull ");
					}
					tgtIndex = 0;
				}
				srcIndex = 0;
			}
		}
	}

	public List<Map<String, String>> getNodeMapList(Document doc) throws Exception {
		Node n = doc.getFirstChild();
		NodeList nl = n.getChildNodes();
		Node an, an2;
		List<Map<String, String>> nodeMapList = new ArrayList<Map<String, String>>();
		int index = 0;
		for (int i = 0; i < nl.getLength(); i++) {

			Map<String, String> nodeMap = new HashMap<String, String>();
			an = nl.item(i);
			
			if (an.getNodeType() == Node.ELEMENT_NODE) {
				NodeList nl2 = an.getChildNodes();
				int mapIndex = 0;
				for (int i2 = 0; i2 < nl2.getLength(); i2++) {

					an2 = nl2.item(i2);
					if (!an2.getNodeName().equalsIgnoreCase("#text")) {
						mapIndex++;
						nodeMap.put(an2.getNodeName() + mapIndex, an2.getTextContent());

					}
				}
				nodeMapList.add(index, nodeMap);
				index++;
			}

		}
		return nodeMapList;
	}
	public List<Map<String, String>> getNodeMapList(Document doc,boolean flag) throws Exception {
		Node n = doc.getFirstChild();
		NodeList nl = n.getChildNodes();
		Node an, an2;
		List<Map<String, String>> nodeMapList = new ArrayList<Map<String, String>>();
		int index = 0;
		for (int i = 0; i < nl.getLength(); i++) {

			Map<String, String> nodeMap = new HashMap<String, String>();
			an = nl.item(i);
			
			if (an.getNodeType() == Node.ELEMENT_NODE) {
				NodeList nl2 = an.getChildNodes();
				int mapIndex = 0;
				for (int i2 = 0; i2 < nl2.getLength(); i2++) {

					an2 = nl2.item(i2);
					if (!an2.getNodeName().equalsIgnoreCase("#text")) {
						mapIndex++;
						if(flag)
						nodeMap.put(an2.getNodeName(), an2.getTextContent());
						else
							nodeMap.put(an2.getNodeName() + mapIndex, an2.getTextContent());
					}
				}
				nodeMapList.add(index, nodeMap);
				index++;
			}

		}
		return nodeMapList;
	}
	public void compareXMLFieldValue(Map<String, String> srcNodeMap, Map<String, String> tgtNodeMap,ExtentTest test,String srcKey,String tgtKey) throws Exception {
		//test.log(Status.INFO,
		//		"---------Validation for following field ***" + srcKey + "***  starts ");
		String srcValue=getXMLFieldValue(srcNodeMap,srcKey);
		String tgtValue=getXMLFieldValue(tgtNodeMap,tgtKey);
		if(srcValue.equalsIgnoreCase(tgtValue)){
			test.log(Status.PASS, "---------Field Value in Source file ( " + srcValue
					+ " ) and Target file ( " + tgtValue + " ) matches");
			test.log(Status.INFO, "---------Validation for following attribute  ***" + srcKey
					+ "*** sequence is successfull ");
		}else{
			test.log(Status.FAIL, "---------Field Value in Source file ( " + srcValue
					+ " ) and Target file ( " + tgtValue + " ) doesnot matches");
			test.log(Status.INFO, "---------Validation for following attribute  ***" + srcKey
					+ "*** sequence is not successfull ");
		}
		test.log(Status.INFO,
				"---------Validation for following field ***" + srcKey + "***  ends ");
	}
	
	public String getXMLFieldValue(Map<String, String> srcMap, String srcKey) {
		String srcValue="";
			if(srcKey.contains(",")){
				for(String key:srcKey.split(",")){
					srcValue=srcValue+srcMap.get(key);
				}
			}else{
				srcValue=srcMap.get(srcKey);
			}
		return srcValue;
	}
//--------------------------------------------------------------------
	public  JsonElement loadJSONFile(String strFilePath) throws Exception {
			FileReader fileReader = new FileReader(strFilePath);
			JsonElement	jsonElement = JsonParser.parseReader(fileReader);
		
		return jsonElement;
	}
	
	public  String loadJSONFile1(String strFilePath) throws Exception {
		FileReader fileReader = new FileReader(strFilePath);
		JsonElement	jsonElement = JsonParser.parseReader(fileReader);	
		return jsonElement.toString();
	}
	
	public Map<String,Object> readJSONFile(JsonElement jsonElement)throws Exception {
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		return gson.fromJson(jsonElement, type);
	}
		
	public void compareJSONKeys(Map<String, Object> srcNodeMap, Map<String, Object> tgtNodeMap,ExtentTest test) throws Exception {
		boolean flag = false;
				Map<String, Object> srcMap = srcNodeMap;
				Map<String, Object> tgtMap = tgtNodeMap;
				for (String srcKey : srcMap.keySet()) {
					//test.log(Status.INFO,
					//		"---------Validation for following attribute ***" + srcKey + "*** starts ");
					for (String tgtKey : tgtMap.keySet()) {
						if (srcKey.equalsIgnoreCase(tgtKey)) {
							flag = true;
							break;
						}
					}
					if (flag) {
						flag = false;
						test.log(Status.PASS, "---------Attribute Name in Source file ( " + srcKey
								+ " ) and Target file ( " + srcKey + " ) matches");
						test.log(Status.INFO, "---------Validation for following attribute value ***" + srcKey
								+ "*** is successfull ");
					} else {
						test.log(Status.FAIL, "---------Attribute Name in Source file ( " + srcKey
								+ " ) and Target file ( " + srcKey + " ) does not matches");
						test.log(Status.INFO, "---------Validation for following attribute value ***" + srcKey
								+ "*** is not successfull ");
					}
				}
			
		
	}
	
	public void compareJSONKeysValues(Map<String, Object> srcNodeMap, Map<String, Object> tgtNodeMap,ExtentTest test) throws Exception {
		boolean flag = false;
				Map<String, Object> srcMap = srcNodeMap;
				Map<String, Object> tgtMap = tgtNodeMap;
				for (String srcKey : srcMap.keySet()) {
					//test.log(Status.INFO,
					//		"---------Validation for following attribute ***" + srcKey + "*** starts ");
					for (String tgtKey : tgtMap.keySet()) {
						if (srcKey.equalsIgnoreCase(tgtKey)) {
							//test.log(Status.INFO, "---------Validation for following attribute value ***" + srcKey
							//		+ "*** starts ");
							if (srcMap.get(srcKey) != null && tgtMap.get(tgtKey) != null) {
								if(srcMap.get(srcKey) instanceof String&&tgtMap.get(tgtKey) instanceof String&& ((String) srcMap.get(srcKey)).equalsIgnoreCase((String) tgtMap.get(tgtKey))){
									flag = true;
								}if(srcMap.get(srcKey) instanceof Map&&tgtMap.get(tgtKey) instanceof Map){
									compareJSONKeysValues(srcMap.get(srcKey),tgtMap.get(tgtKey),test);
								}
								flag = true;
								break;
							}
						}
					}
					if (flag) {
						flag = false;
						test.log(Status.PASS, "---------Value in Source file ( " + srcMap.get(srcKey)
								+ " ) and Target file ( " + tgtMap.get(srcKey) + " ) matches");
						test.log(Status.INFO, "---------Validation for following attribute value ***" + srcKey
								+ "*** is successfull ");
					} else {
						test.log(Status.FAIL, "---------Value in Source file ( " + srcMap.get(srcKey)
								+ " ) and Target file ( " + tgtMap.get(srcKey) + " ) doesnot matches");
						test.log(Status.INFO, "---------Validation for following attribute value ***" + srcKey
								+ "*** is not successfull ");
					}
				}
			
		
	}
	public void compareJSONKeysValues(Object srcNodeMap, Object tgtNodeMap,ExtentTest test) throws Exception {
		boolean flag = false;
				Map<String, Object> srcMap = (Map<String, Object>) srcNodeMap;
				Map<String, Object> tgtMap = (Map<String, Object>) tgtNodeMap;
				for (String srcKey : srcMap.keySet()) {
					//test.log(Status.INFO,
					//		"---------Validation for following attribute ***" + srcKey + "*** starts ");
					for (String tgtKey : tgtMap.keySet()) {
						if (srcKey.equalsIgnoreCase(tgtKey)) {
						//	test.log(Status.INFO, "---------Validation for following attribute value ***" + srcKey
							//		+ "*** starts ");
							if (srcMap.get(srcKey) != null && tgtMap.get(tgtKey) != null) {
								if(srcMap.get(srcKey) instanceof String&&tgtMap.get(tgtKey) instanceof String&& ((String) srcMap.get(srcKey)).equalsIgnoreCase((String) tgtMap.get(tgtKey))){
									flag = true;
								}if(srcMap.get(srcKey) instanceof Map&&tgtMap.get(tgtKey) instanceof Map){
									compareJSONKeysValues(srcMap.get(srcKey),tgtMap.get(tgtKey),test);
								}
								flag = true;
								break;
							}
						}
					}
					if (flag) {
						flag = false;
						test.log(Status.PASS, "---------Value in Source file ( " + srcMap.get(srcKey)
								+ " ) and Target file ( " + tgtMap.get(srcKey) + " ) matches");
						test.log(Status.INFO, "---------Validation for following attribute value ***" + srcKey
								+ "*** is successfull ");
					} else {
						test.log(Status.FAIL, "---------Value in Source file ( " + srcMap.get(srcKey)
								+ " ) and Target file ( " + tgtMap.get(srcKey) + " ) doesnot matches");
						test.log(Status.INFO, "---------Validation for following attribute value ***" + srcKey
								+ "*** is not successfull ");
					}
				}
			
		
	}

	public void compareJSONSequence(Map<String, Object> srcNodeMap, Map<String, Object> tgtNodeMap,ExtentTest test) throws Exception {
		boolean flag = false;
		int srcIndex = 0, tgtIndex = 0;
			
				Map<String, Object> srcMap = srcNodeMap;
				Map<String, Object> tgtMap = tgtNodeMap;
				for (String srcKey : srcMap.keySet()) {
					//test.log(Status.INFO,
					//		"---------Validation for following attribute ***" + srcKey + "*** sequence starts ");
					srcIndex++;
					for (String tgtKey : tgtMap.keySet()) {
						tgtIndex++;
						if (srcKey.equalsIgnoreCase(tgtKey)) {
							flag = true;
							break;
						}
					}
					if (flag && srcIndex == tgtIndex) {
						flag = false;
						test.log(Status.PASS, "---------Attribute Name in Source file ( " + srcKey
								+ " ) and Target file ( " + srcKey + " ) is in same sequence");
						test.log(Status.INFO, "---------Validation for following attribute  ***" + srcKey
								+ "*** sequence is successfull ");
					} else {
						test.log(Status.FAIL, "---------Attribute Name in Source file ( " + srcKey
								+ " ) and Target file ( " + srcKey + " ) is not in same sequence");
						test.log(Status.INFO, "---------Validation for following attribute  ***" + srcKey
								+ "*** sequence is not successfull ");
					}
					tgtIndex = 0;
				}
				srcIndex = 0;
	}
	public void compareJSONFieldValue(Map<String, Object> srcNodeMap, Map<String, Object> tgtNodeMap,ExtentTest test,String srcKey,String tgtKey) throws Exception {
		//test.log(Status.INFO,
		//		"---------Validation for following field ***" + srcKey + "***  starts ");
		String srcValue=(String) srcNodeMap.get(srcKey);
		String tgtValue=(String) tgtNodeMap.get(tgtKey);
		if(srcValue.equalsIgnoreCase(tgtValue)){
			test.log(Status.PASS, "---------Field Value in Source file ( " + srcValue
					+ " ) and Target file ( " + tgtValue + " ) matches");
			test.log(Status.INFO, "---------Validation for following attribute  ***" + srcKey
					+ "*** sequence is successfull ");
		}else{
			test.log(Status.FAIL, "---------Field Value in Source file ( " + srcValue
					+ " ) and Target file ( " + tgtValue + " ) doesnot matches");
			test.log(Status.INFO, "---------Validation for following attribute  ***" + srcKey
					+ "*** sequence is not successfull ");
		}
		test.log(Status.INFO,
				"---------Validation for following field ***" + srcKey + "***  ends ");
	}
	public void compareJSONFieldValueMap(Map<String, Object> srcNodeMap, Map<String, Object> tgtNodeMap,ExtentTest test,String srcKey,String tgtKey) throws Exception {
		//test.log(Status.INFO,
		//		"---------Validation for following field ***" + srcKey + "***  starts ");
			
		String[] srcKeyArray=srcKey.split("\\.");
		String[] tgtKeyArray=tgtKey.split("\\.");
		Map<String,String> srcMap=(Map<String,String>) srcNodeMap.get(srcKeyArray[0]);
		Map<String,String> tgtMap=(Map<String,String>) tgtNodeMap.get(tgtKeyArray[0]);
		String srcValue = getJSONFieldValue(srcMap, srcKeyArray);
		String tgtValue=getJSONFieldValue(tgtMap, tgtKeyArray);
		if(srcValue.equalsIgnoreCase(tgtValue)){
			test.log(Status.PASS, "---------Field Value in Source file ( " + srcValue
					+ " ) and Target file ( " + tgtValue + " ) matches");
			test.log(Status.INFO, "---------Validation for following attribute  ***" + srcKey
					+ "*** sequence is successfull ");
		}else{
			test.log(Status.FAIL, "---------Field Value in Source file ( " + srcValue
					+ " ) and Target file ( " + tgtValue + " ) doesnot matches");
			test.log(Status.INFO, "---------Validation for following attribute  ***" + srcKey
					+ "*** sequence is not successfull ");
		}
		test.log(Status.INFO,
				"---------Validation for following field ***" + srcKey + "***  ends ");
	}

	public String getJSONFieldValue(Map<String, String> srcMap, String[] srcKeyArray) {
		String srcValue="";int i=0;
		for(String srcKey1:srcKeyArray){
			if(i>0){
			if(srcKey1.contains(",")){
				for(String key:srcKey1.split(",")){
					srcValue=srcValue+srcMap.get(key);
				}
			}else{
				srcValue=srcMap.get(srcKey1);
			}
			}i++;
		}
		return srcValue;
	}
	
	
	public void converJsontToXml(File jsonFile,String xmlFile) throws Exception
	{
		
		Reader fileReader = new FileReader(jsonFile);
		BufferedReader bufReader = new BufferedReader(fileReader);
		StringBuilder sb = new StringBuilder();
		String line = bufReader.readLine();
		while (line != null) {
			sb.append(line).append("\n");
			line = bufReader.readLine();
		}
		bufReader.close();
		String xml2String = sb.toString();
		JSONObject json = new JSONObject(xml2String);
		String xml = XML.toString(json);
		System.out.println(xml);
		FileWriter fw = new FileWriter(xmlFile);
		fw.write(xml);
		fw.close();
	}
	


	public void convertXmlToJSON(File xmlFile,String jsonFile) throws Exception
	{
		
		Reader fileReader = new FileReader(xmlFile);
		BufferedReader bufReader = new BufferedReader(fileReader);
		StringBuilder sb = new StringBuilder();
		String line = bufReader.readLine();
		while (line != null) {
			sb.append(line).append("\n");
			line = bufReader.readLine();
		}
		bufReader.close();
		String xml2String = sb.toString();
		JSONObject xmlJSONObj = XML.toJSONObject(xml2String);
		String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
		System.out.println(jsonPrettyPrintString);
		FileWriter fw = new FileWriter(jsonFile);
		fw.write(jsonPrettyPrintString);
		fw.close();
	}

}