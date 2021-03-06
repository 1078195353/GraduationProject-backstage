package com.qxf.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qxf.annotation.MyLog;
import com.qxf.dto.QuestionDto;
import com.qxf.entity.FillQuestion;
import com.qxf.service.FillQuestionService;
import com.qxf.util.EnumCode;
import com.qxf.util.ResultUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 填空题(FillQuestion)表控制层
 *
 * @author makejava
 * @since 2020-05-17 11:25:40
 */
@RestController
@RequestMapping("fill")
public class FillQuestionController {
    /**
     * 服务对象
     */
    @Resource
    private FillQuestionService fillQuestionService;

    // 分页查询
    @GetMapping("/list")
    @MyLog
    public Object getListByPage(Integer startPage,Integer pageSize,String content){
        PageHelper.startPage(startPage,pageSize);
        List<QuestionDto> list = fillQuestionService.getListByPage(content);
        PageInfo<QuestionDto> pageInfo = new PageInfo<>(list);
        return new ResultUtil(EnumCode.OK.getValue(),"请求成功",list,pageInfo.getTotal());
    }

    @PostMapping("/add")
    @MyLog
    public ResultUtil add(@RequestBody FillQuestion fillQuestion){
        fillQuestion.setId(UUID.randomUUID().toString().replace("-",""));
        fillQuestion.setCreateTime(new Date());
        FillQuestion insert = fillQuestionService.insert(fillQuestion);
        if (insert != null){
            return new ResultUtil(EnumCode.OK.getValue(),"请求成功");
        }else {
            return new ResultUtil(EnumCode.INTERNAL_SERVER_ERROR.getValue(),"请求失败");
        }
    }

    @GetMapping("/delete/{id}")
    @MyLog
    public ResultUtil delete(@PathVariable("id") String id){
        boolean success = fillQuestionService.deleteById(id);
        if (success){
            return new ResultUtil(EnumCode.OK.getValue(),"删除成功");
        }else {
            return new ResultUtil(EnumCode.INTERNAL_SERVER_ERROR.getValue(),"删除失败");
        }
    }

}