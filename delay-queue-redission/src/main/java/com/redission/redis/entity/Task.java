package com.redission.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *  Task实体类
 * <p>
 *
 * @author Zaki Chen
 * @version 1.0
 * @since 2023-03-08
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task implements Serializable { // 存入延时队列中的实体类需要实现Serializable序列化接口
    private String taskId;

    private String name;
}
