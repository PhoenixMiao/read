package com.phoenix.read.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("Organizer 主办方")
public class Organizer {
    @Id
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;
}
