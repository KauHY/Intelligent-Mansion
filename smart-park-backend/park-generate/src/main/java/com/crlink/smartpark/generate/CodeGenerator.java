package com.crlink.smartpark.generate;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeGenerator {

    public static void main(String[] args) {
        // 1、创建代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 2、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        // 模块名称
        // TODO 显示的路径名称，也是模块定义，这个根据需要修改
        String moduleName = "visitor";
        //String moduleName = "contract";
        //String moduleName = "system";
        //String moduleName = "customer";
        //String moduleName = "device";
        //String moduleName = "common";
//        String moduleNameCn = "合同管理";
        String daoCodePath = StringUtils.join("park-", moduleName, "/src/main/java");
        String mapperPath = StringUtils.join("park-", moduleName, "/src/main/resources/mapper/");
        String reqPath = StringUtils.join("park-", moduleName, "/src/main/java/com/crlink/smartpark/" + moduleName + "/model/");
        String feignPath = StringUtils.join("park-api", "/src/main/java/com/crlink/smartpark/" + moduleName);
        String entityPath = StringUtils.join("park-api", "/src/main/java/com/crlink/smartpark/" + moduleName);

        gc.setOutputDir(daoCodePath);
        //作者
        // TODO 这里需要修改
        gc.setAuthor("wanglingqiang");
        //生成后是否打开资源管理器
        gc.setOpen(false);
        //重新生成时文件是否覆盖
        gc.setFileOverride(true);
        //去掉Service接口的首字母I
        gc.setServiceName("%sService");
        // id由系统生成，采用雪花算法生成
        gc.setIdType(IdType.ASSIGN_ID);
        //定义生成的实体类中日期类型
        gc.setDateType(DateType.TIME_PACK);
        //开启Swagger2模式（Swagger和项目整合用于请求测试）
        gc.setSwagger2(true);

        mpg.setGlobalConfig(gc);

        // TODO 3、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://10.200.9.28:8016/smart-park?characterEncoding=utf8&serverTimezone=Asia/Shanghai");//mysql8的写法要加时区
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("aiot202207#");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 4、包配置
        PackageConfig pc = new PackageConfig();
//        pc.setModuleName(moduleName); //模块名
        pc.setParent("com.crlink.smartpark");
        pc.setController(moduleName + ".controller");
        pc.setEntity(moduleName + ".entity");
        pc.setService(moduleName + ".service");
        pc.setServiceImpl(moduleName + ".service.impl");
        pc.setMapper(moduleName + ".mapper");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                //这些自定义的值在vm模板的语法是通过${cfg.xxx}来调用的。
                Map<String, Object> map = new HashMap<>();
                // 这些字段是用来定义feign接口的路径的
                // TODO 以下两项需要修改，根据不同的模块和路径进行修改
                //map.put("serverName", "park-customer");
                map.put("serverName", "park-visitor");
                map.put("serverPath", "/park-visitor");
//                map.put("serverName", "park-contract");
                //map.put("serverName", "park-business");
                //map.put("serverPath", "/park-contract");
//                map.put("moduleNameCn", moduleNameCn);
                this.setMap(map);
            }
        };
        // mapper模板
        String mapperTemplatePath = "/templates/mapper.xml.vm";
        // service模板
        //String feignTemplatePath = "/templates/feign.java.vm";
        // entityDTO模板
        String entityDtoTemplatePath = "/templates/entityDTO.java.vm";
//        String entityVOTemplatePath = "/templates/entityVO.java.vm";
//        String entityListVOTemplatePath = "/templates/entityListVO.java.vm";
        // entityQuery模板
        //String entityQueryTemplatePath = "/templates/entityQuery.java.vm";
        //String entityReqTemplatePath = "/templates/entityReqDTO.java.vm";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // mapper配置
        FileOutConfig mapperConfig = new FileOutConfig(mapperTemplatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return mapperPath + File.separator + tableInfo.getEntityName() + "Mapper.xml";
            }
        };
        // feign配置
        //FileOutConfig feignConfig = new FileOutConfig(feignTemplatePath) {
        //    @Override
        //    public String outputFile(TableInfo tableInfo) {
        //        // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
        //        return feignPath + File.separator + "feign" + File.separator + tableInfo.getEntityName() + "Provider.java";
        //    }
        //};
        // entityDTO配置
        FileOutConfig entityDtoConfig = new FileOutConfig(entityDtoTemplatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return entityPath + File.separator + "dto" + File.separator + tableInfo.getEntityName() + "DTO.java";
            }
        };
//        FileOutConfig entityVOConfig = new FileOutConfig(entityVOTemplatePath) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
//                return entityPath + File.separator + "vo" + File.separator + tableInfo.getEntityName() + "VO.java";
//            }
//        };
//        FileOutConfig entityListVOConfig = new FileOutConfig(entityListVOTemplatePath) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
//                return entityPath + File.separator + "vo" + File.separator + tableInfo.getEntityName() + "ListVO.java";
//            }
//        };
        // entityQuery配置
        //FileOutConfig entityQueryConfig = new FileOutConfig(entityQueryTemplatePath) {
        //    @Override
        //    public String outputFile(TableInfo tableInfo) {
        //        // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
        //        return reqPath + File.separator + "query" + File.separator + tableInfo.getEntityName() + "Query.java";
        //    }
        //};
        //FileOutConfig entityReqConfig = new FileOutConfig(entityReqTemplatePath) {
        //    @Override
        //    public String outputFile(TableInfo tableInfo) {
        //        // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
        //        return reqPath + File.separator + tableInfo.getEntityName() + "ReqDTO.java";
        //    }
        //};
        // mapper输出路径
        focList.add(mapperConfig);
        //focList.add(feignConfig);
        focList.add(entityDtoConfig);
//        focList.add(entityVOConfig);
//        focList.add(entityListVOConfig);
//        focList.add(entityQueryConfig);
//        focList.add(entityReqConfig);
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
//        templateConfig.setService("templates/service.java");
//        templateConfig.setServiceImpl("templates/serviceImpl.java");
        //templateConfig.setEntity(null);
        mpg.setTemplate(templateConfig);

        // 5、策略配置
        StrategyConfig strategy = new StrategyConfig();
        //填写表名，帮助生成实体类等mapper相应代码
        // TODO 准备生成的表名称
        //strategy.setInclude("sys_business_menu");
        strategy.setInclude("vis_policy_employee");
//        strategy.setInclude("mee_meeting_reserve");
        //数据库表映射到实体的命名策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //生成实体时去掉表
        // TODO 前缀名称，生成的类名会去掉前缀
        //strategy.setTablePrefix("sys_");
        strategy.setTablePrefix("vis_");
        //数据库表字段映射到实体的命名策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // lombok 模型 @Accessors(chain = true) setter链式操作
        strategy.setEntityLombokModel(true);
        //restful api风格控制器
        strategy.setRestControllerStyle(true);
        //url中驼峰转连字符
        strategy.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(strategy);
        // 6、执行
        mpg.execute();
    }
}
