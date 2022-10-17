package com.xinchen.medicalwaste.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 废物产生源
 *
 */
public class WasteSourceBean implements Serializable {

/**
 * {"id":"704650236647179264","name":"心病科","idCard":"101010198808311221","empno":"0091","number":"009","personInCharge":"心病科","type":"internal","children":[],"parent":{"collType":1},"extention":{},"collType":1}
 * ***/
	private String id;
	private String name;
	private String idCard;
	private String empno;
	private String number;
	private String personInCharge;
	private String type;
	private List<?> children;
	private ParentBean parent;
	private ExtentionBean extention;
	private String collType;

	public static class ParentBean {
		private String collType;

		public String getCollType() {
			return collType;
		}

		public void setCollType(String collType) {
			this.collType = collType;
		}
	}

	public static class ExtentionBean {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getEmpno() {
		return empno;
	}

	public void setEmpno(String empno) {
		this.empno = empno;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPersonInCharge() {
		return personInCharge;
	}

	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<?> getChildren() {
		return children;
	}

	public void setChildren(List<?> children) {
		this.children = children;
	}

	public ParentBean getParent() {
		return parent;
	}

	public void setParent(ParentBean parent) {
		this.parent = parent;
	}

	public ExtentionBean getExtention() {
		return extention;
	}

	public void setExtention(ExtentionBean extention) {
		this.extention = extention;
	}

	public String getCollType() {
		return collType;
	}

	public void setCollType(String collType) {
		this.collType = collType;
	}
}