/*
	Copyright 2015 Denis Prasetio
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

package com.dotosoft.dotoquiz.command.image;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.Filter;

import com.dotosoft.dotoquiz.command.image.impl.ImageWebClient;
import com.dotosoft.dotoquiz.tools.util.BeanUtils;
import com.dotosoft.dotoquiz.tools.util.SingletonFactory;
import com.dotosoft.dotoquiz.utils.StringUtils;

public class QueryImageCommand implements Filter {

	private String apiKey;
	private String toAlbumCollectionKey;
	private String toPhotoCollectionKey;
	private boolean showAll;

	public void setToAlbumCollectionKey(String toAlbumCollectionKey) {
		this.toAlbumCollectionKey = toAlbumCollectionKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setToPhotoCollectionKey(String toPhotoCollectionKey) {
		this.toPhotoCollectionKey = toPhotoCollectionKey;
	}

	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		ImageWebClient webClient = (ImageWebClient) BeanUtils.getProperty(context, apiKey);
		if(webClient == null) {
			throw new Exception("Image API is not exist!");
		}

		List albumCollections = webClient.getAlbums(showAll);
		if(StringUtils.hasValue(toPhotoCollectionKey)) {
			List photoCollections = new ArrayList();
			for(Object album : albumCollections) {
				photoCollections.add(webClient.getPhotos(album));
			}
			context.put(toPhotoCollectionKey, photoCollections);
		}
		
		context.put(toAlbumCollectionKey, albumCollections);

		return false;
	}

	public boolean postprocess(Context context, Exception exception) {
		if (exception == null) return false;
		exception.printStackTrace();
		System.err.println("Exception " + exception.getMessage() + " occurred.");
		return true;
	}

}
