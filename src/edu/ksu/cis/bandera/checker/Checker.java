package edu.ksu.cis.bandera.checker;

/**
 * Checker interface.
 * @author: Roby Joehanes
 */
import java.util.List;
public interface Checker {
	public String check();
	public List getCounterExample();
}
