package com.bjd.smartanalysis.entity.data;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("data_fh_err")
@ExcelIgnoreUnannotated
public class DataFhErr {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("file_id")
    private Integer fileId;

    @TableField("group_date")
    private String groupDate;

    @TableField("err_type")
    private String errType;

    @ExcelProperty(value = "id")
    @TableField("trip_check_code")
    private String tripCheckCode;

    @ExcelProperty(value = "trip_mode")
    @TableField("trip_type")
    private String tripType;

    @ExcelProperty(value = "user_id")
    @TableField("user_code")
    private String userCode;

    @ExcelProperty(value = "card_id")
    @TableField("trip_code")
    private String tripCode;

    @ExcelProperty(value = "card_type")
    @TableField("card_type")
    private String cardType;

    @ExcelProperty(value = "data_source")
    @TableField("data_source")
    private String dataSource;

    @ExcelProperty(value = "start_line_no")
    @TableField("sline_code")
    private String slineCode;

    @ExcelProperty(value = "start_line_dir")
    @TableField("sline_dir")
    private String slineDir;

    @ExcelProperty(value = "start_stop_no")
    @TableField("sstation_code")
    private String sstationCode;

    @ExcelProperty(value = "start_stop_name")
    @TableField("sstation_name")
    private String sstationName;

    @ExcelProperty(value = "start_time")
    @TableField("stime")
    private Date stime;

    @ExcelProperty(value = "start_lon")
    @TableField("slng")
    private String slng;

    @ExcelProperty(value = "start_lat")
    @TableField("slat")
    private String slat;

    @ExcelProperty(value = "end_line_no")
    @TableField("eline_code")
    private String elineCode;

    @ExcelProperty(value = "end_line_dir")
    @TableField("eline_dir")
    private String elineDir;

    @ExcelProperty(value = "end_stop_no")
    @TableField("estation_code")
    private String estationCode;

    @ExcelProperty(value = "end_stop_name")
    @TableField("estation_name")
    private String estationName;

    @ExcelProperty(value = "end_time")
    @TableField("etime")
    private Date etime;

    @ExcelProperty(value = "end_lon")
    @TableField("elng")
    private String elng;

    @ExcelProperty(value = "end_lat")
    @TableField("elat")
    private String elat;

    @ExcelProperty(value = "speed_avg")
    @TableField("ave_speed")
    private String aveSpeed;

    @ExcelProperty(value = "speed_max")
    @TableField("max_speed")
    private String maxSpeed;

    @ExcelProperty(value = "travel_time")
    @TableField("trip_duraion")
    private String tripDuraion;

    @ExcelProperty(value = "trip_distance")
    @TableField("trip_length")
    private String tripLength;

    @ExcelProperty(value = "verify_trip_distance")
    @TableField("trip_check_length")
    private String tripCheckLength;

    @ExcelProperty(value = "CERs")
    @TableField("c_send")
    private Double cSend;

    @ExcelProperty(value = "standard_c_let")
    @TableField("c_base_send")
    private Double cBaseSend;

    @ExcelProperty(value = "c_let")
    @TableField("c_project_send")
    private Double cProjectSend;

    @TableField("data_status")
    private String dataStatus;

    @TableField("c_send_left")
    private Double cSendLeft;

    @TableField("c_send_right")
    private Double cSendRight;
}
