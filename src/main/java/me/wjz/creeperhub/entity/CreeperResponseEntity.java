package me.wjz.creeperhub.entity;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class CreeperResponseEntity extends ResponseEntity {
    public CreeperResponseEntity(Result result) {
        super(result.getData(), result.getStatus());
    }
}
