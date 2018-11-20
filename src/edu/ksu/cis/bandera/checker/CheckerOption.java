package edu.ksu.cis.bandera.checker;

/**
 * Interfaces on Parsing Options.
 * @author: Roby Joehanes
 */
import java.util.*;

public abstract class CheckerOption {
	protected Hashtable options = new Hashtable();
	public CheckerOption() {}
	public CheckerOption(String s) { if (s != null) parseOptions(s); }
	public Object get(String name) { return options.get(name); }
	public abstract void parseOptions(String s);
	public void put(String name, Object val) { options.put(name, val); }
}
