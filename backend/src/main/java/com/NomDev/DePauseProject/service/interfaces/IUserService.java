package com.NomDev.DePauseProject.service.interfaces;
import com.NomDev.DePauseProject.dto.*;
import org.springframework.data.domain.Pageable;


public interface IUserService {
    Response register(RegisterRequest registerRequest);
    Response login(LoginRequest loginRequest);
    Response getAllUsers(Pageable pageable); // Для получения всех пользователей с пагинацией
    Response getUserById(Long userId); // Получение пользователя по ID
    Response deleteUser(Long userId); // Удаление пользователя
    Response getMyInfo(String email); // Получение информации о текущем пользователе
    Response editUser(Long userId, EditUserRequest editUserRequest); // Редактирование пользователя (админ)
    Response editUserRole(Long userId, String role); // Редактирование роли (только ADMIN)
    Response changePassword(ChangePasswordRequest request, String email);
}
