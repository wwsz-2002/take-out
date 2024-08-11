package com.sky.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {

    private String dishFlavor;
    private Long dishId;
    private Long setmealId;

}
