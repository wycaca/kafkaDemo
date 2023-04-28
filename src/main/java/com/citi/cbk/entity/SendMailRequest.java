package com.citi.cbk.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SendMailRequest implements Serializable {
    private static final long serialVersionUID = 8604960062707188630L;

    private String toMail;
    private String fileName;
}
