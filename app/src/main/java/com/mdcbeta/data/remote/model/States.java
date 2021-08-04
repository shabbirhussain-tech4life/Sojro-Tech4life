package com.mdcbeta.data.remote.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class States{

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	@SerializedName("country_id")
	private String countryId;

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

	public void setCountryId(String countryId){
		this.countryId = countryId;
	}

	public String getCountryId(){
		return countryId;
	}

	@Override
	public String toString() {
		return name;
	}
}