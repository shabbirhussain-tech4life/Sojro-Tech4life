package com.mdcbeta.data.remote.model;

import com.google.gson.annotations.SerializedName;


public class Country {

	@SerializedName("sortname")
	private String sortname;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	public void setSortname(String sortname){
		this.sortname = sortname;
	}

	public String getSortname(){
		return sortname;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
	public String toString() {
		return name;
	}
}