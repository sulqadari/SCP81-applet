import javacard.framework.AID;
import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISOException;
import javacard.framework.Shareable;

import uicc.toolkit.ToolkitInterface;
import uicc.toolkit.ToolkitRegistry;
import uicc.toolkit.ToolkitRegistrySystem;

import uicc.usim.toolkit.ToolkitConstants;
import uicc.usim.toolkit.USATEnvelopeHandler;
import uicc.usim.toolkit.USATEnvelopeHandlerSystem;

import org.globalplatform.Personalization;

public class Main extends Applet implements Personalization, ToolkitInterface, ToolkitConstants {

    private
    Main(byte[] parameters, short offset, byte length)
    {
        
    }

    public static void
    install(byte[] parameters, short offset, byte length)
    {
        Main thisApp = new Main(parameters, offset, length);
        thisApp.register(parameters, (short) (offset + 1), parameters[offset]);
        
        ToolkitRegistry regEntry = ToolkitRegistrySystem.getEntry();
        regEntry.setEvent(EVENT_FORMATTED_SMS_PP_ENV);
		regEntry.setEvent(EVENT_FORMATTED_SMS_PP_UPD);
    }

    public void
    process(APDU apdu) throws ISOException { }

    public short
    processData(byte[] inBuffer, short inOffset, short inLength, byte[] outBuffer, short outOffset)
    {
        /* TODO: add personalization data processing machinery. */
        return 0x6f00;
    }

    public void processToolkit(short event)
    {
        USATEnvelopeHandler envHdlr = USATEnvelopeHandlerSystem.getTheHandler();
        short off, len;
        
        switch(event)
        {
            case EVENT_FORMATTED_SMS_PP_ENV:
            case EVENT_FORMATTED_SMS_PP_UPD:
                off = envHdlr.getSecuredDataOffset();
                len = envHdlr.getSecuredDataLength();
                /* TODO: parse incoming SMS and then trigger the HTTPs session */
            break;
            case EVENT_PROFILE_DOWNLOAD:
                
            break;
        }
    }

    public Shareable getshareableInterfaceObject(AID aid, byte param)
    {
        if ((null == aid) && (byte)1 == param)
            return this;
        return null;
    }
}
