/* 
 * Copyright (c) 2008 Stiftung Deutsches Elektronen-Synchrotron, 
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS. 
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND 
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE 
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR 
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE. 
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, 
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION, 
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS 
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY 
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 */
 package de.desy.css.dal.tine;

import junit.framework.TestCase;

import org.epics.css.dal.CharacteristicInfo;
import org.epics.css.dal.DoubleProperty;
import org.epics.css.dal.DynamicValueAdapter;
import org.epics.css.dal.DynamicValueEvent;
import org.epics.css.dal.DynamicValueListener;
import org.epics.css.dal.DynamicValueProperty;
import org.epics.css.dal.impl.DefaultApplicationContext;
import org.epics.css.dal.simple.RemoteInfo;
import org.epics.css.dal.spi.DefaultPropertyFactoryService;
import org.epics.css.dal.spi.LinkPolicy;
import org.epics.css.dal.spi.PropertyFactory;
import org.junit.Before;
import org.junit.Test;

public class TINEPlugTest extends TestCase {
	/**
	 * The application context.
	 */
	private DefaultApplicationContext _applicationContext;

	/**
	 * The TINE property factory.
	 */
	private PropertyFactory _propertyFactory;

	/**
	 * An event counter.
	 */
	private long _eventCount;

	/**
	 * The TINE property we connect to.
	 */
	//private static final String PROPERTY_NAME = "DEFAULT/TIMESRV/device_0/SYSTIME";
	private static final String PROPERTY_NAME = "TINE/DEFAULT/JavaIOC-EQM/valueOnly/valueOnly";

	/**
	 * Set up the test case.
	 */
	@Before
	public void setUp() throws Exception {
		_applicationContext = new TINEApplicationContext("TINEPlugTest"); //$NON-NLS-1$
		_eventCount = 0;
		_propertyFactory = DefaultPropertyFactoryService
				.getPropertyFactoryService().getPropertyFactory(
						_applicationContext, LinkPolicy.ASYNC_LINK_POLICY);
	}

	/**
	 * Test the basic connection behaviour.
	 * 
	 * @throws Exception
	 */
	@Test
	public void basicDynamicValueListenerTest() throws Exception {
		DynamicValueListener listener = createDynamicValueListener();

		DynamicValueProperty property = _propertyFactory.getProperty(
				new RemoteInfo(TINEPlug.PLUG_TYPE, PROPERTY_NAME, null, null), DoubleProperty.class, null);

		assertTrue(_propertyFactory.getPropertyFamily().contains(property));

		long initialEventCount = _eventCount;

		property.addDynamicValueListener(listener);

		Thread.sleep(3000);
		assertTrue(_eventCount > initialEventCount);

		property.removeDynamicValueListener(listener);
		_propertyFactory.getPropertyFamily().destroy(property);

		assertFalse(_propertyFactory.getPropertyFamily().contains(property));
	}

	/**
	 * Test the connection behaviour with an intermediate
	 * <code>getValue()</code> call.
	 * 
	 * @throws Exception
	 */
	@Test
	public void extendedDynamicValueListenerTest() throws Exception {
		DynamicValueListener listener = createDynamicValueListener();

		DynamicValueProperty property = _propertyFactory.getProperty(
				new RemoteInfo(TINEPlug.PLUG_TYPE, PROPERTY_NAME, null, null), DoubleProperty.class, null);

		long initialEventCount = _eventCount;

		property.addDynamicValueListener(listener);

		Thread.sleep(3000);
		assertTrue(_eventCount > initialEventCount);

		long changedEventCount = _eventCount;

		// now we request a value
		Object value = property.getValue();
		assertNotNull(value);

		Thread.sleep(3000);

		// there is one event that is generated by getValue
		assertTrue(_eventCount > changedEventCount + 1);

		property.removeDynamicValueListener(listener);
		_propertyFactory.getPropertyFamily().destroy(property);
	}

	/**
	 * Create a new dynamic value listener.
	 * 
	 * @return A new dynamic value listener.
	 */
	protected DynamicValueListener createDynamicValueListener() {
		return new DynamicValueAdapter() {
			@Override
			public void valueUpdated(DynamicValueEvent arg0) {
				_eventCount++;
			}

			@Override
			public void valueChanged(DynamicValueEvent arg0) {
				_eventCount++;
			}
		};
	}

	public void testMandatoryCharacteristics() {
		
		try {
			
			DynamicValueProperty property = _propertyFactory.getProperty(PROPERTY_NAME, DoubleProperty.class, null);

			CharacteristicInfo[] infos= CharacteristicInfo.getDefaultCharacteristics(DoubleProperty.class, null);
			
			assertNotNull(infos);
			
			for (int i = 0; i < infos.length; i++) {
				CharacteristicInfo info= infos[i];
				assertNotNull(info);
	
				if ( info.isMeta() ) {
					continue;
				}
					
				
				Object value= property.getCharacteristic(info.getName());
				
				System.out.println(info.getName()+" '"+value+"' "+(value!=null ? value.getClass().getName() : ""));
				
				//assertNotNull("'"+info.getName()+"' is null",value);
				//assertTrue("'"+info.getName()+"' is "+value.getClass().getName(), info.getType().isAssignableFrom(value.getClass()));
				
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
}
