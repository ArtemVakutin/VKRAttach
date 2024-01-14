package ru.bk.artv.vkrattach.testutils;

import ru.bk.artv.vkrattach.web.dto.UploadAnswer;

import java.util.ArrayList;
import java.util.List;

public class UploadAnswerBuilder<T> {

    private String dataId = "DefaultDataId";
    private List<T> objects = new ArrayList<>();
    private List<T> errorObjects = new ArrayList<>();

    private UploadAnswerBuilder() {
        // Конструктор приватный, так как мы хотим, чтобы объект создавался только через статический метод create()
    }

    // Мы используем метод create(), чтобы создать экземпляр этого Builder
    public static <T> UploadAnswerBuilder<T> create() {
        return new UploadAnswerBuilder<>();
    }

    // Методы для установки значений полей
    public UploadAnswerBuilder<T> withDataId(String dataId) {
        this.dataId = dataId;
        return this;
    }

    public UploadAnswerBuilder<T> withObjects(List<T> objects) {
        this.objects = objects;
        return this;
    }

    public UploadAnswerBuilder<T> withObject(T object) {
        this.objects.add(object);
        return this;
    }

    public UploadAnswerBuilder<T> withErrorObjects(List<T> errorObjects) {
        this.errorObjects = errorObjects;
        return this;
    }

    public UploadAnswerBuilder<T> withErrorObject(T errorObject) {
        this.errorObjects.add(errorObject);
        return this;
    }

    // Метод build() для создания объекта UploadAnswer с установленными значениями полей
    public UploadAnswer<T> build() {
        return new UploadAnswer<>(dataId, objects, errorObjects);
    }
}