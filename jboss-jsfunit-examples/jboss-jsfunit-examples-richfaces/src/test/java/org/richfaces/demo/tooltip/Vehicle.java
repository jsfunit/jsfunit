package org.richfaces.demo.tooltip;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.richfaces.demo.common.RandomDataHelper;



public class Vehicle {
	public String make;
	public String model;
	public Integer year;
	public Integer milage;
	public String vin;
	public Integer zip;
	public Date listed;
	public Vehicle(String make, String model) {
		this.make = make;
		this.model = model;
		this.listed = new Date();
	}
	
	public static Vehicle list[] = {
		new Vehicle("Ford", "Taurus"),
		new Vehicle("Ford", "Mustang"),
		new Vehicle("Ford", "Focus"),
		new Vehicle("Ford", "Thinderbird"),
		new Vehicle("BMW", "Z3"),
		new Vehicle("BMW", "323i"),
		new Vehicle("BMW", "521"),
		new Vehicle("BMW", "Mustang"),
		new Vehicle("Audi", "A4"),
		new Vehicle("Audi", "A6"),
		new Vehicle("Toyota", "Camry"),
		new Vehicle("Toyota", "Corolla"),
		new Vehicle("Toyota", "Matrix"),
		new Vehicle("Honda", "Accord"),
		new Vehicle("Honda", "Civic")
	};

	public static List allVehicles(int size) {
		List ret = new ArrayList();
		for (int counter=0;counter<size;counter++) {
			Vehicle car = (Vehicle)RandomDataHelper.random(list);
			car.milage = new Integer(RandomDataHelper.random(10000, 100000));
			car.vin = RandomDataHelper.randomString(32);
			car.year = new Integer(RandomDataHelper.random(2000, 2005));
			car.zip = new Integer(RandomDataHelper.random(94500,94600));
			ret.add(car);
		}
		return ret;
	}

	public Date getListed() {
		return listed;
	}

	public void setListed(Date listed) {
		this.listed = listed;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public Integer getMilage() {
		return milage;
	}

	public void setMilage(Integer milage) {
		this.milage = milage;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getZip() {
		return zip;
	}

	public void setZip(Integer zip) {
		this.zip = zip;
	}
	
}

