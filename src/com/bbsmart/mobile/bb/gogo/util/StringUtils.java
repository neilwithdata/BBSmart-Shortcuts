package com.bbsmart.mobile.bb.gogo.util;

import net.rim.device.api.crypto.SHA1Digest;

public class StringUtils {

	public static long stringToLong(String str) {
		long hashValLong = 0;

		SHA1Digest sha1Digest = new SHA1Digest();

		sha1Digest.update(str.getBytes());

		byte[] hashValBytes = sha1Digest.getDigest();

		for(int i = 0; i < 8; i++) {
		    hashValLong |= ((long)(hashValBytes[i]) & 0x0FF) << (8*i);
		}
		
		return hashValLong;
	}

}
