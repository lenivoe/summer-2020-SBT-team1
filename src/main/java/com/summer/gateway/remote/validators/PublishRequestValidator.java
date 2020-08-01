package com.summer.gateway.remote.validators;

import com.summer.gateway.remote.exceptions.EmptyField;
import com.summer.gateway.remote.model.ApiRequestModel;
import com.summer.gateway.remote.model.PublishRequestModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PublishRequestValidator {
    /**
     * Проверить данные во входном запросе
     *
     * @throws EmptyField
     *         Поле пустое или null
     */
    public void validate(PublishRequestModel requestModel) {
        if (isNullOrEmpty(requestModel.getAddress())) throw new EmptyField("address");
        if (isNullOrEmpty(requestModel.getName_service())) throw new EmptyField("name_service");
        if (isNullOrEmpty(requestModel.getVersion_service())) throw new EmptyField("version_service");

        List<ApiRequestModel> requestModelApi = requestModel.getApi();
        for (int i = 0; i < requestModelApi.size(); i++) {
            ApiRequestModel api = requestModelApi.get(i);
            if (isNullOrEmpty(api.getPath())) throw new EmptyField("api[" + i + "] path");
            if (isNullOrEmpty(api.getApi_version())) throw new EmptyField("api[" + i + "] api_version");
            if (isNullOrEmpty(api.getMethod_type())) throw new EmptyField("api[" + i + "] method_type");
        }
        //TODO("Скорей всего нужна проверка на корректность address and path в списке апи")
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.equals("");
    }
}
