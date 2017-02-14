package com.credera;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.credera.api.PodSurferApi;
import com.credera.api.auth.LoginRequest;
import com.credera.api.auth.LoginResponse;
import com.credera.api.podcast.Episode;
import com.credera.api.podcast.GenericPodcastRequest;
import com.credera.api.podcast.GenericPodcastResponse;
import com.credera.api.review.GenericReviewRequest;
import com.credera.api.review.GenericReviewResponse;
import com.credera.api.review.PodcastAndReviewAverageResponse;
import com.credera.api.tag.TagAndCountResponse;
import com.credera.api.user.CreateUserRequest;
import com.credera.api.user.CreateUserResponse;
import com.credera.api.user.GenericUserResponse;
import com.credera.api.user.UpdateProfileInfoRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PodSurferApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PodSurferApplicationTests {
	@Autowired
	private PodSurferApi api;
	
	private static HttpHeaders headers = new HttpHeaders();
	private static GenericUserResponse newUser;
	private static String newUserPassword = "JUnitTe$t$!";
	private static GenericPodcastResponse newPodcast;
	private static GenericReviewResponse newReview;
	
	@Before
	public void setup() {
		
	}
	
	/**************************************************************************
	 * 
	 * User Sans Update
	 * 
	 **************************************************************************/
	
	@Test
	public void test01createNewUser() {
		Random random = new Random();
		CreateUserRequest request = new CreateUserRequest("Unit Test", random.nextInt() + "@j" + random.nextInt() + ".unit", newUserPassword);
		newUser = new GenericUserResponse(null, request.getName(), request.getEmail(), new String[]{}, new String[]{});
		System.out.println("Unit Test Email: " + request.getEmail());
		
		ResponseEntity<CreateUserResponse> response = api.createNewUser(request);
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + response.getBody().getToken());
	}
	
	@Test
	public void test02getMyProfileInfo() {
		ResponseEntity<GenericUserResponse> response = api.getMyProfileInfo(headers);
		GenericUserResponse preGetUser = newUser;
		newUser = response.getBody();
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		assertNotNull("_id", newUser.get_id());
		assertEquals("Name", preGetUser.getName(), newUser.getName());
		assertEquals("Email", preGetUser.getEmail(), newUser.getEmail());
		assertArrayEquals("Interests", preGetUser.getInterests(), newUser.getInterests());
		assertArrayEquals("Bookmarks", preGetUser.getBookmarks(), newUser.getBookmarks());
	}
	
	/*************************************************************************
	 * 
	 * Auth
	 * 
	 **************************************************************************/
	
	@Test
	public void test03login() {
		LoginRequest request = new LoginRequest(newUser.getEmail(), newUserPassword);
		ResponseEntity<LoginResponse> response	= api.login(request);
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		assertNull("Message", response.getBody().getMessage());
		
		headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + response.getBody().getToken());
	}
	
	/**************************************************************************
	 * 
	 * Podcast Sans Update and Delete
	 * 
	 **************************************************************************/
	
	protected void assertPodcastRequestEqualsResponse(GenericPodcastRequest request, GenericPodcastResponse response) {
		assertEquals("Name", request.getName(), response.getName());
		assertEquals("Link", request.getLink(), response.getLink());
		assertEquals("Release", request.getRelease(), response.getRelease());
		assertEquals("Producer", request.getProducer(), response.getProducer());
		assertEquals("Length", request.getLength(), response.getLength());
		assertEquals("Description", request.getDescription(), response.getDescription());
		assertArrayEquals("Episodes", request.getEpisodes(), response.getEpisodes());
		assertArrayEquals("Tags", request.getTags(), response.getTags());
		assertEquals("Image URL", request.getImageURL(), response.getImageURL());
	}
	
	@Test
	public void test04createNewPodcast() {
		Episode[] episodes = {
				new Episode(4, "JUnit 4", "http://junit.org/junit4/", "The fourth version of JUnit", "1:23:59"),
				new Episode(5, "JUnit 5", "http://junit.org/junit5/", "The fifth version of JUnit", "7:25"),
				new Episode(2, "JUnit 2", "http://junit.org/junit2/", "The second version of JUnit", "34:55")
		};
		String[] tags = {
				"test",
				"java",
				"unit",
				"junit",
		};
		GenericPodcastRequest request = new GenericPodcastRequest("JUnit Tunes", "http://junit.org/", "2014-12-04T00:00:00.000Z", "Kent Beck", "5", "The best Java unit test suite", episodes, tags, "http://junit.org/junit4/images/junit-logo.png");
		
		ResponseEntity<GenericPodcastResponse> response = api.createNewPodcast(request, headers);
		newPodcast = response.getBody();
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		assertNotNull("_id", newPodcast.get_id());
		assertPodcastRequestEqualsResponse(request, newPodcast);
	}
	

	@Test
	public void test05getAllPodcasts() {
		ResponseEntity<GenericPodcastResponse[]> response = api.getAllPodcasts(null);
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		assertTrue("Response must have at least one podcast", response.getBody().length >= 1);
		boolean foundNewPodcast = false;
		for (int i = 0;
				!foundNewPodcast && i < response.getBody().length;
				i++) {
			foundNewPodcast = response.getBody()[i].equals(newPodcast);
		}
		assertTrue("Response must have new podcast", foundNewPodcast);
	}
	
	@Test
	public void test06getOnePodcast() {
		ResponseEntity<GenericPodcastResponse> response = api.getOnePodcast(newPodcast.get_id());
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		assertEquals("Podcast", newPodcast, response.getBody());
	}
	
	/**************************************************************************
	 * 
	 * Tag
	 * 
	 **************************************************************************/
	
	@Test
	public void test07getAllTags() {
		ResponseEntity<TagAndCountResponse[]> response = api.getAllTags(null);
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		assertTrue("Response must have at least the same number of tags as new podcast", response.getBody().length >= newPodcast.getTags().length);
		
		Map<String, Boolean> foundTags = new HashMap<String, Boolean>(newPodcast.getTags().length);
		for (String tag : newPodcast.getTags()) {
			foundTags.put(tag, false);
		}
		for (TagAndCountResponse tagAndCount : response.getBody()) {
			if (foundTags.containsKey(tagAndCount.getName())) {
				assertFalse("Each response tag should only appear once", foundTags.get(tagAndCount.getName()));
				foundTags.remove(tagAndCount.getName());
				foundTags.put(tagAndCount.getName(), true);
			}
		}
		for (String name : foundTags.keySet()) {
			assertTrue("Response must have all tags in new podcast", foundTags.get(name));
		}
	}
	
	@Test
	public void test08getAllTagsWithMax() {
		ResponseEntity<TagAndCountResponse[]> response = api.getAllTags(newPodcast.getTags().length - 1);
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		assertTrue("Response must have one fewer tags than new podcast", response.getBody().length == newPodcast.getTags().length - 1);
		
		Map<String, Boolean> foundTags = new HashMap<String, Boolean>(response.getBody().length);
		for (TagAndCountResponse tagAndCount : response.getBody()) {
			foundTags.put(tagAndCount.getName(), false);
		}
		for (TagAndCountResponse tagAndCount : response.getBody()) {
			assertFalse("Each response tag should only appear once", foundTags.get(tagAndCount.getName()));
			foundTags.remove(tagAndCount.getName());
			foundTags.put(tagAndCount.getName(), true);
		}
		for (String name : foundTags.keySet()) {
			assertTrue("Response must have all tags in response", foundTags.get(name));
		}
	}
	
	/**************************************************************************
	 * 
	 * Podcast Update
	 * 
	 **************************************************************************/
	
	@Test
	public void test09updateOnePodcast() {
		Episode[] episodes = {
				new Episode(1, "v1.0.0", "http://podsurfer-6.herokuapp.com/#api-Podcast-update", "Update a podcast", null),
		};
		String[] tags = {
				"api",
				"podsurfer",
		};
		GenericPodcastRequest request = new GenericPodcastRequest("API Blues", "http://podsurfer-6.herokuapp.com/", "2016-08-08T00:00:00.000Z", "Kelleigh Maroney", "1", "The PodSurfer API", episodes, tags, null);
		
		ResponseEntity<GenericPodcastResponse> response = api.updateOnePodcast(request, newPodcast.get_id(), headers);
		GenericPodcastResponse preUpdatePodcast = newPodcast;
		newPodcast = response.getBody();
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		assertNotNull("_id", newPodcast.get_id());
		assertEquals("_id", preUpdatePodcast.get_id(), newPodcast.get_id());
		assertPodcastRequestEqualsResponse(request, newPodcast);
		
		test05getAllPodcasts();
		test06getOnePodcast();
		test07getAllTags();
		test08getAllTagsWithMax();
	}
	
	/**************************************************************************
	 * 
	 * User Update
	 * 
	 **************************************************************************/
	
	@Test
	public void test10updateMyProfileInfo() {
		String[] interests = {
				"java",
				"unit",
				"test",
		};
		String[] bookmarks = {
				newPodcast.get_id(),
		};
		UpdateProfileInfoRequest request = new UpdateProfileInfoRequest("JUnit", interests, bookmarks);
		
		ResponseEntity<GenericUserResponse> response = api.updateMyProfileInfo(request, headers);
		GenericUserResponse preUpdateUser = newUser;
		newUser = response.getBody();
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		assertNotNull("_id", newUser.get_id());
		assertEquals("_id", preUpdateUser.get_id(), newUser.get_id());
		assertEquals("Name", request.getName(), newUser.getName());
		assertEquals("Email", preUpdateUser.getEmail(), newUser.getEmail());
		assertArrayEquals("Interests", request.getInterests(), newUser.getInterests());
		assertArrayEquals("Bookmarks", request.getBookmarks(), newUser.getBookmarks());
	}
	
	/**************************************************************************
	 * 
	 * Review Sans Delete
	 * 
	 **************************************************************************/
	
	protected void assertReviewRequestEqualsResponse(GenericReviewRequest request, GenericReviewResponse response) {
		assertEquals("Name", request.getName(), response.getName());
		assertEquals("Podcast", request.getPodcast(), response.getPodcast());
		assertEquals("Episode", request.getEpisode(), response.getEpisode());
		assertEquals("Rating", request.getRating(), response.getRating());
		assertEquals("Review", request.getReview(), response.getReview());
		assertEquals("Spoilers", request.getSpoilers(), response.getSpoilers());
	}
	
	@Test
	public void test11createNewReview() {
		GenericReviewRequest request = new GenericReviewRequest(newPodcast.get_id(), "It's OK, I guess", newPodcast.getEpisodes()[0].getNumber(), 3, "It lacks polish.", true);
		
		ResponseEntity<GenericReviewResponse> response = api.createNewReview(request, headers);
		newReview = response.getBody();
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		assertNotNull("_id", newReview.get_id());
		assertReviewRequestEqualsResponse(request, newReview);
		assertEquals("Reviewer ID", newUser.get_id(), newReview.getReviewer().getId());
		assertEquals("Reviewer Name", newUser.getName(), newReview.getReviewer().getName());
	}
	
	@Test
	public void test12getAllReviews() {
		ResponseEntity<GenericReviewResponse[]> response = api.getAllReviews(null);
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		assertTrue("Response must have at least one review", response.getBody().length >= 1);
		boolean foundNewReview = false;
		for (int i = 0;
				!foundNewReview && i < response.getBody().length;
				i++) {
			foundNewReview = response.getBody()[i].equals(newReview);
		}
		assertTrue("Response must have new review", foundNewReview);
	}
	
	@Test
	public void test13getAllReviewsWithMax() {
		ResponseEntity<GenericReviewResponse[]> response = api.getAllReviews(1);
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		assertTrue("Response must have exactly one review", response.getBody().length == 1);
	}
	
	@Test
	public void test14getAllMyReviews() {
		ResponseEntity<GenericReviewResponse[]> response = api.getAllMyReviews(headers);
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		assertTrue("Response must have at least one review", response.getBody().length >= 1);
		boolean foundNewReview = false;
		for (int i = 0;
				!foundNewReview && i < response.getBody().length;
				i++) {
			foundNewReview = response.getBody()[i].equals(newReview);
		}
		assertTrue("Response must have new review", foundNewReview);
	}
	
	@Test
	public void test15getAllPodcastReviews() {
		ResponseEntity<GenericReviewResponse[]> response = api.getAllPodcastReviews(newPodcast.get_id());
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		assertTrue("Response must have at least one review", response.getBody().length >= 1);
		boolean foundNewReview = false;
		for (int i = 0;
				!foundNewReview && i < response.getBody().length;
				i++) {
			foundNewReview = response.getBody()[i].equals(newReview);
		}
		assertTrue("Response must have new review", foundNewReview);
	}
	
	@Test
	public void test16getAllPodcastsReviewsAverages() {
		ResponseEntity<PodcastAndReviewAverageResponse[]> response = api.getAllPodcastsReviewsAverages(null);
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		assertTrue("Response must have at least one podcast and average", response.getBody().length >= 1);
		boolean foundNewPodcast = false;
		for (int i = 0;
				!foundNewPodcast && i < response.getBody().length;
				i++) {
			foundNewPodcast = response.getBody()[i].getId().equals(newPodcast.get_id());
		}
		assertTrue("Response must have new podcast", foundNewPodcast);
	}
	
	@Test
	public void test17getAllPodcastsReviewsAveragesWithMax() {
		ResponseEntity<PodcastAndReviewAverageResponse[]> response = api.getAllPodcastsReviewsAverages(1);
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		assertTrue("Response must have exactly one podcast and average", response.getBody().length == 1);
	}
	
	@Test
	public void test18getOnePodcastReviewsAverage() {
		ResponseEntity<PodcastAndReviewAverageResponse> response = api.getOnePodcastReviewsAverage(newPodcast.get_id());
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		assertEquals("_id", newPodcast.get_id(), response.getBody().getId());
		assertTrue("Average must be equal to the rating of the review", Math.abs(newReview.getRating() - response.getBody().getAverage()) < 1e-4);
	}
	
	@Test
	public void test19updateOneReview() {
		GenericReviewRequest request = new GenericReviewRequest(newPodcast.get_id(), "It's much improved", null, 5, "It lacked polish. Now it shines.", false);
		
		ResponseEntity<GenericReviewResponse> response = api.updateOneReview(request, newReview.get_id(), headers);
		GenericReviewResponse preUpdateReview = newReview;
		newReview = response.getBody();
		
		assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		assertNotNull("_id", newReview.get_id());
		assertEquals("_id", preUpdateReview.get_id(), newReview.get_id());
		assertReviewRequestEqualsResponse(request, newReview);
		
		test12getAllReviews();
		test13getAllReviewsWithMax();
		test14getAllMyReviews();
		test15getAllPodcastReviews();
		test16getAllPodcastsReviewsAverages();
		test17getAllPodcastsReviewsAveragesWithMax();
		test18getOnePodcastReviewsAverage();
	}
	
	/**************************************************************************
	 * 
	 * Delete
	 * 
	 **************************************************************************/
	
	@Test
	public void test20deleteExistingReview() {
		{
			ResponseEntity<String> response = api.deleteExistingReview(newReview.get_id(), headers);
			
			assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		}
		
		// Opposite of test10getAllMyReviews
		{
			ResponseEntity<GenericReviewResponse[]> response = api.getAllMyReviews(headers);
			
			assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
			boolean foundNewReview = false;
			for (int i = 0;
					!foundNewReview && i < response.getBody().length;
					i++) {
				foundNewReview = response.getBody()[i].equals(newReview);
			}
			assertFalse("Response must not have new review", foundNewReview);
		}
		
		// Opposite of test11getAllPodcastReviews
		{
			ResponseEntity<GenericReviewResponse[]> response = api.getAllPodcastReviews(newPodcast.get_id());
			
			assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
			boolean foundNewReview = false;
			for (int i = 0;
					!foundNewReview && i < response.getBody().length;
					i++) {
				foundNewReview = response.getBody()[i].equals(newReview);
			}
			assertFalse("Response must not have new review", foundNewReview);
		}
	}
	
	@Test
	public void test21deleteExistingPodcast() {
		{
			ResponseEntity<String> response = api.deleteExistingPodcast(newPodcast.get_id(), headers);
			
			assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
		}
		
		// Opposite of test05getAllPodcasts
		{
			ResponseEntity<GenericPodcastResponse[]> response = api.getAllPodcasts(null);
			
			assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
			boolean foundNewPodcast = false;
			for (int i = 0;
					!foundNewPodcast && i < response.getBody().length;
					i++) {
				foundNewPodcast = response.getBody()[i].equals(newPodcast);
			}
			assertFalse("Response must not have new podcast", foundNewPodcast);
		}
		
		// Opposite of test06getOnePodcast
		{
			ResponseEntity<GenericPodcastResponse> response = api.getOnePodcast(newPodcast.get_id());
			
			assertTrue("Response code must be successfull", response.getStatusCode().is2xxSuccessful());
			assertNull("Podcast", response.getBody());
		}
	}
}
