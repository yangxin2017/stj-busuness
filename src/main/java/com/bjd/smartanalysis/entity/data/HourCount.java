package com.bjd.smartanalysis.entity.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@Data
@TableName("hour_count")
public class HourCount {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("hstr")
    private String hstr;

    @TableField("num")
    private String num;
}
