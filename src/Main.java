import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.security.CryptoException;
import org.globalplatform.Personalization;

public class Main extends Applet implements Personalization {

    private
    Main(byte[] parameters, short offset, byte length) {
        register(parameters, (short) (offset + 1), parameters[offset]);
    }

    public static void
    install(byte[] parameters, short offset, byte length) {
        new Main(parameters, offset, length);
    }

    public void
    process(APDU apdu) throws ISOException {
        if (selectingApplet())
            return;
        byte[] buffer = apdu.getBuffer();
        if (buffer[ISO7816.OFFSET_LC] != (short) 6)
            CryptoException.throwIt((short) 0x6666);
        apdu.setIncomingAndReceive();
        apdu.setOutgoingAndSend((short) 0, (short) 11);
    }

    public short
    processData(byte[] inBuffer, short inOffset, short inLength, byte[] outBuffer, short outOffset)
    {
        /* TODO: add personalization data processing machinery. */
        return 0x6f00;
    }
}
