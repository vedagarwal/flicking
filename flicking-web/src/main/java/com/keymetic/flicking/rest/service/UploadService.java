package com.keymetic.flicking.rest.service;

import java.io.IOException;
import java.util.Date;

import org.jclouds.ContextBuilder;
import org.jclouds.io.Payload;
import org.jclouds.io.Payloads;
import org.jclouds.openstack.swift.v1.features.ObjectApi;
import org.jclouds.rackspace.cloudfiles.v1.CloudFilesApi;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.ImmutableMap;

@Service
public class UploadService {
	public static final String PROVIDER = System.getProperty("provider.cf", "rackspace-cloudfiles-us");
	public static final String REGION = System.getProperty("region", "HKG");	
	public static final String CONTAINER = "FlickingImages";
	private final CloudFilesApi cloudFiles;
	private final String cdnURI="cdn.flickingtruth.com";
	
	
	
	public UploadService() {
		this.cloudFiles = ContextBuilder.newBuilder(PROVIDER).credentials("vedurocks", "ccfb068bc4bfac6bdb771c12d0c37df9")
	            .buildApi(CloudFilesApi.class);;
	     //this.cdnURI = cloudFiles.cdnApiInRegion(REGION).get(CONTAINER).getUri().toString();
	}



	public String uploadFile(MultipartFile file) throws IOException{		
		Date date = new Date();
		String imageName = date.getTime()+file.getOriginalFilename();
	      ObjectApi objectApi = cloudFiles.objectApiInRegionForContainer(REGION, CONTAINER);
	      Payload payload = Payloads.newByteArrayPayload(file.getBytes());	     
	      objectApi.replace(imageName, payload, ImmutableMap.<String, String>of());		
		return cdnURI+"/"+imageName;
	}
	
	public void deleteFile(String url) throws IOException{
		 ObjectApi objectApi = cloudFiles.objectApiInRegionForContainer(REGION, CONTAINER);
		 String[] parts = null;
		 if(url !=null){
			 parts = url.split(cdnURI+"/");
			 objectApi.delete(parts[1]);
		 }		
		
	}
	

}
