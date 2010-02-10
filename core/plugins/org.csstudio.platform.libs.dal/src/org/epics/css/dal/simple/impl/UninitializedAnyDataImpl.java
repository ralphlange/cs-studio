package org.epics.css.dal.simple.impl;

import org.epics.css.dal.DynamicValueProperty;

public class UninitializedAnyDataImpl<T> extends AbstractAnyDataImpl<T> {

	public UninitializedAnyDataImpl(DynamicValueProperty<T> property) {
		super(property,Long.MIN_VALUE);
	}
	public UninitializedAnyDataImpl(DynamicValueProperty<T> property, long beamID) {
		super(property, beamID);
	}

	/* (non-Javadoc)
	 * @see org.epics.css.dal.simple.impl.AbstractAnyDataImpl#getQuality()
	 */
	@Override
	public Quality getQuality() {
		return Quality.Invalid;
	}

	/* (non-Javadoc)
	 * @see org.epics.css.dal.simple.impl.AbstractAnyDataImpl#getStatus()
	 */
	@Override
	public String getStatus() {
		return "Uninitialized";
	}

	public Object[] anySeqValue() {
		throw new UnsupportedOperationException("UninitializedAnyDataImpl does not support this operation.");
	}

	public Object anyValue() {
		throw new UnsupportedOperationException("UninitializedAnyDataImpl does not support this operation.");
	}

	public double[] doubleSeqValue() {
		throw new UnsupportedOperationException("UninitializedAnyDataImpl does not support this operation.");
	}

	public double doubleValue() {
		throw new UnsupportedOperationException("UninitializedAnyDataImpl does not support this operation.");
	}

	public long[] longSeqValue() {
		throw new UnsupportedOperationException("UninitializedAnyDataImpl does not support this operation.");
	}

	public long longValue() {
		throw new UnsupportedOperationException("UninitializedAnyDataImpl does not support this operation.");
	}

	public Number[] numberSeqValue() {
		throw new UnsupportedOperationException("UninitializedAnyDataImpl does not support this operation.");
	}

	public Number numberValue() {
		throw new UnsupportedOperationException("UninitializedAnyDataImpl does not support this operation.");
	}

	public String[] stringSeqValue() {
		throw new UnsupportedOperationException("UninitializedAnyDataImpl does not support this operation.");
	}

	public String stringValue() {
		throw new UnsupportedOperationException("UninitializedAnyDataImpl does not support this operation.");
	}

}
