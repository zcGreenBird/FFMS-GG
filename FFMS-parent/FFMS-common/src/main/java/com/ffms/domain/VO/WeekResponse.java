package com.ffms.domain.VO;/*
  @auther WJW129
  @date 2019/11/10 - 14:05
*/

import lombok.Data;

/**
 *   一个星期每天的消费实体类
 */
@Data
public class WeekResponse {
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;
}
