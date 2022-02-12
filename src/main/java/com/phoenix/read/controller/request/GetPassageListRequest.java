package com.phoenix.read.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("GetPassageListRequest 获取论坛列表")
public class GetPassageListRequest {
    @ApiModelProperty("类别 1:交流 2:答疑 3:综合")
    private Integer type;

    @ApiModelProperty("子类别 1:人文艺术 2:社会科学 3:自然科学")
    private Integer subtype;
}