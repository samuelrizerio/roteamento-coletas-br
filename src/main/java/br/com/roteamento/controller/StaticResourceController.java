package br.com.roteamento.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Controller para servir recursos estáticos CSS
 * 
 * Este controller é necessário para servir arquivos CSS personalizados
 * que não estão sendo reconhecidos automaticamente pelo Spring Boot
 */
@Controller
public class StaticResourceController {

    /**
     * Serve arquivos CSS do tema Java
     * 
     * @param filename Nome do arquivo CSS
     * @return Conteúdo do arquivo CSS com tipo MIME correto
     */
    @GetMapping(value = "/css/{filename:.+}", produces = "text/css")
    @ResponseBody
    public ResponseEntity<String> serveCss(@PathVariable("filename") String filename) {
        try {
            // Tenta carregar o arquivo da pasta static/css
            Resource resource = new ClassPathResource("static/css/" + filename);
            
            if (resource.exists()) {
                String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("text/css;charset=UTF-8"))
                    .body(content);
            } else {
                // Se não encontrar na pasta static, tenta na pasta META-INF
                resource = new ClassPathResource("META-INF/resources/WEB-INF/views/css/" + filename);
                if (resource.exists()) {
                    String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                    return ResponseEntity.ok()
                        .contentType(MediaType.valueOf("text/css;charset=UTF-8"))
                        .body(content);
                }
            }
            
            return ResponseEntity.notFound().build();
            
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                .body("/* Erro ao carregar arquivo CSS: " + e.getMessage() + " */");
        }
    }
}
