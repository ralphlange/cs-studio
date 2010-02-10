package org.epics.css.dal.simple.impl;

import java.util.Arrays;

import org.epics.css.dal.DynamicValueProperty;

public final class DoubleSeqAnyDataImpl extends AbstractAnyDataImpl<double[]> {
	
	public DoubleSeqAnyDataImpl(DynamicValueProperty<double[]> property, long beamID) {
		super(property,beamID);
	}

	public DoubleSeqAnyDataImpl(DynamicValueProperty<double[]> property) {
		super(property,Long.MIN_VALUE);
	}

	public Object[] anySeqValue() {
		return DataUtil.toNumberSeq(response.getValue());
	}

	public Object anyValue() {
		return response.getValue();
	}

	public double[] doubleSeqValue() {
		return response.getValue();
	}

	public double doubleValue() {
		return response.getValue()[0];
	}

	public long[] longSeqValue() {
		return DataUtil.toLongSeq(response.getValue());
	}

	public long longValue() {
		return Math.round(response.getValue()[0]);
	}

	public Number[] numberSeqValue() {
		return DataUtil.toNumberSeq(response.getValue());
	}

	public Number numberValue() {
		return response.getNumber();
	}

	public String[] stringSeqValue() {
		return DataUtil.toStringSeq(response.getValue());
	}

	public String stringValue() {
		return Arrays.toString(response.getValue());
	}

}
