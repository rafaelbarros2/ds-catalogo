package com.rafaelb.dscatalog.exceptions;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class StandardError implements Serializable {

         private Instant timestamp;
         private Integer status;
         private String error;
         private String message;
         private String path;


}
