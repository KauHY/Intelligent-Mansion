//import com.crlink.itech.aiot.jeecgframework.poi.word.WordExportUtil;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.util.*;
//
///**
// * @Description: TODO
// */
//public class DaoChuWordTest {
//    private static final String basePath = "d:\\daima\\poi\\";
////    private static final String basePath = "d:\\daima\\poi\\autopoi-web\\src\\test\\resources\\templates\\";
//
//    public static void main(String[] args) throws Exception {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String curTime = format.format(new Date());
//
//        Map<String, Object> map = new HashMap<String, Object>();
//        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
////        Map<String, Object> map1 = new HashMap<String, Object>();
////        map1.put("type", "个人所得税");
////        map1.put("presum", "1580");
////        map1.put("thissum", "1750");
////        map1.put("curmonth", "1-11月");
////        map1.put("now", curTime);
////        mapList.add(map1);
////        Map<String, Object> map2 = new HashMap<String, Object>();
////        map2.put("type", "增值税");
////        map2.put("presum", "1080");
////        map2.put("thissum", "1650");
////        map2.put("curmonth", "1-11月");
////        map2.put("now", curTime);
////        mapList.add(map2);
//
//        map.put("totalpreyear", "2660");
//        map.put("totalthisyear", "3400");
//        map.put("编号", "ht-1234-7890");
//        map.put("甲方", "淮安智慧谷");
//        map.put("邮编", "514200");
//        map.put ("电话","1347181805");
//        map.put("甲方联系人", "林总");
//        map.put("乙方", "华润数科有限公司");
//        map.put("乙方联系人", "林波总");
//        map.put ("乙方联系电话","1347180005");
//        map.put("乙方联系人身份证号码", "51420013471800051347180005");
//        map.put ("电话","1347181805");
//        map.put("关联房屋", "2栋8楼");
//        map.put("房屋用途", "商用");
//        map.put("建筑面积", "1228.20");
//        map.put("合同开始日期",curTime);
//        map.put("合同结束日期", LocalDate.now().plusDays(30));
//        map.put ("签约日期", LocalDate.now().plusDays(-30));
//        map.put("type", "增值税");
//        map.put("presum", "10801");
//
//        map.put("thissum", "1650");
//        map.put("curmonth", "1-12月");
//        map.put("curmonth1", "1-11月");
//        map.put("now", curTime);
//
//        //单列
//        XWPFDocument document = WordExportUtil.exportWord07(basePath + "租赁合同.docx", map);
//        File savefile = new File("d:\\daima\\poi");
//        if (!savefile.exists()) {
//            savefile.mkdirs();
//        }
//        FileOutputStream fos = new FileOutputStream(basePath + "纳税信息new11.docx");
//        document.write(fos);
//        fos.close();
//    }
//}
