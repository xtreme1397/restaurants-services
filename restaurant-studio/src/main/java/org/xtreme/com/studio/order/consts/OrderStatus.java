package org.xtreme.com.studio.order.consts;

public enum OrderStatus {
	INPROGRESS("In Progress"), DELIVERED("Delivered");
	private final String value;





	OrderStatus(String value) {
		this.value = value;
	}





	public String getValue() {
		return value;
	}





	public static String getEnumByValue(String value) {
		String enumValue = null;
		for (OrderStatus e : OrderStatus.values()) {
			if (value.equals(e.value)) {
				enumValue = e.value;
				break;
			}
		}
		return enumValue;
	}
}
