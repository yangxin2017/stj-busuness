package com.bjd.smartanalysis.entity.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("triptype_count")
public class TriptypeCount {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("trip_type")
    private String tripType;

    @TableField("num")
    private String num;
}
