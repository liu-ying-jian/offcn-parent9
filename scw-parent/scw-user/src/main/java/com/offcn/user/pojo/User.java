package com.offcn.user.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("User实体类测试")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {


    @ApiModelProperty(value = "用户ID")
    private Integer id;
    @ApiModelProperty(value = "用户姓名")
    private String name;
}
