package org.csstudio.trends.databrowser.configview;

import org.csstudio.trends.databrowser.Plugin;
import org.csstudio.trends.databrowser.model.IModelItem;
import org.csstudio.trends.databrowser.model.ModelItem;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/** The JFace label provider for the Model data. 
 *  @author Kay Kasemir
 */
@SuppressWarnings("nls")
public class PVTableLabelProvider extends LabelProvider implements
		ITableLabelProvider, ITableColorProvider
{
    // For the checkbox images
    public static final String SELECTED = "checked";
    public static final String UNSELECTED  = "unchecked";
    private static ImageRegistry images = null;
    
    /** Constructor */
    public PVTableLabelProvider()
    {
        // Images get disposed by registry.
        // Note that registry and its entries are only created once!
        if (images == null)
        {
            images = new ImageRegistry();
            images.put(SELECTED, Plugin
                    .getImageDescriptor("icons/" + SELECTED + ".gif"));
            images.put(UNSELECTED, Plugin
                    .getImageDescriptor("icons/" + UNSELECTED + ".gif"));
        }
    }
    
    /** Get text for all but the 'select' column. */
	public String getColumnText(Object obj, int index)
	{
        if (obj == PVTableHelper.empty_row)
            return index == 0 ? PVTableHelper.empty_row : ""; //$NON-NLS-1$
        return PVTableHelper.getText((ModelItem) obj, index);
	}

    /** Get column image (only for the 'select' column) */
	public Image getColumnImage(Object obj, int index)
	{
        if (obj != PVTableHelper.empty_row)
        {
            IModelItem entry = (IModelItem) obj;
            if (index == PVTableHelper.Column.AUTO_SCALE.ordinal())
                return entry.getAutoScale() ?
                            images.get(SELECTED) : images.get(UNSELECTED);
        }
        return null;
	}

    /** @see org.eclipse.jface.viewers.ITableColorProvider */
    public Color getBackground(Object obj, int index)
    {
        if (index == PVTableHelper.Column.COLOR.ordinal()  &&
            obj != PVTableHelper.empty_row)
        {
            IModelItem entry = (IModelItem) obj;
            return entry.getColor();
        }
        return null;
    }

    /** @see org.eclipse.jface.viewers.ITableColorProvider */
    public Color getForeground(Object obj, int index)
    {
        return null;
    }
}
