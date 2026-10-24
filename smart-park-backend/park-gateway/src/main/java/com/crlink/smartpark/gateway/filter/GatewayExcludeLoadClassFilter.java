package com.crlink.smartpark.gateway.filter;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * <p>
 * 排除需要加载的类
 * </p>
 * */
public class GatewayExcludeLoadClassFilter implements TypeFilter {

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        return metadataReader.getClassMetadata().getClassName().contains("MybatisPlusConfig")
//                || metadataReader.getClassMetadata().getClassName().contains("SwaggerConfig")
                || metadataReader.getClassMetadata().getClassName().contains("CustomMetaObjectHandler");
    }
}
