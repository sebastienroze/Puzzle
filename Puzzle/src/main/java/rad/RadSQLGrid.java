package rad;

import java.util.ArrayList;

public interface RadSQLGrid extends RadComponent {
	public RadSQLGrid addRowSelectListener(ListenerRAD al);

	public RadSQLGrid addDoubleClicListener(ListenerRAD al);

	public void refreshKey(long keyValue);

	public SQLGridSearch getGridSearch();

	public void setGridSearch(SQLGridSearch gs);

	public ArrayList<Long> getKeyValues();

	public int getSelectedLine();

	public long getSelectedKey();

	public void selectKey(long keyValue);

	public void filter();

	public void setOrderBy(String orderBy);

	public void setGroupBy(String groupBy);

}
