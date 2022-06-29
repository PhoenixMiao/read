package com.phoenix.read.controller.request;

import com.phoenix.read.entity.Passage;
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
@ApiModel("CollectionResult 收藏列表返回")
public class CollectionResult {

    @ApiModelProperty("帖子id")
    private Passage passage;

    @ApiModelProperty("收藏时间")
    private String collectTime;
}
