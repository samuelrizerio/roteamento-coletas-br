package br.com.roteamento.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller para servir recursos estáticos CSS e favicon
 * 
 * Este controller é necessário para servir arquivos CSS personalizados
 * e favicon que não estão sendo reconhecidos automaticamente pelo Spring Boot
 */
@Controller
public class StaticResourceController {

    /**
     * Serve o favicon.ico
     * 
     * @return Conteúdo do favicon com tipo MIME correto
     */
    @GetMapping(value = "/favicon.ico", produces = "image/x-icon")
    @ResponseBody
    public ResponseEntity<byte[]> serveFavicon() {
        try {
            Resource resource = new ClassPathResource("static/favicon.ico");
            
            if (resource.exists()) {
                byte[] content = resource.getInputStream().readAllBytes();
                return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("image/x-icon"))
                    .body(content);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Serve a logo SrPC.png
     * 
     * @return Conteúdo da logo com tipo MIME correto
     */
    @GetMapping(value = "/logo", produces = "image/png")
    @ResponseBody
    public ResponseEntity<byte[]> serveLogo() {
        try {
            Resource resource = new ClassPathResource("static/images/SrPC.png");
            
            if (resource.exists()) {
                byte[] content = resource.getInputStream().readAllBytes();
                return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("image/png"))
                    .body(content);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

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
