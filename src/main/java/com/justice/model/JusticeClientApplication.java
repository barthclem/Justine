package com.justice.model;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@EnableScheduling
public class JusticeClientApplication {

	private static final String REST_SERVICE_URI="http://localhost:8080/";
	private static final String AUTH_SERVER_URI="http://localhost:8080/oauth/token";
	private static final String B_PASSWORD_GRANT="?grant_type=password&username=admin&password=admin123";
	private static final String B_ACCESS_TOKEN="?access_token=";
	private static String ACCESS_TOKEN="";


	/**
	 * Http Headers
	 * @return headers
	 */
	private static HttpHeaders getHeaders(){
		HttpHeaders headers=new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		return headers;
	}

	/**
	 *
	 * @return headers with client credentials
	 */
	private static HttpHeaders getHeadersWithClientCredentials(){
		String plainClientCredentials="my_trusted_client:secret";
		String base64ClientCredentials=new String(Base64.encodeBase64(plainClientCredentials.getBytes()));

		System.out.println("\n\n\nUser's credentials : "+base64ClientCredentials+"\n\n\n");
		HttpHeaders headers=new HttpHeaders();
		headers.add("Authorization","Basic "+base64ClientCredentials);
		return headers;
	}


	@SuppressWarnings("unchecked")
	private static AuthTokenInfo sendTokenRequest(){
		RestTemplate restTemplate=new RestTemplate();

		HttpEntity<String> request=new HttpEntity<>(getHeadersWithClientCredentials());
		ResponseEntity<Object> responseEntity=restTemplate.exchange(AUTH_SERVER_URI+B_PASSWORD_GRANT,HttpMethod.POST,request,Object.class);
		LinkedHashMap<String,Object> map=(LinkedHashMap<String,Object>)responseEntity.getBody();

		AuthTokenInfo authTokenInfo=null;
		if(map!=null){
			authTokenInfo=new AuthTokenInfo();
			authTokenInfo.setAccess_token((String)map.get("access_token"));
			authTokenInfo.setRefresh_token((String)map.get("refresh_token"));
			authTokenInfo.setToken_type((String)map.get("token_type"));
			authTokenInfo.setScope((String)map.get("scope"));
			authTokenInfo.setExpires_in(Integer.parseInt((String)map.get("expires_in")));

			System.out.print("\n\n\n"+authTokenInfo+"\n\n\n");

			ACCESS_TOKEN=authTokenInfo.getAccess_token();
		}
		else{
			System.out.print("\n\n\nUser does not exists\n\n\n");
		}
		return authTokenInfo;
	}



	@Scheduled(fixedDelay = 2000)
	public void refreshToken(){
		sendTokenRequest().getAccess_token();
	}


	@SuppressWarnings({"unchecked","rawTypes"})
	private static void listAllUsers(AuthTokenInfo authTokenInfo){
		Assert.notNull(authTokenInfo,"Authenticate first please .....");
		 System.out.println("\n Testing listAll users ...");
		 RestTemplate template=new RestTemplate();

		 HttpEntity<String> request=new HttpEntity<>(getHeaders());

		 ResponseEntity<List> responseEntity=template.exchange(REST_SERVICE_URI+"/users"+B_ACCESS_TOKEN+ACCESS_TOKEN,
				 HttpMethod.GET,request,List.class);

		 List<LinkedHashMap<String,Object>> userMap=(List<LinkedHashMap<String,Object>>) responseEntity.getBody();

		 if(userMap!=null){
		 	userMap.forEach(map->{System.out.println("User : Id = "+map.get("id")+", Name : name = "+map.get("name")
			+", Age : age = "+map.get("age")+", Salary : salary = "+map.get("salary"));});

		 }
		 else{
			 System.out.print("\n\n\nUser does not exists\n\n\n");
		 }
	}

	private static void getUserById(AuthTokenInfo authTokenInfo) {
		Assert.notNull(authTokenInfo, "Authenticate first please .....");
		System.out.println("\n Testing  Get a user ...");
		RestTemplate template = new RestTemplate();

		HttpEntity<String> request = new HttpEntity<>(getHeaders());

		ResponseEntity<User> responseEntity = template.exchange(REST_SERVICE_URI + "/user/5" + B_ACCESS_TOKEN + ACCESS_TOKEN,
				HttpMethod.GET, request, User.class);

		User user=responseEntity.getBody();

		System.out.println(user);
	}

	private static void createUser(AuthTokenInfo authTokenInfo) {
		Assert.notNull(authTokenInfo, "Authenticate first please .....");
		System.out.println("\n Testing create a user ...");
		RestTemplate template = new RestTemplate();

		User user=new User("Red",547,789.0);

		HttpEntity<Object> request = new HttpEntity<>(user,getHeaders());

		ResponseEntity<User> responseEntity = template.exchange(REST_SERVICE_URI + "/user/" + B_ACCESS_TOKEN + ACCESS_TOKEN,
				HttpMethod.POST, request, User.class);


		System.out.println(responseEntity.getBody());
	}

	private static void updateUser(AuthTokenInfo authTokenInfo) {
		Assert.notNull(authTokenInfo, "Authenticate first please .....");
		System.out.println("\n Testing update a user");
		RestTemplate template = new RestTemplate();

		User user=new User("Redis",547,789.0);

		HttpEntity<Object> request = new HttpEntity<>(user,getHeaders());

		ResponseEntity<User> responseEntity = template.exchange(REST_SERVICE_URI + "/user/4" + B_ACCESS_TOKEN + ACCESS_TOKEN,
				HttpMethod.PUT, request, User.class);


		System.out.println(responseEntity.getBody());
	}

	private static void deleteUser(AuthTokenInfo authTokenInfo) {
		Assert.notNull(authTokenInfo, "Authenticate first please .....");
		System.out.println("\n Testing delete users ...");
		RestTemplate template = new RestTemplate();

		HttpEntity<String> request = new HttpEntity<>(getHeaders());

		template.exchange(REST_SERVICE_URI + "/user/5" + B_ACCESS_TOKEN + ACCESS_TOKEN,
				HttpMethod.DELETE, request, User.class);



	}

	private static void deleteAllUsers(AuthTokenInfo authTokenInfo) {
		Assert.notNull(authTokenInfo, "Authenticate first please .....");
		System.out.println("\n Testing delete all users ...");
		RestTemplate template = new RestTemplate();

		HttpEntity<String> request = new HttpEntity<>(getHeaders());

		ResponseEntity<User> responseEntity = template.exchange(REST_SERVICE_URI + "/user/" + B_ACCESS_TOKEN + ACCESS_TOKEN,
				HttpMethod.DELETE, request, User.class);

	}



	public static void main() {

		AuthTokenInfo authTokenInfo=sendTokenRequest();
		listAllUsers(authTokenInfo);

		getUserById(authTokenInfo);

		createUser(authTokenInfo);
		listAllUsers(authTokenInfo);

		updateUser(authTokenInfo);
		listAllUsers(authTokenInfo);

		deleteUser(authTokenInfo);
		listAllUsers(authTokenInfo);

//		deleteAllUsers(authTokenInfo);
//		listAllUsers(authTokenInfo);
	}
}
