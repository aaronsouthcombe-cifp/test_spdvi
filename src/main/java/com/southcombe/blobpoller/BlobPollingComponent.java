/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.southcombe.blobpoller;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import java.awt.BorderLayout;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author aaron southcombe
 */
public class BlobPollingComponent extends JPanel {

    private String connectionString = "";
    private String containerName = "";
    private boolean running = false;
    private int pollingInterval = 30;
    private Timer timer;
    private List<BlobFileInfo> currentBlobs = new ArrayList<>();
    private List<BlobChangeListener> listeners = new ArrayList<>();

    /**
     * Creates new BlobPollingComponent
     */
    public BlobPollingComponent() {
        super();
        initComponents();
        setupTimer();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        JLabel iconLabel = new JLabel("📊 Blob Polling");
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        iconLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        add(iconLabel, BorderLayout.CENTER);
    }
    
    private void setupTimer() {
        timer = new Timer(pollingInterval * 1000, e -> pollBlobs());
        timer.setRepeats(true);
    }
    
    public String getConnectionString() {
        return connectionString;
    }
    
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
    
    public String getContainerName() {
        return containerName;
    }
    
    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    public void setRunning(boolean running) {
        this.running = running;
        if (running && !connectionString.isEmpty() && !containerName.isEmpty()) {
            System.out.println("Starting timer - polling every " + pollingInterval + " seconds");
            timer.start();
        } else {
            System.out.println("Stopping timer");
            timer.stop();
        }
    }
    
    public int getPollingInterval() {
        return pollingInterval;
    }
    
    public void setPollingInterval(int pollingInterval) {
        this.pollingInterval = pollingInterval;
        if (timer != null) {
            timer.setDelay(pollingInterval * 1000);
        }
    }
    
    public void addBlobChangeListener(BlobChangeListener listener) {
        listeners.add(listener);
    }
    
    public void removeBlobChangeListener(BlobChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void pollBlobs() {
        if (connectionString.isEmpty() || containerName.isEmpty()) {
            System.out.println("Polling skipped - missing connection string or container name");
            return;
        }

        System.out.println("Polling blobs from container: " + containerName);
        try {
            BlobContainerClient containerClient = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient();
            
            List<BlobFileInfo> newBlobs = new ArrayList<>();
            for (BlobItem blobItem : containerClient.listBlobs()) {
                BlobFileInfo fileInfo = new BlobFileInfo(
                    blobItem.getName(),
                    blobItem.getProperties().getContentType(),
                    blobItem.getProperties().getContentLength(),
                    blobItem.getProperties().getLastModified()
                );
                newBlobs.add(fileInfo);
            }
            
            for (BlobFileInfo newBlob : newBlobs) {
                if (!currentBlobs.contains(newBlob)) {
                    fireBlobChangeEvent(newBlob);
                }
            }
            
            currentBlobs = newBlobs;
            
        } catch (Exception e) {
            System.err.println("Error polling blobs: " + e.getMessage());
        }
    }
    
    private void fireBlobChangeEvent(BlobFileInfo fileInfo) {
        BlobChangeEvent event = new BlobChangeEvent(this, fileInfo);
        for (BlobChangeListener listener : listeners) {
            listener.blobChanged(event);
        }
    }
    
    public static class BlobFileInfo {
        private final String name;
        private final String mimeType;
        private final long size;
        private final OffsetDateTime uploadDateTime;
        
        public BlobFileInfo(String name, String mimeType, long size, OffsetDateTime uploadDateTime) {
            this.name = name;
            this.mimeType = mimeType;
            this.size = size;
            this.uploadDateTime = uploadDateTime;
        }
        
        public String getName() { return name; }
        public String getMimeType() { return mimeType; }
        public long getSize() { return size; }
        public OffsetDateTime getUploadDateTime() { return uploadDateTime; }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            BlobFileInfo that = (BlobFileInfo) obj;
            return name.equals(that.name) && size == that.size && 
                   uploadDateTime.equals(that.uploadDateTime);
        }
        
        @Override
        public int hashCode() {
            return name.hashCode() + Long.hashCode(size) + uploadDateTime.hashCode();
        }
    }
    
    public static class BlobChangeEvent extends EventObject {
        private final BlobFileInfo fileInfo;
        
        public BlobChangeEvent(Object source, BlobFileInfo fileInfo) {
            super(source);
            this.fileInfo = fileInfo;
        }
        
        public BlobFileInfo getFileInfo() {
            return fileInfo;
        }
    }
    
    public interface BlobChangeListener extends EventListener {
        void blobChanged(BlobChangeEvent event);
    }
}
