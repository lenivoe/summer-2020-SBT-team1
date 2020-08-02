package com.summer.gateway.discovery;

import com.summer.gateway.dao.entity.Api;
import com.summer.gateway.dao.entity.Instance;
import com.summer.gateway.dao.entity.Word;
import com.summer.gateway.dao.repositories.ApiRepository;
import com.summer.gateway.dao.repositories.InstanceRepository;
import com.summer.gateway.dao.repositories.WordRepository;
import com.summer.gateway.remote.exceptions.URIBad;
import com.summer.gateway.remote.model.PublishRequestModel;
import com.summer.gateway.remote.model.PublishResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServiceRegistrar {

    private final ApiRepository apiRepository;
    private final InstanceRepository instanceRepository;
    private final WordRepository wordRepository;

    @Value("${ping.interval}")
    private int pingInterval;

    @Autowired
    public ServiceRegistrar(@NonNull final ApiRepository apiRepository,
                            @NonNull final InstanceRepository instanceRepository,
                            @NonNull final WordRepository wordRepository) {
        this.apiRepository = apiRepository;
        this.instanceRepository = instanceRepository;
        this.wordRepository = wordRepository;
    }

    public PublishResponseModel register(PublishRequestModel request) throws URISyntaxException {
        // URI
        URI uri = createURI(request.getAddress(), request.getPort());
        checkURI(uri);

        // Добавляемые API
        List<Api> requestApi = request.getApi().stream().map(it -> {
            var words = pathToWord(it.getPath());
            return new Api(words, it.getPath(), words.size());
        }).collect(Collectors.toList());

        // Найти соответствия, возможно это апи уже присутствует
        // Поделить апи на новое и старое
        List<Api> newApi = new LinkedList<>();
        List<Api> oldApi = new LinkedList<>();
        List<Api> allApi = apiRepository.findAll();
        for (var na : requestApi) {
            Api find = apiIsExist(na, allApi);
            if (find != null) {
                oldApi.add(find);
            } else {
                newApi.add(na);
            }
        }

        // Все эти API имплементирует этот экземляр
        String uuid = UUID.randomUUID().toString();
        Instance instance = new Instance(uuid, request.getName_service(), uri.toString(), request.getVersion_service());
        newApi.forEach(it -> it.addInstance(instance));
        oldApi.forEach(it -> it.addInstance(instance));

        // Сохранение
        instanceRepository.save(instance);
        newApi.forEach(it -> wordRepository.saveAll(it.getWords()));
        apiRepository.saveAll(newApi);

        apiRepository.saveAll(oldApi);

        return new PublishResponseModel(uuid, pingInterval);
    }

    private Api apiIsExist(Api newApi, List<Api> api) {
        for (var a : api) {
            if (a.getPath().equals(newApi.getPath())) return a;
        }
        return null;
    }

    /**
     * Конвертировать path в word
     */
    private List<Word> pathToWord(String path) {
        List<Word> words = new LinkedList<>();

        int index = 0;
        for (var word : path.split("/")) {
            if (word.equals("")) continue;
            else if (word.startsWith("{") && word.endsWith("}"))
                words.add(new Word("{}", index++));
            else
                words.add(new Word(word, index++));
        }
        return words;
    }


    /**
     * Проверка, возможно сервис с таким URI уже есть
     */
    private void checkURI(URI uri) {
        instanceRepository.findAll().forEach(it -> {
            if (it.getAddress().equals(uri.toString())) throw new URIBad("This URI: " + uri + " already exist");
        });
    }

    /**
     * Создание URI, завит от переданных параметров
     */
    private URI createURI(String address, String port) throws URISyntaxException {
        if (port == null || port.equals("")) {
            return new URI(address);
        } else {
            return new URI(address + ":" + port);
        }
    }
}
