package com.readme.payments.sseEmitter.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
@RequiredArgsConstructor
public class SseEmitterRepository implements EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(String id, SseEmitter sseEmitter) {
        emitters.put(id, sseEmitter);
        return sseEmitter;
    }

    @Override
    public SseEmitter findById(String id) {
        return emitters.get(id);
    }

    @Override
    public Map<String, SseEmitter> findAllStartById(String id) {
        return emitters.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(id))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteById(String id) {
        emitters.get(id).complete();
        emitters.remove(id);
    }


    @Override
    public void deleteAllStartByWithId(String id) {
        emitters.forEach((key, emitter) -> {
            if (key.startsWith(id)){
                emitters.get(key).complete();
                emitters.remove(key);
            }
        });
    }
}
