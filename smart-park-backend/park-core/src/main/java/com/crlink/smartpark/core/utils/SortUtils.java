package com.crlink.smartpark.core.utils;

import cn.hutool.core.collection.CollUtil;
import com.crlink.smartpark.system.vo.DictVO;
import org.apache.commons.lang3.StringUtils;

import java.text.Collator;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SortUtils {
    public static <E> List<E> pinYinSort(List<E> list, Function<E, String> function) {
        List<E> hasDigitList = Collections.synchronizedList(new ArrayList<>());
        List<E> result = list.parallelStream().filter(t -> {
            String value = function.apply(t);
            if (StringUtils.isBlank(value)) {
                return false;
            }
            if (Character.isDigit(value.charAt(0))) {
                hasDigitList.add(t);
                return false;
            }
            return true;
        })
                .sorted((v1, v2) -> Collator.getInstance(Locale.CHINA).compare(function.apply(v1), function.apply(v2)))
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(hasDigitList)) {
            hasDigitList.sort(Comparator.comparing(function));
            result.addAll(hasDigitList);
        }
        return result;
    }

    public static void main(String[] args) throws NoSuchMethodException {
        ArrayList<DictVO> list = new ArrayList<>();

        list.add(new DictVO("1百度", "a"));
        list.add(new DictVO("bbb", "a"));
        list.add(new DictVO("aaa", "a"));
        list.add(new DictVO("2阿里", "b"));
        list.add(new DictVO("腾讯", "b"));
        list.add(new DictVO("百度", "b"));
        list.add(new DictVO("阿里", "b"));

        ArrayList<DictVO> vos = new ArrayList<>(1000);
        for (int i = 0; i < 100; i++) {
            Random random = new Random();
            int ik = random.nextInt(7);
            vos.add(list.get(ik));
        }
        long start = System.currentTimeMillis();
        List<DictVO> dictVOList = pinYinSort(vos, DictVO::getLabel);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        //vos.sort(Comparator.comparing(DictVO::getLabel));
        for (DictVO dictVO : dictVOList) {
            System.out.println(dictVO.getLabel());
        }
    }

}

