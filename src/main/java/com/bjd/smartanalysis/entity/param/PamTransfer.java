package com.bjd.smartanalysis.entity.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("pam_transfer")
public class PamTransfer {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("cx")
    private Float cx;

    @TableField("zxc")
    private Float zxc;

    @TableField("gj")
    private Float gj;

    @TableField("dt")
    private Float dt;
}
