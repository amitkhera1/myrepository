/*
 * This class contains assertions for the following business and technical rules:
 * 1.) 5 percent discount business rule when total is greater than £30
 * 2.) 10 percent business rule when year of publication is greater than 2000
 * 3.) Method: POST (Any other method should return 405 METHOD NOT ALLOWED)
 * 4.) Location: /checkout (Any other call should return 404 NOT FOUND)
 * 5.) Accepts: application/json
 */

import org.testng.annotations.Test;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class BusinessRuleAssertions {

	@Test
	public void assertDiscount5PercentBusinessRule()
		{
	
			MyBooks firstBook = new MyBooks("Moby Dick",2000,200.0);
			MyBooks secondBook = new MyBooks("The Terrible Privacy of Maxwell Sim",2000,200.0);
		
			double totalDiscountedPrice = BusinessLogicRepository.calculateDiscount(firstBook, secondBook);
		
		
			String myJson = BusinessLogicRepository.addJsonWrapper(firstBook.toString(), secondBook.toString());
			String responseFromApi = JerseyConnectionWrapper.createClient(myJson,"POST");
		
		
			Assert.assertEquals(String.valueOf(totalDiscountedPrice), responseFromApi);
		
		}
	
	@Test
	public void assertDiscountYearOfPublicationBusinessRule()
		{
			MyBooks firstBook = new MyBooks("Moby Dick",2001,100.0);
			MyBooks secondBook = new MyBooks("The Terrible Privacy of Maxwell Sim",2001,100.0);
		
			double totalDiscountedPrice = BusinessLogicRepository.calculateDiscount(firstBook, secondBook);
				
			String myJson = BusinessLogicRepository.addJsonWrapper(firstBook.toString(), secondBook.toString());
			String responseFromApi = JerseyConnectionWrapper.createClient(myJson,"POST");
				
			Assert.assertEquals(String.valueOf(totalDiscountedPrice), responseFromApi);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void assertNonPostResponse()
	{
		//submits a GET request instead of POST and asserts a 405 response
		String myJson = "{"+"\"books\""+":"+"[]}";
		String responseFromApi = JerseyConnectionWrapper.createClient(myJson,"GET");
		boolean expected405;
		
		if(responseFromApi.contains("405"))
			expected405 = true;
		else 
			expected405 = false;
		
		Assert.assertEquals(true, expected405);
		

	}
	
	@Test(dependsOnMethods = {"assertNonPostResponse"}, enabled=false) 
	public void assertEndpointLocation()
	{
	// provides a wrong endpoint and expects a 404. This @TEST test case is disabled as shown above.
		
		MyBooks firstBook = new MyBooks("Moby Dick",2001,100.0);
		MyBooks secondBook = new MyBooks("The Terrible Privacy of Maxwell Sim",2001,100.0);
	
				
		String myJson = BusinessLogicRepository.addJsonWrapper(firstBook.toString(), secondBook.toString());
		String responseFromApi = JerseyConnectionWrapper.createClient(myJson,"POST", false);
		
		boolean expected404;
		
		if(responseFromApi.contains("404"))
			expected404 = true;
		else 
			expected404 = false;
		
		Assert.assertEquals(true, expected404);
		
	}
	
	@Test(dependsOnMethods = {"assertNonPostResponse"})
	public void assertContentType()
	{
		//provides text/xml as content-type instead of application/json
		
		MyBooks firstBook = new MyBooks("Moby Dick",2001,100.0);
		MyBooks secondBook = new MyBooks("The Terrible Privacy of Maxwell Sim",2001,100.0);
	
				
		String myJson = BusinessLogicRepository.addJsonWrapper(firstBook.toString(), secondBook.toString());
		String responseFromApi = JerseyConnectionWrapper.createClient(myJson,"POST", "text/xml");
		
		boolean expected415;
		
		if(responseFromApi.contains("415"))
			expected415 = true;
		else 
			expected415 = false;
		
		Assert.assertEquals(true, expected415);
		
	}
	
}
