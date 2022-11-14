package com.bjd.smartanalysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sm_datafile")
public class DataFile {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("belong_id")
    private Integer belongId;

    @TableField("filename")
    private String filename;

    @TableField("filesize")
    private String filesize;

    @TableField("suffix")
    private String suffix;

    @TableField("filepath")
    private String filepath;

    @TableField("ctime")
    private Date ctime;
}
