package com.fg.apn.integration;

public interface IAPNService {
    
    boolean apnNotification(String json, String app, Boolean production, String[] reg, String certificate, String certPassword);
}
