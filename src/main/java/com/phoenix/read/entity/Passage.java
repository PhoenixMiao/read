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
@ApiModel("Passage 帖子")
public class Passage {
    @Id
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("类别 1:交流 2:答疑 3:综合")
    private String type;

    @ApiModelProperty("子类别 1:人文艺术 2:社会科学 3:自然科学")
    private String subtype;

    @ApiModelProperty("编写用户id")
    private String userId;

    @ApiModelProperty("正文")
    private String content;

    @ApiModelProperty("发布时间")
    private String publishTime;

    @ApiModelProperty("来源")
    private String source;
}
