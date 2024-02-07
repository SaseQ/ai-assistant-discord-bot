package it.marczuk.aiassistantdiscordbot.web.qdrant.controller;

import it.marczuk.aiassistantdiscordbot.web.qdrant.service.QdrantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qdrant")
public class QdrantController {

    private final QdrantService qdrantService;

    @PostMapping("/crate-collection")
    public ResponseEntity<?> crateCollection(@RequestParam String name) {
        qdrantService.crateCollection(name);
        return ResponseEntity.status(201).build();
    }
}
