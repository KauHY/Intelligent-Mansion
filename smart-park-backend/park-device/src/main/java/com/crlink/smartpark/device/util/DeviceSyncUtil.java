package com.crlink.smartpark.device.util;

import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.core.utils.DictUtil;
import com.google.common.collect.Sets;
import org.springframework.data.util.Pair;

import java.util.*;

public class DeviceSyncUtil {

    private static final Map<Integer, Set<String>> bziDeviceTypeIccDeviceTypeMap = new HashMap<>();

    private static final Map<String, Set<Integer>> iccDeviceTypeBziDeviceTypeMap = new HashMap<>();

    private static final Map<String, Integer> iccDeviceTypeBziDeviceCategoryMap = new HashMap<>();

    private static final Map<Integer, Set<String>> bziDeviceCategoryIccDeviceTypeMap = new HashMap<>();

    static {
        bziDeviceTypeIccDeviceTypeMap.put(DictUtil.getValue(DictConstant.DeviceType.ACCESS_CONTROL), Sets.newHashSet("8"));
        bziDeviceTypeIccDeviceTypeMap.put(DictUtil.getValue(DictConstant.DeviceType.APARTMENT_ACCESS_CONTROL), Sets.newHashSet("8"));
        bziDeviceTypeIccDeviceTypeMap.put(DictUtil.getValue(DictConstant.DeviceType.BARRIER_ALL_IN_ONE), Sets.newHashSet("35_19"));
        bziDeviceTypeIccDeviceTypeMap.put(DictUtil.getValue(DictConstant.DeviceType.VIDEO_MONITORING), Sets.newHashSet("1_6","1_2"));
        bziDeviceTypeIccDeviceTypeMap.put(DictUtil.getValue(DictConstant.DeviceType.SMART_LOCK), Sets.newHashSet("8"));

        iccDeviceTypeBziDeviceTypeMap.put("8", Sets.newHashSet(
                DictUtil.getValue(DictConstant.DeviceType.ACCESS_CONTROL),
                DictUtil.getValue(DictConstant.DeviceType.APARTMENT_ACCESS_CONTROL),
                DictUtil.getValue(DictConstant.DeviceType.SMART_LOCK)));
        iccDeviceTypeBziDeviceTypeMap.put("35_19", Sets.newHashSet(DictUtil.getValue(DictConstant.DeviceType.BARRIER_ALL_IN_ONE)));
        iccDeviceTypeBziDeviceTypeMap.put("1_6", Sets.newHashSet(DictUtil.getValue(DictConstant.DeviceType.VIDEO_MONITORING)));
        iccDeviceTypeBziDeviceTypeMap.put("1_2", Sets.newHashSet(DictUtil.getValue(DictConstant.DeviceType.VIDEO_MONITORING)));
        iccDeviceTypeBziDeviceCategoryMap.put("8", DictConstant.DeviceCategory.DOOR_ACCESS.getCode());

        bziDeviceCategoryIccDeviceTypeMap.put(DictConstant.DeviceCategory.DOOR_ACCESS.getCode(), Sets.newHashSet("8"));
    }

    public static Pair<List<Integer>, List<String>> getIccDeviceTypesByBziDeviceType(List<Integer> bziTypes) {
        List<Integer> deviceCategorys = new ArrayList<>();
        List<String> deviceTypes = new ArrayList<>();
        for (Integer type : bziTypes) {
            if (!bziDeviceTypeIccDeviceTypeMap.containsKey(type)) {
                continue;
            }
            Set<String> types = bziDeviceTypeIccDeviceTypeMap.get(type);
            for (String tempType : types) {
                if (tempType.contains("_")) {
                    deviceTypes.add(tempType);
                } else {
                    deviceCategorys.add(Integer.parseInt(tempType));
                }
            }
        }
        return Pair.of(deviceCategorys, deviceTypes);
    }

    public static Pair<List<Integer>, List<String>> getIccDeviceTypeByBziDeviceCategory(Integer bziCategory) {
        List<Integer> deviceCategorys = new ArrayList<>();
        List<String> deviceTypes = new ArrayList<>();
        Set<String> types = bziDeviceCategoryIccDeviceTypeMap.get(bziCategory);
        for (String tempType : types) {
            if (tempType.contains("_")) {
                deviceTypes.add(tempType);
            } else {
                deviceCategorys.add(Integer.parseInt(tempType));
            }
        }
        return Pair.of(deviceCategorys, deviceTypes);
    }

    public static Set<Integer> getBziDeviceTypeByIccDeviceType(Integer iccCategory, Integer iccType) {
        Set<Integer> types = iccDeviceTypeBziDeviceTypeMap.get(iccCategory.toString() + "_" + iccType.toString());
        if (types != null) {
            return types;
        }
        return iccDeviceTypeBziDeviceTypeMap.get(iccCategory.toString());
    }

    public static Integer getBziDeviceCategoryByIccDeviceType(Integer iccCategory, Integer iccType) {
        Integer type = iccDeviceTypeBziDeviceCategoryMap.get(iccCategory.toString() + "_" + iccType.toString());
        if (type != null) {
            return type;
        }
        return iccDeviceTypeBziDeviceCategoryMap.get(iccCategory.toString());
    }

}
