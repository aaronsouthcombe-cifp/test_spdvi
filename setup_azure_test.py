#!/usr/bin/env python3

import subprocess
import sys
import time

def install_azure_storage():
    try:
        subprocess.run([sys.executable, "-m", "pip", "install", "azure-storage-blob", "--break-system-packages"],
                      check=True, capture_output=True)
        print("Packages installed")
    except subprocess.CalledProcessError as e:
        print(f"Failed to install azure-storage-blob: {e}")
        return False
    return True

def create_container_and_test_blob():
    try:
        from azure.storage.blob import BlobServiceClient
        
        connection_string = "DefaultEndpointsProtocol=http;AccountName=devstoreaccount1;AccountKey=Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==;BlobEndpoint=http://127.0.0.1:10000/devstoreaccount1;"
        
        client = BlobServiceClient.from_connection_string(connection_string)
        
        container_name = "test-container"
        try:
            client.create_container(container_name)
            print("Container created")
        except Exception as e:
            if "ContainerAlreadyExists" in str(e):
                print("Container ready")
            else:
                print(f"Error creating container: {e}")
                return False
        
        blob_client = client.get_blob_client(container=container_name, blob="test-image.jpg")
        test_content = "This is a test blob file for the BlobPollingComponent"
        blob_client.upload_blob(test_content, overwrite=True)
        print("Test blob uploaded")

        print("\nSetup complete")
        print(f"Connection String: {connection_string}")
        print(f"Container Name: {container_name}")
        
        return True
        
    except ImportError:
        print("Azure Storage Blob library not available")
        return False
    except Exception as e:
        print(f"Error setting up Azure test: {e}")
        return False

def upload_test_blob():
    try:
        from azure.storage.blob import BlobServiceClient
        
        connection_string = "DefaultEndpointsProtocol=http;AccountName=devstoreaccount1;AccountKey=Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==;BlobEndpoint=http://127.0.0.1:10000/devstoreaccount1;"
        
        client = BlobServiceClient.from_connection_string(connection_string)
        container_name = "test-container"
        
        import datetime
        timestamp = datetime.datetime.now().strftime("%Y%m%d_%H%M%S")
        blob_name = f"test-file-{timestamp}.txt"
        
        test_content = f"Test blob uploaded at {datetime.datetime.now()}"
        blob_client = client.get_blob_client(container=container_name, blob=blob_name)
        blob_client.upload_blob(test_content, overwrite=True)
        
        print(f"New test blob uploaded: {blob_name}")

    except Exception as e:
        print(f"Error uploading test blob: {e}")

def main():
    if len(sys.argv) > 1 and sys.argv[1] == "upload":
        upload_test_blob()
        return
    
    print("Setting up Azure Blob Storage test environment...")

    if not install_azure_storage():
        return

    print("Waiting for Azurite to be ready...")
    time.sleep(2)

    if create_container_and_test_blob():
        print("Setup complete")

if __name__ == "__main__":
    main()
