package com.summer.gateway.discovery;

import com.summer.gateway.dao.entity.Api;
import com.summer.gateway.dao.entity.Instance;
import com.summer.gateway.dao.entity.StateService;
import com.summer.gateway.dao.entity.Word;
import com.summer.gateway.dao.repositories.ApiRepository;
import com.summer.gateway.dao.repositories.InstanceRepository;
import com.summer.gateway.dao.repositories.WordRepository;
import com.summer.gateway.remote.exceptions.InstanceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ServiceDelete {

    private final ApiRepository apiRepository;
    private final InstanceRepository instanceRepository;
    private final WordRepository wordRepository;
    private final RouteHandler routeHandler;

    @Autowired
    public ServiceDelete(@NonNull final ApiRepository apiRepository,
                         @NonNull final InstanceRepository instanceRepository,
                         @NonNull final WordRepository wordRepository,
                         @NonNull final RouteHandler routeHandler) {
        this.apiRepository = apiRepository;
        this.instanceRepository = instanceRepository;
        this.wordRepository = wordRepository;
        this.routeHandler = routeHandler;
    }

    /**
     * Удалить микросервис
     */
    public void delete(String uuid) {
        Instance deleteInstance = instanceRepository.findByUuid(uuid);
        if (deleteInstance == null) throw new InstanceNotFound(uuid);

        // Апи привязанные к этому инстансу
        List<Api> api = apiRepository.findByInstancesEquals(deleteInstance);

        //  Отключить этот инстанс от всех API, и удалить поиск тех апи, которые остались без инстансов
        List<Api> deleteApi = new LinkedList<>();
        api.forEach(it -> {
                    it.getInstances().remove(deleteInstance);
                    if (it.getInstances().isEmpty()) {
                        deleteApi.add(it);
                    }
                }
        );

        // Сохранение тех апи, которые могу выполняться без этого инстанса
        api.removeAll(deleteApi);
        apiRepository.saveAll(api);

        // Чтобы удалить апи, надо удалить word
        List<Word> deleteWord = new LinkedList<>();
        deleteApi.forEach(it -> {
            deleteWord.addAll(it.getWords());
            it.getWords().clear();
        });
        apiRepository.saveAll(deleteApi);

        // Удаление
        apiRepository.deleteAll(deleteApi);
        wordRepository.deleteAll(deleteWord);

        // Если состояние удаленного сервиса было ACTIVE и было удалено апи - надо пересобрать маршруты
        if (deleteInstance.getState().equals(StateService.ACTIVE) && !deleteApi.isEmpty()) {
            routeHandler.updateRoutes();
        }

        // Если состояние удаленого сервиса было ACTIVE, но небыло удалено API - надо
        // проверить статус апи и пересобрать маршруты, если требуется
        if (deleteInstance.getState().equals(StateService.ACTIVE) && deleteApi.isEmpty()) {
            api.forEach(it -> {
                it.setActive(false);
                for (var instance : it.getInstances()) {
                    if (instance.getState().equals(StateService.ACTIVE)) {
                        it.setActive(true);
                    }
                }
            });
            apiRepository.saveAll(api);
            routeHandler.updateRoutes();
        }

        instanceRepository.delete(deleteInstance);
    }
}
