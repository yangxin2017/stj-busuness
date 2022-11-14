package com.bjd.smartanalysis.entity.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("data_monitor_detail")
public class DataMonitorDetail {
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("group_id")
    private String groupId;

    @TableField("time")
    private Date time;

    @TableField("lng")
    private String lng;

    @TableField("lat")
    private String lat;

    @TableField("speed")
    private String speed;
}
