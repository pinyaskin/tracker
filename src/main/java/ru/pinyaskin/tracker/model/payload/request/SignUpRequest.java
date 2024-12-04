package ru.pinyaskin.tracker.model.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Запрос на регистрацию")
public class SignUpRequest {
    @Schema(description = "Имя пользователя", example = "John")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя обязательно")
    private String name;

    @Schema(description = "Почтовый адрес", example = "john@yandex.ru")
    @Size(min = 5, max = 255, message = "Почтовый адрес содержит от 5 до 255 символов")
    @NotBlank(message = "Почтовый адрес обязателен")
    @Email
    private String email;

    @Schema(description = "Пароль", example = "i27Js298_s!m")
    @Size(min = 8, max = 255, message = "Длина пароля должна быть от 8 до 255 символов")
    private String password;
}
