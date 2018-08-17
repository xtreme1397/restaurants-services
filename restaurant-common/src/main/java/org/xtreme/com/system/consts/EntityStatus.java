package org.xtreme.com.system.consts;

public enum EntityStatus {
	ACTIVE("Active"), INACTIVE("Inactive");
	private final String value;





	EntityStatus(String value) {
		this.value = value;
	}





	public String getValue() {
		return value;
	}





	public static String getEnumByValue(String value) {
		String enumValue = null;
		for (EntityStatus e : EntityStatus.values()) {
			if (value.equals(e.value)) {
				enumValue = e.value;
				break;
			}
		}
		return enumValue;
	}
}
