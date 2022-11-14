package com.bjd.smartanalysis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("test_a")
public class TestA {
    @TableId
    @TableField("Id")
    private Long Id;
}
