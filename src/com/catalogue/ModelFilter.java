package com.catalogue;

import com.catalogue.entities.CatalogueModel;

public class ModelFilter {

	private String system;
	private String description;
	private Integer price_start;
	private Integer price_end;
	private Integer height;
	private Integer width;
	private Integer length;
	private Integer error;

	public ModelFilter() {
		super();
		// TODO Auto-generated constructor stub
		system = null;
		description = null;
		price_start = null;
		price_end = null;
		height = null;
		width = null;
		length = null;
		error = 10;
	}

	public boolean checkModel(CatalogueModel cm) {
		boolean check_result = true;

		if (system != null) {
			if (cm.getSystem_name().indexOf(system) == -1) {
				check_result = false;
				return check_result;
			}
		}
		if (description != null) {
			if (cm.getDescription().indexOf(description) == -1) {
				check_result = false;
				return check_result;
			}
		}

		if (price_start != null) {
			if (cm.getPrice() < price_start) {
				check_result = false;
				return check_result;
			}
		}

		if (price_end != null) {
			if (cm.getPrice() > price_end) {
				check_result = false;
				return check_result;
			}
		}

		if (height != null) {
			if(error!=null) {
				double err_abs=height*error/100;
				double bot=height-err_abs;
				double top=height+err_abs;
				if(!( (cm.getHeight()>=bot)&&(cm.getHeight()<=top) )) {
					check_result = false;
					return check_result;
				}
			}else {
				if(cm.getHeight().intValue()!=height) {
					check_result = false;
					return check_result;
				}
			}
		}

		if (width != null) {
			if(error!=null) {
				double err_abs=width*error/100;
				double bot=width-err_abs;
				double top=width+err_abs;
				if(!( (cm.getWidth()>=bot)&&(cm.getWidth()<=top) )) {
					check_result = false;
					return check_result;
				}
			}else {
				if(cm.getWidth().intValue()!=width) {
					check_result = false;
					return check_result;
				}
			}
		}

		if (length != null) {
			if(error!=null) {
				double err_abs=length*error/100;
				double bot=length-err_abs;
				double top=length+err_abs;
				if(!( (cm.getDeep()>=bot)&&(cm.getDeep()<=top) )) {
					check_result = false;
					return check_result;
				}
			}else {
				if(cm.getDeep().intValue()!=length) {
					check_result = false;
					return check_result;
				}
			}
		}

		return check_result;
	}

	public void skipFilter() {
		system = null;
		description = null;
		price_start = null;
		price_end = null;
		height = null;
		width = null;
		length = null;
		error = 10;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPrice_start() {
		return price_start;
	}

	public void setPrice_start(Integer price_start) {
		this.price_start = price_start;
	}

	public Integer getPrice_end() {
		return price_end;
	}

	public void setPrice_end(Integer price_end) {
		this.price_end = price_end;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getError() {
		return error;
	}

	public void setError(Integer error) {
		this.error = error;
	}

	

}
