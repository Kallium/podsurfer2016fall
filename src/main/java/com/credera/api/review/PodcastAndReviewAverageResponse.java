package com.credera.api.review;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PodcastAndReviewAverageResponse {
	String id;
	Double average;
	
	public PodcastAndReviewAverageResponse() {
		
	}

	public PodcastAndReviewAverageResponse(String id, Double average) {
		this.id = id;
		this.average = average;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getAverage() {
		return average;
	}

	public void setAverage(Double average) {
		this.average = average;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((average == null) ? 0 : average.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (!(obj instanceof PodcastAndReviewAverageResponse)) {
			return false;
		}
		PodcastAndReviewAverageResponse other = (PodcastAndReviewAverageResponse) obj;
		if (average == null) {
			if (other.average != null) {
				return false;
			}
		} else if (!average.equals(other.average)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
}
