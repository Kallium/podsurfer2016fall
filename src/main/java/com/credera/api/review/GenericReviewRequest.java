package com.credera.api.review;

public class GenericReviewRequest {
	String podcast;
	String name;
	Integer episode;
	Integer rating;
	String review;
	Boolean spoilers;
	
	public GenericReviewRequest() {
		
	}

	public GenericReviewRequest(String podcast, String name, Integer episode,
			Integer rating, String review, Boolean spoilers) {
		this.podcast = podcast;
		this.name = name;
		this.episode = episode;
		this.rating = rating;
		this.review = review;
		this.spoilers = spoilers;
	}

	public String getPodcast() {
		return podcast;
	}

	public void setPodcast(String podcast) {
		this.podcast = podcast;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getEpisode() {
		return episode;
	}

	public void setEpisode(Integer episode) {
		this.episode = episode;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public Boolean getSpoilers() {
		return spoilers;
	}

	public void setSpoilers(Boolean spoilers) {
		this.spoilers = spoilers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((episode == null) ? 0 : episode.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((podcast == null) ? 0 : podcast.hashCode());
		result = prime * result + ((rating == null) ? 0 : rating.hashCode());
		result = prime * result + ((review == null) ? 0 : review.hashCode());
		result = prime * result
				+ ((spoilers == null) ? 0 : spoilers.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof GenericReviewRequest)) {
			return false;
		}
		GenericReviewRequest other = (GenericReviewRequest) obj;
		if (episode == null) {
			if (other.episode != null) {
				return false;
			}
		} else if (!episode.equals(other.episode)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (podcast == null) {
			if (other.podcast != null) {
				return false;
			}
		} else if (!podcast.equals(other.podcast)) {
			return false;
		}
		if (rating == null) {
			if (other.rating != null) {
				return false;
			}
		} else if (!rating.equals(other.rating)) {
			return false;
		}
		if (review == null) {
			if (other.review != null) {
				return false;
			}
		} else if (!review.equals(other.review)) {
			return false;
		}
		if (spoilers == null) {
			if (other.spoilers != null) {
				return false;
			}
		} else if (!spoilers.equals(other.spoilers)) {
			return false;
		}
		return true;
	}
}
