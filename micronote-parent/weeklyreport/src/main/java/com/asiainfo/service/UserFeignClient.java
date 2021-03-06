package com.asiainfo.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.asiainfo.domain.entity.user.User;


@FeignClient(name="user")
public interface UserFeignClient {
	 @RequestMapping("/{id}")
	  public User findByIdFeign(@RequestParam("id") Long id);
}
