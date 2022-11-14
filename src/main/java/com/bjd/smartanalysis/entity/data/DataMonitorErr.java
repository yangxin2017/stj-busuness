package com.bjd.smartanalysis.entity.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("data_monitor_err")
public class DataMonitorErr {
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("file_id")
    private Integer fileId;

    @TableField("group_date")
    private String groupDate;

    @TableField("data_source")
    private String dataSource;

    @TableField("err_type")
    private String errType;

    @TableField("trip_type")
    private String tripType;

    @TableField("user_code")
    private String userCode;

    @TableField("trip_code")
    private String tripCode;

    @TableField("stime")
    private Date stime;

    @TableField("etime")
    private Date etime;

    @TableField("slng")
    private String slng;

    @TableField("slat")
    private String slat;

    @TableField("elng")
    private String elng;

    @TableField("elat")
    private String elat;

    @TableField("ave_speed")
    private String aveSpeed;

    @TableField("max_speed")
    private String maxSpeed;

    @TableField("trip_length")
    private String tripLength;

    @TableField("trip_duration")
    private String tripDuration;

    @TableField("trip_detail_id")
    private String tripDetailId;

    @TableField("data_status")
    private String dataStatus;

}
