#!/usr/bin/env python3
import sys
import datetime
from azure.storage.blob import BlobServiceClient

connection_string = 'DefaultEndpointsProtocol=http;AccountName=devstoreaccount1;AccountKey=Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==;BlobEndpoint=http://127.0.0.1:10000/devstoreaccount1;'

client = BlobServiceClient.from_connection_string(connection_string)
container_name = 'test-container'

timestamp = datetime.datetime.now().strftime("%H%M%S")
filename = f"test-{timestamp}.txt"
content = f"Test file uploaded at {datetime.datetime.now()}"

blob_client = client.get_blob_client(container=container_name, blob=filename)
blob_client.upload_blob(content, overwrite=True)

print(f"Uploaded: {filename}")
