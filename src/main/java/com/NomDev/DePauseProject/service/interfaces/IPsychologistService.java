package com.NomDev.DePauseProject.service.interfaces;
import com.NomDev.DePauseProject.dto.Response;
import org.springframework.data.domain.Pageable;

public interface IPsychologistService {
    Response getAllPsychologists(Pageable pageable); // Получение всех психологов с пагинацией
    Response getPsychologistById(Long id); // Получение психолога по ID
    Response editPsychologistProfile(String email, EditPsychologistRequest request); // Редактирование профиля психолога
    Response deletePsychologist(Long id); // Удаление психолога (админ)
}
