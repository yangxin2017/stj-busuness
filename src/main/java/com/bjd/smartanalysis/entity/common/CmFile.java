package com.bjd.smartanalysis.entity.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("cm_file")
public class CmFile {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("type_id")
    private Integer typeId;

    @TableField("filename")
    private String fileName;

    @TableField("filesize")
    private String fileSize;

    @TableField("suffix")
    private String suffix;

    @TableField("filepath")
    private String filePath;

    @TableField("data_source")
    private String dataSource;

    @TableField("data_time")
    private Date dataTime;

    @TableField("yx_count")
    private Integer yxCount;

    @TableField("err_count")
    private Integer errCount;

    @TableField("total_count")
    private Integer totalCount;

    @TableField("data_status")
    private String dataStatus;

    @TableField("ctime")
    private Date ctime;

}
