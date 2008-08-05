package org.richfaces.demo.tooltip;

import java.util.Date;
import java.util.List;

public class ToolTipData {
	private int tooltipCounter = 0;
	private List vehicles = null;
	private int currentVehicleIndex = -1;
	public int getTooltipCounter() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		return tooltipCounter++;
	}
	public int getTooltipCounterWithoutMod() {
		return this.tooltipCounter;
	}
	public Date getTooltipDate() {
		return new Date();
	}
	public List getVehicles() {
		if (vehicles==null) {
			vehicles = Vehicle.allVehicles(15);
			return vehicles;
		} else {
			return vehicles;
		}
	}
	public Vehicle getCurrentVehicle() {
		if (currentVehicleIndex>0 && currentVehicleIndex<getVehicles().size()) {
			return (Vehicle) getVehicles().get(currentVehicleIndex);
		} else {
			return null;
		}
	}
	public int getCurrentVehicleIndex() {
		return currentVehicleIndex;
	}
	public void setCurrentVehicleIndex(int currentVehicleIndex) {
		this.currentVehicleIndex = currentVehicleIndex;
	}
	
}
