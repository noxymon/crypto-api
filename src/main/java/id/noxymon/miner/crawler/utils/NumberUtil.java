package id.noxymon.miner.crawler.utils;

import org.springframework.util.StringUtils;

public final class NumberUtil {

    public static final Double from(String numberInString){
        if(StringUtils.hasText(numberInString)){
            return Double.valueOf(numberInString);
        }
        return null;
    }
}
