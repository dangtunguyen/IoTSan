package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

public abstract class GInputInfo {
	public String inputName;
	public boolean isMultiple;
	public abstract GInputInfo clone();
}
