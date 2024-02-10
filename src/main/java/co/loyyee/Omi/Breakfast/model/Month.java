package co.loyyee.Omi.Breakfast.model;

public enum Month {

	JAN("JANUARY"),
	FEB("FEBRUARY"),
	MAR("MARCH"),
	APR("APRIL"),
	MAY("MAY"),
	JUN("JUNE"),
	JUL("JULY"),
	AUG("AUGUST"),
	SEPT("SEPTEMBER"),
	OCT ("OCTOBER"),
	NOV("NOVEMBER"),
	DEC("DECEMBER");
	private String fullMonth;
	Month(String fullMonth) {
		this.fullMonth = fullMonth;
	}
	public String getFullMonth() {
		return fullMonth;
	}
}

