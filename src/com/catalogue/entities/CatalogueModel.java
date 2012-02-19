package com.catalogue.entities;

public class CatalogueModel {
	
	private String system_name;
	private String articul;
	private String description;
	private double height;
	private double width;
	private double deep;
	private String color;
	private double price;
	private int full_pic_id;
	private int mid_pic_id;
	private int low_pic_id;
	
	public CatalogueModel(String system_name, String articul,
			String description, double height, double width, double deep,
			String color, double price, int full_pic_id, int mid_pic_id,
			int low_pic_id) {
		super();
		this.system_name = system_name;
		this.articul = articul;
		this.description = description;
		this.height = height;
		this.width = width;
		this.deep = deep;
		this.color = color;
		this.price = price;
		this.full_pic_id = full_pic_id;
		this.mid_pic_id = mid_pic_id;
		this.low_pic_id = low_pic_id;
	}

	public String getSystem_name() {
		return system_name;
	}

	public void setSystem_name(String system_name) {
		this.system_name = system_name;
	}

	public String getArticul() {
		return articul;
	}

	public void setArticul(String articul) {
		this.articul = articul;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getHeight() {
		return (Double)height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public Double getWidth() {
		return (Double)width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public Double getDeep() {
		return (Double)deep;
	}

	public void setDeep(double deep) {
		this.deep = deep;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Double getPrice() {
		return (Double)price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getFull_pic_id() {
		return full_pic_id;
	}

	public void setFull_pic_id(int full_pic_id) {
		this.full_pic_id = full_pic_id;
	}

	public int getMid_pic_id() {
		return mid_pic_id;
	}

	public void setMid_pic_id(int mid_pic_id) {
		this.mid_pic_id = mid_pic_id;
	}

	public int getLow_pic_id() {
		return low_pic_id;
	}

	public void setLow_pic_id(int low_pic_id) {
		this.low_pic_id = low_pic_id;
	}	
}
