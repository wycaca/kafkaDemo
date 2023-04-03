package com.citi.cbk.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditMsg implements Serializable {
    private static final long serialVersionUID = 2192244923502384394L;

    private Integer id;
    private String tel;
    private String email;
    private String address;
}
