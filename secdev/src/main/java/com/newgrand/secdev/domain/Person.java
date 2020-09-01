package com.newgrand.secdev.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Person implements Serializable {
    private String name;
    private  int age;
    private String email;
}
