/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.southcombe.blobpoller;

import java.beans.*;

/**
 *
 * @author aaron southcombe
 */
public class BlobPollingComponentBeanInfo extends SimpleBeanInfo {
    
    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor connectionStringDesc = 
                new PropertyDescriptor("connectionString", BlobPollingComponent.class);
            connectionStringDesc.setDisplayName("Connection String");
            connectionStringDesc.setShortDescription("Azure Blob Storage connection string");
            
            PropertyDescriptor containerNameDesc = 
                new PropertyDescriptor("containerName", BlobPollingComponent.class);
            containerNameDesc.setDisplayName("Container Name");
            containerNameDesc.setShortDescription("Name of the blob container");
            
            PropertyDescriptor runningDesc = 
                new PropertyDescriptor("running", BlobPollingComponent.class);
            runningDesc.setDisplayName("Running");
            runningDesc.setShortDescription("Whether the component is actively polling");
            
            PropertyDescriptor pollingIntervalDesc = 
                new PropertyDescriptor("pollingInterval", BlobPollingComponent.class);
            pollingIntervalDesc.setDisplayName("Polling Interval");
            pollingIntervalDesc.setShortDescription("Polling interval in seconds");
            
            return new PropertyDescriptor[] { 
                connectionStringDesc, 
                containerNameDesc,
                runningDesc,
                pollingIntervalDesc
            };
        } catch (IntrospectionException e) {
            return null;
        }
    }
    
    @Override
    public EventSetDescriptor[] getEventSetDescriptors() {
        try {
            EventSetDescriptor blobChangeDesc = new EventSetDescriptor(
                BlobPollingComponent.class,
                "blobChange",
                BlobPollingComponent.BlobChangeListener.class,
                "blobChanged"
            );
            blobChangeDesc.setDisplayName("Blob Change");
            blobChangeDesc.setShortDescription("Fired when a new blob is detected");
            
            return new EventSetDescriptor[] { blobChangeDesc };
        } catch (IntrospectionException e) {
            return null;
        }
    }
}
