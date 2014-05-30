package de.spline.uves.ndef.request;

import javacard.framework.*;

public abstract class Request {

        final byte classbyte = ISO7816.CLA_ISO7816;
        byte insbyte;

        public boolean isApplicable(APDU apdu){
                byte buffer[] = apdu.getBuffer();
                
                if (buffer[ISO7816.OFFSET_CLA] != classbyte) {
                        return false;
                }
                 
                if (buffer[ISO7816.OFFSET_INS] != insbyte) {
                        return false;
                }

                return true;
        }             
}
