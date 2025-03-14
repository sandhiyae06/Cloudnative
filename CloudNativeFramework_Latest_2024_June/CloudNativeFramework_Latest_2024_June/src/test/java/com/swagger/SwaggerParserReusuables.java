package com.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponses;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.swagger.util.Json;

public class SwaggerParserReusuables {
	private static OpenAPI openAPI;
	private static String swaggerString = "";
	private static String baseURI = "https://petstore.swagger.io/v2";
	private static String resource = "";
	private static String parameter1 = "";
	private static String summary = "";
	private static String schema = "ref schema";
	private static String referenceGetContent = null;
	private static String originalGetContent = null;
	private static String referencePostContent = null;
	private static String originalPostContent = null;
	private static String baseTestContent = null;
	static StringBuilder str = new StringBuilder();
	Properties Readprop = new Properties();
	static File f = new File(".");

	
	// converting yaml to json
	public static String convertYamlToJson(String yaml) throws Exception {
		ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
		Object obj = yamlReader.readValue(yaml, Object.class);
		ObjectMapper jsonWriter = new ObjectMapper();
		return jsonWriter.writeValueAsString(obj);
	}
	// iterating through the paths and splitting the operations
	public static void printOperations(String key, PathItem item) throws IOException {
		if (item.getHead() != null) {
			System.out.print("HEAD - ");
			printOperation(item.getHead());
		}
		if (item.getGet() != null) {
			readGetTestContent();
			System.out.print("GET - ");
			resource = key;
			printGETOperation(item.getGet());
		}
		if (item.getPost() != null) {
			System.out.print("POST - ");
			resource = key;
			printPOSTOperation(item.getPost());

		}
		if (item.getPut() != null) {
			System.out.print("PUT - ");
			printPUTOperation(item.getPut());
		}
		if (item.getDelete() != null) {
			System.out.print("DELETE - ");
			printDELTEOperation(item.getDelete());
		}
		
	}

	// Get Operations
	private static void printGETOperation(Operation op) {
		System.out.println(op.getOperationId());
		summary = op.getOperationId();
		System.out.println("Parameters:");
		if (op.getParameters() != null) {
			for (Parameter p : op.getParameters()) {
				System.out.println(p.getName() + " : " + p.getSchema().getType());
				parameter1 = p.getName();
			}
			if (op.getRequestBody() != null) {
				printBody(op.getRequestBody());
			}
		}

		try {
			readGetTestContent();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			getTest(summary, baseURI, "", resource + "?" + parameter1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//printResponses(op.getResponses());

	}

	// delete operations
	private static void printDELTEOperation(Operation op) {
		System.out.println(op.getOperationId());
		summary = op.getOperationId();
		System.out.println("Parameters:");
		if (op.getParameters() != null) {
			for (Parameter p : op.getParameters()) {
				System.out.println(p.getName() + " : " + p.getSchema().getType());
				parameter1 = p.getName();
				// schema= p.getSchema();
			}
			if (op.getRequestBody() != null) {
				printBody(op.getRequestBody());
			}

		}
		try {
			readDELETETestContent();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			deleteTest(summary, baseURI, "", resource, "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printResponses(op.getResponses());

	}

	private static void printPUTOperation(Operation op) {
		System.out.println(op.getOperationId());
		summary = op.getOperationId();
		System.out.println("summary:" + summary);
		System.out.println("Parameters:");
		if (op.getParameters() != null) {
			for (Parameter p : op.getParameters()) {
				System.out.println(p.getName() + " : " + p.getSchema().getType());
				parameter1 = p.getName();
				// schema= p.getSchema();
			}
			if (op.getRequestBody() != null) {
				printBody(op.getRequestBody());
			}

		}
		try {
			readPUTTestContent();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		putPrintResponses(op.getResponses());

	}

	private static void printPOSTOperation(Operation op) {
		System.out.println(op.getOperationId());
		summary = op.getOperationId();
		System.out.println("Parameters:");
		if (op.getParameters() != null) {
			for (Parameter p : op.getParameters()) {
				System.out.println(p.getName() + " : " + p.getSchema().getType());
				parameter1 = p.getName();
				// schema= p.getSchema();
			}
			if (op.getRequestBody() != null) {
				printBody(op.getRequestBody());
			}

		}
		try {
			readPOSTTestContent();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		postPrintResponses(op.getResponses());

	}

	private static void printOperation(Operation op) {
		System.out.println(op.getOperationId());
		System.out.println("Parameters:");
		if (op.getParameters() != null) {
			for (Parameter p : op.getParameters()) {
				System.out.println(p.getName() + " : " + p.getSchema().getType());
			}
			if (op.getRequestBody() != null) {
				printBody(op.getRequestBody());
			}
		}

		printResponses(op.getResponses());
		System.out.println();
	}

	private static void printBody(RequestBody requestBody) {
		System.out.print("BODY: ");
		requestBody.getContent().forEach((key, item) -> {
			System.out.println(key);
			//printReference(item.getSchema());
		});
	}

	private static void postPrintResponses(ApiResponses responses) {
		System.out.println("Responses:");
		responses.forEach((key, item) -> {
			try {
				postTest(summary, baseURI, "", resource, "", key);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(key);
			System.out.println(key + ": " + item.getDescription());
			item.getContent().forEach((name, media) -> {
				System.out.println("response Code " + name);
				//printReference(media.getSchema());
			});
		});
	}

	private static void putPrintResponses(ApiResponses responses) {
		System.out.println("Responses:");
		responses.forEach((key, item) -> {
			try {
				putTest(summary, baseURI, "", resource, "", key);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(key);
			System.out.println(key + ": " + item.getDescription());
			item.getContent().forEach((name, media) -> {
				System.out.println("response Code " + name);
				//printReference(media.getSchema());
			});
		});
	}

	private static void printResponses(ApiResponses responses) {
		System.out.println("Responses:");
		responses.forEach((key, item) -> {

			System.out.println(key);
			System.out.println(key + ": " + item.getDescription());
			item.getContent().forEach((name, media) -> {
				System.out.println("response Code " + name);
				//printReference(media.getSchema());
			});
		});
	}

	private static void printReference(Schema schema) {
		if (schema instanceof ArraySchema) {
			ArraySchema as = (ArraySchema) schema;
			printReference(as.getItems());
		}

		String componentName = getComponentName(schema.get$ref());
		if (componentName != null) {
			Object objSchema = openAPI.getComponents().getSchemas().get(componentName);

			Schema componentSchema;
			if (objSchema instanceof ArraySchema) {
				ArraySchema as = (ArraySchema) objSchema;
				printReference(as.getItems());
				return;
			} else {
				componentSchema = (Schema) objSchema;
			}

			componentSchema.getProperties().forEach((key, item) -> {
				if (item instanceof Schema) {
					System.out.println("  " + key + " : ");
					System.out.println("  " + key + " : " + ((Schema) item).getType());
				} else {
					System.out.println("  " + key + " : " + item.getClass().getSimpleName());
				}
			});
		}
	}

	private static String getComponentName(String s) {
		if (s == null)
			return null;
		if (s.startsWith("#/components/schemas/")) {
			return s.substring("#/components/schemas/".length());
		}
		return null;
	}

	public static String readFile(File file) throws IOException {
		StringBuilder sb = new StringBuilder();
		InputStream in = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line + System.lineSeparator());
		}

		return sb.toString();
	}

	public static String createGetTest(String fileContents, String reportContent, String baseUri, String header,
			String queryParameter) throws IOException {

		String testContent = fileContents;
		testContent = fileContents.replace("Get Book Details from Library", reportContent);
		testContent = testContent.replace("http://216.10.245.166", baseUri);
		if (header != null) {
			testContent = testContent.replace("Content-Type\",\"application/json", header);
		}
		testContent = testContent.replace("getBook()", reportContent + "()");
		testContent = testContent.replace("/Library/GetBook.php?AuthorName=John", queryParameter);
		return testContent;
	}

	public static String createPutTest(String fileContents, String reportContent, String baseUri, String header,
			String resource, String Schema, String Response) throws IOException {

		String testContent = fileContents;
		testContent = fileContents.replace("Add Book Details to Library", reportContent);
		testContent = testContent.replace("http://216.10.245.166", baseUri);
		if (header != null) {
			testContent = testContent.replace("Content-Type\",\"application/json", header);
		}
		testContent = testContent.replace("putBook()", reportContent + "()");
		testContent = testContent.replace("/Library/Addbook.php", resource);
		testContent = testContent.replace("payload.Addbook()", Schema);
		testContent = testContent.replace("200", Response);
		return testContent;
	}

	public static String createPostTest(String fileContents, String reportContent, String baseUri, String header,
			String resource, String Schema, String Response) throws IOException {

		String testContent = fileContents;
		testContent = fileContents.replace("Add Book Details to Library", reportContent);
		testContent = testContent.replace("http://216.10.245.166", baseUri);
		if (header != null) {
			testContent = testContent.replace("Content-Type\",\"application/json", header);
		}
		testContent = testContent.replace("addBook()", reportContent + "()");
		testContent = testContent.replace("/Library/Addbook.php", resource);
		testContent = testContent.replace("payload.Addbook()", Schema);
		testContent = testContent.replace("200", Response);
		return testContent;
	}

	public static String createDeleteTest(String fileContents, String reportContent, String baseUri, String header,
			String resource, String Schema) throws IOException {

		String testContent = fileContents;
		testContent = fileContents.replace("Delete Book Details from Library", reportContent);
		testContent = testContent.replace("http://216.10.245.166", baseUri);
		if (header != null) {
			testContent = testContent.replace("Content-Type\",\"application/json", header);
		}
		testContent = testContent.replace("deleteBook()", reportContent + "()");
		testContent = testContent.replace("/Library/DeleteBook.php", resource);
		testContent = testContent.replace("payload.Deletebook()", Schema);
		return testContent;
	}

	public static void readGetTestContent() throws IOException {
		File file = new File(f.getCanonicalPath() + "//src//referencefiles//GetBookDetailsPass.java");
		referenceGetContent = readFile(file);
	}

	public static void readPOSTTestContent() throws IOException {
		File file = new File(f.getCanonicalPath() + "//src//referencefiles//AddBook.java");
		referencePostContent = readFile(file);
	}

	public static void readPUTTestContent() throws IOException {
		File file = new File(f.getCanonicalPath() + "//src//referencefiles//PutBook.java");
		referencePostContent = readFile(file);
	}

	public static void readDELETETestContent() throws IOException {
		File file = new File(f.getCanonicalPath() + "//src//referencefiles//DeleteBook.java");
		referencePostContent = readFile(file);
	}

	public static String readBASETestContent() throws IOException {
		File file = new File(f.getCanonicalPath() + "//src//referencefiles//Base.java");
		return baseTestContent = readFile(file);
	}

	public static void getTest(String summary, String BaseURI, String Header, String queryParameter)
			throws IOException {
		Header = "Content-Type\",\"application/json";
		if (!summary.equals("")) {
			originalGetContent = createGetTest(referenceGetContent, summary, BaseURI, Header, queryParameter);
			str.append(originalGetContent);
		}
	}

	public static void postTest(String summary, String BaseURI, String Header, String resource, String Schema,
			String Response) throws IOException {
		Header = "Content-Type\",\"application/json";
		if (!summary.equals("")) {
			originalPostContent = createPostTest(referencePostContent, summary, BaseURI, Header, resource, schema,
					Response);
			str.append(originalPostContent);
		}
	}

	public static void putTest(String summary, String BaseURI, String Header, String resource, String Schema,
			String Response) throws IOException {
		Header = "Content-Type\",\"application/json";
		if (!summary.equals("")) {
			originalPostContent = createPutTest(referencePostContent, summary, BaseURI, Header, resource, schema,
					Response);
			str.append(originalPostContent);
		}
	}

	public static void deleteTest(String summary, String BaseURI, String Header, String resource, String Schema)
			throws IOException {
		Header = "Content-Type\",\"application/json";
		if (!summary.equals("")) {
			originalPostContent = createDeleteTest(referencePostContent, summary, BaseURI, Header, resource, schema);
			str.append(originalPostContent);
		}
	}

	public static void writeToFile() throws IOException {
		baseTestContent = readBASETestContent();
		baseTestContent = baseTestContent.replace("}", str.toString() + "}");
		FileWriter fw = new FileWriter(f.getCanonicalPath() + "//src//swaggerOutput//testout.java");
		fw.write(baseTestContent);
		fw.close();
	}

}