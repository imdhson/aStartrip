package ce.daegu.ac.kr.aStartrip.controller;

import ce.daegu.ac.kr.aStartrip.dto.ArticleDTO;
import ce.daegu.ac.kr.aStartrip.dto.CardDTO;
import ce.daegu.ac.kr.aStartrip.dto.MemberDetails;
import ce.daegu.ac.kr.aStartrip.entity.ArticlePermissionENUM;
import ce.daegu.ac.kr.aStartrip.service.ArticleService;
import ce.daegu.ac.kr.aStartrip.service.CardService;
import ce.daegu.ac.kr.aStartrip.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileRController {
    private final FileService fileService;
    private final ArticleService articleService;
    private final CardService cardService;

    @PostMapping("/api/upload")
    public boolean uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("cardId") long cardId, @AuthenticationPrincipal MemberDetails memberDetails) {
        CardDTO cardDTO = cardService.findCardById(cardId);
        ArticleDTO articleDTO = articleService.findArticlebyID(cardDTO.getArticleNum());
        String memberName;
        try {
            //게시글에 접근 권한 있는지 넣어야함.
            memberName = memberDetails.getMember().getName();
        } catch (NullPointerException nullPointerException) {
            //비로그인 사용자 or 접근 권한 없는 사용자
            return false;
        }
        if (articleDTO.getWriter().equals(memberName)) {
            //게시글 작성자와 로그인 사용자의 email이 같은 경우에만 저장
            String filename = fileService.saveFile(file, cardId);
            if (filename != null) {
                return true;
            }
        }
        return false;

    }

    @PostMapping("/api/load")
    public ResponseEntity<Resource> loadFile(@RequestParam("cardId") long cardId, @AuthenticationPrincipal MemberDetails memberDetails) {
        CardDTO cardDTO = cardService.findCardById(cardId);
        ArticleDTO articleDTO = articleService.findArticlebyID(cardDTO.getArticleNum());
        String memberName = "";
        try {
            //게시글에 접근 권한 있는지 넣어야함.
            memberName = memberDetails.getMember().getName();
        } catch (NullPointerException nullPointerException) {
            //비로그인 사용자 or 접근 권한 없는 사용자

        }

//   파일을 리턴하기 전에 아티클에 대한 권한이 있는지 체크하고 접근 시작 !
        if (articleDTO.getArticlePermission() == ArticlePermissionENUM.OPEN || articleDTO.getWriter().equals(memberName)) {
            //게시글의 member와 요청온 member가 같은 경우에만 정상 리턴
            //or 게시글이 공개 상태인 경우 리턴
            Resource resource = fileService.loadFile(cardId);
            if (resource != null) {
                //파일을 찾았을 떄
                String extension = fileService.getExtension(resource.getFilename()).toLowerCase().trim();
                String isImage = "false";
                if (extension.equals("jpg") || extension.equals("heif") || extension.equals("png") ||
                        extension.equals("jpeg") || extension.equals("bmp") ||
                        extension.equals(("gif")) || extension.equals("webp") ||
                        extension.equals("svg")) { //이미지 파일인지 확인
                    isImage = "true";
                }

                return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .header("CardFile-isImage", isImage)
                        .body(resource);

            } else {
                //파일을 찾기 못하였을 때
                return ResponseEntity.notFound().build();
            }

        }
        return ResponseEntity.badRequest().build();
    }
}
