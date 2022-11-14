package com.bjd.smartanalysis.entity.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_count")
public class UserCount {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("datestr")
    private String datestr;

    @TableField("num")
    private String num;
}
