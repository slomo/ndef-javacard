package de.spline.uves.ndef;

import com.licel.jcardsim.base.Simulator;
import javacard.framework.AID;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

public class JcardsimTestCase extends NdefTestCase {

        Simulator simulator;
        byte[] aid = stringToBytes("D2 76 00 00 85 01 01");
        AID appletAID = new AID(aid, (short)0, (byte)aid.length);

        public JcardsimTestCase() {
                simulator = new Simulator();
                simulator.installApplet(appletAID, Ndef.class);
                simulator.selectApplet(appletAID);
        }

        public void assertTrace(String[] trace) throws Exception {

                for (int i = 0; i < trace.length; i += 2) {
                        byte[] response = simulator.transmitCommand(stringToBytes(trace[i]));
                        assertEquals(response, stringToBytes(trace[i+1]));
                }
        }
}
