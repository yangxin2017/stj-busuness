package com.bjd.smartanalysis.entity.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("data_key")
public class DataKey {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("key_type")
    private String keyType;

    @TableField("keystr")
    private String keystr;
}
