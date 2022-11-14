package com.bjd.smartanalysis.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {
    public static boolean isLegalDate(String sDate) {
        String[] formatStrings = {"yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm"};
        boolean isInvalidFormat = false;
        Date dateObj;
        for (String formatString : formatStrings) {
            try {
                SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateInstance();
                sdf.applyPattern(formatString);
                sdf.setLenient(false);
                dateObj = sdf.parse(sDate);
//                if (sDate.equals(sdf.format(dateObj))) {
//                    isInvalidFormat = false;
//                    break;
//                }
            } catch (ParseException e) {
                isInvalidFormat = true;
            }
        }
        return isInvalidFormat;
    }
}
