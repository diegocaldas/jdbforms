/*
 * Created on Jul 4, 2004
 *
 * Test per bean
 */
package net.myPackage;
/**
 * @author ivan.codarin@amm.uniud.it
 *
 * Simple class to create background color.
 */
public class IvanBackgroudColor {

	private String BG_LIGHT;
	private String BG_DARK;
	private String bgColor;
	private int idNumber;
	
	/**
	 * @param ID_Number
	 */
	public IvanBackgroudColor() {
		BG_LIGHT="#EEEEEE";
		BG_DARK="#666666";
	}

	/**
	 * @param idNumber The idNumber to set.
	 */
	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
	}
	/**
	 * @return Returns the idNumber.
	 */
	public int getIdNumber() {
		return idNumber;
	}
	/**
	 * @return Returns the bgColor.
	 */
	public String getBgColor() {
		if (this.idNumber % 2 ==1)
			this.bgColor=BG_DARK;
		else
			this.bgColor=BG_LIGHT;
		return bgColor;
	}
	
}
