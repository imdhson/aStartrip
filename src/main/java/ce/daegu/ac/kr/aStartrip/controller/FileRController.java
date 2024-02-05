package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FileRController {
    private final FileService fileService;

    @PostMapping("/api/upload")
    public boolean uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("cardId") Long cardId) {
        String filename = fileService.saveFile(file, cardId);
        if (filename != null) {
            return true;
        }
        return false;

    }

    @PostMapping("/api/load")
    public ResponseEntity<Resource> loadFile(@RequestParam("cardId") Long cardId) {
//   파일을 리턴하기 전에 아티클에 대한 권한이 있는지 체크하고 접근 시작 !
        Resource resource = fileService.loadFile(cardId);
        if (resource == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
