package com.credera.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;

import com.credera.api.auth.LoginRequest;
import com.credera.api.auth.LoginResponse;
import com.credera.api.podcast.GenericPodcastRequest;
import com.credera.api.podcast.GenericPodcastResponse;
import com.credera.api.recommendation.Recommendation;
import com.credera.api.review.GenericReviewRequest;
import com.credera.api.review.GenericReviewResponse;
import com.credera.api.review.PodcastAndReviewAverageResponse;
import com.credera.api.tag.TagAndCountResponse;
import com.credera.api.user.CreateUserRequest;
import com.credera.api.user.CreateUserResponse;
import com.credera.api.user.GenericUserResponse;
import com.credera.api.user.UpdateProfileInfoRequest;

@Controller
public class PodSurferApi {
	static SanitizingRestTemplate restTemplate;
	static URI apiRoot;
	
	public PodSurferApi() throws URISyntaxException {
		restTemplate = new SanitizingRestTemplate();
		
		// Forward 4xx and 5xx responses from the API server
		restTemplate.setErrorHandler(new ResponseErrorHandler() {
			public boolean hasError(ClientHttpResponse response) {
				return false;
			}

			public void handleError(ClientHttpResponse response) throws IOException {
				throw new HttpClientErrorException(response.getStatusCode(), response.getStatusText());
				
			}
		});
		
		// Prevent a connection from not closing properly
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setOutputStreaming(false);
		restTemplate.setRequestFactory(requestFactory);
		
		// Strip Content-Length header to prevent client-side confusion
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
		interceptors.add(new ClientHttpRequestInterceptor() {
			public ClientHttpResponse intercept(HttpRequest request, byte[] body,
					ClientHttpRequestExecution execution) throws IOException {
				ClientHttpResponse response = execution.execute(request, body);
				response.getHeaders().remove(HttpHeaders.CONTENT_LENGTH);
				
				return response;
			}
			
		});
		restTemplate.setInterceptors(interceptors);
		
		// Proxy error messages from the API server
		restTemplate.getMessageConverters().add(new ErrorMessageConverter());
		
		// Define API root URL
		apiRoot = new URI("https://podsurfer-api-6.herokuapp.com");
	}
	
	/*************************************************************************
	 * 
	 * Auth
	 * 
	 **************************************************************************/
	
	// Login with an existing user
	@PostMapping(value = "/auth/local")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		ResponseEntity<LoginResponse> response = restTemplate.postForEntity(apiRoot.resolve("/auth/local"), request, LoginResponse.class);
		
		return response;
	}
	
	/**************************************************************************
	 * 
	 * Podcast
	 * 
	 **************************************************************************/
	
	// Create a new podcast
	@PostMapping(value = "/api/podcast")
	public ResponseEntity<GenericPodcastResponse> createNewPodcast(@RequestBody GenericPodcastRequest request, @RequestHeader HttpHeaders headers) {
		HttpEntity<GenericPodcastRequest> entity = new HttpEntity<GenericPodcastRequest>(request, headers);
		ResponseEntity<GenericPodcastResponse> response = restTemplate.postForEntity(apiRoot.resolve("/api/podcast"), entity, GenericPodcastResponse.class);
		
		return response;
	}
	
	// Delete a podcast
	@DeleteMapping(value = "/api/podcast/{id}")
	public ResponseEntity<String> deleteExistingPodcast(@PathVariable("id") String id, @RequestHeader HttpHeaders headers) {
		HttpEntity<?> request = new HttpEntity<Object>(headers);
		ResponseEntity<String> response = restTemplate.exchange(apiRoot.resolve("/api/podcast/" + id), HttpMethod.DELETE, request, String.class);
		
		return response;
	}
	
	// Get all the podcasts, limited to 'max'
	@GetMapping(value = "/api/podcast")
	public ResponseEntity<GenericPodcastResponse[]> getAllPodcasts(@RequestParam(value = "max", required = false) Integer max) {
		ResponseEntity<GenericPodcastResponse[]> response = restTemplate.getForEntity(apiRoot.resolve(max == null ? "/api/podcast" : "/api/podcast?max=" + max), GenericPodcastResponse[].class);
		
		return response;
	}
	
	// Get one podcast
	@GetMapping(value = "/api/podcast/{id}")
	public ResponseEntity<GenericPodcastResponse> getOnePodcast(@PathVariable("id") String id) {
		ResponseEntity<GenericPodcastResponse> response = restTemplate.getForEntity(apiRoot.resolve("/api/podcast/" + id), GenericPodcastResponse.class);

		return response;
	}
	
	// Update a podcast
	@PutMapping(value = "/api/podcast/{id}")
	public ResponseEntity<GenericPodcastResponse> updateOnePodcast(@RequestBody GenericPodcastRequest request, @PathVariable("id") String id, @RequestHeader HttpHeaders headers) {
		HttpEntity<GenericPodcastRequest> entity = new HttpEntity<GenericPodcastRequest>(request, headers);
		ResponseEntity<GenericPodcastResponse> response = restTemplate.exchange(apiRoot.resolve("/api/podcast/" + id), HttpMethod.PUT, entity, GenericPodcastResponse.class);

		return response;
	}
	
	/**************************************************************************
	 * 
	 * Review
	 * 
	 **************************************************************************/
	
	// Create a new review
	@PostMapping(value = "/api/review")
	public ResponseEntity<GenericReviewResponse> createNewReview(@RequestBody GenericReviewRequest request, @RequestHeader HttpHeaders headers) {
		HttpEntity<GenericReviewRequest> entity = new HttpEntity<GenericReviewRequest>(request, headers);
		ResponseEntity<GenericReviewResponse> response = restTemplate.postForEntity(apiRoot.resolve("/api/review"), entity, GenericReviewResponse.class);

		return response;
	}
	
	// Delete a review
	@DeleteMapping(value = "/api/review/{id}")
	public ResponseEntity<String> deleteExistingReview(@PathVariable("id") String id, @RequestHeader HttpHeaders headers) {
		HttpEntity<?> request = new HttpEntity<Object>(headers);
		ResponseEntity<String> response = restTemplate.exchange(apiRoot.resolve("/api/review/" + id), HttpMethod.DELETE, request, String.class);
		
		return response;
	}
	
	// Get all the reviews, limited to 'max'
	@GetMapping(value = "/api/review")
	public ResponseEntity<GenericReviewResponse[]> getAllReviews(@RequestParam(value = "max", required = false) Integer max) {
		ResponseEntity<GenericReviewResponse[]> response = restTemplate.getForEntity(apiRoot.resolve(max == null ? "/api/review" : "/api/review?max=" + max), GenericReviewResponse[].class);
		
		return response;
	}
	
	// Get all the reviews I've submitted
	@GetMapping(value = "/api/review/mine")
	public ResponseEntity<GenericReviewResponse[]> getAllMyReviews(@RequestHeader HttpHeaders headers) {
		HttpEntity<?> request = new HttpEntity<Object>(headers);
		ResponseEntity<GenericReviewResponse[]> response = restTemplate.exchange(apiRoot.resolve("/api/review/mine"), HttpMethod.GET, request, GenericReviewResponse[].class);
		return response;
	}
	
	// Get all the reviews for a specific podcast
	@GetMapping(value = "/api/review/{id}")
	public ResponseEntity<GenericReviewResponse[]> getAllPodcastReviews(@PathVariable("id") String id) {
		ResponseEntity<GenericReviewResponse[]> response = restTemplate.getForEntity(apiRoot.resolve("/api/review/" + id), GenericReviewResponse[].class);
		
		return response;
	}
	
	// Get the average review score for all podcasts, limited to 'max'
	@GetMapping(value = "/api/review/all/average")
	public ResponseEntity<PodcastAndReviewAverageResponse[]> getAllPodcastsReviewsAverages(@RequestParam(value = "max", required = false) Integer max) {
		ResponseEntity<PodcastAndReviewAverageResponse[]> response = restTemplate.getForEntity(apiRoot.resolve(max == null ? "/api/review/all/average" : "/api/review/all/average?max=" + max), PodcastAndReviewAverageResponse[].class);
		
		return response;
	}
	
	// Get the average review score for a specific podcast
	@GetMapping(value = "/api/review/{id}/average")
	public ResponseEntity<PodcastAndReviewAverageResponse> getOnePodcastReviewsAverage(@PathVariable("id") String id) {
		ResponseEntity<PodcastAndReviewAverageResponse> response = restTemplate.getForEntity(apiRoot.resolve("/api/review/" + id + "/average"), PodcastAndReviewAverageResponse.class);
		
		return response;
	}
	
	// Update one of my reviews
	@PutMapping(value = "/api/review/{id}")
	public ResponseEntity<GenericReviewResponse> updateOneReview(@RequestBody GenericReviewRequest request, @PathVariable("id") String id, @RequestHeader HttpHeaders headers) {
		HttpEntity<GenericReviewRequest> entity = new HttpEntity<GenericReviewRequest>(request, headers);
		ResponseEntity<GenericReviewResponse> response = restTemplate.exchange(apiRoot.resolve("/api/review/" + id), HttpMethod.PUT, entity, GenericReviewResponse.class);

		return response;
	}
	
	/**************************************************************************
	 * 
	 * User
	 * 
	 **************************************************************************/
	
	// Create a new user
	@PostMapping(value = "/api/user")
	public ResponseEntity<CreateUserResponse> createNewUser(@RequestBody CreateUserRequest request) {
		ResponseEntity<CreateUserResponse> response = restTemplate.postForEntity(apiRoot.resolve("/api/user"), request, CreateUserResponse.class);

		return response;
	}
	
	// Get information about myself
	@GetMapping(value = "/api/user/me")
	public ResponseEntity<GenericUserResponse> getMyProfileInfo(@RequestHeader HttpHeaders headers) {
		HttpEntity<?> request = new HttpEntity<Object>(headers);
		ResponseEntity<GenericUserResponse> response = restTemplate.exchange(apiRoot.resolve("/api/user/me"), HttpMethod.GET, request, GenericUserResponse.class);

		return response;
	}
	
	// Update my user profile
	@PutMapping(value = "/api/user")
	public ResponseEntity<GenericUserResponse> updateMyProfileInfo(@RequestBody UpdateProfileInfoRequest request, @RequestHeader HttpHeaders headers) {
		HttpEntity<UpdateProfileInfoRequest> entity = new HttpEntity<UpdateProfileInfoRequest>(request, headers);
		ResponseEntity<GenericUserResponse> response = restTemplate.exchange(apiRoot.resolve("/api/user"), HttpMethod.PUT, entity, GenericUserResponse.class);

		return response;
	}
	
	/**************************************************************************
	 * 
	 * Tags
	 * 
	 **************************************************************************/
	
	// Get all tags and the number of times each appears, limited to 'max'
	@GetMapping(value = "/api/tag")
	public ResponseEntity<TagAndCountResponse[]> getAllTags(@RequestParam(value = "max", required = false) Integer max) {
		ResponseEntity<GenericPodcastResponse[]> response = getAllPodcasts(null);
		if (!response.getStatusCode().is2xxSuccessful()) {
			return new ResponseEntity<TagAndCountResponse[]>(response.getStatusCode());
		}
		
		Map<String, Integer> tags = new HashMap<String, Integer>();
		for (GenericPodcastResponse podcast : response.getBody()) {
			if (podcast.hasTags()) {
				for (String tag : podcast.getTags()) {
					if (tags.containsKey(tag)) {
						Integer count = tags.get(tag) + 1;
						tags.remove(tag);
						tags.put(tag, count);
					} else {
						tags.put(tag, 1);
					}
				}
			}
		}

		if (max != null) {
			Random random = new Random();
			String[] keySetStorage = new String[tags.size()];
			while (tags.size() > max) {
				String randomKey = tags.keySet().toArray(keySetStorage)[random.nextInt(tags.size())];
				tags.remove(randomKey);
			}
		}
		
		int i = 0;
		TagAndCountResponse[] body = new TagAndCountResponse[tags.size()];
		for (String name : tags.keySet()) {
			body[i] = new TagAndCountResponse(name, tags.get(name));
			i++;
		}

		return new ResponseEntity<TagAndCountResponse[]>(body, HttpStatus.OK);
	}
	
	// Get information about myself
	@GetMapping(value = "/api/recommendation")
	public ResponseEntity<Recommendation[]> getRecommendations(@RequestHeader HttpHeaders headers) {
		
		ArrayList<Recommendation> recs = new ArrayList<Recommendation>();
		Map<String, Integer> tagPool = new HashMap<String, Integer>();
		Map<String, GenericPodcastResponse> allPodcasts = new HashMap<String, GenericPodcastResponse>();
		
		//get account info
		ResponseEntity<GenericUserResponse> user = getMyProfileInfo(headers);
		//get all podcasts for quick lookup
		GenericPodcastResponse[] podcastArray = getAllPodcasts(null).getBody();
		//get all reviewed podcasts
		GenericReviewResponse[] myReviews = getAllMyReviews(headers).getBody();
		
		//store bookmarks and interests in their own arrays
		String[] bookmarks = user.getBody().getBookmarks();
		String[] interests = user.getBody().getInterests();
		
		for(GenericPodcastResponse p : podcastArray) {
			allPodcasts.put(p.get_id(), p);
		}
		
		//add tags from bookmarks
		for(String s : bookmarks) {
			GenericPodcastResponse p = allPodcasts.get(s);
			for(String tag : p.getTags()) {
				tag = tag.toLowerCase();
				Double numToAdd = getOnePodcastReviewsAverage(p.get_id()).getBody().getAverage();
				if(numToAdd == null) 
				{
					numToAdd = 1.0;
				}
				if(tagPool.containsKey(tag)) {
					tagPool.put(tag, tagPool.get(tag) + numToAdd.intValue());
				}
				else {
					tagPool.put(tag, numToAdd.intValue());
				}
			}
		}
		
		//add tags to tag pool from interests
		for(int i = 0; i < interests.length; i++) {
			Scanner tagFinder = new Scanner(interests[i]);
			while(tagFinder.hasNext()) {
				String tag = tagFinder.next().toLowerCase();
				if(tagPool.containsKey(tag)) {
					tagPool.put(tag, tagPool.get(tag) + 1);
				}
				else {
					tagPool.put(tag, 1);
				}
			}
			tagFinder.close();
		}
		
		//add tags to tag pool from reviewed podcasts
		for(int i = 0; i < myReviews.length; i++) {
			if(myReviews[i].getRating() > 4) {
				GenericPodcastResponse p = allPodcasts.get(myReviews[i].getPodcast());
				for(String tag : p.getTags()) {
					tag = tag.toLowerCase();
					if(tagPool.containsKey(tag)) {
						tagPool.put(tag, tagPool.get(tag) + myReviews[i].getRating());
					}
					else {
						tagPool.put(tag, myReviews[i].getRating());
					}
				}
			}
		}
		
		//set score for each podcast
		for(GenericPodcastResponse p : podcastArray) {
			double score = 0;
			if(p.getTags() != null) {
				for(String tag : p.getTags()) {
					tag = tag.toLowerCase();
					if(tagPool.containsKey(tag)) {
						score += tagPool.get(tag);
					}
				}
			}
			
			boolean isBookmark = false;
			String myId = p.get_id();
			for(String s : bookmarks) {
				if(myId.equals(s)) {
					isBookmark = true;
				}
			}
			if(!isBookmark) {
				recs.add(new Recommendation(p, score));
			}
		}
		
		//sort array
		Collections.sort(recs, new Comparator<Recommendation>() {
			public int compare(Recommendation arg0, Recommendation arg1) {
				return (int)(arg1.getScore() - arg0.getScore());
			}
		});
		
		return new ResponseEntity<Recommendation[]>(recs.toArray(new Recommendation[recs.size()]), HttpStatus.OK);
	}
}
