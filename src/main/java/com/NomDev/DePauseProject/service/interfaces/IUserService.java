package com.NomDev.DePauseProject.service.interfaces;
import com.NomDev.DePauseProject.dto.EditUserRequest;
import com.NomDev.DePauseProject.dto.LoginRequest;
import com.NomDev.DePauseProject.dto.RegisterRequest;
import com.NomDev.DePauseProject.dto.Response;
import org.springframework.data.domain.Pageable;


public interface IUserService {
    Response register(RegisterRequest registerRequest);
    Response login(LoginRequest loginRequest);
    Response getAllUsers(Pageable pageable); // Для получения всех пользователей с пагинацией
    Response getUserById(Long userId); // Получение пользователя по ID
    Response deleteUser(Long userId); // Удаление пользователя
    Response getMyInfo(String email); // Получение информации о текущем пользователе
    Response editUser(Long userId, EditUserRequest editUserRequest); // Редактирование пользователя (админ)
    Response editProfile(String email, EditUserRequest editUserRequest); // Редактирование профиля (сам пользователь)
}
