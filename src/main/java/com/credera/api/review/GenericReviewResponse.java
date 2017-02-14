package com.credera.api.review;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericReviewResponse {
	String _id;
	String name;
	String podcast;
	Integer episode;
	Integer rating;
	String review;
	Boolean spoilers;
	Reviewer reviewer;
	
	public GenericReviewResponse() {
		
	}

	public GenericReviewResponse(String _id, String name, String podcast,
			Integer episode, Integer rating, String review, Boolean spoilers,
			Reviewer reviewer) {
		super();
		this._id = _id;
		this.name = name;
		this.podcast = podcast;
		this.episode = episode;
		this.rating = rating;
		this.review = review;
		this.spoilers = spoilers;
		this.reviewer = reviewer;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPodcast() {
		return podcast;
	}

	public void setPodcast(String podcast) {
		this.podcast = podcast;
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

	public Reviewer getReviewer() {
		return reviewer;
	}

	public void setReviewer(Reviewer reviewer) {
		this.reviewer = reviewer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + ((episode == null) ? 0 : episode.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((podcast == null) ? 0 : podcast.hashCode());
		result = prime * result + ((rating == null) ? 0 : rating.hashCode());
		result = prime * result + ((review == null) ? 0 : review.hashCode());
		result = prime * result
				+ ((reviewer == null) ? 0 : reviewer.hashCode());
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
		if (!(obj instanceof GenericReviewResponse)) {
			return false;
		}
		GenericReviewResponse other = (GenericReviewResponse) obj;
		if (_id == null) {
			if (other._id != null) {
				return false;
			}
		} else if (!_id.equals(other._id)) {
			return false;
		}
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
		if (reviewer == null) {
			if (other.reviewer != null) {
				return false;
			}
		} else if (!reviewer.equals(other.reviewer)) {
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
