package de.spline.uves.ndef;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

abstract public class NdefTestCase extends TestCase {

	public static String bytesToString(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			buffer.append(Integer.toHexString(bytes[i] & 0xF0).charAt(0));
			buffer.append(Integer.toHexString(bytes[i] & 0x0F));
		}
		return buffer.toString().toUpperCase();
	}

	public static byte[] stringToBytes(String str)
			throws UnsupportedOperationException {
		String hexstr = str.replaceAll("[^A-Z0-9]+", "");

		if ((hexstr.length() % 2) != 0) {
			throw new UnsupportedOperationException(
					"Length of string must be even");
		}

		byte[] bytes = new byte[hexstr.length() / 2];
		for (int i = 0; i < hexstr.length(); i += 2) {
			byte value = (byte) Integer
					.parseInt(hexstr.substring(i, i + 2), 16);
			bytes[i / 2] = value;
		}

		return bytes;
	}

	public static void assertEquals(byte[] that, byte[] those) {
		assertEquals(bytesToString(that), bytesToString(those));
	}
}
