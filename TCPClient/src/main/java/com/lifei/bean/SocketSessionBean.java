package com.lifei.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: lifei
 * @Description:
 * @Date: 2020/9/7
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocketSessionBean {

    public String loginMessage;

    public String pubMessage;
}
