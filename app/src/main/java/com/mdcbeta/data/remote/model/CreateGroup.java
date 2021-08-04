package com.mdcbeta.data.remote.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class CreateGroup{

	@SerializedName("id")
	private String id;

	@SerializedName("color")
	private String color;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("members")
	private List<Integer> members;


	@SerializedName("group_diseases")
	private List<Integer> group_diseases;

	@SerializedName("name")
	private String name;

	@SerializedName("permission")
	private String permission;

	@SerializedName("group_id")
	private String group_id;


	public CreateGroup() {
	}

	public CreateGroup(String color, String userId, List<Integer> members, String name, String permission) {
		this.color = color;
		this.userId = userId;
		this.members = members;
		this.name = name;
		this.permission = permission;
	}


	public List<Integer> getGroup_diseases() {
		return group_diseases;
	}

	public void setGroup_diseases(List<Integer> group_diseases) {
		this.group_diseases = group_diseases;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<Integer> getMembers() {
		return members;
	}

	public void setMembers(List<Integer> members) {
		this.members = members;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
}