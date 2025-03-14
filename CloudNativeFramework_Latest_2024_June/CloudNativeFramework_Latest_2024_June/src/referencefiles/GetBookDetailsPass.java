
	

@Test()
public void getBook() throws Exception


{
	test = report.startTest("Get Book Details from Library");
	RestAssured.baseURI="http://216.10.245.166";
	Response resp=given().
	header("Content-Type","application/json").
	when().
	get("/Library/GetBook.php?AuthorName=John").
	then().
	extract().response();
	int statusCode=resp.getStatusCode();
	 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(200),test);
	System.out.println(resp.asString());
	   JsonPath js=new JsonPath(resp.asString());
	   resuableComponents.validateResponse (resp.asString(),resp.asString(),test);
}


}
