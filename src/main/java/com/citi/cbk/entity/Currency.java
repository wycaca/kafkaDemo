package com.citi.cbk.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Currency implements Serializable {
    private static final long serialVersionUID = -7583851529603672011L;

    private String name;
    private String value;
    private String valueUsd;
    private String allocation;
}
