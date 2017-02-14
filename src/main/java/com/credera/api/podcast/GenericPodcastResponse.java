package com.credera.api.podcast;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericPodcastResponse {
	String _id;
	String name;
	String link;
	String release;
	String producer;
	String length;
	String description;
	Episode[] episodes;
	String[] tags;
	String imageURL;

	public GenericPodcastResponse() {
		
	}
	
	public GenericPodcastResponse(String _id, String name, String link,
			String release, String producer, String length, String description,
			Episode[] episodes, String[] tags, String imageURL) {
		this._id = _id;
		this.name = name;
		this.link = link;
		this.release = release;
		this.producer = producer;
		this.length = length;
		this.description = description;
		this.episodes = episodes;
		this.tags = tags;
		this.imageURL = imageURL;
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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getRelease() {
		return release;
	}

	public void setRelease(String release) {
		this.release = release;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean hasEpisodes() {
		return episodes != null;
	}
	
	public Episode[] getEpisodes() {
		return episodes;
	}

	public void setEpisodes(Episode[] episodes) {
		this.episodes = episodes;
	}

	public boolean hasTags() {
		return tags != null;
	}
	
	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + Arrays.hashCode(episodes);
		result = prime * result
				+ ((imageURL == null) ? 0 : imageURL.hashCode());
		result = prime * result + ((length == null) ? 0 : length.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((producer == null) ? 0 : producer.hashCode());
		result = prime * result + ((release == null) ? 0 : release.hashCode());
		result = prime * result + Arrays.hashCode(tags);
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
		if (!(obj instanceof GenericPodcastResponse)) {
			return false;
		}
		GenericPodcastResponse other = (GenericPodcastResponse) obj;
		if (_id == null) {
			if (other._id != null) {
				return false;
			}
		} else if (!_id.equals(other._id)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (!Arrays.equals(episodes, other.episodes)) {
			return false;
		}
		if (imageURL == null) {
			if (other.imageURL != null) {
				return false;
			}
		} else if (!imageURL.equals(other.imageURL)) {
			return false;
		}
		if (length == null) {
			if (other.length != null) {
				return false;
			}
		} else if (!length.equals(other.length)) {
			return false;
		}
		if (link == null) {
			if (other.link != null) {
				return false;
			}
		} else if (!link.equals(other.link)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (producer == null) {
			if (other.producer != null) {
				return false;
			}
		} else if (!producer.equals(other.producer)) {
			return false;
		}
		if (release == null) {
			if (other.release != null) {
				return false;
			}
		} else if (!release.equals(other.release)) {
			return false;
		}
		if (!Arrays.equals(tags, other.tags)) {
			return false;
		}
		return true;
	}
}
