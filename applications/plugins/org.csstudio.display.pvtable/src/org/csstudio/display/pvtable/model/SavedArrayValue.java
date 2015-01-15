/*******************************************************************************
 * Copyright (c) 2014 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.display.pvtable.model;

import java.util.List;

import org.csstudio.vtype.pv.PV;
import org.epics.util.array.ListInt;
import org.epics.util.array.ListNumber;
import org.epics.vtype.VDoubleArray;
import org.epics.vtype.VEnumArray;
import org.epics.vtype.VFloatArray;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.epics.vtype.VType;

/** Saved value of an array table item
 *  @author Kay Kasemir
 */
@SuppressWarnings("nls")
public class SavedArrayValue extends SavedValue
{
    final private List<String> saved_value;

    /** Initialize from text
     *  @param value_text
     */
    public SavedArrayValue(final List<String> value_texts)
    {
        saved_value = value_texts;
    }

    /** @return Number of array elements */
    public int size()
    {
        return saved_value.size();
    }

    /** @param index Array index, <code>0 .. size()-1</code>
     *  @return Array element
     */
    public String get(final int index)
    {
        return saved_value.get(index);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEqualTo(final VType current_value, final double tolerance) throws Exception
    {
        if (current_value == null)
            return true;
        if (current_value instanceof VNumberArray)
        {
            final ListNumber values = ((VNumberArray)current_value).getData();
            final int N = values.size();
            if (N != saved_value.size())
                return false;
            for (int i=0; i<N; ++i)
            {
                final double v1 = values.getDouble(i);
                final double v2 = getSavedNumber(saved_value.get(i)).doubleValue();
                if (Math.abs(v2 - v1) > tolerance)
                    return false;
            }
            return true;
        }
        if (current_value instanceof VStringArray)
            return ((VStringArray)current_value).getData().equals(saved_value);
        if (current_value instanceof VEnumArray)
        {
            final ListInt indices = ((VEnumArray)current_value).getIndexes();
            final int N = indices.size();
            if (N != saved_value.size())
                return false;
            for (int i=0; i<N; ++i)
            {
                final int v1 = indices.getInt(i);
                final int v2 = getSavedNumber(saved_value.get(i)).intValue();
                if (Math.abs(v2 - v1) > tolerance)
                    return false;
            }
            return true;
        }
        // PVManager reports VString as current value for _disconnected_ channels?!
        // Disconnected -> Can't match, VString no array, overall "not equal"
        if (current_value instanceof VString)
            return false;
        throw new Exception("Cannot compare against unhandled type " + current_value.getClass().getName());
    }

    /** {@inheritDoc} */
    @Override
    public void restore(final PV pv) throws Exception
    {
        // Determine what type to write based on current value of the PV
        final VType pv_type = pv.read();
        if ((pv_type instanceof VDoubleArray) || (pv_type instanceof VFloatArray))
        {   // Write any floating point as double
            final int N = saved_value.size();
            final double[] data = new double[N];
            for (int i=0; i<N; ++i)
                data[i] = Double.parseDouble(saved_value.get(i));
            pv.write(data);
        }
        else if (pv_type instanceof VNumberArray  ||  pv_type instanceof VEnumArray)
        {   // Write any non-floating number as int
            // JCA_PV doesn't handle long[],
            // so int[] is the widest type
            final int N = saved_value.size();
            final int[] data = new int[N];
            // Parse as double to allow "1e10" or "100.0"
            // If it's "3.14", only "3" will of course be restored.
            for (int i=0; i<N; ++i)
                data[i] = (int) Double.parseDouble(saved_value.get(i));
            pv.write(data);
        }
        else
            throw new Exception("Cannot write type " + pv_type.getClass().getName());
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        final StringBuilder buf = new StringBuilder();
        boolean first = true;
        for (String value : saved_value)
        {
            if (first)
                first = false;
            else
                buf.append(", ");
            buf.append(value);
        }
        return buf.toString();
    }
}
