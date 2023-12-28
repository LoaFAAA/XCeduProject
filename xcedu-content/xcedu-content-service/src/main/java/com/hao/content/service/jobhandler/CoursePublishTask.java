package com.hao.content.service.jobhandler;

import com.hao.messagesdk.model.po.MqMessage;
import com.hao.messagesdk.service.MessageProcessAbstract;
import com.hao.messagesdk.service.MqMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CoursePublishTask extends MessageProcessAbstract {
    //执行任务执行的逻辑
    public boolean execute(MqMessage mqMessage) {
        //从mqMessage拿到课程id
        Long courseId = Long.parseLong(mqMessage.getBusinessKey1());

        //向ES写索引

        //向redis写索引

        //课程静态化上传到minIO

        //确保全部完成后返回true
        return false;
    }

    private void generateCourseHtml(MqMessage mqMessage,Long courseId){
        Long task = mqMessage.getId();
        MqMessageService mqMessageService = this.getMqMessageService();
        //做任务幂等性处理

        //查询各阶段执行状态
        int stageOne = mqMessageService.getStageOne(task);
    }
}
