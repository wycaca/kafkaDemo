package com.citi.cbk.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableMsg implements Serializable {
    private static final long serialVersionUID = 2941337253699782349L;

    private String name;
    private String tel;
    private String email;
    private String address;

    private List<Currency> currencies;
}
