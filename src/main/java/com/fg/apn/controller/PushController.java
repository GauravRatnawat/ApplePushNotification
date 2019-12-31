package com.fg.apn.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fg.apn.integration.IAPNService;


@RestController
@RequestMapping(value = "/")
public class PushController {

    @Autowired
    private IAPNService apnService;
    @Autowired
    private Environment env;
    
    private static final Logger LOGGER = LogManager.getLogger(PushController.class);
    
    @RequestMapping(value = "/apn", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    private Boolean apnNotification(@RequestParam String json, @RequestParam String app, @RequestParam Boolean production, @RequestParam String reg) {
        try {
            if(production){
                return  apnService.apnNotification(json, app, production, reg.split(","), env.getProperty("apncertificate."+app+".prod"), env.getProperty("apnnotification."+app+".prod.password"));
            }else{
                return apnService.apnNotification(json, app, production, reg.split(","), env.getProperty("apncertificate."+app+".dev"), env.getProperty("apnnotification."+app+".dev.password"));
            }
        } catch (Exception e) {
            LOGGER.error("Exception while using LDAP:-  ", e);
        }
        return Boolean.FALSE;
    }
}
