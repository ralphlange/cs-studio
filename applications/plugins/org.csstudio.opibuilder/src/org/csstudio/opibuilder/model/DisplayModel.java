/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.opibuilder.model;

import java.util.ArrayList;
import java.util.List;

import org.csstudio.opibuilder.OPIBuilderPlugin;
import org.csstudio.opibuilder.datadefinition.DisplayScaleData;
import org.csstudio.opibuilder.properties.ActionsProperty;
import org.csstudio.opibuilder.properties.BooleanProperty;
import org.csstudio.opibuilder.properties.ComplexDataProperty;
import org.csstudio.opibuilder.properties.IntegerProperty;
import org.csstudio.opibuilder.properties.VersionProperty;
import org.csstudio.opibuilder.properties.WidgetPropertyCategory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.gef.GraphicalViewer;
import org.osgi.framework.Version;

/**
 * The root model for an OPI Display.
 * 
 * @author Alexander Will, Sven Wende, Kai Meyer (class of same name in SDS)
 * @author Xihui Chen
 * 
 */
public class DisplayModel extends AbstractContainerModel {

	/**
	 * The type ID of this model.
	 */
	public static final String ID = "org.csstudio.opibuilder.Display"; //$NON-NLS-1$

	/**
	 * Space of grid in pixels.
	 */
	public static final String PROP_GRID_SPACE = "grid_space"; //$NON-NLS-1$
	/**
	 * If the grid should be visible.
	 */
	public static final String PROP_SHOW_GRID = "show_grid"; //$NON-NLS-1$
	/**
	 * If the ruler should be visible.
	 */
	public static final String PROP_SHOW_RULER = "show_ruler"; //$NON-NLS-1$
	/**
	 * If the moving widgets will be snapped to the geometry of other widgets.
	 */
	public static final String PROP_SNAP_GEOMETRY = "snap_to_geometry"; //$NON-NLS-1$
	/**
	 * If the dash boundary line of the display should be visible.
	 */
	public static final String PROP_SHOW_EDIT_RANGE = "show_edit_range"; //$NON-NLS-1$

	/**
	 * If the tab close button should be hidden.
	 */
	public static final String PROP_SHOW_CLOSE_BUTTON = "show_close_button"; //$NON-NLS-1$

	public static final String PROP_BOY_VERSION = "boy_version"; //$NON-NLS-1$

	/**
	 * Auto scale all the widgets as the window resizes. If this is set to true,
	 * zoom operation will not work.
	 */
	public static final String PROP_AUTO_ZOOM_TO_FIT_ALL = "auto_zoom_to_fit_all"; //$NON-NLS-1$
	
	/**
	 * Automatically scale all widgets when window resizes.
	 */
	public static final String PROP_AUTO_SCALE_WIDGETS = "auto_scale_widgets"; //$NON-NLS-1$


	private GraphicalViewer viewer;

	private IPath opiFilePath;

	private int displayID;

	public DisplayModel() {
		super();
		setLocation(-1, -1);
		setSize(800, 600);
	}

	@Override
	protected void configureProperties() {
		addProperty(new IntegerProperty(PROP_GRID_SPACE, "Grid Space",
				WidgetPropertyCategory.Display, 6, 1, 1000));
		addProperty(new BooleanProperty(PROP_SHOW_GRID, "Show Grid",
				WidgetPropertyCategory.Display, true));
		addProperty(new BooleanProperty(PROP_SHOW_RULER, "Show Ruler",
				WidgetPropertyCategory.Display, true));
		addProperty(new BooleanProperty(PROP_SNAP_GEOMETRY, "Snap to Geometry",
				WidgetPropertyCategory.Display, true));
		addProperty(new BooleanProperty(PROP_SHOW_EDIT_RANGE,
				"Show Edit Range", WidgetPropertyCategory.Display, true));
		addProperty(new BooleanProperty(PROP_AUTO_ZOOM_TO_FIT_ALL,
				"Auto Zoom to Fit All", WidgetPropertyCategory.Behavior, false));
		addProperty(new ComplexDataProperty(
				PROP_AUTO_SCALE_WIDGETS, "Auto Scale Widgets", 
				WidgetPropertyCategory.Behavior, new DisplayScaleData(), "Scale Widgets as windows resizes"));
	
		addProperty(new BooleanProperty(PROP_SHOW_CLOSE_BUTTON,
				"Show Close Button", WidgetPropertyCategory.Display, true));
		Version version = OPIBuilderPlugin.getDefault().getBundle()
				.getVersion();
		addProperty(new VersionProperty(PROP_BOY_VERSION, "BOY Version",
				WidgetPropertyCategory.Basic, version.toString()));

		setPropertyVisible(PROP_BORDER_COLOR, false);
		setPropertyVisible(PROP_BORDER_STYLE, false);
		setPropertyVisible(PROP_BORDER_WIDTH, false);
		setPropertyVisible(PROP_VISIBLE, false);
		setPropertyVisible(PROP_ENABLED, false);
		setPropertyVisible(PROP_TOOLTIP, false);
		setPropertyVisible(PROP_ACTIONS, false);
		setPropertyVisible(PROP_FONT, false);
		setPropertyVisibleAndSavable(PROP_BOY_VERSION, false, true);
		addProperty(new ActionsProperty(PROP_ACTIONS, "Actions",
				WidgetPropertyCategory.Behavior, false));
		setPropertyDescription(PROP_COLOR_FOREGROUND, "Grid Color");
		setPropertyValue(PROP_NAME, ""); //$NON-NLS-1$
		removeProperty(PROP_SCALE_OPTIONS);

	}
	
	/**
	 * @return true if Children should be auto scaled when this container is resized.
	 */
	public DisplayScaleData getDisplayScaleData(){
		return (DisplayScaleData)getPropertyValue(PROP_AUTO_SCALE_WIDGETS);
	}

	public boolean isShowGrid() {
		return (Boolean) getCastedPropertyValue(PROP_SHOW_GRID);
	}

	public boolean isShowRuler() {
		return (Boolean) getCastedPropertyValue(PROP_SHOW_RULER);
	}

	public boolean isSnapToGeometry() {
		return (Boolean) getCastedPropertyValue(PROP_SNAP_GEOMETRY);
	}

	public boolean isShowEditRange() {
		return (Boolean) getCastedPropertyValue(PROP_SHOW_EDIT_RANGE);
	}

	public boolean isShowCloseButton() {
		return (Boolean) getPropertyValue(PROP_SHOW_CLOSE_BUTTON);
	}

	public boolean isAutoZoomToFitAll() {
		return (Boolean) getPropertyValue(PROP_AUTO_ZOOM_TO_FIT_ALL);
	}

	@Override
	public String getTypeID() {
		return ID;
	}

	/**
	 * @param opiFilePath
	 *            the opiFilePath to set
	 */
	public void setOpiFilePath(IPath opiFilePath) {
		this.opiFilePath = opiFilePath;
	}

	/**
	 * @return the opiFilePath
	 */
	public IPath getOpiFilePath() {
		return opiFilePath;
	}

	/**
	 * Set the viewer of the display model if this model belongs to a viewer.
	 * 
	 * @param viewer
	 *            the viewer to set
	 */
	public void setViewer(GraphicalViewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * @return the viewer, null if it has no viewer.
	 */
	public GraphicalViewer getViewer() {
		return viewer;
	}

	/**
	 * @param displayID
	 *            the unique displayID to set
	 */
	public void setDisplayID(int displayID) {
		this.displayID = displayID;
	}

	/**
	 * @return the displayID
	 */
	public int getDisplayID() {
		return displayID;
	}

	public Version getBOYVersion() {
		return new Version((String) getPropertyValue(PROP_BOY_VERSION));
	}

	/**
	 * @return the list of connections belongs to this display model.
	 */
	public List<ConnectionModel> getConnectionList() {		
		return getConnectionList(this);
	}
	
	private List<ConnectionModel> getConnectionList(AbstractContainerModel container){
		List<ConnectionModel> connectionModels = new ArrayList<ConnectionModel>();
		List<AbstractWidgetModel> allDescendants = getAllDescendants();
		for(AbstractWidgetModel widget : allDescendants){
			if(!widget.getSourceConnections().isEmpty()){
				for(ConnectionModel connectionModel: widget.getSourceConnections()){
					if(allDescendants.contains(connectionModel.getTarget())){
						connectionModels.add(connectionModel);
					}
				}
			}			
		}		
		return connectionModels;
	}

	public AbstractWidgetModel getWidgetFromWUID(String wuid) {
		return getWidgetFromWUID(this, wuid);
	}

	private static AbstractWidgetModel getWidgetFromWUID(
			AbstractContainerModel container, String wuid) {
		for (AbstractWidgetModel widget : container.getChildren()) {
			if (widget.getWUID().equals(wuid))
				return widget;
			else if (widget instanceof AbstractContainerModel) {
				AbstractWidgetModel w = getWidgetFromWUID(
						(AbstractContainerModel) widget, wuid);
				if (w != null)
					return w;
			}
		}
		return null;
	}

	
	@Override
	public void scale(double widthRatio, double heightRatio) {
		int minWidth = getDisplayScaleData().getMinimumWidth();
		if(minWidth < 0){
			minWidth = getWidth();
		}
		int minHeight = getDisplayScaleData().getMinimumHeight();
		if(minHeight < 0){
			minHeight = getHeight();
		}		
		if(getWidth() *widthRatio < minWidth)
			widthRatio = minWidth/(double)getWidth();
		if(getHeight()*heightRatio < minHeight)
			heightRatio = minHeight/(double)getHeight();
		for(AbstractWidgetModel child : getChildren())
			child.scale(widthRatio, heightRatio);
	}
}
