package org.lucius.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.javafx.iio.ios.IosDescriptor;
import io.swagger.annotations.Api;
import org.lucius.entity.Case_Info;
import org.lucius.mapper.CaseMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "Case API")
public class CaseController {

    @Resource
    public CaseMapper caseMapper;


    @RequestMapping("updateCase")
    @Transactional(isolation = Isolation.DEFAULT)
    public String updateCase(){

        Case_Info case_info = new Case_Info();
        case_info.setAge("11111");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("gender","male");
        caseMapper.update(case_info,queryWrapper);
        int i = 1/0;
        return "success";
    }
}
