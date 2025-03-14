package com.APITesting;
import static io.restassured.RestAssured.given;

import org.junit.Test;

import ResuableLibrary.ResuableComponents;
import baseTestPackage.BaseTest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;




public class Base extends BaseTest{
	ResuableComponents resuableComponents=new ResuableComponents();

	@Test()
	public void addPet() throws Exception{
		test = report.startTest("addPet");
		RestAssured.baseURI="https://petstore.swagger.io/v2";
			Response resp=given().
					header("Content-Type","application/json").
					body(ref schema).
					when().
					post("/pet").
					then().
					extract().response();
			System.out.println("Response"+resp.asString());
			int statusCode=resp.getStatusCode();
			 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(405),test);
		
	}

	@Test()
	public void updatePet() throws Exception{
		test = report.startTest("updatePet");
		RestAssured.baseURI="https://petstore.swagger.io/v2";
			Response resp=given().
					header("Content-Type","application/json").
					body(ref schema).
					when().
					put("/pet").
					then().
					extract().response();
			System.out.println("Response"+resp.asString());
			int statusCode=resp.getStatusCode();
			 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(400),test);
		
	}

	@Test()
	public void updatePet() throws Exception{
		test = report.startTest("updatePet");
		RestAssured.baseURI="https://petstore.swagger.io/v2";
			Response resp=given().
					header("Content-Type","application/json").
					body(ref schema).
					when().
					put("/pet").
					then().
					extract().response();
			System.out.println("Response"+resp.asString());
			int statusCode=resp.getStatusCode();
			 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(404),test);
		
	}

	@Test()
	public void updatePet() throws Exception{
		test = report.startTest("updatePet");
		RestAssured.baseURI="https://petstore.swagger.io/v2";
			Response resp=given().
					header("Content-Type","application/json").
					body(ref schema).
					when().
					put("/pet").
					then().
					extract().response();
			System.out.println("Response"+resp.asString());
			int statusCode=resp.getStatusCode();
			 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(405),test);
		
	}


	

@Test()
public void findPetsByStatus() throws Exception


{
	test = report.startTest("findPetsByStatus");
	RestAssured.baseURI="https://petstore.swagger.io/v2";
	Response resp=given().
	header("Content-Type","application/json").
	when().
	get("/pet/findByStatus?status").
	then().
	extract().response();
	int statusCode=resp.getStatusCode();
	 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(200),test);
	System.out.println(resp.asString());
	   JsonPath js=new JsonPath(resp.asString());
	   resuableComponents.validateResponse (resp.asString(),resp.asString(),test);
}


}

	

@Test()
public void findPetsByTags() throws Exception


{
	test = report.startTest("findPetsByTags");
	RestAssured.baseURI="https://petstore.swagger.io/v2";
	Response resp=given().
	header("Content-Type","application/json").
	when().
	get("/pet/findByTags?tags").
	then().
	extract().response();
	int statusCode=resp.getStatusCode();
	 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(200),test);
	System.out.println(resp.asString());
	   JsonPath js=new JsonPath(resp.asString());
	   resuableComponents.validateResponse (resp.asString(),resp.asString(),test);
}


}

	

@Test()
public void getPetById() throws Exception


{
	test = report.startTest("getPetById");
	RestAssured.baseURI="https://petstore.swagger.io/v2";
	Response resp=given().
	header("Content-Type","application/json").
	when().
	get("/pet/{petId}?petId").
	then().
	extract().response();
	int statusCode=resp.getStatusCode();
	 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(200),test);
	System.out.println(resp.asString());
	   JsonPath js=new JsonPath(resp.asString());
	   resuableComponents.validateResponse (resp.asString(),resp.asString(),test);
}


}

	@Test()
	public void updatePetWithForm() throws Exception{
		test = report.startTest("updatePetWithForm");
		RestAssured.baseURI="https://petstore.swagger.io/v2";
			Response resp=given().
					header("Content-Type","application/json").
					body(ref schema).
					when().
					post("/pet/{petId}").
					then().
					extract().response();
			System.out.println("Response"+resp.asString());
			int statusCode=resp.getStatusCode();
			 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(405),test);
		
	}


@Test()
public void deletePet() throws Exception
	{
	test = report.startTest("deletePet");
		RestAssured.baseURI="https://petstore.swagger.io/v2";
		Response resp=given().
		header("Content-Type","application/json").
		body(ref schema).
		when().delete("/pet/{petId}").
		then()
		.extract().response();
		int statusCode=resp.getStatusCode();
		System.out.println(resp.asString());
		 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(200),test);
		
		System.out.println(resp.asString());
		JsonPath js=new JsonPath(resp.asString());
	
	}


	@Test()
	public void uploadFile() throws Exception{
		test = report.startTest("uploadFile");
		RestAssured.baseURI="https://petstore.swagger.io/v2";
			Response resp=given().
					header("Content-Type","application/json").
					body(ref schema).
					when().
					post("/pet/{petId}/uploadImage").
					then().
					extract().response();
			System.out.println("Response"+resp.asString());
			int statusCode=resp.getStatusCode();
			 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(200),test);
		
	}


	

@Test()
public void getInventory() throws Exception


{
	test = report.startTest("getInventory");
	RestAssured.baseURI="https://petstore.swagger.io/v2";
	Response resp=given().
	header("Content-Type","application/json").
	when().
	get("/store/inventory?petId").
	then().
	extract().response();
	int statusCode=resp.getStatusCode();
	 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(200),test);
	System.out.println(resp.asString());
	   JsonPath js=new JsonPath(resp.asString());
	   resuableComponents.validateResponse (resp.asString(),resp.asString(),test);
}


}

	@Test()
	public void placeOrder() throws Exception{
		test = report.startTest("placeOrder");
		RestAssured.baseURI="https://petstore.swagger.io/v2";
			Response resp=given().
					header("Content-Type","application/json").
					body(ref schema).
					when().
					post("/store/order").
					then().
					extract().response();
			System.out.println("Response"+resp.asString());
			int statusCode=resp.getStatusCode();
			 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(200),test);
		
	}


	@Test()
	public void placeOrder() throws Exception{
		test = report.startTest("placeOrder");
		RestAssured.baseURI="https://petstore.swagger.io/v2";
			Response resp=given().
					header("Content-Type","application/json").
					body(ref schema).
					when().
					post("/store/order").
					then().
					extract().response();
			System.out.println("Response"+resp.asString());
			int statusCode=resp.getStatusCode();
			 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(400),test);
		
	}


	

@Test()
public void getOrderById() throws Exception


{
	test = report.startTest("getOrderById");
	RestAssured.baseURI="https://petstore.swagger.io/v2";
	Response resp=given().
	header("Content-Type","application/json").
	when().
	get("/store/order/{orderId}?orderId").
	then().
	extract().response();
	int statusCode=resp.getStatusCode();
	 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(200),test);
	System.out.println(resp.asString());
	   JsonPath js=new JsonPath(resp.asString());
	   resuableComponents.validateResponse (resp.asString(),resp.asString(),test);
}


}

@Test()
public void deleteOrder() throws Exception
	{
	test = report.startTest("deleteOrder");
		RestAssured.baseURI="https://petstore.swagger.io/v2";
		Response resp=given().
		header("Content-Type","application/json").
		body(ref schema).
		when().delete("/store/order/{orderId}").
		then()
		.extract().response();
		int statusCode=resp.getStatusCode();
		System.out.println(resp.asString());
		 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(200),test);
		
		System.out.println(resp.asString());
		JsonPath js=new JsonPath(resp.asString());
	
	}


	@Test()
	public void createUser() throws Exception{
		test = report.startTest("createUser");
		RestAssured.baseURI="https://petstore.swagger.io/v2";
			Response resp=given().
					header("Content-Type","application/json").
					body(ref schema).
					when().
					post("/user").
					then().
					extract().response();
			System.out.println("Response"+resp.asString());
			int statusCode=resp.getStatusCode();
			 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(default),test);
		
	}


	@Test()
	public void createUsersWithArrayInput() throws Exception{
		test = report.startTest("createUsersWithArrayInput");
		RestAssured.baseURI="https://petstore.swagger.io/v2";
			Response resp=given().
					header("Content-Type","application/json").
					body(ref schema).
					when().
					post("/user/createWithArray").
					then().
					extract().response();
			System.out.println("Response"+resp.asString());
			int statusCode=resp.getStatusCode();
			 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(default),test);
		
	}


	@Test()
	public void createUsersWithListInput() throws Exception{
		test = report.startTest("createUsersWithListInput");
		RestAssured.baseURI="https://petstore.swagger.io/v2";
			Response resp=given().
					header("Content-Type","application/json").
					body(ref schema).
					when().
					post("/user/createWithList").
					then().
					extract().response();
			System.out.println("Response"+resp.asString());
			int statusCode=resp.getStatusCode();
			 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(default),test);
		
	}


	

@Test()
public void loginUser() throws Exception


{
	test = report.startTest("loginUser");
	RestAssured.baseURI="https://petstore.swagger.io/v2";
	Response resp=given().
	header("Content-Type","application/json").
	when().
	get("/user/login?password").
	then().
	extract().response();
	int statusCode=resp.getStatusCode();
	 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(200),test);
	System.out.println(resp.asString());
	   JsonPath js=new JsonPath(resp.asString());
	   resuableComponents.validateResponse (resp.asString(),resp.asString(),test);
}


}

	

@Test()
public void logoutUser() throws Exception


{
	test = report.startTest("logoutUser");
	RestAssured.baseURI="https://petstore.swagger.io/v2";
	Response resp=given().
	header("Content-Type","application/json").
	when().
	get("/user/logout?password").
	then().
	extract().response();
	int statusCode=resp.getStatusCode();
	 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(200),test);
	System.out.println(resp.asString());
	   JsonPath js=new JsonPath(resp.asString());
	   resuableComponents.validateResponse (resp.asString(),resp.asString(),test);
}


}

	

@Test()
public void getUserByName() throws Exception


{
	test = report.startTest("getUserByName");
	RestAssured.baseURI="https://petstore.swagger.io/v2";
	Response resp=given().
	header("Content-Type","application/json").
	when().
	get("/user/{username}?username").
	then().
	extract().response();
	int statusCode=resp.getStatusCode();
	 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(200),test);
	System.out.println(resp.asString());
	   JsonPath js=new JsonPath(resp.asString());
	   resuableComponents.validateResponse (resp.asString(),resp.asString(),test);
}


}
	@Test()
	public void updateUser() throws Exception{
		test = report.startTest("updateUser");
		RestAssured.baseURI="https://petstore.swagger.io/v2";
			Response resp=given().
					header("Content-Type","application/json").
					body(ref schema).
					when().
					put("/user/{username}").
					then().
					extract().response();
			System.out.println("Response"+resp.asString());
			int statusCode=resp.getStatusCode();
			 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(400),test);
		
	}

	@Test()
	public void updateUser() throws Exception{
		test = report.startTest("updateUser");
		RestAssured.baseURI="https://petstore.swagger.io/v2";
			Response resp=given().
					header("Content-Type","application/json").
					body(ref schema).
					when().
					put("/user/{username}").
					then().
					extract().response();
			System.out.println("Response"+resp.asString());
			int statusCode=resp.getStatusCode();
			 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(404),test);
		
	}


@Test()
public void deleteUser() throws Exception
	{
	test = report.startTest("deleteUser");
		RestAssured.baseURI="https://petstore.swagger.io/v2";
		Response resp=given().
		header("Content-Type","application/json").
		body(ref schema).
		when().delete("/user/{username}").
		then()
		.extract().response();
		int statusCode=resp.getStatusCode();
		System.out.println(resp.asString());
		 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(200),test);
		
		System.out.println(resp.asString());
		JsonPath js=new JsonPath(resp.asString());
	
	}

}
	
