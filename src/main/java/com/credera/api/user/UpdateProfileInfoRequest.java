package com.credera.api.user;

import java.util.Arrays;

public class UpdateProfileInfoRequest {
	String name;
	String[] interests;
	String[] bookmarks;
	
	public UpdateProfileInfoRequest() {

	}

	public UpdateProfileInfoRequest(String name, String[] interests,
			String[] bookmarks) {
		super();
		this.name = name;
		this.interests = interests;
		this.bookmarks = bookmarks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getInterests() {
		return interests;
	}

	public void setInterests(String[] interests) {
		this.interests = interests;
	}

	public String[] getBookmarks() {
		return bookmarks;
	}

	public void setBookmarks(String[] bookmarks) {
		this.bookmarks = bookmarks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bookmarks);
		result = prime * result + Arrays.hashCode(interests);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (!(obj instanceof UpdateProfileInfoRequest)) {
			return false;
		}
		UpdateProfileInfoRequest other = (UpdateProfileInfoRequest) obj;
		if (!Arrays.equals(bookmarks, other.bookmarks)) {
			return false;
		}
		if (!Arrays.equals(interests, other.interests)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
}
