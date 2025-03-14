
@Test()
public void deleteBook() throws Exception
	{
	test = report.startTest("Delete Book Details from Library");
		RestAssured.baseURI="http://216.10.245.166";
		Response resp=given().
		header("Content-Type","application/json").
		body(payload.Deletebook()).
		when().delete("/Library/DeleteBook.php").
		then()
		.extract().response();
		int statusCode=resp.getStatusCode();
		System.out.println(resp.asString());
		 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(200),test);
		
		System.out.println(resp.asString());
		JsonPath js=new JsonPath(resp.asString());
	
	}

