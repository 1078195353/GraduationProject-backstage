package com.qxf.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qxf.annotation.MyLog;
import com.qxf.dto.QuestionDto;
import com.qxf.entity.JudgeQuestion;
import com.qxf.service.JudgeQuestionService;
import com.qxf.util.EnumCode;
import com.qxf.util.ResultUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 判断题(JudgeQuestion)表控制层
 *
 * @author makejava
 * @since 2020-05-17 11:25:40
 */
@RestController
@RequestMapping("judge")
public class JudgeQuestionController {
    /**
     * 服务对象
     */
    @Resource
    private JudgeQuestionService judgeQuestionService;

    // 分页查询
    @GetMapping("/list")
    @MyLog
    public Object getListByPage(Integer startPage,Integer pageSize,String content){
        PageHelper.startPage(startPage,pageSize);
        List<QuestionDto> list = judgeQuestionService.getListByPage(content);
        PageInfo<QuestionDto> pageInfo = new PageInfo<>(list);
        return new ResultUtil(EnumCode.OK.getValue(),"请求成功",list,pageInfo.getTotal());
    }

    @PostMapping("/add")
    @MyLog
    public ResultUtil add(@RequestBody JudgeQuestion judgeQuestion){
        judgeQuestion.setId(UUID.randomUUID().toString().replace("-",""));
        judgeQuestion.setCreateTime(new Date());
        JudgeQuestion insert = judgeQuestionService.insert(judgeQuestion);
        if (insert != null){
            return new ResultUtil(EnumCode.OK.getValue(),"请求成功");
        }else {
            return new ResultUtil(EnumCode.INTERNAL_SERVER_ERROR.getValue(),"请求失败");
        }

    }

    @GetMapping("/delete/{id}")
    @MyLog
    public ResultUtil delete(@PathVariable("id") String id){
        boolean success = judgeQuestionService.deleteById(id);
        if (success){
            return new ResultUtil(EnumCode.OK.getValue(),"删除成功");
        }else {
            return new ResultUtil(EnumCode.INTERNAL_SERVER_ERROR.getValue(),"删除失败");
        }
    }

}