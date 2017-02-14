package com.credera.api.recommendation;

import com.credera.api.podcast.*;

public class Recommendation {
	
	private GenericPodcastResponse podcast;
	private double score;
	
	public GenericPodcastResponse getPodcast() {
		return podcast;
	}

	public void setPodcast(GenericPodcastResponse podcast) {
		this.podcast = podcast;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Recommendation() {
		
	}
	
	public Recommendation(GenericPodcastResponse p, double s) {
		podcast = p;
		score = s;
	}
	
	

}
