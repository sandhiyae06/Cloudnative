package com.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.parser.OpenAPIV3Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.swagger.util.Json;

public class SwaggerParser {
	public static OpenAPI openAPI;
	public static String swaggerString = "";
	
	static SwaggerParserReusuables swag= new SwaggerParserReusuables();
	static File f = new File(".");

	public static  void main(String[] args) throws Exception {

		File f1 = new File(f.getCanonicalPath() + "//src//yamlfile//Petstore.yaml");
		System.out.println(f.getCanonicalPath() );
		swaggerString = FileUtils.readFileToString(f1);
		String JSON = SwaggerParserReusuables.convertYamlToJson(swaggerString);
		System.out.println("JSON" + JSON);
		FileWriter fw = new FileWriter(f.getCanonicalPath() + "//src//yamlfile//petStore.json");
		fw.write(JSON);
		fw.close();
		String filepath=f.getCanonicalPath() + "//src//yamlfile//petStore.json";
		openAPI = new io.swagger.v3.parser.OpenAPIV3Parser().read(filepath);
		openAPI.getPaths().forEach((key, item) -> {

			try {
				swag.printOperations(key, item);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		swag.writeToFile();
	}

}