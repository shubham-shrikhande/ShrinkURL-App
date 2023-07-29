package com.tm.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.entity.UrlInfo;

public interface UrlRepository extends JpaRepository<UrlInfo, Integer> {

	public UrlInfo findByDuplicateUrl(String tempUrl);
}
