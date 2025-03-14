
	@Test()
	public void addBook() throws Exception{
		test = report.startTest("Add Book Details to Library");
		RestAssured.baseURI="http://216.10.245.166";
			Response resp=given().
					header("Content-Type","application/json").
					body(payload.Addbook()).
					when().
					post("/Library/Addbook.php").
					then().
					extract().response();
			System.out.println("Response"+resp.asString());
			int statusCode=resp.getStatusCode();
			 resuableComponents.validateResponse (Integer.toString(statusCode),Integer.toString(200),test);
		
	}

