package com.mdcbeta.data.remote.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Location{

	@SerializedName("area")
	private String area;

	@SerializedName("address")
	private String address;

	@SerializedName("id")
	private String id;

	@SerializedName("state_id")
	private String stateId;

	@SerializedName("country_id")
	private String countryId;

	@SerializedName("city_id")
	private String cityId;

	public String getArea(){
		return area;
	}

	public String getAddress(){
		return address;
	}

	public String getId(){
		return id;
	}

	public String getStateId(){
		return stateId;
	}

	public String getCountryId(){
		return countryId;
	}

	public String getCityId(){
		return cityId;
	}
}