package test.lomboktest.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeletePostResponse {

    private boolean success;
    private String message;
}
