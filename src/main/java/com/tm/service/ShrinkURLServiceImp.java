package com.tm.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tm.binding.URLBinding;
import com.tm.entity.UrlInfo;
import com.tm.repo.UrlRepository;

@Service
public class ShrinkURLServiceImp implements IShrinkURLService {

	@Autowired
	private UrlRepository urlrepo;

	@Override
	public String getTempUrl(URLBinding binding) {
		UrlInfo info = new UrlInfo();
		info.setOriginalUrl(binding.getOriginalUrl());

		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		LocalDateTime currentDateTime = currentTimestamp.toLocalDateTime();
		LocalDateTime updatedDateTime = currentDateTime.plus(5, ChronoUnit.MINUTES); // add 5 min to the current
																						// DateAndTime
		Timestamp updatedTimestamp = Timestamp.valueOf(updatedDateTime);

		info.setTillTime(updatedTimestamp);
		String duplicateUrl = generateRandomWord();
		info.setDuplicateUrl(duplicateUrl);

		urlrepo.save(info); // saving data
		return "http://localhost:8009/ShrinkURL/request/" + duplicateUrl;
	}

	// creating a random word as temp url
	private String generateRandomWord() {
		int length = 6;
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		StringBuilder stringBuilder = new StringBuilder();

		Random random = new Random();
		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(alphabet.length());
			char randomChar = alphabet.charAt(randomIndex);
			stringBuilder.append(randomChar);
		}

		return stringBuilder.toString();
	}

	@Override
	public String getOriginalUrl(String tempUrl) {
		UrlInfo info = urlrepo.findByDuplicateUrl(tempUrl);

		if (info != null) {

			if (checkRequestValidity(info)) {
				return info.getOriginalUrl();
			} else {

				urlrepo.deleteById(info.getId());
				System.out.println("");
				return null;
			}
		}
		return null;
	}

	// Checking whether link is expired or not
	private boolean checkRequestValidity(UrlInfo info) {
		Timestamp time = info.getTillTime();
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());

		if (time.after(currentTime)) {

			return true;
		} else if (time.before(currentTime)) {

			return false;
		} else {
			return true;
		}
	}
}
