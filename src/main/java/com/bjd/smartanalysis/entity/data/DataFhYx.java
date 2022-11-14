package com.bjd.smartanalysis.entity.data;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("data_fh_yx")
public class DataFhYx {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("file_id")
    private Integer fileId;

    @TableField("group_date")
    private String groupDate;

    @TableField("trip_check_code")
    private String tripCheckCode;

    @TableField("trip_type")
    private String tripType;

    @TableField("user_code")
    private String userCode;

    @TableField("trip_code")
    private String tripCode;

    @TableField("card_type")
    private String cardType;

    @TableField("data_source")
    private String dataSource;

    @TableField("sline_code")
    private String slineCode;

    @TableField("sline_dir")
    private String slineDir;

    @TableField("sstation_code")
    private String sstationCode;

    @TableField("sstation_name")
    private String sstationName;

    @TableField("stime")
    private Date stime;

    @TableField("slng")
    private String slng;

    @TableField("slat")
    private String slat;

    @TableField("eline_code")
    private String elineCode;

    @TableField("eline_dir")
    private String elineDir;

    @TableField("estation_code")
    private String estationCode;

    @TableField("estation_name")
    private String estationName;

    @TableField("etime")
    private Date etime;

    @TableField("elng")
    private String elng;

    @TableField("elat")
    private String elat;

    @TableField("ave_speed")
    private String aveSpeed;

    @TableField("max_speed")
    private String maxSpeed;

    @TableField("trip_duraion")
    private String tripDuraion;

    @TableField("trip_length")
    private String tripLength;

    @TableField("trip_check_length")
    private String tripCheckLength;

    @TableField("c_send")
    private Double cSend;

    @TableField("c_base_send")
    private Double cBaseSend;

    @TableField("c_project_send")
    private Double cProjectSend;

    @TableField("c_send_left")
    private Double cSendLeft;

    @TableField("c_send_right")
    private Double cSendRight;
}
