package com.hanserwei.entity.dto;

public record UserRegisterDTO(String username, String password, String checkPassword, String email,
                              String phoneNumber) {
}
