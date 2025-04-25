package inter.galaxy.me;

import javacard.framework.AID;
import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISOException;
import javacard.framework.Shareable;
import uicc.toolkit.ProactiveHandler;
import uicc.toolkit.ProactiveHandlerSystem;
import uicc.toolkit.ProactiveResponseHandler;
import uicc.toolkit.ProactiveResponseHandlerSystem;
import uicc.toolkit.ToolkitInterface;
import uicc.toolkit.ToolkitRegistry;
import uicc.toolkit.ToolkitRegistrySystem;

import uicc.usim.toolkit.ToolkitConstants;
import uicc.usim.toolkit.USATEnvelopeHandler;
import uicc.usim.toolkit.USATEnvelopeHandlerSystem;

import org.globalplatform.Personalization;

import org.globalplatform.GPSystem;
import org.globalplatform.GlobalService;
import org.globalplatform.HTTPAdministration;
import org.globalplatform.HTTPReportListener;

public class Main extends Applet implements Personalization, ToolkitInterface, ToolkitConstants
{
	private short toolkitExceptionReason;
	private byte[] data;
	private TriggerSession trigger;

    public Main()
	{
		data = new byte[128];
		trigger = new TriggerSession(); 
	}

    public static void
    install(byte[] parameters, short offset, byte length)
    {
        Main thisApp = new Main();
        thisApp.register(parameters, (short) (offset + 1), parameters[offset]);
        
        ToolkitRegistry regEntry = ToolkitRegistrySystem.getEntry();
        regEntry.setEvent(EVENT_FORMATTED_SMS_PP_ENV);
		regEntry.setEvent(EVENT_FORMATTED_SMS_PP_UPD);
    }

    public short
    processData(byte[] inBuffer, short inOffset, short inLength, byte[] outBuffer, short outOffset)
    {
		short offset = outOffset;
		outBuffer[offset++] = (byte)0x74;
		outBuffer[offset++] = (byte)0x65;
		outBuffer[offset++] = (byte)0x73;
		outBuffer[offset++] = (byte)0x74;
        outBuffer[offset++] = (byte)0x90;
		outBuffer[offset++] = (byte)0x00;
        return (short)(offset - outOffset);
    }

    public void
	processToolkit(short event)
    {
        USATEnvelopeHandler envHdlr = USATEnvelopeHandlerSystem.getTheHandler();
		ProactiveHandler proHdlr = ProactiveHandlerSystem.getTheHandler();
        ProactiveResponseHandler proRespHdlr = ProactiveResponseHandlerSystem.getTheHandler();
        short off, len;
        
        switch(event)
        {
            case EVENT_FORMATTED_SMS_PP_ENV:
            case EVENT_FORMATTED_SMS_PP_UPD: {
                off = envHdlr.getSecuredDataOffset();
                len = envHdlr.getSecuredDataLength();
				proHdlr.clear();
				envHdlr.copyValue((short)0, data, off, len);
                /* TODO: parse incoming SMS and then trigger the HTTPs session */
				try
				{
					trigger.triggerSession(data, (short)0, len);
				}
				
				catch(ISOException e)
				{
					toolkitExceptionReason = e.getReason();
				}
			} break;
            case EVENT_PROFILE_DOWNLOAD:
                
            break;
        }
    }

    public Shareable
	getshareableInterfaceObject(AID aid, byte param)
    {
        if ((null == aid) && (byte)1 == param)
            return this;
        return null;
    }

	public void
    process(APDU apdu) throws ISOException { }
}

class TriggerSession implements HTTPReportListener
{

	private static short reportListenerState;
	
	public void httpAdministationSessionReport(short status)
	{
		reportListenerState = status;
	}
	   
    public void triggerSession(byte[] data, short dataOffset, short dataLength )
    {
		final short FAMILY_HTTP_ADMINISTRATION = (short) (GPSystem.FAMILY_HTTP_ADMINISTRATION << 8);
		
		// get GlobalService object
		GlobalService globalService = GPSystem.getService(null, FAMILY_HTTP_ADMINISTRATION);
		
		// getServiceInterface
		HTTPAdministration httpAdmin = (HTTPAdministration) globalService.getServiceInterface(GPSystem.getRegistryEntry(null), FAMILY_HTTP_ADMINISTRATION, null, (short) 0, (short) 0);

		// trigger HTTP Administration to start Session
		httpAdmin.requestHTTPAdministrationSession(data, dataOffset, dataLength);
    }	
}