package com.tm.service;

import com.tm.binding.URLBinding;

public interface IShrinkURLService {

	public String getTempUrl(URLBinding binding);
    
	public String getOriginalUrl(String tempUrl);
}
