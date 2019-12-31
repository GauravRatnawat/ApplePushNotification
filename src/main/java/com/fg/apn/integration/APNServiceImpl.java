package com.fg.apn.integration;

import java.util.List;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationBigPayload;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;

import org.apache.log4j.BasicConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class APNServiceImpl implements IAPNService {

	private static final Logger LOGGER = LogManager.getLogger(APNServiceImpl.class);
	
    @Override
    public boolean apnNotification(String json, String app, Boolean sendProductionNotification, String[] iphoneRegId,String certificate, String certPassword) {
        try {
            BasicConfigurator.configure();
            PushNotificationBigPayload payload = PushNotificationBigPayload.fromJSON(json);
           
            while(payload.getPayloadSize() >= payload.getMaximumPayloadSize() && payload.getPayload().get("aps") != null){
                JSONObject apsJson = (JSONObject)payload.getPayload().get("aps");
                JSONObject alertJson = (JSONObject)apsJson.get("alert");
                String notificationBody = (String)alertJson.get("body");
                notificationBody = notificationBody.substring(0, 3*notificationBody.length()/4).trim()+"...";
                payload.addCustomAlertBody(notificationBody);
            }
            List<PushedNotification> NOTIFICATIONS = Push.payload(payload, APNServiceImpl.class.getResourceAsStream(certificate), certPassword, sendProductionNotification, iphoneRegId);
            
            for (PushedNotification NOTIFICATION : NOTIFICATIONS) {
                if (NOTIFICATION.isSuccessful()) {
                    LOGGER.info("PUSH NOTIFICATION SENT SUCCESSFULLY TO: " + NOTIFICATION.getDevice().getToken());
                } else {
                    String INVALIDTOKEN = NOTIFICATION.getDevice().getToken();
                    /*
                     * ADD CODE HERE TO REMOVE INVALIDTOKEN FROM YOUR
                     * DATABASE
                     */
                    /* FIND OUT MORE ABOUT WHAT THE PROBLEM WAS */
                    LOGGER.error("THEPROBLEM while apn notification:-"+NOTIFICATION.getException() +"for token:-"+INVALIDTOKEN);
                    /*
                     * IF THE PROBLEM WAS AN ERROR-RESPONSE PACKET RETURNED
                     * BY APPLE, GET IT
                     */
                    ResponsePacket THEERRORRESPONSE = NOTIFICATION.getResponse();
                    if (THEERRORRESPONSE != null) {
                        LOGGER.info("THE ERROR status:-"+THEERRORRESPONSE.getStatus()+"THE ERROR RESPONSE:-"+THEERRORRESPONSE.getMessage());
                    }
                }
            }
            return true;
        } catch (CommunicationException | KeystoreException |JSONException e) {
            LOGGER.error("Exception in apn WITH DETAILS :- "+e.getMessage());
        }  catch (Exception e) {
            LOGGER.error("Exception in apn WITH DETAILS :- "+e.getMessage());
        }
        return false;
    }
}
