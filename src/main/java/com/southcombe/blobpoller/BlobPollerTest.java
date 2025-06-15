/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package com.southcombe.blobpoller;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author aaron southcombe
 */
public class BlobPollerTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Blob Polling Component Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            
            BlobPollingComponent blobComponent = new BlobPollingComponent();
            blobComponent.addBlobChangeListener(event -> {
                BlobPollingComponent.BlobFileInfo fileInfo = event.getFileInfo();
                String message = String.format("New blob detected: %s (Size: %d bytes, Time: %s)",
                    fileInfo.getName(), fileInfo.getSize(), fileInfo.getUploadDateTime());
                JOptionPane.showMessageDialog(frame, message, "Blob Change", JOptionPane.INFORMATION_MESSAGE);
            });
            
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(blobComponent, BorderLayout.CENTER);
            
            JPanel controlPanel = new JPanel();
            JTextField connectionField = new JTextField(20);
            JTextField containerField = new JTextField(10);
            JCheckBox runningCheckBox = new JCheckBox("Running");
            JSpinner intervalSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 3600, 1));
            
            controlPanel.add(new JLabel("Connection:"));
            controlPanel.add(connectionField);
            controlPanel.add(new JLabel("Container:"));
            controlPanel.add(containerField);
            controlPanel.add(runningCheckBox);
            controlPanel.add(new JLabel("Interval:"));
            controlPanel.add(intervalSpinner);
            
            connectionField.addActionListener(e -> {
                blobComponent.setConnectionString(connectionField.getText());
                System.out.println("Connection set: " + connectionField.getText());
            });
            containerField.addActionListener(e -> {
                blobComponent.setContainerName(containerField.getText());
                System.out.println("Container set: " + containerField.getText());
            });
            runningCheckBox.addActionListener(e -> {
                blobComponent.setRunning(runningCheckBox.isSelected());
                System.out.println("Running set: " + runningCheckBox.isSelected());
            });
            intervalSpinner.addChangeListener(e -> {
                blobComponent.setPollingInterval((Integer) intervalSpinner.getValue());
                System.out.println("Interval set: " + intervalSpinner.getValue());
            });

            JButton setButton = new JButton("Set Values");
            setButton.addActionListener(e -> {
                blobComponent.setConnectionString(connectionField.getText());
                blobComponent.setContainerName(containerField.getText());
                blobComponent.setPollingInterval((Integer) intervalSpinner.getValue());
                System.out.println("Values set manually - Connection: " + !connectionField.getText().isEmpty() +
                                 ", Container: " + !containerField.getText().isEmpty());
            });

            JButton startButton = new JButton("START POLLING");
            startButton.addActionListener(e -> {
                blobComponent.setConnectionString(connectionField.getText());
                blobComponent.setContainerName(containerField.getText());
                blobComponent.setPollingInterval((Integer) intervalSpinner.getValue());
                blobComponent.setRunning(true);
                runningCheckBox.setSelected(true);
                System.out.println("MANUALLY STARTED POLLING");
            });

            controlPanel.add(setButton);
            controlPanel.add(startButton);
            
            panel.add(controlPanel, BorderLayout.SOUTH);
            frame.add(panel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
