package edu.ksu.cis.bandera.bui.counterexample;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * This provides a data model to be used with a JTable and will
 * store information about Objects and their values.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 */
public class ObjectTableModel extends javax.swing.table.AbstractTableModel {

    /**
     * This is the default value that we will return if no other column or row
     * matches the request.
     */
    private static final String DEFAULT_VALUE_STRING = "";

    /**
     * This is the number of columns that exist in this table.
     */
    private static final int MAX_COLUMNS = 2;

    /**
     * This is the unique name for the column whose name is name (I know, confusing.).
     */
    private static final int NAME_COLUMN = 0;

    /**
     * This is the unique name for the column whose name is value (I know, not much better than name.).
     */
    private static final int VALUE_COLUMN = 1;

    /**
     * This is an array of variables that should be displayed in the watch window.  We will
     * assume all others are marked as ignore if they are not in this list.
     */
    private ArrayList watchList;

    /**
     * This is a Collection of all objects in the counter example.  The key for
     * this map is the name of the variable in String form.
     */
    private Map objectMap;

    /**
     * This is the name of the value column of the table.
     */
    private final static java.lang.String VALUE_COLUMN_NAME = "Value";

    /**
     * This is the name of the name column of the table.
     */
    private final static java.lang.String NAME_COLUMN_NAME = "Name";

    /**
     * This is the default name for columns in the table.
     */
    private final static java.lang.String DEFAULT_COLUMN_NAME = "";

	private TraceManager traceManager;
	private final static int BUTTON_COLUMN = 2;
	private final static java.lang.String BUTTON_COLUMN_NAME = "";
    /**
     * ObjectValueTable constructor comment.
     */
    public ObjectTableModel() {
        this(null);
    }
/**
 * Initialize this object table model using this trace manager.
 * Creation date: (12/7/01 1:04:34 PM)
 *
 * @param traceManager edu.ksu.cis.bandera.bui.counterexample.TraceManager
 */
public ObjectTableModel(TraceManager traceManager) {
	super();
	
	this.traceManager = traceManager;
	
    watchList = new ArrayList();
    
    if(traceManager != null) {
    	//objectMap = traceManager.getAllVariablesMap();
    	objectMap = new HashMap();
    	traceManager.updateObjectTableModelValues(this);
    }
    
    // check to see if objectMap or watchList are null.  this might cause problems later! -todd

}
/**
 * Add this variable and value to the object map.
 * Creation date: (12/8/01 10:31:27 AM)
 *
 * @param key java.lang.Object The variable name.
 * @param value java.lang.Object The variable's value.
 */
public void add(Object key, Object value) {

	// add the value and the key to a list (the value should be a ValueNode)
	List valueList = new ArrayList(2);
	valueList.add(key);
	valueList.add(value);
	
	objectMap.put(key.toString(), valueList);
}
    /**
     * Get the count of the columns in this table.  This is a fixed value.  This
     * method satisfies the interface TableModel.
     *
     * @return int The number of columns in this table.
     */
    public int getColumnCount() {
        return (MAX_COLUMNS);
    }
/**
 * Retrieve the name of column at position given.  This satisfies the interface TableModel.
 * Creation date: (10/16/01 9:20:02 PM)
 *
 * @return java.lang.String The name of the column or a blank String if not a valid column number.
 * @param column int The number of the column that we are trying to retrieve the name from.
 */
public String getColumnName(int column) {
    String columnName = "";

    switch (column) {
        case NAME_COLUMN :
            columnName = NAME_COLUMN_NAME;
            break;
        case VALUE_COLUMN :
            columnName = VALUE_COLUMN_NAME;
            break;
        case BUTTON_COLUMN :
            columnName = BUTTON_COLUMN_NAME;
            break;
        default :
            columnName = DEFAULT_COLUMN_NAME;
            break;
    }

    return (columnName);
}
    /**
     * Get the count of the rows for this table.  This returns a count of the number of variables
     * that are currently watched.  This satisfies the TableModel interface.
     *
     * @return int The count of rows that are in this table.
     */
    public int getRowCount() {
        return (watchList.size());
    }
    /**
     * Get the title for this table.  This is a fixed value (hard-coded in this method).  This
     * satisfies the TableModel interface.
     * Creation date: (10/16/01 9:39:44 PM)
     *
     * @return java.lang.String The title for this table.
     */
    public String getTitle() {
        return ("Watched Objects");
    }
/**
 * This will retrieve the data object in the space specified
 * by the two integers (row and column).
 *
 * @return Object The object at the location row,column.
 * @exception None.
 * @param int row The row which the requested object lies in.
 * @param int column The column which the requested object lies in.
 * @pre The objectList is initialized.
 * @post The requested object (at location row, column) is returned
 *       or an exception is thrown if it cannot be found or invalid
 *       values are given for row or column.
 */
public Object getValueAt(int row, int column) {
    Object result = null;

    if (row > getRowCount()) {
        // throw an exception
        System.err.println("Invalid row request.  row = " + row);
        return (null); // for now, do this.
    }

    if (column > getColumnCount()) {
        // throw an exception
        System.err.println("Invalid column request.  column = " + column);
        return (null); // for now, do this.
    }

    Object key = watchList.get(row);
    List list = (List) objectMap.get(key.toString());
    Object currentKey = list.get(0);
    Object currentValue = list.get(1);
    switch (column) {
        case NAME_COLUMN :
            result = key.toString();
            break;

        case VALUE_COLUMN :
            result = currentValue;
            break;

        case BUTTON_COLUMN :
            if (currentValue == null) {
                result = null;
            }
            else {
                if ((currentValue instanceof String) ||
		    (currentValue instanceof Character) ||
		    (currentValue instanceof Number) ||
		    (currentValue instanceof Boolean)) {
                    result = null;
                }
                else {
                    result = currentKey;
                }
            }
            break;

        default :
            result = DEFAULT_VALUE_STRING;
            break;
    }

    return (result);
}
    /**
     * Ignore the watched variable at the row specified.
     * Creation date: (10/16/01 10:44:30 PM)
     *
     * @param row int The row to ignore.
     */
    public void ignore(int row) {
        Object key = watchList.get(row);
        ignore(key);
    }
	/**
	  * Ignore the object that is requested.
	  *
	  * @param Object The variable that we wish to ignore.
	  */
	public void ignore(Object key) {
        watchList.remove(key.toString());
    }
    /**
     * Remove the requested row from the table.  This makes a call to ignore
     * since that is the appropriate action in this case.  This satisfies the interface
     * for TableModel.
     * Creation date: (10/16/01 10:42:14 PM)
     *
     * @param row int The row to remove.
     */
    public void removeRow(int row) {
        ignore(row);
    }
/**
 * This will update the values that are stored in the watch variables list.
 * Creation date: (12/7/01 2:20:57 PM)
 */
public void update() {
	traceManager.updateObjectTableModelValues(this);
}
	/**
  	  * Watch this variable.  This object will be added to the watch list.
  	  *
  	  * @param Object key The object to watch.
  	  */
	public void watch(Object key) {
        watchList.add(key.toString());
    }
}
