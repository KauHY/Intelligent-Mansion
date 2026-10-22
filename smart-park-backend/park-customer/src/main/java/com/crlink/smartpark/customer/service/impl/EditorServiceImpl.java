package com.crlink.smartpark.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.customer.entity.Editor;
import com.crlink.smartpark.customer.mapper.EditorMapper;
import com.crlink.smartpark.customer.service.EditorService;
import org.springframework.stereotype.Service;

@Service
public class EditorServiceImpl extends ServiceImpl<EditorMapper, Editor> implements EditorService {
}
