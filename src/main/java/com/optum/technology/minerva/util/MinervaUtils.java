package com.optum.technology.minerva.util;

import java.io.File;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.optum.technology.minerva.entity.UserInfo;
import com.optum.technology.minerva.entity.UserRole;

public class MinervaUtils {

	public static LocalDate dateParser(String date) {
		String parsed[] = date.split("/");
		int m = Integer.parseInt(parsed[0]);
		int d = Integer.parseInt(parsed[1]);
		int y = Integer.parseInt(parsed[2]);
		LocalDate cal = new LocalDate(y, m, d);
		return cal;
	}

	public static boolean hasRole(UserInfo emp, String role) {
		for (UserRole role_type : emp.getUserRole()) {
			if (role.equals(role_type.getRole())) {
				return true;
			}
		}
		return false;
	}

	public static String getStatusEq(byte status) {
		switch (status) {
		case 1:
			return "Open";
		case 2:
			return "Closed";
		default:
			return "New";
		}
	}

	public static LocalDateTime parseDate(String startDate) {
		String[] startArr = startDate.split("/");
		LocalDateTime start = new LocalDateTime(Integer.parseInt(startArr[2]), Integer.parseInt(startArr[0]),
				Integer.parseInt(startArr[1]), 0, 0);
		return start;
	}

	public static String fileNameAppender(String fileName, String folderDir) {
		File tempFileChecker = new File(folderDir + fileName);

		if (tempFileChecker.exists()) {
			String fix = new LocalDateTime().toString().replace("-", "").replace(" ", "").replace(":", "").replace(".",
					"");

			String[] wordSplit = fileName.split("\\.");
			if (wordSplit.length > 1) {
				String noExt = wordSplit[0];

				String extension = wordSplit[1];
				fileName = noExt + fix + "." + extension;
			} else {
				fileName = fileName + fix;
			}
		}
		return fileName;
	}
}
