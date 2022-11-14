package com.bjd.smartanalysis.entity.flow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_flow_template_node")
public class SysFlowTemplateNode {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("template_id")
    private Integer templateId;

    @TableField("step")
    private Integer step;

    @TableField("name")
    private String name;

    @TableField("type")
    private String type;// 审核节点

    @TableField("check_role")
    private Integer checkRole;
}
