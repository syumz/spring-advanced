package org.example.expert.domain.todo.service;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private WeatherClient weatherClient;

    @InjectMocks
    private TodoService todoService;

    @Test
    public void todo를_정상적으로_등록한다(){
        // given
        TodoSaveRequest request = new TodoSaveRequest("title", "contents");
        AuthUser authUser = new AuthUser(1L, "sy@gmail.com", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        String weather = weatherClient.getTodayWeather();
        Todo todo =  new Todo(request.getTitle(), request.getContents(), weather, user);

        given(todoRepository.save(any())).willReturn(todo);


        // when
        TodoSaveResponse result = todoService.saveTodo(authUser, request);

        // then
        assertNotNull(result);
    }

}