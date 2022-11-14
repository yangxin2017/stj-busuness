package com.bjd.smartanalysis.entity.param;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("pam_jianpai")
@ExcelIgnoreUnannotated
public class PamJianpai {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("type")
    private String type;

    @TableField("time")
    @ExcelProperty(value = "日期")
    private String time;

    @TableField("p0_1")
    @ExcelProperty(value = "0:00-1:00")
    private String p0_1;

    @TableField("p1_2")
    @ExcelProperty(value = "1:00-2:00")
    private String p1_2;

    @TableField("p2_3")
    @ExcelProperty(value = "2:00-3:00")
    private String p2_3;

    @TableField("p3_4")
    @ExcelProperty(value = "3:00-4:00")
    private String p3_4;

    @TableField("p4_5")
    @ExcelProperty(value = "4:00-5:00")
    private String p4_5;

    @TableField("p5_6")
    @ExcelProperty(value = "5:00-6:00")
    private String p5_6;

    @TableField("p6_7")
    @ExcelProperty(value = "6:00-7:00")
    private String p6_7;

    @TableField("p7_8")
    @ExcelProperty(value = "7:00-8:00")
    private String p7_8;

    @TableField("p8_9")
    @ExcelProperty(value = "8:00-9:00")
    private String p8_9;

    @TableField("p9_10")
    @ExcelProperty(value = "9:00-10:00")
    private String p9_10;

    @TableField("p10_11")
    @ExcelProperty(value = "10:00-11:00")
    private String p10_11;

    @TableField("p11_12")
    @ExcelProperty(value = "11:00-12:00")
    private String p11_12;

    @TableField("p12_13")
    @ExcelProperty(value = "12:00-13:00")
    private String p12_13;

    @TableField("p13_14")
    @ExcelProperty(value = "13:00-14:00")
    private String p13_14;

    @TableField("p14_15")
    @ExcelProperty(value = "14:00-15:00")
    private String p14_15;

    @TableField("p15_16")
    @ExcelProperty(value = "15:00-16:00")
    private String p15_16;

    @TableField("p16_17")
    @ExcelProperty(value = "16:00-17:00")
    private String p16_17;

    @TableField("p17_18")
    @ExcelProperty(value = "17:00-18:00")
    private String p17_18;

    @TableField("p18_19")
    @ExcelProperty(value = "18:00-19:00")
    private String p18_19;

    @TableField("p19_20")
    @ExcelProperty(value = "19:00-20:00")
    private String p19_20;

    @TableField("p20_21")
    @ExcelProperty(value = "20:00-21:00")
    private String p20_21;

    @TableField("p21_22")
    @ExcelProperty(value = "21:00-22:00")
    private String p21_22;

    @TableField("p22_23")
    @ExcelProperty(value = "22:00-23:00")
    private String p22_23;

    @TableField("p23_24")
    @ExcelProperty(value = "23:00-24:00(次日0点）")
    private String p23_24;
}
