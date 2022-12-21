package rad.swing;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.JViewport;

class SwingUtil {
	//http://smi-protege.stanford.edu/repos/protege/protege-core/trunk/src/edu/stanford/smi/protege/util/ComponentUtilities.java
    /**
     * Scrolls a table so that a certain cell becomes visible.
     * Source: http://javaalmanac.com/egs/javax.swing.table/Vis.html
     * @param table
     * @param rowIndex
     * @param vColIndex
     */
    public static void scrollToVisible(JTable table, int rowIndex, int vColIndex) {
        if (!(table.getParent() instanceof JViewport)) {
            return;
        }
        JViewport viewport = (JViewport)table.getParent();

        // This rectangle is relative to the table where the
        // northwest corner of cell (0,0) is always (0,0).
        Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);

        // The location of the viewport relative to the table
        Point pt = viewport.getViewPosition();

        // Translate the cell location so that it is relative
        // to the view, assuming the northwest corner of the
        // view is (0,0)
        rect.setLocation(rect.x-pt.x, rect.y-pt.y);

        table.scrollRectToVisible(rect);

        // Scroll the area into view
        //viewport.scrollRectToVisible(rect);
    }
}
