package de.spline.uves.ndef;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class SelectTest extends NdefTestCase {

	public void testSelect() throws Exception {
		byte[] testBytes = {(byte) 0x01, (byte) 0xF7, (byte) 0x32};
		assertEquals(testBytes, stringToBytes("01 F7 32 "));
	}
}
