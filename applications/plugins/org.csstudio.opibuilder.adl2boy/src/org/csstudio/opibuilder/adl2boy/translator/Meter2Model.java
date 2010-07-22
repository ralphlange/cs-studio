package org.csstudio.opibuilder.adl2boy.translator;

import org.csstudio.opibuilder.model.AbstractContainerModel;
import org.csstudio.opibuilder.model.AbstractWidgetModel;
import org.csstudio.opibuilder.widgets.model.AbstractMarkedWidgetModel;
import org.csstudio.opibuilder.widgets.model.ActionButtonModel;
import org.csstudio.opibuilder.widgets.model.XMeterModel;
import org.csstudio.utility.adlparser.fileParser.ADLWidget;
import org.csstudio.utility.adlparser.fileParser.widgets.Meter;
import org.eclipse.swt.graphics.RGB;

public class Meter2Model extends AbstractADL2Model {
	XMeterModel meterModel = new XMeterModel();

	public Meter2Model(ADLWidget adlWidget, RGB[] colorMap, AbstractContainerModel parentModel) {
		super(adlWidget, colorMap, parentModel);
		parentModel.addChild(meterModel, true);
		Meter meterWidget = new Meter(adlWidget);
		if (meterWidget != null) {
			setADLObjectProps(meterWidget, meterModel);
			setADLMonitorProps(meterWidget, meterModel);
		}

		//set color mode
		String color_mode = meterWidget.getColor_mode();
		System.out.println("Meter Color mode " + color_mode);
		if ( color_mode.equals("static") ){
			meterModel.setPropertyValue(XMeterModel.PROP_FORECOLOR_ALARMSENSITIVE, false);
		}
		else if (color_mode.equals("alarm") ){
			meterModel.setPropertyValue(XMeterModel.PROP_FORECOLOR_ALARMSENSITIVE, true);
		}
		else if (color_mode.equals("discrete") ){
			meterModel.setPropertyValue(XMeterModel.PROP_FORECOLOR_ALARMSENSITIVE, false);
			//TODO Meter2Model Figure out what to do if colorMode is discrete
		}
		
		//TODO Add PV Limits to Meter2Model
		// Decorate the meter Model
		//TODO Meter2Model cannot show value or channel at this time. can this be added to the widget or do we need to make a grouping container.
		String label = meterWidget.getLabel();
		if ( label.equals("none")){
			meterModel.setPropertyValue(AbstractMarkedWidgetModel.PROP_SHOW_MARKERS, false);
			meterModel.setPropertyValue(AbstractMarkedWidgetModel.PROP_SHOW_SCALE, false);
		}
		if ( label.equals("no decorations")){
			meterModel.setPropertyValue(AbstractMarkedWidgetModel.PROP_SHOW_MARKERS, false);
			meterModel.setPropertyValue(AbstractMarkedWidgetModel.PROP_SHOW_SCALE, false);
		}
		if ( label.equals("outline")){
			meterModel.setPropertyValue(AbstractMarkedWidgetModel.PROP_SHOW_MARKERS, false);
			meterModel.setPropertyValue(AbstractMarkedWidgetModel.PROP_SHOW_SCALE, true);
		}
		if ( label.equals("limits")){
			meterModel.setPropertyValue(AbstractMarkedWidgetModel.PROP_SHOW_MARKERS, false);
			meterModel.setPropertyValue(AbstractMarkedWidgetModel.PROP_SHOW_SCALE, true);
		}
		if ( label.equals("channel")){
			meterModel.setPropertyValue(AbstractMarkedWidgetModel.PROP_SHOW_MARKERS, false);
			meterModel.setPropertyValue(AbstractMarkedWidgetModel.PROP_SHOW_SCALE, true);
		}

	}

	@Override
	public	AbstractWidgetModel getWidgetModel() {
		return meterModel;
	}

}
