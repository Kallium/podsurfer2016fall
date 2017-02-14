package com.credera.api.user;

import java.util.Arrays;

public class GenericUserResponse {
	String _id;
	String name;
	String email;
	String[] interests;
	String[] bookmarks;
	
	public GenericUserResponse() {
		
	}
	
	public GenericUserResponse(String _id, String name, String email,
			String[] interests, String[] bookmarks) {
		this._id = _id;
		this.name = name;
		this.email = email;
		this.interests = interests;
		this.bookmarks = bookmarks;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + Arrays.hashCode(bookmarks);
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		if (!(obj instanceof GenericUserResponse)) {
			return false;
		}
		GenericUserResponse other = (GenericUserResponse) obj;
		if (_id == null) {
			if (other._id != null) {
				return false;
			}
		} else if (!_id.equals(other._id)) {
			return false;
		}
		if (!Arrays.equals(bookmarks, other.bookmarks)) {
			return false;
		}
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
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
