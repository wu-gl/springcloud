package com.study.controller;


import com.study.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by goudongdong on 2018-09-14 20:09
 */

@RestController
@RequestMapping("/")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 查询全部
     */
    @GetMapping(value = "/saveOrder")
    public void saveOrder(HttpServletRequest request) throws Exception {
        orderService.saveOrder();
    }
}
