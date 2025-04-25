import javacard.framework.AID;
import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.framework.Shareable;
import javacard.security.CryptoException;

import uicc.toolkit.EnvelopeHandler;
import uicc.toolkit.EnvelopeHandlerSystem;
import uicc.toolkit.ProactiveHandler;
import uicc.toolkit.ProactiveHandlerSystem;
import uicc.toolkit.ToolkitConstants;
import uicc.toolkit.ToolkitInterface;
import uicc.toolkit.ToolkitRegistry;
import uicc.toolkit.ToolkitRegistrySystem;

import org.globalplatform.Personalization;

public class Main extends Applet implements Personalization, ToolkitInterface, ToolkitConstants {

    private
    Main(byte[] parameters, short offset, byte length) {
        register(parameters, (short) (offset + 1), parameters[offset]);
    }

    public static void
    install(byte[] parameters, short offset, byte length) {
        new Main(parameters, offset, length);
    }

    public void
    process(APDU apdu) throws ISOException { }

    public short
    processData(byte[] inBuffer, short inOffset, short inLength, byte[] outBuffer, short outOffset)
    {
        /* TODO: add personalization data processing machinery. */
        return 0x6f00;
    }

    public void processToolkit(short event) {

    }

    public Shareable getshareableInterfaceObject(AID aid, byte param)
    {
        if ((null == aid) && (byte)1 == param)
            return this;
        return null;
    }
}
