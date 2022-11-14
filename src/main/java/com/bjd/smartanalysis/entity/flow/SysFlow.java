package com.bjd.smartanalysis.entity.flow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_flow")
public class SysFlow {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("task_id")
    private Integer taskId;

    @TableField("template_id")
    private Integer templateId;

    @TableField("status")
    private String status;//

    @TableField("is_finish")
    private Integer isFinish;

    @TableField("cur_node_step")
    private Integer curNodeStep;

    @TableField("last_check_user")
    private Integer lastCheckUser;

    @TableField("last_check_result")
    private String lastCheckResult;

    @TableField("time")
    private Date time;
}
